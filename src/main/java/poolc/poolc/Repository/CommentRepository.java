package poolc.poolc.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import poolc.poolc.domain.Comment;
import poolc.poolc.domain.Post;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment){
        em.persist(comment);
    }

    public void delete(Long commentID){
        em.remove(em.find(Comment.class,commentID));
    }

    public Comment findOne(Long commentID){
        return em.find(Comment.class,commentID);
    }

    public List<Comment> findAllByPost(Long PostID){
        return em.createQuery("select c from Comment c where c.post = :post",Comment.class)
                .setParameter("post",em.find(Post.class, PostID))
                .getResultList();
    }
}
