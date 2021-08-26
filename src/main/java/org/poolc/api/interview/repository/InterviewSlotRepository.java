package org.poolc.api.interview.repository;

import org.poolc.api.interview.domain.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
    @Query("SELECT i FROM interview_slots i LEFT JOIN FETCH i.interviewees")
    List<InterviewSlot> findAllByFetchJoin();

    Optional<InterviewSlot> findByDateAndStartTimeAndEndTime(LocalDate date, LocalTime startTime, LocalTime endTime);
}
