package org.poolc.api.interview.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.*;
import org.poolc.api.interview.repository.InterviewSlotRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewService {
    private final InterviewSlotRepository interviewSlotRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public InterviewTableResponse getInterviewTable(Member loginMember) {
        List<InterviewSlotsByDateResponse> interviewSlotsResponses = new ArrayList<>();
        interviewSlotRepository.findAllByFetchJoin().stream()
                .distinct()
                .map(slot -> new InterviewSlotResponse(slot, loginMember.isAdmin()))
                .collect(Collectors.groupingBy(InterviewSlotResponse::getDate))
                .forEach((k, v) -> interviewSlotsResponses.add(new InterviewSlotsByDateResponse(k, v)));

        InterviewTableResponse interviewTableResponse = new InterviewTableResponse(Optional.ofNullable(loginMember)
                .map(Member::getInterviewSlot)
                .map(InterviewSlot::getId)
                .orElse(null), interviewSlotsResponses);
        return interviewTableResponse;
    }

    @Transactional
    public void applyInterviewSlot(Member member, Long slotId) throws NoPermissionException {
        InterviewSlot slot = getInterviewSlot(slotId);
        checkIntervieweesAcceptable(slot);
        member.applyInterviewSlot(slot);
        interviewSlotRepository.saveAndFlush(slot);
        memberRepository.saveAndFlush(member);
    }

    @Transactional
    public void cancelApplicationInterviewSlot(Member member, Long slotId) {
        InterviewSlot slot = getInterviewSlot(slotId);
        member.cancelInterviewSlot(slotId);
        interviewSlotRepository.saveAndFlush(slot);
        memberRepository.saveAndFlush(member);
    }

    @Transactional
    public void createInterviewSlot(Member admin, RegisterInterviewSlotRequest request) {
        checkAdmin(admin);
        interviewSlotRepository.save(new InterviewSlot(request));
    }

    @Transactional
    public void updateInterviewSlot(Member admin, Long updateSlotId, UpdateInterviewSlotRequest request) throws NoPermissionException {
        checkAdmin(admin);
        InterviewSlot slot = getInterviewSlot(updateSlotId);
        checkUpdateCapacity(request, slot);

        slot.update(request);
        interviewSlotRepository.saveAndFlush(slot);
    }

    @Transactional
    public void deleteInterviewSlot(Member admin, Long deleteSlotId) {
        checkAdmin(admin);
        getInterviewSlot(deleteSlotId);
        interviewSlotRepository.delete(getInterviewSlot(deleteSlotId));
    }

    @Transactional
    public void deleteAllInterviewSlots(Member admin) {
        checkAdmin(admin);
        interviewSlotRepository.deleteAll();
    }

    private InterviewSlot getInterviewSlot(Long slotId) {
        return interviewSlotRepository.findById(slotId)
                .orElseThrow(() -> new NoSuchElementException("No slot found with given slotId"));
    }

    private void checkAdmin(Member member) {
        if (!member.isAdmin())
            throw new UnauthorizedException("No permission to create Interview slot");
    }

    private void checkIntervieweesAcceptable(InterviewSlot slot) throws NoPermissionException {
        if (!slot.checkIntervieweesAcceptable()) {
            throw new NoPermissionException("It's over capacity");
        }
    }

    private void checkUpdateCapacity(UpdateInterviewSlotRequest request, InterviewSlot slot) throws NoPermissionException {
        if (!slot.checkUpdateCapacity(request.getCapacity()))
            throw new NoPermissionException("The capacity to modify is less than current capacity");
    }
}
