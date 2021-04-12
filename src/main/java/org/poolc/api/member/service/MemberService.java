package org.poolc.api.member.service;

import net.bytebuddy.utility.RandomString;
import org.apache.commons.lang3.tuple.MutablePair;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.vo.YearSemester;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.exception.DuplicateMemberException;
import org.poolc.api.member.exception.WrongPasswordException;
import org.poolc.api.member.repository.MemberQueryRepository;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;
    private final MemberQueryRepository memberQueryRepository;
    private final ActivityService activityService;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordHashProvider passwordHashProvider, MemberQueryRepository memberQueryRepository, ActivityService activityService) {
        this.memberRepository = memberRepository;
        this.passwordHashProvider = passwordHashProvider;
        this.memberQueryRepository = memberQueryRepository;
        this.activityService = activityService;
    }

    public void create(MemberCreateValues values) {
        boolean hasDuplicate = memberRepository.existsByLoginIDOrEmailOrPhoneNumberOrStudentID(values.getLoginID(),
                values.getEmail(), values.getPhoneNumber(), values.getStudentID());

        if (hasDuplicate) {
            throw new DuplicateMemberException("There are one or more duplicates of the following: loginID, email, phone number or studentID");
        }

        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID(values.getLoginID())
                        .passwordHash(passwordHashProvider.encodePassword(values.getPassword()))
                        .email(values.getEmail())
                        .phoneNumber(values.getPhoneNumber())
                        .name(values.getName())
                        .department(values.getDepartment())
                        .studentID(values.getStudentID())
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(values.getProfileImageURL())
                        .introduction(values.getIntroduction())
                        .isExcepted(false)
                        .roles(MemberRoles.getDefaultFor(MemberRole.UNACCEPTED))
                        .build());
    }

    public List<Member> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        members.sort(Comparator.comparing(Member::getName));
        return members;
    }

    public List<Member> getAllMembersAndUpdateMemberIsExcepted() {
        List<Member> members = getAllMembers();
        members.forEach(member -> member.updateIsExcepted());
        return members;
    }

    public List<Member> getAllMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    public Member getMemberByLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID"));
    }

    public String resetMemberPasswordToken(String email) {
        Member resetMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("No user found with given email"));
        String resetPasswordToken = RandomString.make(40);
        resetMember.setPasswordResetToken(resetPasswordToken);
        memberRepository.saveAndFlush(resetMember);
        return resetPasswordToken;
    }

    public void updateMemberPassword(String passwordResetToken, String newPassword) {
        Member resetMember = memberRepository.findByPasswordResetToken(passwordResetToken)
                .orElseThrow(() -> new NoSuchElementException("No user found with given passwordResetToken"));
        String newPasswordHash = passwordHashProvider.encodePassword(newPassword);
        resetMember.updatePassword(newPasswordHash);
        memberRepository.saveAndFlush(resetMember);
    }

    public Member getMemberIfRegistered(String loginID, String password) {
        return memberRepository.findByLoginID(loginID)
                .filter(member -> passwordHashProvider.matches(password, member.getPasswordHash()))
                .orElseThrow(() -> new WrongPasswordException("아이디와 비밀번호를 확인해주세요."));
    }

    public List<MutablePair<String, Long>> getHoursWithMembers(String when) {
        YearSemester yearSemester = activityService.getYearSemesterFromString(when);
        LocalDate startDate = activityService.getFirstDateFromYearSemester(yearSemester);
        LocalDate endDate = activityService.getLastDateFromYearSemester(yearSemester);
        return memberQueryRepository.getHours(startDate, endDate);
    }

    public void authorizeMember(String loginID) {
        Member findMember = getMemberByLoginID(loginID);
        findMember.acceptMember();
        memberRepository.saveAndFlush(findMember);
    }

    public void toggleAdmin(Member admin, String loginID) {
        Member targetMember = getMemberByLoginID(loginID);
        admin.changeRole(targetMember, targetMember.getRoles().hasRole(MemberRole.ADMIN) ?
                MemberRole.MEMBER :
                MemberRole.ADMIN);
        memberRepository.saveAndFlush(targetMember);
    }

    public void selfChangeToRole(Member member, MemberRole role) {
        member.selfChangeRole(role);
        memberRepository.saveAndFlush(member);
    }

    public void changeToRole(Member admin, String targetMemberLoginID, MemberRole role) {
        Member targetMember = getMemberByLoginID(targetMemberLoginID);
        admin.changeRole(targetMember, role);
        memberRepository.saveAndFlush(targetMember);
    }

    public void toggleIsExcepted(Member member, String targetLoginID) {
        Member targetMember = getMemberByLoginID(targetLoginID);
        member.toggleExcept(targetMember);
        memberRepository.saveAndFlush(targetMember);
    }

    public void updateMember(Member member, UpdateMemberRequest updateMemberRequest) {
        String encodePassword = passwordHashProvider.encodePassword(updateMemberRequest.getPassword());
        member.updateMemberInfo(updateMemberRequest, encodePassword);
        memberRepository.saveAndFlush(member);
    }

    public void deleteMember(String loginID) {
        memberRepository.delete(getMemberByLoginID(loginID));
    }
}
