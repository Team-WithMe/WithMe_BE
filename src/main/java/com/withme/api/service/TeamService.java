package com.withme.api.service;

import com.withme.api.controller.dto.CreateTeamRequestDto;
import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.controller.dto.TeamNoticeCreateRequestDto;
import com.withme.api.controller.dto.TeamSearchDto;
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

        // NOTE 검색 조건에 맞는 teamSkill이 없고 검색 조건의 Skill이 없으면
        if (teamSkillList.size() == 0 && skillList.size() == 0){
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
                teamList = teamRepository.findDistinctTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(teamSkillList, Status.DISPLAYED)
                        .orElseThrow(
                                () -> new NullPointerException("팀 조회 오류 (검색O)")
                        );
            }else {
                teamList = teamRepository.findDistinctTeamsByTeamSkillsInAndStatusOrderByCreatedTimeAsc(teamSkillList, Status.DISPLAYED)
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
                    .collect(Collectors.toList());
            teamSkillsParamsList.add(teamSkillsParams);
        }

        // NOTE 검색 조건을 위해 리스트를 합침
        List<TeamSkill> params = teamSkillsParamsList.stream()
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());

        log.info("!23 :" + params.size());

        return params;
    }

    /**
     * 팀 등록
     * */
    @Transactional
    public Long createTeam(CreateTeamRequestDto createTeamDto
            , String authHeader
    ) {
        // NOTE 현재 접속한 유저 ID 구해서 적용필요
        Long user_idx = tokenProvider.getUserIdFromToken(authHeader);
        // NOTE 팀으로 변경
        //Long user_idx = 1L;
        Team team = createTeamDto.setTeamSkill();

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
        Team returnTeam = teamRepository.save(team);

        return returnTeam.getId();
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
    /**
     * 팀 상세 정보 조회
     * */
    public TeamListResponseMapping getTeamListByTeamId(Long teamId) {
        return teamRepository.findTeamById(teamId)
                .orElseThrow(() -> new NullPointerException("Team not found"));
    }
}
