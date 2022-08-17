package com.withme.api.domain.team;

import com.withme.api.controller.dto.TeamListResponseMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
//    String findTeams = "SELECT new map (T.teamIdx AS team_idx, T.teamName AS team_name, T.teamDesc AS team_desc) " +
//            "FROM Team T";
//    @Query(value = findTeams)
      Optional<List<TeamListResponseMapping>> findTeams();

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
}
