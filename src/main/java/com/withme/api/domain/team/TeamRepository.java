package com.withme.api.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    // NOTE 팀 이름 검색시 조회
    String findTeamsByTeamName = "SELECT new map (T.teamIdx AS team_idx, T.teamName AS team_name, T.teamDesc AS team_desc) " +
            "FROM Team T WHERE T.teamName like :team_name AND T.shown = true";
    @Query(value = findTeamsByTeamName)
    List<Map<String, Object>> findTeamsByTeamName(@Param("team_name") String team_name);

    @Query(value = "SELECT COUNT(T) FROM Team T WHERE T.teamName LIKE :team_name ")
    int countTeamByTeamNameLike(@Param("team_name") String teamName);

    // NOTE 기본적인 팀조회
    String findTeams = "SELECT new map (T.teamIdx AS team_idx, T.teamName AS team_name, T.teamDesc AS team_desc) " +
            "FROM Team T";
    @Query(value = findTeams)
    List<Map<String, Object>> findTeams();

    int countTeamBy();
}
