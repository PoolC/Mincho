package org.poolc.api.member.service;

import org.apache.commons.lang3.tuple.MutablePair;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.activity.vo.YearSemester;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.dto.UpdateMemberStatusRequest;
import org.poolc.api.member.exception.DuplicateMemberException;
import org.poolc.api.member.repository.MemberQueryRepository;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
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
                        .roles(new HashSet<>() {{
                            add(MemberRole.UNACCEPTED);
                        }})
                        .build());
    }

    public void updateMember(Member member, UpdateMemberRequest updateMemberRequest) {
        String encodePassword = passwordHashProvider.encodePassword(updateMemberRequest.getPassword());
        member.updateMemberInfo(updateMemberRequest, encodePassword);
        memberRepository.saveAndFlush(member);
    }

    public void authorizeMember(String loginID) {
        Member findMember = findMemberbyLoginID(loginID);
        findMember.acceptMember();
        memberRepository.flush();
    }

    public void updateIsAdmin(Member member, String toggleIsAdminMemberLoginID) {
        Member targetMember = findMemberbyLoginID(toggleIsAdminMemberLoginID);
        member.toggleAdmin(targetMember);
        memberRepository.saveAndFlush(targetMember);
    }

    public void deleteMember(String loginID) {
        memberRepository.delete(findMemberbyLoginID(loginID));
    }

    public Member findMemberbyLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID"));
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAllSortByLatestOrder();
    }

    public Member getMemberIfRegistered(String loginID, String password) {
        return memberRepository.findByLoginID(loginID)
                .filter(member -> passwordHashProvider.matches(password, member.getPasswordHash()))
                .orElseThrow(() -> new UnauthenticatedException("No user found with given loginID and password"));
    }

    public List<Member> getAllMembersByName(String name) {
        return memberRepository.findByName(name);
    }

    public List<MutablePair<String, Long>> getHoursWithMembers(String when) {
        YearSemester yearSemester = activityService.getYearSemesterFromString(when);
        LocalDate startDate = activityService.getFirstDateFromYearSemester(yearSemester);
        LocalDate endDate = activityService.getLastDateFromYearSemester(yearSemester);
        return memberQueryRepository.getHours(startDate, endDate);
    }

    public void updateStatus(Member member, UpdateMemberStatusRequest request) {
        Member targetMember = findMemberbyLoginID(request.getLoginId());
        member.adminUpdatesStatusOf(targetMember, MemberRole.valueOf(request.getStatus()));
        memberRepository.saveAndFlush(targetMember);
    }

    public void updateIsExcepted(Member member, String toggleIsExceptedLoginId) {
        Member targetMember = findMemberbyLoginID(toggleIsExceptedLoginId);
        member.except(targetMember);
        memberRepository.saveAndFlush(targetMember);
    }
}
