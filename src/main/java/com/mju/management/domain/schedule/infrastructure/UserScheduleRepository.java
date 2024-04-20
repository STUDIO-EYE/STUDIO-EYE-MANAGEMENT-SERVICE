package com.mju.management.domain.schedule.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {

    List<UserSchedule> findAllByUserId(Long userId);

    Optional<UserSchedule> findByUserScheduleIdAndUserId(Long userScheduleId, Long userId);
}
