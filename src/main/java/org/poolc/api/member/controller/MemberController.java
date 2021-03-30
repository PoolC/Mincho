package org.poolc.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.*;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.service.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.function.Predicate.not;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberController {
    private final MemberService memberService;
    private final ProjectService projectService;
    private final ActivityService activityService;
    private final JavaMailSender javaMailSender;

    @GetMapping
    public ResponseEntity<Map<String, List<MemberResponse>>> getAllMembers(@AuthenticationPrincipal Member member) {
        List<MemberResponse> memberResponses = memberService.getAllMembers()
                .stream()
                .filter(responseMember -> (member != null && member.isAdmin()) || !responseMember.shouldHide())  // TODO: public member 만들면 null 체크 지우기
                .map(MemberResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(Collections.singletonMap("data", memberResponses));
    }

    @GetMapping(value = "/name")
    public ResponseEntity<Map<String, List<MemberResponse>>> findMembersForProject(@RequestParam String name) {
        List<MemberResponse> memberResponses = memberService.getAllMembersByName(name)
                .stream()
                .map(MemberResponse::of)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(Collections.singletonMap("data", memberResponses));
    }

    @GetMapping(value = "/hour")
    public ResponseEntity<Map<String, List<MemberResponseWithHour>>> findMembersWithHoursInSpecificSemester(@RequestParam String when) {
        List<MemberResponseWithHour> list = new ArrayList<>();
        Map<Member, Long> map = new HashMap<>();
        memberService.getAllMembers().forEach(m -> map.put(m, 0L));
        memberService.getHoursWithMembers(when).forEach(m -> System.out.println(m.getRight()));
        memberService.getHoursWithMembers(when).forEach(m -> map.replace(memberService.getMemberByLoginID(m.getKey()), m.getValue()));
        for (Member member : map.keySet()) {
            list.add(MemberResponseWithHour.of(member, map.get(member)));
        }
        return ResponseEntity.ok().body(Collections.singletonMap("data", list));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<MemberResponse> getMember(@AuthenticationPrincipal Member member) {
        String loginID = member.getLoginID();

        List<Member> memberList = new ArrayList<>(); // TODO: 이부분 수정해야할 듯

        List<ActivityResponse> activitiesByActivityMembers = activityService.findActivitiesByActivityMembers(loginID)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
        List<ActivityResponse> activitiesByHost = activityService.findActivitiesByHost(member)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
        List<ProjectResponse> projects = projectService.findProjectsByProjectMembers(loginID)
                .stream().map(project -> ProjectResponse.of(project, memberList))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(MemberResponse.of(member, activitiesByHost, activitiesByActivityMembers, projects));
    }

    @GetMapping(value = "/{loginID}")
    public ResponseEntity<MemberResponse> adminGetMemberInfoByloginID(@PathVariable String loginID) {
        Member member = memberService.getMemberByLoginID(loginID);

        List<Member> memberList = new ArrayList<>(); // TODO: 이부분 수정해야할 듯

        List<ActivityResponse> activitiesByActivityMembers = activityService.findActivitiesByActivityMembers(loginID)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
        List<ActivityResponse> activitiesByHost = activityService.findActivitiesByHost(member)
                .stream().map(ActivityResponse::of)
                .collect(Collectors.toList());
        List<ProjectResponse> projects = projectService.findProjectsByProjectMembers(loginID)
                .stream().map(project -> ProjectResponse.of(project, memberList))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(MemberResponse.of(member, activitiesByHost, activitiesByActivityMembers, projects));
    }

    @GetMapping(value = "/role")
    public ResponseEntity<Map<String, List<MemberRolesResponse>>> getRoles(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(Collections.singletonMap("data", Stream.of(MemberRole.values())
                .filter(not(MemberRole.SUPER_ADMIN::equals))
                .filter(role -> (member != null && member.isAdmin()) || role.isSelfToggleable())  // TODO: public member 만들면 null 체크 지우기
                .filter(role -> (member != null && member.isAdmin()) || !MemberRole.QUIT.equals(role))
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
    public ResponseEntity<Void> updateMember(@AuthenticationPrincipal Member member,
                                             @RequestBody UpdateMemberRequest updateMemberRequest) {
        checkIsValidMemberUpdateInput(updateMemberRequest);

        memberService.updateMember(member, updateMemberRequest);
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
    public ResponseEntity<Void> selfChangeRole(@AuthenticationPrincipal Member member, @RequestBody ToggleRoleRequest role) {
        memberService.selfChangeToRole(member, MemberRole.valueOf(role.getRole()));
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/reset-password-token")
    public ResponseEntity<String> sendResetPasswordTokenMail(@RequestBody MemberResetRequest memberResetRequest) throws MessagingException {
        String email = memberResetRequest.getEmail();
        String resetPasswordToken = memberService.resetMemberPasswordToken(email);

        sendEmailPasswordResetToken(email, resetPasswordToken);

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/reset-password")
    public ResponseEntity<Void> updateMemberPassword(@RequestBody MemberResetRequest memberResetRequest) {
        checkIsValidMemberResetRequest(memberResetRequest);

        String passwordResetToken = memberResetRequest.getPasswordResetToken();
        String newPassword = memberResetRequest.getNewPassword();

        memberService.updateMemberPassword(passwordResetToken, newPassword);

        return ResponseEntity.ok().build();
    }

    private void sendEmailPasswordResetToken(String email, String resetPasswordToken) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setSubject("풀씨 홈페이지 비밀번호 재설정 안내메일입니다");
        helper.setText("안녕하세요.\n Poolc 홈페이지 비밀번호 재설정 안내 메일입니다. \n\n" +
                "아래 링크를 눌러 비밀번호 재설정을 진행해주세요.\n" +
                "<https://poolc.org/password/reset?token=" + resetPasswordToken + ">\n" +
                "이 링크는 24시간 동안 유효합니다.\n" +
                "감사합니다.", false);
        helper.setTo(email);
        javaMailSender.send(mimeMessage);
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
