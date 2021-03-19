package org.poolc.api.post;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile("postTest")
@RequiredArgsConstructor
public class PostDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final String 비임원 = "MEMBER_ID";
    private final String 임원 = "ADMIN_ID";

    private final Long noticeBoardId = 1L;
    private final Long freeBoardId = 2L;
    private final Long adminBoardId = 6L;

    @Override
    public void run(String... args) {
        Member notAdmin = memberRepository.findByLoginID(비임원).get();
        Member admin = memberRepository.findByLoginID(임원).get();

        Board noticeBoard = boardRepository.findById(noticeBoardId).get();
        Board freeBoard = boardRepository.findById(freeBoardId).get();
        Board adminBoard = boardRepository.findById(adminBoardId).get();

        List<String> file_list = new ArrayList<>();
        file_list.add("https://s.pstatic.net/shopping.phinf/20210315_22/6303748a-9e79-49ff-807a-1f28626988d5.jpg");
        file_list.add("https://s.pstatic.net/shopping.phinf/20210309_6/7865a9bf-017f-4d45-a945-006cd6903f8e.jpg");


        Post noticePost = new Post(noticeBoard, admin, "test1", "test1", null, file_list);
        Post freePost = new Post(freeBoard, notAdmin, "test2", "test2", null, file_list);

        postRepository.save(noticePost);
        postRepository.save(freePost);

        postRepository.save(new Post(adminBoard, admin, "test3", "test3", null, null));
        postRepository.save(new Post(freeBoard, notAdmin, "작성자willBeDeleted", "willBeDeleted", null, null));
        postRepository.save(new Post(freeBoard, notAdmin, "작성자XwillBeDeleted", "willBeDeleted", null, null));
        postRepository.save(new Post(freeBoard, notAdmin, "임원willBeDeleted", "willBeDeleted", null, null));
        postRepository.save(new Post(freeBoard, notAdmin, "test4", "test4", null, null));

        commentRepository.save(new Comment(noticePost, notAdmin, "test1"));
        commentRepository.save(new Comment(freePost, notAdmin, "test2"));
        commentRepository.save(new Comment(noticePost, notAdmin, "test3"));
        commentRepository.save(new Comment(freePost, notAdmin, "test4"));
        commentRepository.save(new Comment(freePost, notAdmin, "test5"));
        commentRepository.save(new Comment(freePost, notAdmin, "test6"));

    }
}
