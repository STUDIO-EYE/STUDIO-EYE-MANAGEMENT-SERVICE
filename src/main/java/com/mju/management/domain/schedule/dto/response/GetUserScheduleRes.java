package com.mju.management.domain.schedule.dto.response;

import com.mju.management.domain.schedule.infrastructure.Schedule;
import com.mju.management.domain.schedule.infrastructure.UserSchedule;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserScheduleRes {

    private Long scheduleId;
    private Long userId;
    private String content;
    private LocalDate startDate;
    private LocalDate endDate;

    public static GetUserScheduleRes from(UserSchedule userSchedule){
        return GetUserScheduleRes.builder()
                .scheduleId(userSchedule.getUserScheduleId())
                .userId(userSchedule.getUserId())
                .content(userSchedule.getContent())
                .startDate(userSchedule.getStartDate())
                .endDate(userSchedule.getEndDate())
                .build();
    }
}
