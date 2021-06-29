package org.poolc.api.post.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final BoardService boardService;
    private final int PAGE_SIZE = 15;

    public Long create(Member writer, RegisterPostRequest request) {
        Board correspondingBoard = boardService.findBoardById(request.getBoardId());

        checkWritePermission(writer, correspondingBoard);

        Post post = postRepository.save(new Post(writer, correspondingBoard, request));
        return post.getId();
    }


    public Post getPost(Member user, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("no post found with given postId"));
        Board correspondingBoard = post.getBoard();

        checkReadPermissions(user, correspondingBoard);
        post.getCommentList().sort(Comparator.comparing(Comment::getCreatedAt));
        return post;
    }

//    public List<Post> getPostsByBoard(Member user, String urlPath) {
//        Board board = boardService.findBoardByUrlPath(urlPath);
//
//        checkReadPermissions(user, board);
//
//        return postRepository.findAllByBoard(board);
//    }

    public List<Post> getPostsByBoard(Member user, String urlPath, Long Page) {
        Board board = boardService.findBoardByUrlPath(urlPath);
        checkReadPermissions(user, board);
        return postRepository.findPaginationByBoard(board, PageRequest.of(PAGE_SIZE * (Page.intValue() - 1), PAGE_SIZE * Page.intValue(), Sort.by("id").descending()));
    }


    @Transactional
    public void updatePost(Long postId, Member user, UpdatePostRequest request) {
        Post updatePost = getPost(user, postId);
        checkWriter(user, updatePost);
        updatePost.update(request);
    }

    @Transactional
    public void deletePost(Member user, Long postId) {
        Post deletePost = getPost(user, postId);
        checkWriterOrAdmin(user, deletePost);
        postRepository.delete(deletePost);
    }

    private void checkWritePermission(Member writer, Board board) {
        if (!board.memberHasWritePermissions(writer)) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkReadPermissions(Member user, Board board) {
        checkReadPermissionIfNoLogin(user, board);
        checkReadPermissionIfLogin(user, board);
    }

    private void checkReadPermissionIfNoLogin(Member user, Board board) {
        if (user == null && !board.isPublicReadPermission()) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkReadPermissionIfLogin(Member user, Board board) {
        if (user != null && !board.memberHasReadPermissions(user.getRoles())) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkWriter(Member user, Post post) {
        if (!post.getMember().equals(user)) {
            throw new org.poolc.api.auth.exception.UnauthorizedException("접근할 수 없습니다.");
        }
    }

    private void checkWriterOrAdmin(Member user, Post post) {
        if (!post.getMember().equals(user) && !user.isAdmin()) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }
}
