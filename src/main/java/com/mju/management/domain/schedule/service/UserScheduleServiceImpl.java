package com.mju.management.domain.schedule.service;

import com.mju.management.domain.schedule.dto.reqeust.CreateScheduleRequestDto;
import com.mju.management.domain.schedule.dto.reqeust.ScheduleWithDateReq;
import com.mju.management.domain.schedule.dto.response.GetUserScheduleRes;
import com.mju.management.domain.schedule.infrastructure.UserSchedule;
import com.mju.management.domain.schedule.infrastructure.UserScheduleRepository;
import com.mju.management.global.model.Exception.ExceptionList;
import com.mju.management.global.model.Exception.NonExistentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserScheduleServiceImpl implements UserScheduleService{

    private final UserScheduleRepository userScheduleRepository;
    @Override
    @Transactional
    public void createMySchedule(Long userId, CreateScheduleRequestDto createScheduleRequestDto) {

        UserSchedule userSchedule = UserSchedule.builder()
                .userId(userId)
                .content(createScheduleRequestDto.getContent())
                .startDate(createScheduleRequestDto.readStartDateAsLocalDateType())
                .endDate(createScheduleRequestDto.readEndDateAsLocalDateType())
                .isChecked(false)
                .build();

        userScheduleRepository.save(userSchedule);
    }

    @Override
    public List<UserSchedule> getMySchedule(Long userId) {

        List<UserSchedule> allByUserId = userScheduleRepository.findAllByUserId(userId);
        if (!allByUserId.isEmpty())
            return allByUserId;
        else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);
    }

    // 날짜로 조회
    @Override
    public List<UserSchedule> getMyScheduleWithDate(Long userId, ScheduleWithDateReq scheduleWithDateReq) {

        List<UserSchedule> allByUserId = userScheduleRepository.findAllByUserId(userId);
        LocalDate date = scheduleWithDateReq.readDateAsLocalDateType();
        List<UserSchedule> userScheduleList = new ArrayList<>();

        for (UserSchedule userSchedule : allByUserId) {
            LocalDate startDate = userSchedule.getStartDate().minusDays(1);
            LocalDate endDate = userSchedule.getEndDate().plusDays(1);
            if (date.isAfter(startDate) && date.isBefore(endDate))
                userScheduleList.add(userSchedule);

        }

        if (!userScheduleList.isEmpty())
            return userScheduleList;
        else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_CHECKLIST);


    }

    @Override
    @Transactional
    public void updateMySchedule(Long userId, Long userScheduleId, CreateScheduleRequestDto updateScheduleRequestDto) {
        Optional<UserSchedule> findUserSchedule = userScheduleRepository.findByUserScheduleIdAndUserId(userScheduleId, userId);
        if (findUserSchedule.isPresent()) {
            UserSchedule userSchedule = findUserSchedule.get();
            userSchedule.update(updateScheduleRequestDto);
        } else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);

    }

    @Override
    @Transactional
    public void deleteMyToDo(Long userId, Long userScheduleId) {

        Optional<UserSchedule> findUserSchedule = userScheduleRepository.findByUserScheduleIdAndUserId(userScheduleId, userId);
        if (findUserSchedule.isPresent()) {
            UserSchedule userSchedule = findUserSchedule.get();
            userScheduleRepository.delete(userSchedule);
        } else
            throw new NonExistentException(ExceptionList.NON_EXISTENT_PROJECT);

    }

    @Override
    public GetUserScheduleRes getMyScheduleOne(Long userScheduleId) {

        UserSchedule userSchedule = userScheduleRepository.findById(userScheduleId)
                .orElseThrow(()-> new NonExistentException(ExceptionList.NON_EXISTENT_SCHEDULE));
        return GetUserScheduleRes.from(userSchedule);
    }

}
