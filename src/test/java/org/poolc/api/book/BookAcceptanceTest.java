package org.poolc.api.book;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.book.dto.BookWithBorrowerResponse;
import org.poolc.api.member.dto.RegisterMemberRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.poolc.api.auth.AuthAcceptanceTest.loginRequest;

@ActiveProfiles("bookTest")
public class BookAcceptanceTest extends AcceptanceTest {

    @Test
    void findAllBooks() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getBooksRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().as(List.class)).hasSize(2);
    }

    @Test
    void findAllBooksWithUnAuthorizedMember() {

        ExtractableResponse<Response> response = getBooksRequest("1234");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void findOneBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getBookRequest(accessToken, 1l);
        BookWithBorrowerResponse bookWithBorrowerDto = response.as(BookWithBorrowerResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookWithBorrowerDto.getId()).isEqualTo(1l);
        assertThat(bookWithBorrowerDto.getBorrower().getName()).isEqualTo("MEMBER_NAME");
    }

    @Test
    void findOneBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getBookRequest(accessToken, 100l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void borrowBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 2l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void borrowBookWithBorrower() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    void borrowBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 3l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void returnMyBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void returnBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 3l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void returnNotMyBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    void returnBookNotBorrowed() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 2l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    public static ExtractableResponse<Response> getBooksRequest(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/book")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> getBookRequest(String accessToken, Long bookId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/book/{bookID}", bookId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> borrowBookRequest(String accessToken, Long bookId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/book/borrow/{bookID}", bookId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> returnBookRequest(String accessToken, Long bookId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/book/return/{bookID}", bookId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateMemberInfoRequest(String accessToken, String name, String password, String passwordCheck, String department) {
        RegisterMemberRequest request = new RegisterMemberRequest(name, null, password, passwordCheck, null, null, department, null);

        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/member/me")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteMemberRequest(String accessToken, String name) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/member", name)
                .then().log().all()
                .extract();
    }
}
