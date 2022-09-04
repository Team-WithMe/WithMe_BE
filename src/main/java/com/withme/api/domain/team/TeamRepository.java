package com.withme.api.domain.team;

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

    // NOTE 기본적인 팀조회
    //String findTeams = "SELECT new map (T.id as team_idx, T.teamName as team_name, T.teamCategory as caregoty, T.teamDesc as desc, SK.skillTeams as skillTeams, U.nickname as nickname) FROM Team T INNER JOIN TeamSkill TS INNER JOIN Skill SK LEFT JOIN TeamUser TU LEFT JOIN User U ORDER BY T.id DESC";
    //String findAll = "SELECT T FROM Team T fetch all properties ORDER BY T.createdTime DESC";

      int countTeamBy();
    Optional<List<TeamListResponseMapping>> findAllByStatusOrderByCreatedTimeDesc(@Param("status")Status status);

    Optional<List<TeamListResponseMapping>> findAllByStatusOrderByCreatedTimeAsc(@Param("status")Status status);

    Optional<List<TeamListResponseMapping>> findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(@Param("teamSkills")List<TeamSkill> teamSkills, @Param("status")Status status);

    Optional<List<TeamListResponseMapping>> findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeAsc(@Param("teamSkills")List<TeamSkill> teamSkills, @Param("status")Status status);

   Optional<TeamListResponseMapping> findTeamById(@Param("teamId") Long teamId);


}
