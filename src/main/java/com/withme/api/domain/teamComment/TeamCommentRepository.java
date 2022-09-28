package com.withme.api.domain.teamComment;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamNotice.TeamNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamCommentRepository extends JpaRepository<TeamComment, Long> {

    Optional<List<TeamComment>> findTeamCommentByTeamAndParentIsNullOrderByIdDesc(@Param("team") Team team);

    @Query("SELECT TC FROM TeamComment TC INNER JOIN TC.parent TCP INNER JOIN TC.team TCT INNER JOIN TC.user TCU WHERE TCT.id =:team_id AND TCP.id =:comment_id ORDER BY TC.id ASC")
    List<TeamComment> findTeamCommentsByTeamIdAndId(@Param("team_id") Long team_id, @Param("comment_id") Long comment_id);

    @Query("SELECT TC FROM TeamComment TC WHERE TC.team.id =:team_id AND TC.id =:comment_id ORDER BY TC.id ASC")
    TeamComment findTeamCommentByTeamIdAndId(@Param("team_id") Long team_id, @Param("comment_id") Long comment_id);
}
