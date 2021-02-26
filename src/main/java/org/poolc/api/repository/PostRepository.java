package org.poolc.api.repository;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.domain.Post;
import org.poolc.api.member.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public void delete(Long postID) {
        em.remove(em.find(Post.class, postID));
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAllByBoard(Board board) {
        return em.createQuery("select p from Post p where p.board = :board", Post.class)
                .setParameter("board", board)
                .getResultList();
    }

    public List<Post> findAllByAuthor(Member member) {
        return em.createQuery("select p from Post p where p.member = :author", Post.class)
                .setParameter("author", member)
                .getResultList();
    }


}
