package org.poolc.api.post.repository;

import org.poolc.api.board.domain.Board;
import org.poolc.api.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p WHERE p.board = :board ")
    List<Post> findAllByBoard(@Param("board") Board board);

//    @Query(value = "SELECT p FROM Post p")
//    List<Post> findAllFetchJoin();

//    @Query(value = "SELECT p FROM Post p join fetch p.board WHERE p.id = :postId")
//    Optional<Post> findpost(@Param("postId") Long postId);

}

//    public List<Post> findAllByBoard(Board board) {
//        return em.createQuery("select p from Post p where p.board = :board", Post.class)
//                .setParameter("board", board)
//                .getResultList();
//    }

//    public List<Post> findAllByAuthor(Member member) {
//        return em.createQuery("select p from Post p where p.member = :author", Post.class)
//                .setParameter("author", member)
//                .getResultList();
//    }

