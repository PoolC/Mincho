package org.poolc.api.member.controller;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberListResponse;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.dto.UpdateMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberListResponse> getAllMembers(HttpServletRequest request) {
        Member member = memberService.findMemberbyUUID(request.getAttribute("UUID").toString());

        List<MemberResponse> memberList = memberService.getAllMembers()
                .stream().map(m -> new MemberResponse(member))
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
    public ResponseEntity<MemberResponse> getMember(HttpServletRequest request) {
        Member member = memberService.findMemberbyUUID(request.getAttribute("UUID").toString());

        return ResponseEntity.ok()
                .body(new MemberResponse(member));
    }

    @PutMapping(path = "/me")
    public ResponseEntity updateMember(HttpServletRequest request, @RequestBody UpdateMemberRequest updateMemberRequest) {
        checkIsValidMemberUpdateInput(updateMemberRequest);
        memberService.updateMember(request.getAttribute("UUID").toString(), updateMemberRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{loginID}")
    public ResponseEntity<MemberResponse> adminGetMemberInfoByloginID(HttpServletRequest request, @PathVariable("loginID") String loginID) {
        Member member = memberService.findMemberbyLoginID(loginID);

        return ResponseEntity.ok().body(new MemberResponse(member));
    }

    @DeleteMapping(value = "/{loginID}")
    public ResponseEntity deleteMember(HttpServletRequest request, @PathVariable("loginID") String loginID) {
        memberService.deleteMember(loginID);

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/activate/{loginID}")
    public ResponseEntity ActivateMember(HttpServletRequest request, @PathVariable("loginID") String loginID) {
        memberService.authorizeMember(loginID);

        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/admin/{loginID}")
    public ResponseEntity updateMemberAdmin(HttpServletRequest request, @PathVariable("loginID") String loginID, @RequestBody Boolean isAdmin) {
        memberService.updateIsAdmin(loginID, isAdmin);

        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
