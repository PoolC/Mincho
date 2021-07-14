package org.poolc.api.interview.domain;

import lombok.Builder;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "INTERVIEW_TABLE_SEQ_GENERATOR")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member> members = new ArrayList<>();

    @Builder
    public InterviewSlot(LocalDate date, LocalTime startTime, LocalTime endTime, int capacity, List<Member> members) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.members = members;
    }
}
