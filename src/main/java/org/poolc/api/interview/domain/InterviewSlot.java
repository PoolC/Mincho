package org.poolc.api.interview.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.interview.dto.RegisterInterviewSlotRequest;
import org.poolc.api.interview.dto.UpdateInterviewSlotRequest;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity(name = "interview_slots")
@SequenceGenerator(
        name = "INTERVIEW_SLOTS_SEQ_GENERATOR",
        sequenceName = "INTERVIEW_SLOTS_SEQ",
        allocationSize = 1
)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_date_start_time_end_time",
                columnNames = {"date", "start_time", "end_time"})
})
public class InterviewSlot extends TimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTERVIEW_SLOTS_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "interviewSlot", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Member> interviewees;

    protected InterviewSlot() {
    }

    @Builder
    public InterviewSlot(LocalDate date, LocalTime startTime, LocalTime endTime, int capacity, List<Member> members) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.interviewees = members;
    }

    public InterviewSlot(RegisterInterviewSlotRequest request) {
        this.date = request.getDate();
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.capacity = request.getCapacity();
        this.interviewees = new ArrayList<>();
    }

    public boolean checkSlotIdSame(Long slotId) {
        if (id == slotId)
            return true;
        return false;
    }

    public void insertMember(Member member) {
        interviewees.add(member);
    }

    public void deleteMember(Member member) {
        this.interviewees = interviewees.stream()
                .filter(m -> !member.equals(m))
                .collect(Collectors.toList());
    }

    public void update(UpdateInterviewSlotRequest request) {
        this.startTime = request.getStartTime();
        this.endTime = request.getEndTime();
        this.capacity = request.getCapacity();
    }

    public boolean checkIntervieweesAcceptable() {
        if (capacity > interviewees.size())
            return true;
        return false;
    }

    public boolean checkUpdateCapacity(int updateCapacity) {
        if (interviewees.size() < updateCapacity)
            return true;
        return false;
    }
}
