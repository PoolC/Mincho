package org.poolc.api.interview.controller;

import org.poolc.api.interview.dto.InterviewSlotResponse;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.member.domain.Member;
import org.springframework.http.ResponseEntity;

import javax.naming.NoPermissionException;
import java.util.List;

public interface InterviewController {
    public ResponseEntity<List<InterviewSlotResponse>> getInterviewTable(Member member);

    public ResponseEntity<InterviewSlotResponse> applyInterviewSlot(Member member, Long slotId) throws NoPermissionException;

    public ResponseEntity<Void> cancelInterviewSlot(Member member, Long slotId);

    public ResponseEntity<Void> enrollInterviewSlot(Member member, RegisterInterviewSlotRequest request);

    public ResponseEntity<Void> updateInterviewSlot(Member member, Long slotId, UpdateInterviewSlotRequest request) throws NoPermissionException;

    public ResponseEntity<Void> deleteInterviewSlot(Member member, Long slotId);

    public ResponseEntity<Void> deleteAllInterviewSlot(Member member);
}
