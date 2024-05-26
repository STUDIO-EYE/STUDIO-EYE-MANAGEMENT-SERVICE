package com.mju.management.domain.schedule.infrastructure;

import com.mju.management.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class UserSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_schedule_id")
    private Long userScheduleId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "is_checked")
    private boolean isChecked;

    public void update(CreateScheduleRequestDto updateScheduleRequestDto) {
        this.content = updateScheduleRequestDto.getContent();
        this.startDate = updateScheduleRequestDto.readStartDateAsLocalDateType();
        this.endDate = updateScheduleRequestDto.readEndDateAsLocalDateType();
    }

    @Builder
    public UserSchedule(Long userScheduleId, Long userId, String content, LocalDate startDate, LocalDate endDate, boolean isChecked) {
        this.userScheduleId = userScheduleId;
        this.userId = userId;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isChecked = isChecked;
    }
}
