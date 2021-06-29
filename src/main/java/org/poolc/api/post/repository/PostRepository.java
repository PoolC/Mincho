package org.poolc.api.post.repository;

import org.poolc.api.board.domain.Board;
import org.poolc.api.post.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {
    List<Post> findPaginationByBoard(Board board, Pageable pageable);
}

