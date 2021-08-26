package org.poolc.api.interview.controller;

import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<InterviewTableResponse> enrollInterviewSlot(@AuthenticationPrincipal Member member, @RequestBody RegisterInterviewSlotRequest request) {
        interviewService.createInterviewSlot(member, request);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping(value = "/slots/{slotId}")
    public ResponseEntity<InterviewTableResponse> updateInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId, @RequestBody UpdateInterviewSlotRequest request) throws NoPermissionException {
        interviewService.updateInterviewSlot(member, slotId, request);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/slots/{slotId}")
    public ResponseEntity<InterviewTableResponse> deleteInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId) {
        interviewService.deleteInterviewSlot(member, slotId);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/slots")
    public ResponseEntity<InterviewTableResponse> deleteAllInterviewSlot(@AuthenticationPrincipal Member member) {
        interviewService.deleteAllInterviewSlots(member);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/application/{slotId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InterviewTableResponse> applyInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable Long slotId) throws NoPermissionException {
        interviewService.applyInterviewSlot(member, slotId);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping(value = "/application/{loginId}")
    public ResponseEntity<InterviewTableResponse> cancelInterviewSlot(@AuthenticationPrincipal Member member, @PathVariable String loginId) {
        interviewService.cancelApplicationInterviewSlot(member, loginId);
        InterviewTableResponse response = interviewService.getInterviewTable(member);
        return ResponseEntity.ok().body(response);
    }


}
