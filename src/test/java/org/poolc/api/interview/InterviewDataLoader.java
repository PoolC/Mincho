package org.poolc.api.interview;

import lombok.RequiredArgsConstructor;
import org.poolc.api.interview.domain.InterviewSlot;
import org.poolc.api.interview.repository.InterviewSlotRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
@Profile("interviewTest")
@RequiredArgsConstructor
public class InterviewDataLoader implements CommandLineRunner {
    private final InterviewSlotRepository interviewSlotRepository;

    @Override
    public void run(String... args) {
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 07, 8))
                        .startTime(LocalTime.of(15, 00))
                        .endTime(LocalTime.of(15, 15))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 8))
                        .startTime(LocalTime.of(15, 15))
                        .endTime(LocalTime.of(15, 30))
                        .capacity(2)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 8))
                        .startTime(LocalTime.of(15, 30))
                        .endTime(LocalTime.of(15, 45))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 8))
                        .startTime(LocalTime.of(15, 45))
                        .endTime(LocalTime.of(16, 00))
                        .capacity(4)
                        .build()
        );

        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 07, 9))
                        .startTime(LocalTime.of(15, 00))
                        .endTime(LocalTime.of(15, 15))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 9))
                        .startTime(LocalTime.of(15, 15))
                        .endTime(LocalTime.of(15, 30))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 9))
                        .startTime(LocalTime.of(15, 30))
                        .endTime(LocalTime.of(15, 45))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 9))
                        .startTime(LocalTime.of(15, 45))
                        .endTime(LocalTime.of(16, 00))
                        .capacity(4)
                        .build()
        );

        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 07, 10))
                        .startTime(LocalTime.of(15, 00))
                        .endTime(LocalTime.of(15, 15))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 10))
                        .startTime(LocalTime.of(15, 15))
                        .endTime(LocalTime.of(15, 30))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 10))
                        .startTime(LocalTime.of(15, 30))
                        .endTime(LocalTime.of(15, 45))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 10))
                        .startTime(LocalTime.of(15, 45))
                        .endTime(LocalTime.of(16, 00))
                        .capacity(4)
                        .build()
        );

        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 20))
                        .startTime(LocalTime.of(15, 45))
                        .endTime(LocalTime.of(16, 00))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 20))
                        .startTime(LocalTime.of(12, 00))
                        .endTime(LocalTime.of(12, 15))
                        .capacity(4)
                        .build()
        );
        interviewSlotRepository.save(
                InterviewSlot.builder()
                        .date(LocalDate.of(2021, 7, 20))
                        .startTime(LocalTime.of(12, 15))
                        .endTime(LocalTime.of(12, 30))
                        .capacity(4)
                        .build()
        );
    }
}
