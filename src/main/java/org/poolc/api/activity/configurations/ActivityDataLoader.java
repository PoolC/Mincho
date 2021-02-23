package org.poolc.api.activity.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.domain.Activity;
import org.poolc.api.activity.domain.ActivityTag;
import org.poolc.api.activity.repository.ActivityRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Profile("activityTest")
@RequiredArgsConstructor
public class ActivityDataLoader implements CommandLineRunner {

    private final ActivityRepository activityRepository;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Override
    public void run(String... args) {
        Member member = new Member(UUID.randomUUID().toString(),
                "MEMBER_ID",
                passwordHashProvider.encodePassword("MEMBER_PASSWORD"),
                "example@email.com",
                "examplePhoneNumber",
                "MEMBER_NAME",
                "exampleDepartment",
                "exampleStudentID",
                true,
                false,
                null,
                null,
                null,
                null,
                false,
                null);
        memberRepository.save(member);
        LocalDate date = LocalDate.now();
        LocalDate date2 = LocalDate.of(2020, 1, 11);
        Activity activity = new Activity("정윤석의 c++ ", "dsds", member, date, "dsds", false, 1l, false);
        activity.getTags().add(new ActivityTag(activity, "꿀잼보장"));
        activityRepository.save(activity);
        Activity activity2 = new Activity("정윤석의 c++ 2", "dsds", member, date2, "dsds", false, 1l, false);
        Activity activity3 = new Activity("정윤석의 c++ 3", "dsds", member, date, "dsds", false, 1l, false);

        activity2.getTags().add(new ActivityTag(activity2, "꿀잼보장"));
        activity2.getTags().add(new ActivityTag(activity2, "ㄹㅇ로"));
        activityRepository.save(activity2);
        activityRepository.save(activity3);

    }
}
