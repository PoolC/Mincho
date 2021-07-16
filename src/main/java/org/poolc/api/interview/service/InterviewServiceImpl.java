package org.poolc.api.interview.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.interview.repository.InterviewSlotRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewSlotRepository interviewSlotRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional(readOnly = true)
    public List<InterviewSlot> getAllInterviewSlot() {
        return interviewSlotRepository.findAll();
    }

    @Override
    @Transactional
    public InterviewSlot applyInterviewSlot(Member member, Long slotId) throws NoPermissionException {
        InterviewSlot slot = getInterviewSlot(slotId);
        checkIntervieweesAcceptable(slot);
        member.applyInterviewSlot(slot);
        slot.insertMember(member);
        memberRepository.saveAndFlush(member);
        return slot;
    }

    @Override
    @Transactional
    public void cancelApplicationInterviewSlot(Member member, Long slotId) {
        InterviewSlot slot = getInterviewSlot(slotId);
        member.cancelInterviewSlot(slotId);
        slot.deleteMember(member);
        memberRepository.saveAndFlush(member);
    }

    @Override
    @Transactional
    public void createInterviewSlot(Member admin, RegisterInterviewSlotRequest request) {
        checkAdmin(admin);
        checkDuplicateSlotWithDateAndStartTimeAndEndTime(request);
        interviewSlotRepository.save(new InterviewSlot(request));
    }

    @Override
    @Transactional
    public void updateInterviewSlot(Member admin, Long updateSlotId, UpdateInterviewSlotRequest request) throws NoPermissionException {
        checkAdmin(admin);
        InterviewSlot slot = getInterviewSlot(updateSlotId);
        checkUpdateCapacity(request, slot);
        checkDuplicateSlotWithDateAndStartTimeAndEndTime(request, slot);

        slot.update(request);
        interviewSlotRepository.saveAndFlush(slot);
    }

    @Override
    @Transactional
    public void deleteInterviewSlot(Member admin, Long deleteSlotId) {
        checkAdmin(admin);
        getInterviewSlot(deleteSlotId);
        interviewSlotRepository.delete(getInterviewSlot(deleteSlotId));
    }

    @Override
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

    private void checkDuplicateSlotWithDateAndStartTimeAndEndTime(RegisterInterviewSlotRequest request) {
        if (interviewSlotRepository.findByDateAndStartTimeAndEndTime(request.getDate(), request.getStartTime(), request.getEndTime()).isPresent())
            throw new IllegalArgumentException("exist duplicate slot with date, startTime, endTime");
    }

    private void checkDuplicateSlotWithDateAndStartTimeAndEndTime(UpdateInterviewSlotRequest request, InterviewSlot interviewSlot) {
        if (interviewSlotRepository.findByDateAndStartTimeAndEndTime(interviewSlot.getDate(), request.getStartTime(), request.getEndTime()).isPresent())
            throw new IllegalArgumentException("exist duplicate slot with date, startTime, endTime");
    }

    private void checkUpdateCapacity(UpdateInterviewSlotRequest request, InterviewSlot slot) throws NoPermissionException {
        if (!slot.checkUpdateCapacity(request.getCapacity()))
            throw new NoPermissionException("The capacity to modify is less than current capacity");
    }
}
