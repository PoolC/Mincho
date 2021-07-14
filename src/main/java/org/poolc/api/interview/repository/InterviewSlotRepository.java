package org.poolc.api.interview.repository;

import org.poolc.api.interview.domain.InterviewSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InterviewSlotRepository extends JpaRepository<InterviewSlot, Long> {
}
