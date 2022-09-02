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
//
//    // NOTE 팀 이름 검색시 조회
//    String findTeamsByTeamName = "SELECT new map (T.teamIdx AS team_idx, T.teamName AS team_name, T.teamDesc AS team_desc) " +
//            "FROM Team T WHERE T.teamName like :team_name AND T.shown = true";
//    @Query(value = findTeamsByTeamName)
//    List<Map<String, Object>> findTeamsByTeamName(@Param("team_name") String team_name);
//
//    @Query(value = "SELECT COUNT(T) FROM Team T WHERE T.teamName LIKE :team_name ")
//    int countTeamByTeamNameLike(@Param("team_name") String teamName);
//
    // NOTE 기본적인 팀조회
    String findTeams = "SELECT new map (T.id as team_idx, T.teamName as team_name, T.teamCategory as caregoty, T.teamDesc as desc, SK.skillTeams as skillTeams, U.nickname as nickname) FROM Team T INNER JOIN TeamSkill TS INNER JOIN Skill SK LEFT JOIN TeamUser TU LEFT JOIN User U ORDER BY T.id DESC";
    String findAll = "SELECT T FROM Team T fetch all properties ORDER BY T.createdTime DESC";
//      @Query(value = findTeams)
      Optional<List<TeamListResponseMapping>> findTeamsByOrderById();

      int countTeamBy();
//
//    int countTeamBy();
//    String findTeamsBy = "SELECT t FROM Team t WHERE t.skills in :skills";
//    @Query(value = findTeamsBy)
//    Optional<List<TeamListResponseMapping>> findTeamsBySkills(@Param("skills")Set<Skill> skills);
//
//    // NOTE SKILL로 팀 리스트 검색
//    Optional<List<TeamListResponseMapping>> findTeamBySkillsIn(@Param("skills")Set<Skill> skills);
//
//    Optional<List<TeamListResponseMapping>> findAllByShownIsTrue();
//
//    Optional<List<TeamListResponseMapping>> findTeamsBySkillsInAndShownIsTrue(@Param("skills")Set<Skill> skills);

//    String findTeamsBy = "SELECT t FROM Team t WHERE t.skills in :skills";
//    @Query(value = findTeamsBy)
//    Optional<List<TeamListResponseMapping>> findTeamsBySkills(@Param("skills") Set<Skill> skills);

    // NOTE SKILL로 팀 리스트 검색
//    Optional<List<TeamListResponseMapping>> findTeamBySkillsIn(@Param("skills")List<Skill> skills);

    Optional<List<TeamListResponseMapping>> findAllByStatusOrderByCreatedTimeDesc(@Param("status")Status status);
//    @Query("SELECT T FROM Team T where T.teamSkills in (:teamSkills)")
    Optional<List<TeamListResponseMapping>> findTeamsByTeamSkillsInAndStatusOrderByCreatedTimeDesc(@Param("teamSkills")List<TeamSkill> teamSkills, @Param("status")Status status);


//    @Query("SELECT T FROM Team T")
    List<TeamListResponseMapping> findTeamsBy();
}
