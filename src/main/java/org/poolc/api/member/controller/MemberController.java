package org.poolc.api.member.controller;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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

    @PostMapping
    public ResponseEntity<Void> createMember(@RequestBody RegisterMemberRequest request) {
        checkIsValidMemberInput(request);

        memberService.create(new MemberCreateValues(request));

        return ResponseEntity.accepted().build();
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberResponse> getMember(HttpServletRequest request) {
        Member member = memberService.getMemberByUUIDIfRegistered(request.getAttribute("UUID").toString());
        if (!member.getIsActivated()) {
            throw new UnauthenticatedException("아직 허가되지 않은 회원입니다");
        }
        return ResponseEntity.ok()
                .body(new MemberResponse(member.getUUID(),
                        member.getEmail(),
                        member.getPhoneNumber(),
                        member.getName(),
                        member.getDepartment(),
                        member.getStudentID(),
                        member.getProfileImageURL(),
                        member.getIntroduction()));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemberResponse>> getAllMembers(HttpServletRequest request) {
        Member member = memberService.getMemberByUUIDIfRegistered(request.getAttribute("UUID").toString());
        if (request.getAttribute("isAdmin").equals("false")) {
            throw new UnauthenticatedException("임원진이 아닙니다");
        }
        if (!member.getIsActivated()) {
            throw new UnauthenticatedException("아직 허가되지 않은 회원입니다");
        }

        List<MemberResponse> memberResponses = memberService.getAllMembers()
                .stream().map(m -> new MemberResponse(member.getUUID(),
                        member.getEmail(),
                        member.getPhoneNumber(),
                        member.getName(),
                        member.getDepartment(),
                        member.getStudentID(),
                        member.getProfileImageURL(),
                        member.getIntroduction()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(memberResponses);
    }

    @DeleteMapping
    public ResponseEntity deleteMember(HttpServletRequest request, @Param("UUID") String UUID) {
        if (request.getAttribute("isAdmin").equals("false")) {
            throw new UnauthenticatedException("임원진이 아닙니다");
        }
        memberService.deleteMemberByUUID(UUID);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/me")
    public ResponseEntity updateMember(HttpServletRequest request, @RequestBody RegisterMemberRequest registerMemberRequest) {
        return ResponseEntity.ok().build();
    }

    private void checkIsValidMemberInput(RegisterMemberRequest request) {
        if (!request.getPassword().equals(request.getPasswordCheck())) {
            throw new IllegalArgumentException("passwords should match");
        }

        if (!correctEmailFormat(request)) {
            throw new IllegalArgumentException("Incorrect email format");
        }

        // TODO: 벨리데이션... 체크하기......
    }

    private boolean correctEmailFormat(RegisterMemberRequest request) {
        return request.getEmail().matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$");
    }

    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
    }
}
