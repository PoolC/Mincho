package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Board;
import poolc.poolc.repository.BoardRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
public class BoardRepositoryTest {
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 게시판저장() throws Exception{
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        em.flush();
        em.clear();

        Board findBoard = boardRepository.findOne(board.getId());

        board.equals(findBoard);
    }

    @Test
    public void 게시판삭제() throws Exception{
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);
        boardRepository.delete(board);

        em.flush();
        em.clear();

        Board findBoard = boardRepository.findOne(board.getId());
        Assertions.assertNull(findBoard);
    }


}
