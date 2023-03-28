package com.withme.api.domain.teamUser;

import com.withme.api.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {

    Optional<TeamUser> findTeamUserByTeamAndMemberType(@Param("team_id") Team team_id, @Param("member_type") MemberType member_type);
}
