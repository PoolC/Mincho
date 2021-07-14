package org.poolc.api.interview.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.interview.repository.InterviewSlotRepository;
import org.poolc.api.member.domain.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {
    private final InterviewSlotRepository interviewSlotRepository;

    @Transactional(readOnly = true)
    @Override
    public List<InterviewSlot> getAllInterviewSlot() {
        return interviewSlotRepository.findAll();
    }

    @Transactional
    @Override
    public InterviewSlot applyInterviewSlot(Member member, Long slotId) {
        InterviewSlot slot = getInterviewSlot(slotId);
        member.applyInterviewSlot(slot);
        slot.insertMember(member);
        return slot;
    }

    @Transactional
    @Override
    public void cancelApplicationInterviewSlot(Member member, Long slotId) {
        InterviewSlot slot = getInterviewSlot(slotId);
        member.cancelInterviewSlot();
        slot.deleteMember(member);
    }

    @Transactional
    @Override
    public void createInterviewSlot(RegisterInterviewSlotRequest request) {
        interviewSlotRepository.save(InterviewSlot.of(request));
    }

    @Transactional
    @Override
    public void updateInterviewSlot(Long updateSlotId, UpdateInterviewSlotRequest request) {
        InterviewSlot interviewSlot = getInterviewSlot(updateSlotId);
        interviewSlot.update(request);
    }

    @Transactional
    @Override
    public void deleteInterviewSlot(Long deleteSlotId) {
        interviewSlotRepository.delete(getInterviewSlot(deleteSlotId));
    }

    @Override
    public void deleteAllInterviewSlots() {
        interviewSlotRepository.deleteAll();
    }

    private InterviewSlot getInterviewSlot(Long slotId) {
        return interviewSlotRepository.findById(slotId).orElseThrow(() -> new NoSuchElementException("No slot found with given slotId"));
    }
}
