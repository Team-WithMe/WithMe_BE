package com.withme.api.domain.teamLike;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamLikeRepository extends JpaRepository<TeamLike, Long> {

    // NOTE 사용자의 게시물 좋아요 여부 체크
    @Query(value = "SELECT * FROM TEAM_LIKE TL WHERE TL.TEAM_IDX =:team_id AND TL.USER_IDX =:user_id", nativeQuery = true)
    Optional<TeamLike> countTeamLikesByTeamAndUser(@Param("team_id") Long team_id, @Param("user_id") Long user_id);

}
