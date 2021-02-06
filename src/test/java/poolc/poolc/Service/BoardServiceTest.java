package poolc.poolc.Service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.*;
import poolc.poolc.repository.BoardRepository;
import poolc.poolc.repository.CommentRepository;
import poolc.poolc.repository.MemberRepository;
import poolc.poolc.repository.PostRepository;
import poolc.poolc.service.BoardService;
import poolc.poolc.service.BookService;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {
    @Autowired
    BoardService boardService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    EntityManager em;


    @Test
    public void 게시판생성() throws Exception{
        //given

        //when
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardService.saveBoard(board);
        //then
        em.flush();
        em.clear();
        board.equals(boardService.findOneBoard(board.getId()));
    }

    @Test
    public void 게시글댓글있는게시판삭제() throws Exception{
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardService.saveBoard(board);

        Post post1 = new Post(board, member, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post(board, member, "ddd", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post2);

        Comment comment1 = new Comment(post1, member, "aaaa", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment(post1, member, "ddddd", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment2);

        Comment comment3 = new Comment(post2, member, "ddddd", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment3);
        //when
        boardService.deleteBoard(board.getId());
        //then

        em.flush();
        em.clear();
        assertNull(boardService.findOneBoard(board.getId()));
        assertTrue(postRepository.findAllByBoard(board).isEmpty());
        assertTrue(commentRepository.findAllByPost(post1.getId()).isEmpty());
        assertTrue(commentRepository.findAllByPost(post2.getId()).isEmpty());
    }

    @Test
    public void 게시판업데이트() throws Exception{
        //given
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardService.saveBoard(board);
        //when
        boardService.update(board.getId(), "poolc","/poolc", "noRead", "noWrite");
        //then
        em.flush();
        em.clear();
        Board findBoard = boardService.findOneBoard(board.getId());
        assertEquals("poolc",findBoard.getName());
        assertEquals("/poolc",findBoard.getUrlPath());
        assertEquals("noRead",findBoard.getReadPermission());
        assertEquals("noWrite",findBoard.getWritePermission());
    }

}
