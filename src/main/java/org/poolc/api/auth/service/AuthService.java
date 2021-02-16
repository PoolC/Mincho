package org.poolc.api.auth.service;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;

    @Autowired
    public AuthService(JwtTokenProvider jwtTokenProvider, MemberService memberService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.memberService = memberService;
    }

    public String createAccessToken(String loginID, String password) {
        Member member = memberService.getMemberIfRegistered(loginID, password);
        if (!member.getIsActivated()) {
            throw new UnauthenticatedException("인증되지 않은 회원입니다.");
        }
        return jwtTokenProvider.createToken(member);
    }
}
