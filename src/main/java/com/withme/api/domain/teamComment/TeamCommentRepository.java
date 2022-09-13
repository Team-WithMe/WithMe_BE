package com.withme.api.domain.teamComment;

import com.withme.api.domain.team.Team;
import com.withme.api.domain.teamNotice.TeamNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamCommentRepository extends JpaRepository<TeamComment, Long> {

    Optional<List<TeamComment>> findTeamCommentByTeamAndParentIsNullOrderByIdDesc(@Param("team") Team team);

    List<TeamComment> findTeamCommentsByTeamIdAndId(@Param("team_id") Long team_id, @Param("comment_id") Long comment_id);
}
