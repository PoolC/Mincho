package org.poolc.api.asciidocs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.poolc.api.asciidocs.ApiDoc;
import org.poolc.api.auth.dto.AuthRequest;
import org.poolc.api.auth.service.AuthService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("인증 API 문서")
public class AuthDoc extends ApiDoc {
    @MockBean
    private AuthService authService;
    @MockBean
    private MemberService memberService;

    @SneakyThrows
    @Test
    void createAccessToken() {
        final String RESULT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI...";
        String loginID = "testID";
        String password = "testPassword";
        String UUID = "ff162508-d01c-4fb7-bdd2-9dc745200da7";
        Member member = Mockito.mock(Member.class);

        given(authService.createAccessToken(loginID, password)).willReturn(RESULT_TOKEN);
        given(memberService.getMemberIfRegistered(loginID, password)).willReturn(null);
        given(member.getUUID()).willReturn(UUID);
        given(member.isAdmin()).willReturn(true);

        mockMvc.perform(post("/login").content(new ObjectMapper().writeValueAsString(new AuthRequest(loginID, password))).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(RESULT_TOKEN)))
                .andDo(document("auth",
                        requestFields(
                                fieldWithPath("loginID").type(JsonFieldType.STRING).description("회원 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("발급된 토큰")
                        )));
    }
}
