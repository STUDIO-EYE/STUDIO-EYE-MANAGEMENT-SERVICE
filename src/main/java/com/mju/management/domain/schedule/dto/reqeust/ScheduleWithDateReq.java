package com.mju.management.domain.schedule.dto.reqeust;

import com.mju.management.global.model.exception.ExceptionList;
import com.mju.management.global.model.exception.InvalidDateFormatException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleWithDateReq {

    @NotBlank(message = "조회할 날짜를 입력해주세요.")
    @Schema(description = "조회할 날짜", defaultValue = "2024-04-23")
    private String date;

    public LocalDate readDateAsLocalDateType(){
        return convertStringToLocalDate(date);
    }

    public LocalDate convertStringToLocalDate(String date){
        try{
            return LocalDate.parse(date, DateTimeFormatter.ISO_DATE);
        }catch (DateTimeParseException e){
            throw new InvalidDateFormatException(ExceptionList.INVALID_DATE_FORMAT);
        }
    }
}
