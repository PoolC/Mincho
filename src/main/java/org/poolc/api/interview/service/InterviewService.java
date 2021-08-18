package org.poolc.api.interview.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.*;
import org.poolc.api.interview.repository.InterviewSlotRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.service.MemberService;
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
    private final MemberService memberService;
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
    public void cancelApplicationInterviewSlot(Member loginMember, String loginId) {
        adminCancelInterviewSlot(loginMember, loginId);
        unacceptedCancelInterviewSlot(loginMember, loginId);
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
        InterviewSlot deleteInterviewSlot = getInterviewSlot(deleteSlotId);
        deleteInterviewSlot.deleteAllMembers();
        interviewSlotRepository.delete(deleteInterviewSlot);
    }

    @Transactional
    public void deleteAllInterviewSlots(Member admin) {
        checkAdmin(admin);
        interviewSlotRepository.findAll().stream().forEach(InterviewSlot::deleteAllMembers);
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

    private void adminCancelInterviewSlot(Member loginMember, String loginId) {
        if (loginMember.isAdmin()) {
            Member interviewCancelMember = memberService.getMemberByLoginID(loginId);
            InterviewSlot slot = interviewCancelMember.cancelInterviewSlot();
            memberRepository.saveAndFlush(interviewCancelMember);
            interviewSlotRepository.saveAndFlush(slot);
        }
    }

    private void unacceptedCancelInterviewSlot(Member loginMember, String loginId) {
        if (!loginMember.isAcceptedMember()) {
            Member interviewCancelMember = memberService.getMemberByLoginID(loginId);
            checkSameMember(loginMember, interviewCancelMember);
            InterviewSlot slot = loginMember.cancelInterviewSlot();
            memberRepository.saveAndFlush(loginMember);
            interviewSlotRepository.saveAndFlush(slot);
        }
    }

    private void checkSameMember(Member loginMember, Member interviewCancelMember) {
        if (!interviewCancelMember.equals(loginMember)) {
            throw new UnauthorizedException("본인 아이디가 아니면 허가되지 않습니다.");
        }
    }
}
