package org.poolc.api.post;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Profile("postTest")
@RequiredArgsConstructor
public class PostDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostService postService;
    private final CommentRepository commentRepository;

    private final String 비임원 = "MEMBER_ID";
    private final String 임원 = "ADMIN_ID";

    private final Long noticeBoardId = 1L;
    private final Long freeBoardId = 2L;
    private final Long adminBoardId = 6L;
    private final Long paginationBoardId = 9L;

    @Override
    public void run(String... args) {
        Member notAdmin = memberRepository.findByLoginID(비임원).get();
        Member admin = memberRepository.findByLoginID(임원).get();

        Board noticeBoard = boardRepository.findById(noticeBoardId).get();
        Board freeBoard = boardRepository.findById(freeBoardId).get();
        Board adminBoard = boardRepository.findById(adminBoardId).get();
        Board paginationBoard = boardRepository.findById(paginationBoardId).get();

        List<String> file_list = new ArrayList<String>(Arrays.asList(new String[]{"https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg", "https://s.pstatic.net/shopping.phinf/20210309_6/7865a9bf-017f-4d45-a945-006cd6903f8e.jpg"}));

        Long noticePostId = postService.create(admin, RegisterPostRequest.builder()
                .boardId(noticeBoardId)
                .title("test1")
                .body("test1")
                .file_list(file_list)
                .build());
        Long freePostId = postService.create(admin, RegisterPostRequest.builder()
                .boardId(freeBoardId)
                .title("test2")
                .body("test2")
                .file_list(file_list)
                .build());

        postService.create(admin, RegisterPostRequest.builder()
                .boardId(adminBoardId)
                .title("test3")
                .body("test3")
                .file_list(null)
                .build());
        postService.create(notAdmin, RegisterPostRequest.builder()
                .boardId(freeBoardId)
                .title("작성자willBeDeleted")
                .body("willBeDeleted")
                .file_list(null)
                .build());
        postService.create(notAdmin, RegisterPostRequest.builder()
                .boardId(freeBoardId)
                .title("작성자XwillBeDeleted")
                .body("willBeDeleted")
                .file_list(null)
                .build());
        postService.create(notAdmin, RegisterPostRequest.builder()
                .boardId(freeBoardId)
                .title("임원willBeDeleted")
                .body("willBeDeleted")
                .file_list(null)
                .build());
        postService.create(notAdmin, RegisterPostRequest.builder()
                .boardId(freeBoardId)
                .title("test4")
                .body("test4")
                .file_list(null)
                .build());

        for (int i = 0; i < 46; i++) {
            postService.create(notAdmin, RegisterPostRequest.builder()
                    .boardId(paginationBoardId)
                    .title("test" + i)
                    .body("test" + i)
                    .file_list(null)
                    .build());
        }
        Post noticePost = postService.getPost(admin, noticePostId);
        Post freePost = postService.getPost(admin, freePostId);
        commentRepository.save(new Comment(noticePost, notAdmin, "test1"));
        commentRepository.save(new Comment(freePost, notAdmin, "test2"));
        commentRepository.save(new Comment(noticePost, notAdmin, "test3"));
        commentRepository.save(new Comment(freePost, notAdmin, "test4"));
        commentRepository.save(new Comment(freePost, notAdmin, "test5"));
        commentRepository.save(new Comment(freePost, notAdmin, "test6"));

    }
}
