package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final BoardService boardService;
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createPost(@AuthenticationPrincipal Member writer,
                                                        @RequestBody RegisterPostRequest registerPostRequest) {
        Board correspondingBoard = boardService.findBoardById(registerPostRequest.getBoardId());

        checkWritePermissions(writer, correspondingBoard);

        PostCreateValues postCreateValues = new PostCreateValues(writer, correspondingBoard, registerPostRequest);
        Long postId = postService.create(postCreateValues);

        return ResponseEntity.accepted().body(Collections.singletonMap("postId", postId));
    }


    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getPost(@AuthenticationPrincipal Member user,
                                                @PathVariable Long postId) {
        Post findPost = postService.getPost(postId);
        Board correspondingBoard = findPost.getBoard();

        checkReadPermissions(user, correspondingBoard);

        PostResponse postResponse = PostResponse.of(findPost);
        return ResponseEntity.ok().body(postResponse);
    }

    @GetMapping(value = "/board/{urlPath}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<PostResponse>>> findPostsByBoard(@AuthenticationPrincipal Member user, @PathVariable String urlPath) {
        Board correspondingBoard = boardService.findBoardByUrlPath(urlPath);

        checkReadPermissions(user, correspondingBoard);

        HashMap<String, List<PostResponse>> responseBody = new HashMap<>() {{
            put("data", postService.getPostsByBoard(correspondingBoard).stream()
                    .map(PostResponse::of)
                    .collect(Collectors.toList()));
        }};

        return ResponseEntity.ok().body(responseBody);
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal Member user,
                                           @PathVariable Long postId,
                                           @RequestBody UpdatePostRequest updatePostRequest) {
        Post updatePost = postService.getPost(postId);

        checkWriter(user, updatePost);

        PostUpdateValues postUpdateValues = new PostUpdateValues(updatePostRequest);
        postService.updatePost(postId, postUpdateValues);

        return ResponseEntity.ok().build();
    }

    private void checkWriter(Member user, Post updatePost) {
        if (!updatePost.getMember().getUUID().equals(user.getUUID())) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }


    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member user, @PathVariable Long postId) {
        Post updatePost = postService.getPost(postId);

        checkWriterOrAdmin(user, updatePost);

        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({UnauthenticatedException.class, org.poolc.api.auth.exception.UnauthorizedException.class})
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void checkWritePermissions(Member user, Board board) {
        if (!board.memberHasWritePermissions(user.getRoles())) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkReadPermissions(Member user, Board board) {
        checkReadPermissionIfNoLogin(user, board);
        checkReadPermissionIfLogin(user, board);
    }

    private void checkReadPermissionIfNoLogin(Member user, Board correspondingBoard) {
        if (user == null && !correspondingBoard.isPublicReadPermission()) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkReadPermissionIfLogin(Member user, Board board) {
        if (user != null && !board.memberHasReadPermissions(user.getRoles())) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkWriterOrAdmin(Member user, Post updatePost) {
        if (!updatePost.getMember().getUUID().equals(user.getUUID()) && !user.isAdmin()) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }
}
