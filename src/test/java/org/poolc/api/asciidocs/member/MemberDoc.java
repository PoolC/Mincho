package org.poolc.api.asciidocs.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.poolc.api.asciidocs.ApiDoc;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.member.vo.MemberCreateValues;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MemberDoc extends ApiDoc {
    @MockBean
    private MemberService memberService;

    @SneakyThrows
    @Test
    void createMember() {
        RegisterMemberRequest requestValues = new RegisterMemberRequest("exampleName", "exampleLoginID",
                "examplePassword", "examplePassword",
                "example@email.com", "examplePhoneNumber",
                "exampleDepartment", "exampleStudentID", "introduction");
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
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("자기소개")
                        )));
    }
}
