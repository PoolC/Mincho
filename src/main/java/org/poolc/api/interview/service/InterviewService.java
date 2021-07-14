package org.poolc.api.interview.service;

import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.member.domain.Member;

import java.util.List;
import java.util.NoSuchElementException;

public interface InterviewService {
    public List<InterviewSlot> getAllInterviewSlot();

    public InterviewSlot applyInterviewSlot(Member member, Long slotId) throws NoSuchElementException;

    public void cancelApplicationInterviewSlot(Member member, Long slotId);

    public void createInterviewSlot(RegisterInterviewSlotRequest request);

    public void updateInterviewSlot(Long updateSlotId, UpdateInterviewSlotRequest request);

    public void deleteInterviewSlot(Long deleteSlotId);

    public void deleteAllInterviewSlots();
}
