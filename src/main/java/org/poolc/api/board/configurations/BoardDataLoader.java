package org.poolc.api.board.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.member.repository.MemberRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("boardTest")
@RequiredArgsConstructor
public class BoardDataLoader implements CommandLineRunner {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;

    @Override
    public void run(String... args) {
        boardRepository.save(
                new Board("공지사항", "notice", "PUBLIC", "ADMIN")
        );
        boardRepository.save(
                new Board("자유게시판", "free", "MEMBER", "MEMBER")
        );
        boardRepository.save(
                new Board("구인홍보게시판", "recruit", "MEMBER", "MEMBER")
        );

        boardRepository.save(
                new Board("학술게시판", "study_priv", "MEMBER", "MEMBER")
        );
        boardRepository.save(
                new Board("게임제작부", "gamedev", "MEMBER", "MEMBER")
        );
        boardRepository.save(
                new Board("삭제할게시판", "willbedeleted", "PUBLIC", "MEMBER")
        );
        boardRepository.save(
                new Board("수정할게시판", "updateBoard", "PUBLIC", "MEMBER")
        );
    }
}
