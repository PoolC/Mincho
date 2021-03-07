package org.poolc.api.member.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.activity.dto.ActivityResponse;
import org.poolc.api.activity.service.ActivityService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberListResponse;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.poolc.api.project.dto.ProjectResponse;
import org.poolc.api.project.service.ProjectService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final ProjectService projectService;
    private final ActivityService activityService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberListResponse> getAllMembers(@AuthenticationPrincipal Member member) {
        List<MemberResponse> memberList = memberService.getAllMembers()
                .stream().map(MemberResponse::of)
                .collect(Collectors.toList());
        MemberListResponse MemberListResponses = new MemberListResponse(memberList);
        return ResponseEntity.ok().body(MemberListResponses);
    }

    @GetMapping(value = "/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberListResponse> findMembersForProject(HttpServletRequest request) {
        List<MemberResponse> memberList = memberService.getAllMembersByName(request.getParameter("name"))
                .stream().map(MemberResponse::of)
                .collect(Collectors.toList());
        MemberListResponse MemberListResponses = new MemberListResponse(memberList);
        return ResponseEntity.ok().body(MemberListResponses);
    }

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody RegisterMemberRequest request) {
        checkIsValidMemberCreateInput(request);

        memberService.create(new MemberCreateValues(request));

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PutMapping(path = "/me")
    public ResponseEntity updateMember(@AuthenticationPrincipal Member member, @RequestBody UpdateMemberRequest
            updateMemberRequest) {
        checkIsValidMemberUpdateInput(updateMemberRequest);
        memberService.updateMember(member, updateMemberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{loginID}")
    public ResponseEntity<MemberResponse> adminGetMemberInfoByloginID(@PathVariable String loginID) {
        Member member = memberService.findMemberbyLoginID(loginID);

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

    @DeleteMapping(value = "/{loginID}")
    public ResponseEntity deleteMember(@PathVariable String loginID) {
        memberService.deleteMember(loginID);

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/activate/{loginID}")
    public ResponseEntity ActivateMember(@PathVariable String loginID) {
        memberService.authorizeMember(loginID);

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/admin/{loginID}")
    public ResponseEntity toggleIsAdmin(@AuthenticationPrincipal Member member, @PathVariable String loginID) {
        memberService.updateIsAdmin(member, loginID);

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
        return email.matches("^[_a-z0A-Z-9-]+(.[_a-zA-Z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    }

}
