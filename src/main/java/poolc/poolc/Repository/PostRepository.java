package poolc.poolc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poolc.poolc.domain.Board;
import poolc.poolc.domain.Member;
import poolc.poolc.domain.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);

    List<Post> findByMember(Member member);
}
