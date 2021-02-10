package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Board;
import poolc.poolc.repository.BoardRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 게시판저장() {
        //given
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());

        //when
        boardRepository.save(board);

        //then
        em.flush();
        em.clear();

        Optional<Board> findBoard = boardRepository.findById(board.getId());

        board.equals(findBoard);
    }

    @Test
    public void 게시판삭제() {
        //given
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        //when
        boardRepository.delete(board);

        //then
        em.flush();
        em.clear();

        Optional<Board> findBoard = boardRepository.findById(board.getId());
        Assertions.assertTrue(findBoard.isEmpty());
    }


}