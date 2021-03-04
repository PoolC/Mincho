package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.PostsResponse;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final PostService postService;

    private String PUBLIC = "PUBLIC";
    private String MEMBER = "MEMBER";
    private String ADMIN = "ADMIN";
    private String UUID = "UUID";
    private String TRUE = "true";
    private String ISADMIN = "isAdmin";
    private String NOPERMISSION = "No Permission";

    // TODO: DB로 확인하지 못하므로 확인용으로 만든 api, test가 끝나면 반드시 지워야한다.
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostsResponse> getAllPost() {
        List<PostResponse> postResponses = postService.getAllposts()
                .stream().map(PostResponse::of)
                .collect(Collectors.toList());

        PostsResponse postsResponse = new PostsResponse(postResponses);

        return ResponseEntity.ok().body(postsResponse);
    }

    @PostMapping
    public ResponseEntity<Void> createPost(@AuthenticationPrincipal Member writer,
                                           @RequestBody RegisterPostRequest registerPostRequest) {
        Board correspondingBoard = boardService.findBoardById(registerPostRequest.getBoardId());

        checkWritePermissions(writer, correspondingBoard);

        PostCreateValues postCreateValues = new PostCreateValues(writer, correspondingBoard, registerPostRequest);
        postService.create(postCreateValues);

        return ResponseEntity.accepted().build();
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

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member user, @PathVariable Long postId) {
        Post updatePost = postService.getPost(postId);

        checkWriterOrAdmin(user, updatePost);

        postService.deletePost(postId);
        return ResponseEntity.ok().build();
    }

    private void checkWriter(Member user, Post updatePost) {
        if (!updatePost.getMember().getUUID().equals(user.getUUID())) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
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
