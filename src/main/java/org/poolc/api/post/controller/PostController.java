package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.poolc.api.post.service.PostService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createPost(@AuthenticationPrincipal Member writer,
                                                        @RequestBody RegisterPostRequest request) {
        Long postId = postService.create(writer, request);
        return ResponseEntity.ok().body(Collections.singletonMap("postId", postId));
    }


    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getPost(@AuthenticationPrincipal Member user,
                                                @PathVariable Long postId) {
        PostResponse postResponse = PostResponse.of(postService.getPost(user, postId));
        return ResponseEntity.ok().body(postResponse);
    }


    @GetMapping(value = "/board/{urlPath}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<PostResponse>>> findPostsByBoardPagination(@AuthenticationPrincipal Member user,
                                                                                      @PathVariable String urlPath, @RequestParam Long page) {
        HashMap<String, List<PostResponse>> responseBody = new HashMap<>() {
            {
                put("data", postService.getPostsByBoard(user, urlPath, page).stream()
                        .map(PostResponse::of)
                        .collect(Collectors.toList())
                );
            }
        };

        return ResponseEntity.ok().body(responseBody);
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Void> updatePost(@AuthenticationPrincipal Member user,
                                           @PathVariable Long postId,
                                           @RequestBody UpdatePostRequest request) {
        postService.updatePost(postId, user, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal Member user, @PathVariable Long postId) {
        postService.deletePost(user, postId);
        return ResponseEntity.ok().build();
    }
}
