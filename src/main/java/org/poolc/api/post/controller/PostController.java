package org.poolc.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostResponse;
import org.poolc.api.post.dto.PostsResponse;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.poolc.api.post.exception.UnauthorizedException;
import org.poolc.api.post.service.PostService;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    private String PUBLIC = "PUBLIC";
    private String MEMBER = "MEMBER";
    private String ADMIN = "ADMIN";
    private String UUID = "UUID";
    private String TRUE = "true";
    private String ISADMIN = "isAdmin";
    private String NOPERMISSION = "No Permission";

    @PostMapping
    public ResponseEntity<Void> createPost(HttpServletRequest request,
                                           @RequestBody RegisterPostRequest registerPostRequest) {
        String uuid = request.getAttribute(UUID).toString();
        Member writer = memberService.findMember(uuid);
        Board board = boardService.get(registerPostRequest.getBoardId());

        checkPermissionToWrite(writer, board);

        PostCreateValues postCreateValues = new PostCreateValues(writer, board, registerPostRequest);
        postService.create(postCreateValues);

        return ResponseEntity.accepted().build();
    }


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostsResponse> getAllPost(HttpServletRequest request) {
        List<PostResponse> postResponses = postService.getAllposts()
                .stream().map(post -> new PostResponse(post))
                .collect(Collectors.toList());

        PostsResponse postsResponse = new PostsResponse(postResponses);

        return ResponseEntity.ok().body(postsResponse);
    }

    @GetMapping(value = "/{postId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostResponse> getPost(HttpServletRequest request, @PathVariable Long postId) throws NoPermissionException {
        Post findPost = postService.getPost(postId);


        if (findPost.getBoard().getReadPermission().equals(PUBLIC)) {
            PostResponse postResponse = new PostResponse(findPost);
            return ResponseEntity.ok().body(postResponse);
        }

        if (findPost.getBoard().getReadPermission().equals(MEMBER) && request.getAttribute(UUID) != null) {
            PostResponse postResponse = new PostResponse(findPost);
            return ResponseEntity.ok().body(postResponse);
        }

        checkLogin(request);

        String uuid = request.getAttribute(UUID).toString();
        Member user = memberService.findMember(uuid);

        if (findPost.getBoard().getReadPermission().equals(ADMIN) && user.getIsAdmin().equals(true)) {
            PostResponse postResponse = new PostResponse(findPost);
            return ResponseEntity.ok().body(postResponse);
        }

        throw new UnauthorizedException(NOPERMISSION);
    }

    @GetMapping(value = "/board/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PostsResponse> getPostsByBoard(HttpServletRequest request, @PathVariable Long boardId) {
        Board board = boardService.get(boardId);
        if (board.getReadPermission().equals(PUBLIC)) {
            PostsResponse postsResponse = getPostsResponseByBoard(board);
            return ResponseEntity.ok().body(postsResponse);
        }

        if (board.getReadPermission().equals(MEMBER) && request.getAttribute(UUID) != null) {
            PostsResponse postsResponse = getPostsResponseByBoard(board);
            return ResponseEntity.ok().body(postsResponse);
        }

        checkLogin(request);

        String uuid = request.getAttribute(UUID).toString();
        Member user = memberService.findMember(uuid);

        if (board.getReadPermission().equals(MEMBER) && user.getIsAdmin().equals(true)) {
            PostsResponse postsResponse = getPostsResponseByBoard(board);
            return ResponseEntity.ok().body(postsResponse);
        }

        throw new UnauthorizedException(NOPERMISSION);
    }

    private void checkLogin(HttpServletRequest request) {
        if (request.getAttribute(UUID) == null) {
            throw new UnauthorizedException(NOPERMISSION);
        }
    }

    private PostsResponse getPostsResponseByBoard(Board board) {
        List<Post> findPosts = postService.getPostByBoardId(board);
        List<PostResponse> transformPostResponse = findPosts.stream()
                .map(post -> new PostResponse(post))

                .collect(Collectors.toList());
        return new PostsResponse(transformPostResponse);
    }

    @PutMapping(value = "/{postId}")
    public ResponseEntity<Void> updatePost(@PathVariable Long postId,
                                           @RequestBody UpdatePostRequest updatePostRequest,
                                           HttpServletRequest request) {
        Post updatePost = postService.getPost(postId);
        String uuid = request.getAttribute(UUID).toString();
        Member user = memberService.findMember(uuid);

        if (!updatePost.getMember().getUUID().equals(user.getUUID())) {
            throw new UnauthorizedException(NOPERMISSION);
        }

        PostUpdateValues postUpdateValues = new PostUpdateValues(updatePostRequest);
        postService.updatePost(postId, postUpdateValues);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
                                           HttpServletRequest request) {
        Post updatePost = postService.getPost(postId);
        String uuid = request.getAttribute(UUID).toString();
        Member user = memberService.findMember(uuid);

        if (updatePost.getMember().getUUID().equals(user.getUUID())) {
            postService.deletePost(postId);
            return ResponseEntity.ok().build();
        }

        if (request.getAttribute(ISADMIN).equals(TRUE)) {
            postService.deletePost(postId);
            return ResponseEntity.ok().build();
        }

        throw new UnauthenticatedException(NOPERMISSION);

    }

    @ExceptionHandler({UnauthenticatedException.class, UnauthorizedException.class})
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void checkPermissionToWrite(Member writer, Board board) {
        if (writer.getIsAdmin().equals(false) && board.getWritePermission().equals(ADMIN)) {
            throw new UnauthorizedException(NOPERMISSION);
        }
    }
}
