package poolc.poolc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import poolc.poolc.domain.Comment;
import poolc.poolc.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}