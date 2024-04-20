package com.mju.management.domain.schedule.service;

import com.mju.management.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.mju.management.domain.schedule.dto.response.GetUserScheduleRes;
import com.mju.management.domain.schedule.infrastructure.UserSchedule;

import java.util.List;

public interface UserScheduleService {
    void createMySchedule(Long userId, CreateScheduleRequestDto createScheduleRequestDto);

    List<UserSchedule> getMySchedule(Long userId);

    void updateMySchedule(Long userId, Long userScheduleId, CreateScheduleRequestDto updateScheduleRequestDto);

    void deleteMyToDo(Long userId, Long userScheduleId);

    GetUserScheduleRes getMyScheduleOne(Long userScheduleId);
}
