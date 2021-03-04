package org.poolc.api.comment.service;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.post.domain.Post;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;

    public void createComment(CommentCreateValues values) {
        commentRepository.save(
                Comment.builder()
                        .body(values.getBody())
                        .member(values.getMember())
                        .post(values.getPost())
                        .build());
    }

    public List<Comment> findCommentsByPost(Post post) {
        return commentRepository.findAllByPost(post);
    }

    public Comment findOne(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("No comment found with given commentId"));
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }


    public void updateComment(Long commentId, CommentUpdateValues commentUpdateValues) {
        Comment updateComment = findOne(commentId);
        updateComment.updateComment(commentUpdateValues);
        commentRepository.flush();
    }

    public void deleteComment(Long commentId) {
        Comment deleteComment = findOne(commentId);
        commentRepository.delete(deleteComment);
    }

}
