package com.withme.api.domain.teamSkill;

import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.domain.skill.Skill;
import com.withme.api.domain.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeamSkillRepository extends JpaRepository<TeamSkill, Long> {

    Optional<List<TeamListResponseMapping>> findTeamSkillsBySkillIn(@Param("skill")List<Skill> skill);
}
