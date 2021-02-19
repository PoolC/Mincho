package org.poolc.api.book;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.book.dto.BookCreateRequest;
import org.poolc.api.book.dto.BookUpdateRequest;
import org.poolc.api.book.dto.BookWithBorrowerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

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
        assertThat(response.body().jsonPath().getList("data")).hasSize(2);
    }

    @Test
    void findAllBooksWithUnAuthorizedMember() {

        ExtractableResponse<Response> response = getBooksRequest("1234");
        System.out.println(response.body().asString());
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 423l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 432l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    @Test
    void registerBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void registerBookNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void registerBookAlreadyExist() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createBookRequest(accessToken, "형철이의 삶", "박형철", "d", "ㅇㄴ");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void updateBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ", 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateNonExistingBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ", 432l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateBookNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ", 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void updateBookAlreadyExistSameBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶", "박형철", "d", "ㅇㄴ", 2l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void deleteBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteBookNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 1l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void deleteNonExistingBook() {
        String accessToken = loginRequest("MEMBER_ID1", "MEMBER_PASSWORD1")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 432l);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
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

    public static ExtractableResponse<Response> createBookRequest(String token, String title, String author, String imageURL, String info) {
        BookCreateRequest request = new BookCreateRequest(title, author, imageURL, info);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/book")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateBookRequest(String token, String title, String author, String imageURL, String info, Long id) {
        BookUpdateRequest request = new BookUpdateRequest(title, author, imageURL, info);
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/book/{bookID}", id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteBookRequest(String accessToken, Long id) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/book/{bookID}", id)
                .then().log().all()
                .extract();
    }
}
