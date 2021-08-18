package org.poolc.api.interview.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.InterviewTableResponse;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.interview.service.InterviewService;
import org.poolc.api.member.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;

@RestController
@RequestMapping("/interview")
@RequiredArgsConstructor
public class InterviewController {
    private final InterviewService interviewService;

    @GetMapping("/slots")
    public ResponseEntity<InterviewTableResponse> getInterviewTable(@AuthenticationPrincipal Member member) {
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/slots")
    public ResponseEntity<Void> enrollInterviewSlot(@AuthenticationPrincipal Member member, @RequestBody RegisterInterviewSlotRequest request) {
        interviewService.createInterviewSlot(member, request);
        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/slots/{slotId}")
    public ResponseEntity<Void> updateInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId, @RequestBody UpdateInterviewSlotRequest request) throws NoPermissionException {
        interviewService.updateInterviewSlot(member, slotId, request);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/slots/{slotId}")
    public ResponseEntity<Void> deleteInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId) {
        interviewService.deleteInterviewSlot(member, slotId);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping(value = "/slots")
    public ResponseEntity<Void> deleteAllInterviewSlot(@AuthenticationPrincipal Member member) {
        interviewService.deleteAllInterviewSlots(member);
        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/application/{slotId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterviewTableResponse> applyInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId) throws NoPermissionException {
        InterviewSlot slot = interviewService.applyInterviewSlot(member, slotId);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.accepted().body(response);
    }

    @DeleteMapping(value = "/application/{slotId}")
    public ResponseEntity<InterviewTableResponse> cancelInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId) {
        interviewService.cancelApplicationInterviewSlot(member, slotId);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.accepted().body(response);
    }


}
