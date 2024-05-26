package com.mju.management.domain.schedule.controller;

import com.mju.management.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.mju.management.domain.schedule.dto.reqeust.ScheduleWithDateReq;
import com.mju.management.domain.schedule.dto.response.GetUserScheduleRes;
import com.mju.management.domain.schedule.infrastructure.UserSchedule;
import com.mju.management.domain.schedule.service.UserScheduleService;
import com.mju.management.global.config.jwtInterceptor.JwtContextHolder;
import com.mju.management.global.model.Result.CommonResult;
import com.mju.management.global.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@Tag(name = "내 일정 CRUD API", description = "내 일정 CRUD API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userSchedules")
@CrossOrigin("*")
public class UserScheduleController {

    private final UserScheduleService userScheduleService;
    private final ResponseService responseService;

    //일정 등록
    @Operation(summary = "내 일정 등록", description = "내 일정 등록 API")
    @ResponseStatus(OK)
    @PostMapping
    public CommonResult createMySchedule(@Valid @RequestBody CreateScheduleRequestDto createScheduleRequestDto){
        userScheduleService.createMySchedule(JwtContextHolder.getUserId(), createScheduleRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 내 일정 하나 조회
    @Operation(summary = "내 일정 하나 조회", description = "내 일정 하나 조회 API")
    @ResponseStatus(OK)
    @GetMapping("/{userScheduleId}")
    public CommonResult getMyScheduleOne(@PathVariable Long userScheduleId) {
        GetUserScheduleRes schedule = userScheduleService.getMyScheduleOne(userScheduleId);
        return responseService.getSingleResult(schedule);
    }

    // 내 일정 목록 조회
    @Operation(summary = "내 일정 목록 조회", description = "내 일정 목록 조회 API")
    @ResponseStatus(OK)
    @GetMapping
    public CommonResult getMySchedule() {
        List<UserSchedule> userScheduleList = userScheduleService.getMySchedule(JwtContextHolder.getUserId());
        return responseService.getListResult(userScheduleList);
    }

    // 특정 날짜의 내 일정 목록 조회
    @Operation(summary = "특정 날짜의 내 일정 목록 조회", description = "특정 날짜의 내 일정 목록 조회 API")
    @ResponseStatus(OK)
    @GetMapping("/date")
    public CommonResult getMyScheduleWithDate(@RequestBody ScheduleWithDateReq scheduleWithDateReq) {
        List<UserSchedule> userScheduleList = userScheduleService.getMyScheduleWithDate(JwtContextHolder.getUserId(), scheduleWithDateReq);
        return responseService.getListResult(userScheduleList);
    }

    // 내 일정 수정
    @Operation(summary = "내 일정 수정", description = "내 일정 수정 API")
    @ResponseStatus(OK)
    @PutMapping("/{userScheduleId}")
    public CommonResult updateMySchedule(
            @PathVariable Long userScheduleId,
            @Valid @RequestBody CreateScheduleRequestDto updateScheduleRequestDto
    ) {
        userScheduleService.updateMySchedule(JwtContextHolder.getUserId(), userScheduleId, updateScheduleRequestDto);
        return responseService.getSuccessfulResult();
    }

    // 내 일정 삭제
    @DeleteMapping("/{userScheduleId}")
    @Operation(summary = "내 일정 삭제", description = "내 일정 삭제 api")
    public CommonResult deleteMyToDo(@PathVariable Long userScheduleId) {
        userScheduleService.deleteMyToDo(JwtContextHolder.getUserId(), userScheduleId);
        return responseService.getSuccessfulResult();
    }

}
