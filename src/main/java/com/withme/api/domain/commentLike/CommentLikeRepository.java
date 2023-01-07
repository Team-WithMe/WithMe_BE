package com.withme.api.domain.commentLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    // NOTE 사용자의 댓글 좋아요 여부 체크
    @Query(value = "SELECT * FROM COMMENT_LIKE CL WHERE CL.TEAM_ID =:team_id AND CL.USER_ID =:user_id AND CL.COMMENT_ID =:comment_id ", nativeQuery = true)
    Optional<CommentLike> findCommentLikeByTeamAndUserAndTeamComment(@Param("team_id") Long team_id, @Param("user_id") Long user_id, @Param("comment_id") Long comment_id);

}
