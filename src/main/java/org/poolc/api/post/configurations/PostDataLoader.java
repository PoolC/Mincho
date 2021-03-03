package org.poolc.api.post.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("postTest")
@RequiredArgsConstructor
public class PostDataLoader implements CommandLineRunner {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

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

        postRepository.save(new Post(noticeBoard, admin, "test1", "test1", null));
        postRepository.save(new Post(freeBoard, notAdmin, "test2", "test2", null));
        postRepository.save(new Post(adminBoard, admin, "test3", "test3", null));
        postRepository.save(new Post(freeBoard, notAdmin, "작성자willBeDeleted", "willBeDeleted", null));
        postRepository.save(new Post(freeBoard, notAdmin, "작성자XwillBeDeleted", "willBeDeleted", null));
        postRepository.save(new Post(freeBoard, notAdmin, "임원willBeDeleted", "willBeDeleted", null));
        postRepository.save(new Post(freeBoard, notAdmin, "test4", "test4", null));
    }
}
