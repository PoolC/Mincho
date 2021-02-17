package org.poolc.api.auth.service;

import org.poolc.api.auth.exception.UnactivatedException;
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
            throw new UnactivatedException("No activated user");
        }
        return jwtTokenProvider.createToken(member);
    }
}
