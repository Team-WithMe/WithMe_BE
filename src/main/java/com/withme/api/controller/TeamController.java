package com.withme.api.controller;

import com.withme.api.controller.dto.*;
import com.withme.api.jwt.TokenProvider;
import com.withme.api.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class TeamController {

    private final TeamService teamService;

    private final TokenProvider tokenProvider;
    @Operation(
            summary = "팀 리스트 조회"
            , description = "팀 리스트를 검색, 정렬 기능으로 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200"
                    ,description = "팀 리스트 조회 성공"
            )
            , @ApiResponse(
            responseCode = "400"
            ,description = "검색 조건 파라미터 오류"
            ,content = {@Content(schema = @Schema(example = "BAD_REQUEST"))}
    )
            , @ApiResponse(
            responseCode = "422"
            ,description = "팀리스트 결과 없음"
            ,content = {@Content(schema = @Schema(example = "UNPROCESSABLE_ENTITY"))}
    )
            , @ApiResponse(
            responseCode = "500"
            ,description = "팀리스트 조회 중 오류"
            ,content = {@Content(schema = @Schema(example = "INTERNAL_SERVER_ERROR"))}
    )
    })
    /**
     * 팀 조회
     * */
    @ResponseBody
    @PostMapping("/team/team-list")
    private ResponseEntity selectTeam(@Valid@RequestBody(required = false) TeamSearchDto params){
        try {
            log.info("params = " + params);
            params.getSkills().forEach(v -> System.out.println("v = " + v));
            List<TeamListResponseMapping> teamData = teamService.getTeamList(params);
            log.info("teamData : " + teamData);
            if (teamData != null){
                return new ResponseEntity<>(teamData, HttpStatus.OK);
            }else {
                return new ResponseEntity<>(teamData, HttpStatus.BAD_REQUEST);
            }

        }catch (NullPointerException e){
            log.warn("[ERROR] : 팀 조회시 조건에 맞는 팀이 존재하지 않음");
            e.printStackTrace();
            return new ResponseEntity<>("팀 조회중 값을 찾지 못함", HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (Exception e){
            log.warn("[ERROR] : 팀 조회시 오류");
            e.printStackTrace();
            return new ResponseEntity<>("팀 조회중 Exception 발생", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @PostMapping("/team")
    private ResponseEntity createTeam(
           @Valid @RequestBody(required = false) CreateTeamRequestDto createTeamRequestDto
         // , @RequestHeader("Authorization") String authHeader
    ) {
        Map<String, Object> res = new HashMap<>();
        try {
            // NOTE 팀 생성
            Long teamIdx = teamService.createTeam(createTeamRequestDto
                    // ,authHeader
            );
            res.put("status", 201);
            res.put("teamIdx", teamIdx);
            return new ResponseEntity<>(res, HttpStatus.CREATED);

        }catch (NullPointerException e){
            log.warn("[ERROR] : 팀을 생성한 사용자가 조회되지않음");
            e.printStackTrace();
            res.put("status", 422);
            return new ResponseEntity<>(res,  HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.warn("[ERROR] : 팀등록 중 오류");
            e.printStackTrace();
            res.put("status", 500);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "팀 상세정보 조회"
            , description = "팀 상세정보를 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201"
                    , description = "팀 상세정보 조회 성공"
            ),
            @ApiResponse(
                    responseCode = "422"
                    , description = "팀 상세정보 조회 실패"
            ),
            @ApiResponse(
                    responseCode = "500"
                    , description = "팀 상세정보 조회 중 서버오류"
            )
    })
    @GetMapping("/team/{teamId}/detail")
    public ResponseEntity teamDetail(@PathVariable(value = "teamId") Long teamId) {
        Map<String, Object> result = new HashMap<>();
        try{
            result.put("teamDetail", teamService.getTeamListByTeamId(teamId));
            result.put("status", 201);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }catch (NullPointerException e){
            e.printStackTrace();
            result.put("status", 422);
            return new ResponseEntity<>(result, HttpStatus.UNPROCESSABLE_ENTITY);
        }catch (Exception e){
            e.printStackTrace();
            result.put("status", 500);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Operation(
            summary = "팀 게시물 제목, 내용 업데이트"
            , description = "팀 제목, 내용을 업데이트한다."
    )@ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201"
                    , description = "팀 제목, 내용을 업데이트 성공"
            )
    })
    @PutMapping("/team/{teamId}/post")
    public ResponseEntity teamPostUpdate(@Valid @RequestBody TeamPostUpdateRequestDto teamPostUpdateRequestDto,
                                         @PathVariable(value = "teamId") Long teamId){
        Map<String, Object> result = new HashMap<>();
        Long teamIdx = teamService.teamPostUpdate(teamPostUpdateRequestDto, teamId);
        result.put("teamIdx", teamIdx);
        result.put("status", 201);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
    @Operation(
            summary = "팀 게시물 댓글 추가"
            , description = "팀 댓글을 추가한다."
    )@ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201"
                    , description = "팀 댓글 추가 성공"
            )
    })
    @PostMapping("/team/{teamId}/comment")
    public ResponseEntity teamCommentRegister(@Valid @RequestBody TeamCommentAddRequestDto dto,
                                         @PathVariable(value = "teamId") Long teamId){
        Map<String, Object> result = new HashMap<>();
        Long teamIdx = teamService.addTeamComment(dto.getContent(), teamId);
        result.put("teamIdx", teamIdx);
        result.put("status", 201);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @Operation(
            summary = "공지사항 작성"
            , description = "팀 공지사항을 작성한다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201"
            , description = "팀 공지사항 작성 성공"
        )
    })
    @PostMapping("/team/{teamId}/notice")
    public ResponseEntity<Object> createTeamNotice(
            @PathVariable Long teamId
            , @Valid @RequestBody TeamNoticeCreateRequestDto dto
            , @RequestHeader("Authorization") String authHeader
    ) {
        log.debug("createTeamNotice {}, {} invoked", teamId, dto);

        teamService.createTeamNotice(teamId, dto, authHeader);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
