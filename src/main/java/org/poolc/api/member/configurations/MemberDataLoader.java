package org.poolc.api.member.configurations;


import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("test")
@RequiredArgsConstructor
public class MemberDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Override
    public void run(String... args) {
        memberRepository.save(
                new Member(UUID.randomUUID().toString(),
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
                        null));
        memberRepository.save(
                new Member(UUID.randomUUID().toString(),
                        "UNACCEPTED_MEMBER_ID",
                        passwordHashProvider.encodePassword("UNACCEPTED_MEMBER_PASSWORD"),
                        "example2@email.com",
                        "examplePhoneNumber2",
                        "MEMBER_NAME2",
                        "exampleDepartment",
                        "exampleStudentID2",
                        false,
                        false,
                        null,
                        null,
                        null,
                        null,
                        false,
                        null));
        memberRepository.save(
                new Member(UUID.randomUUID().toString(),
                        "ADMIN_ID",
                        passwordHashProvider.encodePassword("ADMIN_PASSWORD"),
                        "example3@email.com",
                        "examplePhoneNumber3",
                        "MEMBER_NAME3",
                        "exampleDepartment",
                        "exampleStudentID3",
                        true,
                        true,
                        null,
                        null,
                        null,
                        null,
                        false,
                        null));
    }
}
