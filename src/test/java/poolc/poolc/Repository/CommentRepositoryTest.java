package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.*;
import poolc.poolc.repository.BoardRepository;
import poolc.poolc.repository.CommentRepository;
import poolc.poolc.repository.MemberRepository;
import poolc.poolc.repository.PostRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class CommentRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 댓글생성() throws Exception{
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        Post post = new Post(board, member, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post);

        //when
        Comment comment = new Comment(post, member, "aaaa", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment);


        //then
        em.flush();
        em.clear();
        comment.equals(commentRepository.findOne(comment.getId()));
    }

    @Test
    public void 댓글삭제() throws Exception{
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        Post post = new Post(board, member, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post);

        Comment comment = new Comment(post, member, "aaaa", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment);
        //when
        commentRepository.delete(comment.getId());

        //then
        em.flush();
        em.clear();
        Assertions.assertNull(commentRepository.findOne(comment.getId()));
    }

    @Test
    public void 게시글ID로댓글전체조회() throws Exception{
        //given
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        Post post = new Post(board, member, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post);

        Comment comment1 = new Comment(post, member, "aaaa", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment1);

        Comment comment2 = new Comment(post, member, "bbbb", LocalDateTime.now(), LocalDateTime.now());
        commentRepository.save(comment2);
        //when
        List<Comment> comments = commentRepository.findAllByPost(post.getId());
        //then
        Assertions.assertEquals(2L, comments.size());
    }

}
