package org.poolc.api.member.controller;

import org.poolc.api.member.dto.MemberResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MemberResponse> getMember() {
        return ResponseEntity.badRequest().build();
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
    
}
