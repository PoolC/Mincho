package org.poolc.api.member;


import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.poolc.domain.Poolc;
import org.poolc.api.poolc.repository.PoolcRespository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("memberTest")
@RequiredArgsConstructor
public class MemberDataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final PoolcRespository poolcRespository;

    @Override
    public void run(String... args) {
        poolcRespository.save(
                Poolc.builder()
                        .presidentName("김성하")
                        .phoneNumber("010-8710-2644")
                        .location("공학관 A 537호")
                        .locationUrl("https://user-images.githubusercontent.com/48787170/110197969-54ebda00-7e92-11eb-9cde-b5e46c8490f0.png")
                        .introduction("안녕하세요! 연세대학교 공과대학 프로그래밍 동아리 풀씨입니다.\uD83C\uDF31\n" +
                                "풀씨(PoolC)는 '풀그림(프로그래밍의 순수 우리말) 개발의 씨앗이 되자' 라는 의미입니다.\n" +
                                "\n" +
                                "\n" +
                                "프로그래밍을 중심으로 관심분야가 비슷한 사람들끼리 모여 스터디나 세미나, 프로젝트를 진행하고, 외부적으로는 공모전에 참여하거나, 게임을 만들어 발표하고 있습니다.\n" +
                                "동아리방에는 성능 좋은 컴퓨터들과 다양한 관련 서적이 구비되어 있어, 언제든지 함께 공부하고 궁금한 점을 질문할 수 있습니다.\n" +
                                "\n" +
                                "\n" +
                                "뿐만 아니라 여러 친목 활동을 통해 동아리 부원들이 더 친해질 수 있도록 노력하고 있습니다.\n" +
                                "동아리에서 좋은 친구 얻어 가셔야죠!\uD83D\uDC40\n" +
                                "\n" +
                                "\n" +
                                "소프트웨어에 관심 있는 분은 망설이지 마시고 지금 풀씨의 문을 두드리세요.\n" +
                                "다양한 기업의 지원, 프로그래밍을 좋아하고 열정 있는 선배들과 동료들을 만날 수 있는 동아리,\n" +
                                "풀씨입니다.\n" +
                                "\n" +
                                "\n" +
                                "✨이런 분들과 함께하고 싶어요!\n" +
                                "프로그래밍에 관심 있는 분\n" +
                                "프로그래밍을 좋아하는 분\n" +
                                "프로그래밍을 좋아하는 친구를 사귀고 싶은 분\n" +
                                "함께 스터디 & 세미나 & 프로젝트를 하고 싶은 분\n" +
                                "\n")
                        .mainImageUrl("https://user-images.githubusercontent.com/48787170/108064727-6f574280-70a0-11eb-8d27-d667d85f58ee.png\n")
                        .applyUri("https://docs.google.com/forms/d/1sLyd5sVN_cQ_HeCNO7xblHtSFiHvU1nsyqxS5dWgM7w/edit")
                        .isSubscriptionPeriod(false)
                        .build()
        );
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.ADMIN))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.ADMIN))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
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
                        .roles(MemberRoles.getDefaultFor(MemberRole.MEMBER))
                        .build());
    }
}
