package org.poolc.api.board.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("boardTest")
@RequiredArgsConstructor
public class BoardDataLoader implements CommandLineRunner {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    private final String 비임원 = "MEMBER_ID";
    private final String 임원 = "ADMIN_ID";

    private final Long noticeBoardId = 1L;
    private final Long freeBoardId = 2L;
    private final Long adminBoardId = 6L;

    @Override
    public void run(String... args) {
        boardRepository.save(
                new Board("공지사항", "notice", MemberRole.PUBLIC, MemberRole.ADMIN)
        );
        boardRepository.save(
                new Board("자유게시판", "free", MemberRole.MEMBER, MemberRole.MEMBER)
        );
        boardRepository.save(
                new Board("구인홍보게시판", "recruit", MemberRole.MEMBER, MemberRole.MEMBER)
        );
        boardRepository.save(
                new Board("학술게시판", "study_priv", MemberRole.MEMBER, MemberRole.MEMBER)
        );
        boardRepository.save(
                new Board("게임제작부", "gamedev", MemberRole.MEMBER, MemberRole.MEMBER)
        );
        boardRepository.save(
                new Board("임원진", "meeting", MemberRole.ADMIN, MemberRole.ADMIN)
        );
        boardRepository.save(
                new Board("삭제할게시판", "willbedeleted", MemberRole.PUBLIC, MemberRole.MEMBER)
        );
        boardRepository.save(
                new Board("수정할게시판", "updateBoard", MemberRole.PUBLIC, MemberRole.MEMBER)
        );
    }
}
