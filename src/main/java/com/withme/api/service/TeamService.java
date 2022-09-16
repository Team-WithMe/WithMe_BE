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
import com.withme.api.exception.BusinessException;
import com.withme.api.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
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

            // NOTE 스킬 입력ㅆ
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
    public TeamNotice createTeamNotice(Long teamId, TeamNoticeCreateRequestDto dto, Long userIdFromToken) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Found. id : " + teamId));

        if(!team.IsUserTeamLeader(userIdFromToken)) {
            throw new BusinessException("This User is not the Leader of this Team.");
        };

        User user = userRepository.findById(userIdFromToken)
                .orElseThrow(() -> new EntityNotFoundException("User Not Found. id :" + userIdFromToken));

        return teamNoticeRepository.save(dto.toEntity(team, user));
    }

    @Transactional
    public List<TeamNoticeResponseDto> selectTeamNoticeList(Long teamId) {
        List<TeamNoticeResponseDto> teamNoticeResponseDtoList = new ArrayList<>();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Found. id : " + teamId));

        List<TeamNotice> teamNoticeList = team.getTeamNotice();
        teamNoticeList.forEach(teamNotice -> {
            teamNoticeResponseDtoList.add(new TeamNoticeResponseDto(teamNotice));
        });

        return teamNoticeResponseDtoList;
    }

    /**
     * 팀 상세 정보 조회
     * */
    public TeamListResponseMapping getTeamListByTeamId(Long teamId) {
        return teamRepository.findTeamById(teamId)
                .orElseThrow(() -> new NullPointerException("Team not found"));
    }

    @Transactional
    public List<UserResponseDto> selectTeamMemberList(Long teamId) {
        List<UserResponseDto> userResponseDtoList = new ArrayList<>();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team Not Found. id : " + teamId));
        List<TeamUser> teamUserList = team.getTeamUsers();
        teamUserList.forEach(teamUser -> {
            userResponseDtoList.add(new UserResponseDto(teamUser.getUser()));
        });

        return userResponseDtoList;
    }
}
