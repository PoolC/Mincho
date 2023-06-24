package org.poolc.api.tool.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.tool.service.ToolService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tool")
@RequiredArgsConstructor
public class ToolController {
    private final ToolService toolService;

    @GetMapping(value = "/qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> createQr(@AuthenticationPrincipal Member member,
                                           @RequestParam String str) {
        byte[] imageBytes = toolService.createQr(member, str);
        return ResponseEntity.ok().body(imageBytes);
    }
}
