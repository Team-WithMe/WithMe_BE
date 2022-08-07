package com.withme.api.controller;


import com.withme.api.controller.dto.CreateTeamRequestDto;
import com.withme.api.controller.dto.TeamListResponseMapping;
import com.withme.api.controller.dto.TeamSearchDto;
import com.withme.api.domain.team.Team;
import com.withme.api.service.TeamService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @Operation(
            summary = "팀 생성"
            , description = "사용자가 등록한 팀의 정보를 DB에 저장한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201"
                    ,description = "팀 등록 성공"
            )
            , @ApiResponse(
                    responseCode = "400"
                    ,description = "팀을 생성사용자 조회시 오류"
                    ,content = {@Content(schema = @Schema(example = "NullPointException"))}
            )
            , @ApiResponse(
                    responseCode = "500"
                    ,description = "팀등록 중 오류"
                    ,content = {@Content(schema = @Schema(example = "Exception"))}
            )
    })
    @PostMapping("/createTeam-test")
    private ResponseEntity createTeam(@RequestBody(required = false) CreateTeamRequestDto createTeamRequestDto) {
        Map<String, Object> res = new HashMap<>();
        try {

            // NOTE 팀 생성
            int result = teamService.createTeamTest(createTeamRequestDto);

            if (result == 6){
                throw new Exception();
            }else if (result == 5){
                throw new NullPointerException();
            }else {
                res.put("status", 200);
                return new ResponseEntity<>(res, HttpStatus.CREATED);
            }

        }catch (NullPointerException e){
            log.warn("[ERROR] : 팀을 생성한 사용자가 조회되지않음");
            e.printStackTrace();
            res.put("status", 400);
            return new ResponseEntity<>(res,  HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.warn("[ERROR] : 팀등록 중 오류");
            e.printStackTrace();
            res.put("status", 500);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     *  팀 목록 조회, 태그 검색
     * */
    @PostMapping("/TeamListTest")
    @ResponseBody
    private ResponseEntity teamListTest(@RequestBody(required = false) TeamSearchDto teamSearchDto) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<TeamListResponseMapping> teamList = teamService.getTeamList(teamSearchDto);
            result.put("teamList", teamList);
            return new ResponseEntity(teamList, HttpStatus.OK);
        }catch (Exception e){
            log.warn("[ERROR] : 팀 조회 중 오류");
            e.printStackTrace();
            return new ResponseEntity("팀 조회 중 오류 발생!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 팀 등록
     * */
    @ApiOperation(value = "팀 생성")
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
