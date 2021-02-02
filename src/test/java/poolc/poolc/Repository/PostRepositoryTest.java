package poolc.poolc.Repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import poolc.poolc.domain.Board;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.Post;
import poolc.poolc.domain.ProjectMember;
import poolc.poolc.repository.BoardRepository;
import poolc.poolc.repository.MemberRepository;
import poolc.poolc.repository.PostRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class PostRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    BoardRepository  boardRepository;
    @Autowired
    EntityManager em;

    @Test
    public void 게시글작성() throws Exception{
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

        Assertions.assertEquals(post, postRepository.findOne(post.getId()));
    }

    @Test
    public void 게시글삭제() throws Exception{
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

        em.flush();
        em.clear();
        postRepository.delete(post.getId());
        Assertions.assertNull(postRepository.findOne(post.getId()));
    }

    @Test
    public void 게시판에있는게시글전체조회() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member1 = new Member("1", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member1);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        Post post1 = new Post(board, member1, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post(board, member1, "bbb", "cccc", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post2);

        List<Post> boards = postRepository.findAllByBoard(board);
        Assertions.assertEquals(2L,boards.size());
    }

    @Test
    public void 멤버가작성한게시글전체조회() throws Exception{
        List<ProjectMember> projectMembers = new ArrayList<>();
        String a = "ddd";
        Member member = new Member("2", "anfro2520", "jasotn12@naver.com", "010-4595-9147", "hyungchulpak",
                "ComputerScience", "2015147514", false, false, LocalDateTime.now(), LocalDateTime.now(), "pass",
                LocalDateTime.now(),"aaa","나는 박형철이다", false, a.getBytes(), a.getBytes(), projectMembers );

        memberRepository.save(member);
        Board board = new Board("test", "/test", "read", "write", LocalDateTime.now(), LocalDateTime.now());
        boardRepository.save(board);

        Post post1 = new Post(board, member, "aaaa", "aaaaaa", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post(board, member, "bbb", "cccc", LocalDateTime.now(), LocalDateTime.now());
        postRepository.save(post2);

        List<Post> boards = postRepository.findAllByAuthor(member);
        Assertions.assertEquals(2L,boards.size());
    }
}
