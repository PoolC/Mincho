package org.poolc.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.*;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.service.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {
    private final MemberService memberService;
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<Map<String, List<MemberResponse>>> getAllMembers(@AuthenticationPrincipal Member loginMember) {
        List<MemberResponse> responses = memberService.getAllMembersResponse(loginMember);
        return ResponseEntity.ok().body(Collections.singletonMap("data", responses));
    }

    @GetMapping(value = "/name")
    public ResponseEntity<Map<String, List<MemberResponse>>> findMembersForProject(@RequestParam String name) {
        List<MemberResponse> memberResponses = memberService.getAllMembersResponseByName(name);
        return ResponseEntity.ok().body(Collections.singletonMap("data", memberResponses));
    }

    @GetMapping(value = "/hour")
    public ResponseEntity<Map<String, List<MemberResponseWithHour>>> findMembersWithHoursInSpecificSemester() {
        List<MemberResponseWithHour> responseList = memberService.getHoursWithMembers();
        return ResponseEntity.ok().body(Collections.singletonMap("data", responseList));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<MemberResponse> getMe(@AuthenticationPrincipal Member loginMember) {
        memberService.checkMe(loginMember);
        MemberResponse response = MemberResponse.of(loginMember);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{loginID}")
    public ResponseEntity<MemberResponse> getMemberWithProjectAndActivity(@PathVariable String loginID) {
        //TODO: project schema 변경한 뒤에 refactoring 진행
        Member findMember = memberService.getMemberByLoginID(loginID);
        List<ActivityResponse> activityResponses = memberService.getMemberActivityResponses(loginID);
        List<ActivityResponse> hostActivityResponses = memberService.getHostActivityResponses(findMember);
        List<ProjectResponse> projectResponses = projectService.findProjectsByProjectMembers(loginID).stream().map(project -> ProjectResponse.of(project, memberService.findMembers(project.getMemberLoginIDs())))
                .collect(Collectors.toList());
        MemberResponse response = MemberResponse.of(findMember, hostActivityResponses, activityResponses, projectResponses);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/role")
    public ResponseEntity<Map<String, List<MemberRolesResponse>>> getRoles(@AuthenticationPrincipal Member loginMember) {
        return ResponseEntity.ok(Collections.singletonMap("data", Stream.of(MemberRole.values())
                .filter(not(MemberRole.SUPER_ADMIN::equals))
                .filter(role -> (!Optional.ofNullable(loginMember).isEmpty() && loginMember.isAdmin()) || role.isSelfToggleable())  // TODO: public member 만들면 null 체크 지우기
                .filter(role -> (!Optional.ofNullable(loginMember).isEmpty() && loginMember.isAdmin()) || !MemberRole.QUIT.equals(role))
                .map(MemberRolesResponse::of)
                .collect(Collectors.toList())));
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody RegisterMemberRequest request) {
        checkIsValidMemberCreateInput(request);
        memberService.create(new MemberCreateValues(request));
        return ResponseEntity.accepted().build();
    }

    @PutMapping(path = "/me")
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal Member loginMember,
                                             @RequestBody UpdateMemberRequest updateMemberRequest) {
        checkIsValidMemberUpdateInput(updateMemberRequest);
        memberService.updateMember(loginMember, updateMemberRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/activate/{loginID}")
    public ResponseEntity<Void> ActivateMember(@PathVariable String loginID) {
        memberService.authorizeMember(loginID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/excepted/{loginId}")
    public ResponseEntity<Void> exceptMember(@AuthenticationPrincipal Member admin, @PathVariable String loginId) {
        memberService.toggleIsExcepted(admin, loginId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/admin/{loginID}")
    public ResponseEntity<Void> toggleAdmin(@AuthenticationPrincipal Member admin, @PathVariable String loginID) {
        memberService.toggleAdmin(admin, loginID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/role/{loginID}")
    public ResponseEntity<Void> changeRole(@AuthenticationPrincipal Member admin, @PathVariable String loginID, @RequestBody ToggleRoleRequest role) {
        memberService.changeToRole(admin, loginID, MemberRole.valueOf(role.getRole()));
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/role")
    public ResponseEntity<Void> selfChangeRole(@AuthenticationPrincipal Member loginMember, @RequestBody ToggleRoleRequest role) {
        memberService.selfChangeToRole(loginMember, MemberRole.valueOf(role.getRole()));
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/reset-password-token")
    public ResponseEntity<String> sendResetPasswordTokenMail(@RequestBody MemberResetRequest request) throws MessagingException {
        memberService.sendResetPasswordMail(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/reset-password")
    public ResponseEntity<Void> updateMemberPassword(@RequestBody MemberResetRequest request) {
        checkIsValidMemberResetRequest(request);
        memberService.resetPassword(request);
        return ResponseEntity.ok().build();
    }

    private void checkIsValidMemberCreateInput(RegisterMemberRequest request) {
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("passwords should match");
        }

        if (!correctEmailFormat(request.getEmail())) {
            throw new IllegalArgumentException("Incorrect email format");
        }

        // TODO: 학번 10자리 숫자 확인

        // TODO: 전화 번호 11자리 숫자 확인
    }

    //TODO: abstract class로 구현
    private void checkIsValidMemberResetRequest(MemberResetRequest request) {
        if (!request.getNewPassword().equals(request.getNewPasswordCheck())) {
            throw new IllegalArgumentException("passwords should match");
        }
    }

    private void checkIsValidMemberUpdateInput(UpdateMemberRequest request) {
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("passwords should match");
        }

        if (!correctEmailFormat(request.getEmail())) {
            throw new IllegalArgumentException("Incorrect email format");
        }

        // TODO: 전화 번호 11자리 숫자 확인

    }

    private boolean correctEmailFormat(String email) {
        return email.matches("^(([^<>()\\[\\]\\\\.,;:\\s@']+(\\.[^<>()\\[\\]\\\\.,;:\\s@']+)*)|('.+'))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
    }
}
