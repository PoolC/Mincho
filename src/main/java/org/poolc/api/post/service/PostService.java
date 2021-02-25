package org.poolc.api.post.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.board.domain.Board;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void create(PostCreateValues postCreateValues) {
        postRepository.save(new Post(postCreateValues));
    }

    public Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("no post found with given postId"));
    }

    public List<Post> getAllposts() {
        return postRepository.findAll();
    }

    public List<Post> getPostByBoardId(Board board) {
        return postRepository.findAllByBoard(board);
    }

    @Transactional
    public void updatePost(Long postId, PostUpdateValues postUpdateValues) {
        Post updatePost = getPost(postId);
        updatePost.update(postUpdateValues);
    }

    @Transactional
    public void deletePost(Long postId) {
        Post deletePost = getPost(postId);
        postRepository.delete(deletePost);
    }
}
