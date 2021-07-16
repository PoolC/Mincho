package org.poolc.api.interview.repository;

import org.poolc.api.interview.domain.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    Optional<InterviewSlot> findByDateAndStartTimeAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);
}
