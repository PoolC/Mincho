package org.poolc.api.comment.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.CommentResponse;
import org.poolc.api.comment.dto.RegisterCommentRequest;
import org.poolc.api.comment.dto.UpdateCommentRequest;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal Member user,
                                              @RequestBody RegisterCommentRequest registerCommentRequest) {
        Post correspondingPost = postService.getPost(registerCommentRequest.getPostId());
        String body = registerCommentRequest.getBody();
        CommentCreateValues commentCreateValues = new CommentCreateValues(correspondingPost, user, body);
        commentService.createComment(commentCreateValues);

        return ResponseEntity.accepted().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<CommentResponse>>> getAllComments() {
        HashMap<String, List<CommentResponse>> responseBody = new HashMap<>() {{
            put("data", commentService.findAll().stream()
                    .map(CommentResponse::of)
                    .collect(Collectors.toList()));
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/post/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HashMap<String, List<CommentResponse>>> getCommentsByPost(@PathVariable Long postId) {
        Post correspondingPost = postService.getPost(postId);
        HashMap<String, List<CommentResponse>> responseBody = new HashMap<>() {{
            put("data", commentService.findCommentsByPost(correspondingPost).stream()
                    .map(CommentResponse::of)
                    .collect(Collectors.toList()));
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @GetMapping(value = "/{commentId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CommentResponse> getComment(@AuthenticationPrincipal Member member,
                                                      @PathVariable Long commentId) {
        CommentResponse commentResponseBody = CommentResponse.of(commentService.findOne(commentId));
        return ResponseEntity.ok().body(commentResponseBody);
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Member member,
                                              @RequestBody UpdateCommentRequest requestBody,
                                              @PathVariable Long commentId) {
        Comment correspondingComment = commentService.findOne(commentId);
        if (!member.equals(correspondingComment.getMember())) {
            throw new AuthorizationServiceException("작성자가 아닙니다.");
        }
        CommentUpdateValues commentUpdateValues = new CommentUpdateValues(requestBody);
        commentService.updateComment(commentId, commentUpdateValues);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(AuthorizationServiceException.class)
    public ResponseEntity<String> AuthorizationServiceExceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

}
