package org.poolc.api.book;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.poolc.api.AcceptanceTest;
import org.poolc.api.auth.dto.AuthResponse;
import org.poolc.api.book.dto.BookRequest;
import org.poolc.api.book.dto.BookResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
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
        assertThat(response.body().jsonPath().getList("data")).hasSize(7);
    }

    @Test
    void 로그인X_findAllBooks() {
        String accessToken = "";

        ExtractableResponse<Response> response = getBooksRequest(accessToken);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("data")).hasSize(7);
    }


    @Test
    void findOneBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        borrowBookRequest(accessToken, 1L);

        ExtractableResponse<Response> response = getBookRequest(accessToken, 1L);
        BookResponse bookResponse = response.as(BookResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookResponse.getId()).isEqualTo(1L);
        assertThat(bookResponse.getBorrower().getName()).isEqualTo("MEMBER_NAME");
    }

    @Test
    void 로그인X_findOneBook() {
        String accessToken = "";
        ExtractableResponse<Response> response = getBookRequest(accessToken, 1L);
        BookResponse bookResponse = response.as(BookResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(bookResponse.getId()).isEqualTo(1L);
        assertThat(bookResponse.getBorrower().getName()).isEqualTo("MEMBER_NAME");
    }

    @Test
    void findOneBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = getBookRequest(accessToken, 100L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void borrowBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 2L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void borrowBookWithBorrower() {
        borrowBookRequest(loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken(), 3L);
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 3L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void borrowBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = borrowBookRequest(accessToken, 423L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void returnMyBook() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();
        borrowBookRequest(accessToken, 4L);

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 4L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void returnBookDoesNotExist() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 432L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void returnNotMyBook() {
        borrowBookRequest(loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken(), 5L);
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 5L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void returnBookNotBorrowed() {
        String accessToken = loginRequest("MEMBER_ID2", "MEMBER_PASSWORD2")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = returnBookRequest(accessToken, 6L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void registerBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
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
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void registerBookAlreadyExist() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = createBookRequest(accessToken, "형철이의 삶", "박형철", "d", "ㅇㄴ");
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void updateBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "풀씨", "형철띠", "d", "ㅇㄴ", 7L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    @Test
    void updateNonExistingBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ", 432L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void updateBookNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶 3", "박형철", "d", "ㅇㄴ", 6L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void updateBookAlreadyExistSameBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = updateBookRequest(accessToken, "형철이의 삶", "박형철", "d", "ㅇㄴ", 6L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @DirtiesContext
    @Test
    void deleteBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 1L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void deleteBookNonAdminUser() {
        String accessToken = loginRequest("MEMBER_ID", "MEMBER_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 6L);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Test
    void deleteNonExistingBook() {
        String accessToken = loginRequest("ADMIN_ID", "ADMIN_PASSWORD")
                .as(AuthResponse.class)
                .getAccessToken();

        ExtractableResponse<Response> response = deleteBookRequest(accessToken, 432L);
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
        BookRequest request = new BookRequest(title, author, imageURL, info);
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
        BookRequest request = new BookRequest(title, author, imageURL, info);
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