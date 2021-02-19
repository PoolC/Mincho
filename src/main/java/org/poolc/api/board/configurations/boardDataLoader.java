package org.poolc.api.board.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.repository.BoardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("boardtest")
@RequiredArgsConstructor
public class boardDataLoader implements CommandLineRunner {
    private final BoardRepository boardRepository;

    @Override
    public void run(String... args) {
        boardRepository.save(
                new Board("공지사항", "/공지사항", "true", "true")
        );
        boardRepository.save(
                new Board("자유게시판", "/자유게시판", "true", "true")
        );
        boardRepository.save(
                new Board("구인홍보게시판", "/구인홍보게시판", "true", "true")
        );
        boardRepository.save(
                new Board("학술게시판", "/학술게시판", "true", "true")
        );
        boardRepository.save(
                new Board("게임제작부", "/게임제작부", "true", "true")
        );
        boardRepository.save(
                new Board("삭제할게시판", "/삭제할게시판", "true", "true")
        );
        boardRepository.save(
                new Board("수정할게시판", "/수정할게시판", "false", "false")
        );
    }
}
