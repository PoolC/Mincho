package org.poolc.api.auth.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnactivatedException;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.poolc.domain.Poolc;
import org.poolc.api.poolc.service.PoolcService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final PoolcService poolcService;

    public String createAccessToken(String loginID, String password) {
        Member member = memberService.getMemberIfRegistered(loginID, password);
        Poolc poolc = poolcService.get();
        if (!poolc.checkSubscriptionPeriod() && !member.isAcceptedMember() && !member.isMember()) {
            throw new UnactivatedException("관리자 승인 전에는 로그인이 불가능합니다.");
        }
        return jwtTokenProvider.createToken(member);
    }
}
