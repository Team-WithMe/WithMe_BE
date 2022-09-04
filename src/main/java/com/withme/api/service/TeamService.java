package com.withme.api.service;

import com.withme.api.controller.dto.*;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.skill.SkillName;
import com.withme.api.domain.team.Status;
import com.withme.api.domain.team.Team;
import com.withme.api.domain.team.TeamCategory;
import com.withme.api.domain.team.TeamRepository;
import com.withme.api.domain.teamNotice.TeamNotice;
import com.withme.api.domain.teamNotice.TeamNoticeRepository;
import com.withme.api.domain.teamSkill.TeamSkill;
import com.withme.api.domain.teamSkill.TeamSkillRepository;
import com.withme.api.domain.teamUser.MemberType;
import com.withme.api.domain.teamUser.TeamUser;
import com.withme.api.domain.user.User;
import com.withme.api.domain.user.UserRepository;
import com.withme.api.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private final TokenProvider tokenProvider;
    private final TeamNoticeRepository teamNoticeRepository;


    private final TeamSkillRepository teamSkillRepository;

    @Transactional
    public List<TeamListResponseMapping> getTeamList(TeamSearchDto teamSearchDto) throws Exception {

        List<TeamListResponseMapping> teamList = new ArrayList<>();

        // NOTE 검색 조건 사용을 위해 TeamSkill 조회
        List<TeamSkill> teamSkills = teamSkillRepository.findAll();
        // NOTE 스킬 입력
        List<Skill> skillList = teamSearchDto.toSkillList();
        // NOTE 검색 조건 만들기
        List<TeamSkill> teamSkillList = toTeamListParams(skillList, teamSkills);

        if (teamSkillList.size() <= 0){
            // NOTE 내림 차순
            if (teamSearchDto.getSort() == 0){
                teamList = teamRepository.findAllByStatusOrderByCreatedTimeDesc(Status.DISPLAYED).orElseThrow(
                        () -> new NullPointerException("팀 조회 오류 (검색X)")
                );
            }else {
                teamList = teamRepository.findAllByStatusOrderByCreatedTimeAsc(Status.DISPLAYED).orElseThrow(
                        () -> new NullPointerException("팀 조회 오류 (검색X)")
                );
            }
        }else{
            if (teamSearchDto.getSort() == 0){
                teamList = teamRepository.findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(teamSkillList, Status.DISPLAYED)
                        .orElseThrow(
                                () -> new NullPointerException("팀 조회 오류 (검색O)")
                        );
            }else {
                teamList = teamRepository.findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeAsc(teamSkillList, Status.DISPLAYED)
                        .orElseThrow(
                                () -> new NullPointerException("팀 조회 오류 (검색O)")
                        );
            }
        }

        return teamList;
    }
    // NOTE 팀 검색 조건 처리 로직
    public List<TeamSkill> toTeamListParams(List<Skill> skills, List<TeamSkill> teamSkills) {
        List<TeamSkill> teamSkillsParams = new ArrayList<>();
        List<List<TeamSkill>> teamSkillsParamsList = new ArrayList<>();
        // NOTE 검색 조건 걸러냄
        for (Skill skill : skills) {
            teamSkillsParams = teamSkills.stream()
                    .filter(teamSkill -> {
                        return teamSkill.getSkill().getSkillName().equals(skill.getSkillName());
                    })
                    .peek(v ->
                            System.out.println("v.getSkill().getSkillName() = " + v.getSkill().getSkillName()))
                    .collect(Collectors.toList());
            teamSkillsParamsList.add(teamSkillsParams);
        }

        // NOTE 검색 조건을 위해 리스트를 합침
        List<TeamSkill> params = teamSkillsParamsList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        return params;
    }

    /**
     * 팀 등록
     * */
    @Transactional
    public int createTeam(CreateTeamRequestDto createTeamDto, String authHeader) {
        // NOTE 현재 접속한 유저 ID 구해서 적용필요
        Long user_idx = tokenProvider.getUserIdFromToken(authHeader);

        Team team = Team.builder()
                    .teamCategory(TeamCategory.PROJECT)
                    .teamDesc(createTeamDto.getDescription())
                    .teamName(createTeamDto.getName())
                    .status(Status.HIDDEN)
                    .build();

            // NOTE 스킬 입력
            Skill skill = new Skill();
            TeamSkill teamSkill = new TeamSkill();
            for (String skillName : createTeamDto.getSkills()) {
                skill = Skill.builder()
                        .skillName(SkillName.valueOf(skillName))
                        .build();
                teamSkill = TeamSkill.builder()
                        .team(team)
                        .skill(skill)
                        .build();
                team.addTeamSkill(teamSkill);
            }


            User user = userRepository.findById(user_idx).orElseThrow(
                    ()-> new NullPointerException("존재하지않는 사용자"));

            log.info("team ::: " + user);
            // NOTE 팀등록
            TeamUser teamUser = TeamUser.builder()
                    .memberType(MemberType.LEADER)
                    .team(team)
                    .user(user)
                    .build();
            team.addTeamUser(teamUser);
            teamRepository.save(team);

            return 1;
    }

    @Transactional
    public TeamNotice createTeamNotice(Long teamId, TeamNoticeCreateRequestDto dto, String authHeader) {
        /**
         * 1. authHeader 파싱해서 user id 가져오기
         * 2. team에 uesr가 있는지 확인하기 -> 없으면 exception 발생
         * 3. team에 공지사항 등록
         */

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team Id not exist."));

        Long userIdFromToken = tokenProvider.getUserIdFromToken(authHeader);
        team.isUserJoined(userIdFromToken);

        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new UsernameNotFoundException("User not exist."));

        return teamNoticeRepository.save(dto.toEntity(team, user));
    }

//    @Transactional
//    public List<TeamNoticeResponseDto> selectTeamNoticeList(Long teamId) {
//        List<TeamNoticeResponseDto> teamNoticeResponseDtoList = new ArrayList<>();
//
//        Team team = teamRepository.findById(teamId)
//                .orElseThrow(() -> new IllegalArgumentException("Team Id not exist."));
//
//        List<TeamNotice> teamNoticeList = team.getTeamNotice();
//        teamNoticeList.forEach(teamNotice -> {
//            teamNoticeResponseDtoList.add(new TeamNoticeResponseDto(teamNotice));
//        });
//
//        return teamNoticeResponseDtoList;
//    }

    /**
     * 팀 상세 정보 조회
     * */
    public TeamListResponseMapping getTeamListByTeamId(Long teamId) {
        return teamRepository.findTeamById(teamId)
                .orElseThrow(() -> new NullPointerException("Team not found"));
    }
}
