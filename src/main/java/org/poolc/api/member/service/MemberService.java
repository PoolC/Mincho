package org.poolc.api.member.service;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRoles;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.exception.DuplicateMemberException;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordHashProvider passwordHashProvider) {
        this.memberRepository = memberRepository;
        this.passwordHashProvider = passwordHashProvider;
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
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{ add(MemberRoles.UNACCEPTED); }})
                        .build());
    }

    public void updateMember(String UUID, UpdateMemberRequest updateMemberRequest) {
        Member findMember = memberRepository.findById(UUID).get();
        String encodePassword = passwordHashProvider.encodePassword(updateMemberRequest.getPassword());
        findMember.updateMemberInfo(updateMemberRequest, encodePassword);
        memberRepository.flush();
    }

    public void authorizeMember(String loginID) {
        Member findMember = memberRepository.findByLoginID(loginID).get();
        findMember.acceptMember();
        memberRepository.flush();
    }

    public void updateIsAdmin(String loginID, Boolean isAdmin) {
        Member findMember = memberRepository.findByLoginID(loginID).get();
        findMember.grantAdminPrivileges();
        memberRepository.flush();
    }

    public void deleteMember(String loginID) {
        memberRepository.delete(memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given loginID")));
    }

    public Member findMemberbyUUID(String UUID) {
        return memberRepository.findById(UUID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given UUID"));
    }

    public Member findMemberbyLoginID(String loginID) {
        return memberRepository.findByLoginID(loginID)
                .orElseThrow(() -> new NoSuchElementException("No user found with given UUID"));
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberIfRegistered(String loginID, String password) {
        return Optional.ofNullable(memberRepository.findByLoginID(loginID))
                .get()
                .filter(member -> passwordHashProvider.matches(password, member.getPasswordHash()))
                .orElseThrow(() -> new UnauthenticatedException("No user found with given loginID and password"));
    }
}
