package org.poolc.api.tool.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.tool.domain.Qr;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToolService {
    private final MemberService memberService;

    public byte[] createQr(Member member, String str) {
        memberService.throwIfNotMember(member);

        Qr qr = new Qr(str);
        return qr.createQrImage();
    }
}
