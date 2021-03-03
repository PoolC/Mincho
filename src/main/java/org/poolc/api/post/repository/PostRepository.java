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
}

