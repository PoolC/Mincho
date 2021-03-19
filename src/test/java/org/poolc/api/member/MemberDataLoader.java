package org.poolc.api.member;


import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
@Profile("memberTest")
@RequiredArgsConstructor
public class MemberDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Override
    public void run(String... args) {
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("MEMBER_ID")
                        .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD"))
                        .email("example@email.com")
                        .phoneNumber("010-4444-4444")
                        .name("MEMBER_NAME")
                        .department("exampleDepartment")
                        .studentID("2021147593")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("MEMBER_ID2")
                        .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD2"))
                        .email("example-1@email.com")
                        .phoneNumber("010-3333-3333")
                        .name("MEMBER_NAME2")
                        .department("exampleDepartment")
                        .studentID("2021147592")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("MEMBER_ID3")
                        .passwordHash(passwordHashProvider.encodePassword("MEMBER_PASSWORD3"))
                        .email("example15@email.com")
                        .phoneNumber("010-4444-1234")
                        .name("MEMBER_NAME")
                        .department("exampleDepartment")
                        .studentID("2021111111")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("UNACCEPTED_MEMBER_ID")
                        .passwordHash(passwordHashProvider.encodePassword("UNACCEPTED_MEMBER_PASSWORD"))
                        .email("example2@email.com")
                        .phoneNumber("010-5555-5555")
                        .name("MEMBER_NAME2")
                        .department("exampleDepartment")
                        .studentID("2021147594")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.UNACCEPTED);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("ADMIN_ID")
                        .passwordHash(passwordHashProvider.encodePassword("ADMIN_PASSWORD"))
                        .email("example3@email.com")
                        .phoneNumber("010-6666-6666")
                        .name("MEMBER_NAME3")
                        .department("exampleDepartment")
                        .studentID("2021147595")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.ADMIN);
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("WILL_REVOKE_ADMIN_ID")
                        .passwordHash(passwordHashProvider.encodePassword("ADMIN_PASSWORD"))
                        .email("example33@email.com")
                        .phoneNumber("010-6655-6655")
                        .name("MEMBER_NAME4")
                        .department("exampleDepartment")
                        .studentID("2021147596")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("자기소개")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.ADMIN);
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("WILL_DELETE_MEMBER_ID")
                        .passwordHash(passwordHashProvider.encodePassword("DELETED_MEMBER_PASSWORD"))
                        .email("example4@email.com")
                        .phoneNumber("010-7777-7777")
                        .name("MEMBER_NAME5")
                        .department("exampleDepartment")
                        .studentID("2021147597")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("UPDATE_MEMBER_ID")
                        .passwordHash(passwordHashProvider.encodePassword("UPDATE_MEMBER_PASSWORD"))
                        .email("example5@email.com")
                        .phoneNumber("010-8888-8888")
                        .name("MEMBER_NAME6")
                        .department("exampleDepartment")
                        .studentID("2021147598")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("NOT_ADMIN_ID")
                        .passwordHash(passwordHashProvider.encodePassword("NOT_ADMIN_PASSWORD"))
                        .email("example6@email.com")
                        .phoneNumber("010-9999-9999")
                        .name("MEMBER_NAME7")
                        .department("exampleDepartment")
                        .studentID("2021147599")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID("TO_BE_EXPELLED_ID")
                        .passwordHash(passwordHashProvider.encodePassword("TO_BE_EXPELLED_PASSWORD"))
                        .email("example1234@email.com")
                        .phoneNumber("010-9999-9999")
                        .name("TO_BE_EXPELLED_NAME7")
                        .department("exampleDepartment")
                        .studentID("2021147600")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
    }
}
