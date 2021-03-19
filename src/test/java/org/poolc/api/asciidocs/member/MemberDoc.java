package org.poolc.api.asciidocs.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.poolc.api.asciidocs.ApiDoc;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashSet;
import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberDoc extends ApiDoc {
    @Autowired
    private PasswordHashProvider passwordHashProvider;

    @SneakyThrows
    @Test
    void createMember() {
        MemberService memberService = Mockito.mock(MemberService.class);
        RegisterMemberRequest requestValues = new RegisterMemberRequest("exampleName", "exampleLoginID",
                "examplePassword", "examplePassword",
                "example@poolc.org", "examplePhoneNumber",
                "exampleDepartment", "exampleStudentID", "introduction", "/files/profile_placeholder_02.png");
        MemberCreateValues createValues = new MemberCreateValues(requestValues);

        doNothing().when(memberService).create(createValues);

        mockMvc.perform(post("/member").content(new ObjectMapper().writeValueAsString(requestValues)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(document("member/create",
                        requestFields(
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("loginID").type(JsonFieldType.STRING).description("로그인 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING).description("비밀번호 확인"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                fieldWithPath("department").type(JsonFieldType.STRING).description("학과/학부"),
                                fieldWithPath("studentID").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("profileImageURL").type(JsonFieldType.STRING).description("프사 파일 주소"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개")
                        )));
    }

    @SneakyThrows
    @Test
    void getMemberMe() {
        Member adminMember = Member.builder()
                .UUID(UUID.randomUUID().toString())
                .loginID("ADMIN_ID")
                .passwordHash(passwordHashProvider.encodePassword("ADMIN_PASSWORD"))
                .email("admin@email.com")
                .phoneNumber("010-1234-4321")
                .name("ADMIN_NAME2")
                .department("exampleDepartment")
                .studentID("2014147500")
                .passwordResetToken(null)
                .passwordResetTokenValidUntil(null)
                .profileImageURL("/files/profile_placeholder_02.png")
                .introduction("나는 이소정. 차기 풀씨 회장")
                .isExcepted(false)
                .roles(new HashSet<>() {{
                    add(MemberRole.ADMIN);
                    add(MemberRole.MEMBER);
                }})
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(adminMember, "", adminMember.getAuthorities()));

        mockMvc.perform(get("/member/me").header("Authorization", "Bearer eyGJQWF...."))
                .andExpect(status().isOk())
                .andDo(document("member/me",
                        requestHeaders(headerWithName("Authorization").description("Bearer 액세스 토큰")),
                        responseFields(
                                fieldWithPath("loginID").type(JsonFieldType.STRING).description("로그인 ID"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("phoneNumber").type(JsonFieldType.STRING).description("핸드폰 번호"),
                                fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                fieldWithPath("department").type(JsonFieldType.STRING).description("학과"),
                                fieldWithPath("studentID").type(JsonFieldType.STRING).description("학번"),
                                fieldWithPath("profileImageURL").type(JsonFieldType.STRING).description("프로필 사진 주소"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개"),
                                fieldWithPath("isActivated").type(JsonFieldType.BOOLEAN).description("활동 회원 여부"),
                                fieldWithPath("isAdmin").type(JsonFieldType.BOOLEAN).description("관리자 여부"),
                                fieldWithPath("hostActivities").type(JsonFieldType.ARRAY).description("주최자로서의 활동 목록"),
                                fieldWithPath("participantActivities").type(JsonFieldType.ARRAY).description("참여자로서의 활동 목록"),
                                fieldWithPath("projects").type(JsonFieldType.ARRAY).description("참여 프로젝트 목록"),
                                fieldWithPath("status").type(JsonFieldType.STRING).description("회원 활동 상태")
                        )));
    }
}
