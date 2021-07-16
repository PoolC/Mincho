package org.poolc.api.interview.service;

import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.member.domain.Member;

import javax.naming.NoPermissionException;
import java.util.List;

public interface InterviewService {
    public List<InterviewSlot> getAllInterviewSlot();

    public InterviewSlot applyInterviewSlot(Member member, Long slotId) throws NoPermissionException;

    public void cancelApplicationInterviewSlot(Member member, Long slotId);

    public void createInterviewSlot(Member admin, RegisterInterviewSlotRequest request);

    public void updateInterviewSlot(Member admin, Long updateSlotId, UpdateInterviewSlotRequest request) throws NoPermissionException;

    public void deleteInterviewSlot(Member admin, Long deleteSlotId);

    public void deleteAllInterviewSlots(Member admin);
}
