package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.dto.*;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostSearchValues;
import org.poolc.api.search.service.PostSearcherService;
import org.poolc.api.search.vo.PostSearchResult;
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
    private final PostSearcherService postSearcherService;

    @PostMapping
    public ResponseEntity<Map<String, Long>> createPost(@AuthenticationPrincipal Member writer,
                                                        @RequestBody RegisterPostRequest request) {
        Long postId = postService.create(writer, request);
        return ResponseEntity.accepted().body(Collections.singletonMap("postId", postId));
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

    @GetMapping(value="/search")
    public ResponseEntity<PostsResponse> searchPostBySingleTerm(@AuthenticationPrincipal Member user,
                                                                @RequestParam String query) {
        List<String> boardNames = user.getAccessibleBoards(boardService.getAllBoards())
                .stream()
                .map(Board::getUrlPath)
                .collect(Collectors.toList());

        PostSearchValues values = new PostSearchValues(query, query, query, boardNames);
        PostsResponse posts = PostsResponse.of(searchPosts(user, values));
        return ResponseEntity.ok(posts);
    }

    @PostMapping(value="/search")
    public ResponseEntity<PostsResponse> advancedPostSearch(@AuthenticationPrincipal Member user,
                                                                 @RequestBody PostSearchRequest request) {
        List<String> boardNames = user.getAccessibleBoards(boardService.getAllBoards())
                .stream()
                .map(Board::getUrlPath)
                .collect(Collectors.toList());

        PostSearchValues values = new PostSearchValues(request.getTitle(), request.getBody(), request.getAuthor(),
                boardNames);
        PostsResponse posts = PostsResponse.of(searchPosts(user, values));
        return ResponseEntity.ok(posts);
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

    private List<PostResponse> searchPosts(Member user, PostSearchValues values) {
        PostSearchResult result = postSearcherService.search(values);

        return result.getPostIds().stream()
                .map(postId -> postService.getPost(user, postId))
                .map(PostResponse::of)
                .collect(Collectors.toList());
    }
}
