package com.withme.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TeamService {

//    private final TeamRepository teamRepository;
//    private final UserRepository userRepository;
//
//    /**
//     * 팀 리스트 조회
//     * */
//    public List<Map<String, Object>> selectTeamList(Map<String, Object> params){
//        try {
//            // NOTE null 값이 아니면 검색 조건 분기
//            if (params != null){
//                HashMap<String, Object> teamsCountMap = new HashMap<>();
//                // NOTE 팀이름 검색
//                if (params.containsKey("team_name")){
//                    List<Map<String, Object>> teamsList = Optional.ofNullable(teamRepository.findTeamsByTeamName("%" + params.get("team_name") + "%"))
//                            .orElseThrow(()-> new NullPointerException("팀 검색 조회 중 오류"));
//
//                    // NOTE 팀이름 카운트 조회
//                    int countTeamByTeamNameLike = teamRepository.countTeamByTeamNameLike("%" + params.get("team_name") + "%");
//                    teamsCountMap.put("team_count", countTeamByTeamNameLike);
//                    teamsList.add(teamsCountMap);
//
//                    return teamsList;
//                }else{
//                    List<Map<String, Object>> teamsList = Optional.ofNullable(teamRepository.findTeams())
//                            .orElseThrow(()-> new NullPointerException("팀 조회 중 오류"));
//
//                    // NOTE 팀 카운트 조회
//                    int countTeamBy = teamRepository.countTeamBy();
//                    teamsCountMap.put("team_count", countTeamBy);
//                    teamsList.add(teamsCountMap);
//
//                    return teamsList;
//                }
//
//            }else{
//                throw new NullPointerException();
//            }
//        }catch (NullPointerException e){
//            e.printStackTrace();
//            return null;
//        }catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    /**
//     * 팀 등록
//     * */
//    @Transactional
//    public int createTeam(Map<String, Object> paramsMap) {
//        try{
//            if (paramsMap != null){
//                Team team = Team.builder()
////                        .teamCategory(String.valueOf(paramsMap.getOrDefault("team_category", "기본 카테고리")))
//                        .teamDesc(String.valueOf(paramsMap.getOrDefault("team_desc", "기본 정보")))
//                        .teamName(String.valueOf(paramsMap.getOrDefault("team_name", "기본 팀이름")))
////                        .teamIntroduce(String.valueOf(paramsMap.getOrDefault("team_introduce", "기본 팀소개")))
////                        .teamNotice(String.valueOf(paramsMap.getOrDefault("team_notice", "기본 공지사항")))
////                        .shown(true)
//                        .build();
//
//                Long user_idx = ((Number)paramsMap.get("user_idx")).longValue();
//                User user = userRepository.findById(user_idx).orElseThrow(
//                        ()-> new NullPointerException("존재하지않는 사용자"));
//
//                log.warn("team ::: " + user);
//                // NOTE 팀등록
////                team.getMembers().add(user);
//                teamRepository.save(team);
//                return 1;
//            }else {
//                log.warn("[ERROR] : 파싱된 팀 정보가 없음 ");
//                return 4;
//            }
//        }catch (NullPointerException e){
//            return 5;
//        }catch (Exception e){
//            return 6;
//        }
//
//
//    }
//
//    /**
//     * 팀 등록
//     * */
//    public int createTeamTest(CreateTeamRequestDto createTeamDto) {
//        try{
//            Long user_idx = 1L;
//
//            Team team = Team.builder()
////                    .teamCategory("")
//                    .teamDesc(createTeamDto.getDescription())
//                    .teamName(createTeamDto.getName())
////                    .teamIntroduce("")
////                    .teamNotice("")
////                    .shown(true)
//                    .build();
//
//            // NOTE 스킬 입력
//            Skill skill = new Skill();
//            for (String skillName : createTeamDto.getSkills()) {
////                skill.setSkillName(skillName);
////                team.getSkills().add(skill);
//                teamRepository.save(team);
//            }
//
//            User user = userRepository.findById(user_idx).orElseThrow(
//                    ()-> new NullPointerException("존재하지않는 사용자"));
//
//            log.info("team ::: " + user);
//            // NOTE 팀등록
////            team.getMembers().add(user);
//            teamRepository.save(team);
//            return 1;
//
//        }catch (NullPointerException e){
//            return 5;
//        }catch (Exception e){
//            return 6;
//        }
//    }
//
//    /**
//     * 팀 삭제
//     * */
//    public Map<String, Object> deleteTeam(Map<String, Object> params){
//        HashMap<String, Object> result = new HashMap<>();
//        try {
//            teamRepository.deleteById(((Number)params.get("team_idx")).longValue());
//            result.put("result", "success");
//            return result;
//        }catch (Exception e){
//            e.printStackTrace();
//            result.put("result", "Exception");
//            return result;
//        }
//    }
//
//    public List<TeamListResponseMapping> getTeamList(TeamSearchDto teamSearchDto) throws Exception {
//        // NOTE 스킬 입력
//        Set<Skill> skills = new HashSet<>();
//        for (Skill s: teamSearchDto.getSkills()){
//                skills.add(s);
//        }
//        List<TeamListResponseMapping> teamList;
//
//        if (skills.size() <= 0){
//            teamList = teamRepository.findAllByShownIsTrue().orElseThrow(
//                    () -> new Exception("팀 조회 오류 (검색X)")
//            );
//        }else{
//            teamList = teamRepository.findTeamsBySkillsInAndShownIsTrue(skills).orElseThrow(
//                    () -> new Exception("팀 조회 오류 (검색O)")
//            );
//        }
//        return teamList;
//
//    }
}
