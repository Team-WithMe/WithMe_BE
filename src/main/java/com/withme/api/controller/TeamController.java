package com.withme.api.controller;


import com.withme.api.domain.team.TeamVo;
import com.withme.api.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/team")
@RestController
public class TeamController {

    private final TeamService teamService;

    /**
     * 팀 조회
     * */
    @PostMapping("/TeamList")
    private ResponseEntity selectTeam(@RequestBody(required = false) String jsonString){
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            if (jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                paramsMap = jsonObject.toMap();
            }
            List<Map<String, Object>> teamList = teamService.selectTeamList(paramsMap);
            log.info("teamList : " + teamList);
            if (teamList != null){
                return new ResponseEntity<>(teamList, HttpStatus.OK);
            }else {
                teamList.add((Map<String, Object>) paramsMap.put("result", "검색조회중 오류"));
                return new ResponseEntity<>(teamList, HttpStatus.OK);
            }

        }catch (Exception e){
            log.warn("[ERROR] : 팀 조회시 오류");
            e.printStackTrace();
            return new ResponseEntity<>("팀 조회중 Exception 발생", HttpStatus.BAD_REQUEST);
        }
    }

    // NOTE 찬규님이 보내준 데이터 맞춤으로 팀 추가
    @PostMapping("/createTeam-test")
    private ResponseEntity createTeam(@RequestBody(required = false) TeamVo teamVo) {
        try {

            // NOTE 팀 생성
            int result = teamService.createTeamTest(teamVo);

            if (result == 6){
                throw new Exception();
            }else if (result == 5){
                throw new NullPointerException();
            }else {
                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        }catch (NullPointerException e){
            log.warn("[ERROR] : 팀을 생성한 사용자가 조회되지않음");
            e.printStackTrace();
            return new ResponseEntity<>(5,  HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.warn("[ERROR] : 팀등록 중 오류");
            e.printStackTrace();
            return new ResponseEntity<>(6, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 팀 등록
     * */
    @PostMapping("/createTeam")
    private ResponseEntity createTeam(@RequestBody(required = false) String jsonString) {
        try {

            if (jsonString != null) {
                Long user_idx = 172L;

                JSONObject jsonObject = new JSONObject(jsonString);
                Map<String, Object> paramsMap = jsonObject.toMap();
                paramsMap.put("user_idx", user_idx);
                // NOTE 팀 생성
                int result = teamService.createTeam(paramsMap);

                if(result == 6){
                    throw new Exception();
                }else if (result == 5){
                    throw new NullPointerException();
                }else if (result == 4){
                    throw new JSONException("");
                }else{
                    return new ResponseEntity<>(result, HttpStatus.OK);
                }
            }else {
                // NOTE 데이터 없음
                return new ResponseEntity<>("팀 등록 데이터없음", HttpStatus.OK);
            }

        }catch (JSONException e) {
            log.warn("[ERROR] : 팀생성시 JSON 파싱오류");
            e.printStackTrace();
            return new ResponseEntity<>(1, HttpStatus.BAD_REQUEST);
        }catch (NullPointerException e){
            log.warn("[ERROR] : 팀을 생성한 사용자가 조회되지않음");
            e.printStackTrace();
            return new ResponseEntity<>(2, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.warn("[ERROR] : 팀등록 중 오류");
            e.printStackTrace();
            return new ResponseEntity<>(3, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * 팀 삭제
     * */
    @PostMapping("deleteTeam")
    private ResponseEntity deleteTeam(@RequestBody(required = false) String jsonString){
        try {
            if (jsonString != null) {
                JSONObject jsonObject = new JSONObject(jsonString);
                Map<String, Object> paramsMap = jsonObject.toMap();
                Map<String, Object> result = teamService.deleteTeam(paramsMap);
                if ("success".equals(result.get("result"))){
                    return new ResponseEntity("success", HttpStatus.OK);
                }else{
                    throw new Exception("팀 삭제중 Exception");
                }

            }else {
                return new ResponseEntity("삭제할 idx가 없음", HttpStatus.OK);
            }
        }catch (JSONException e){
            e.printStackTrace();
            return new ResponseEntity("JSON파싱 EXCEPTION", HttpStatus.BAD_REQUEST);
        }catch (Exception e ){
            e.printStackTrace();
            return new ResponseEntity("팀삭제중 EXCEPTION", HttpStatus.BAD_REQUEST);
        }
    }

}
