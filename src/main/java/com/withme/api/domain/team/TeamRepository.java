package com.withme.api.domain.team;

import com.withme.api.controller.dto.TeamDetailResponseDto;
import com.withme.api.controller.dto.TeamDetailResponseMapping;
import com.withme.api.controller.dto.TeamListResponseDto;
import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.teamSkill.TeamSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

      int countTeamBy();

    int countTeamByTeamNameEquals(@Param("team_name") String team_name);
    Optional<List<Team>> findAllByStatusOrderByCreatedTimeDesc(@Param("status")Status status);

    Optional<List<Team>> findAllByStatusOrderByCreatedTimeAsc(@Param("status")Status status);

    Optional<List<Team>> findDistinctTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(@Param("teamSkills")List<TeamSkill> teamSkills, @Param("status")Status status);

    Optional<List<Team>> findDistinctTeamsByTeamSkillsInAndStatusOrderByCreatedTimeAsc(@Param("teamSkills")List<TeamSkill> teamSkills, @Param("status")Status status);
    // NOTE 테스트용
    Optional<List<TeamListResponseMapping>> findDistinctTeamsByTeamSkillsInOrderByCreatedTimeDesc(@Param("teamSkills")List<TeamSkill> teamSkills);
    // NOTE 테스트용
    Optional<List<TeamListResponseMapping>> findAllByOrderByCreatedTimeDesc();

   Optional<Team> findTeamById(@Param("teamId") Long teamId);

   Optional<List<Team>> findTop5ByStatusOrderByViewCount(@Param("status") Status status);

}
