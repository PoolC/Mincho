package org.poolc.api.comment.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.dto.RegisterCommentRequest;
import org.poolc.api.comment.dto.UpdateCommentRequest;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.comment.vo.CommentCreateValues;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@AuthenticationPrincipal Member user,
                                              @RequestBody RegisterCommentRequest registerCommentRequest) {
        Post correspondingPost = postService.getPost(registerCommentRequest.getPostId());

        String body = registerCommentRequest.getBody();
        CommentCreateValues commentCreateValues = new CommentCreateValues(correspondingPost, user, body);
        commentService.createComment(commentCreateValues);

        return ResponseEntity.accepted().build();
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<Void> updateComment(@AuthenticationPrincipal Member member,
                                              @RequestBody UpdateCommentRequest requestBody,
                                              @PathVariable Long commentId) {
        Comment correspondingComment = commentService.findOne(commentId);

        checkWriter(member, correspondingComment);

        CommentUpdateValues commentUpdateValues = new CommentUpdateValues(requestBody);
        commentService.updateComment(commentId, commentUpdateValues);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<Void> deleteComment(@AuthenticationPrincipal Member user,
                                              @PathVariable Long commentId) {
        Comment correspondingComment = commentService.findOne(commentId);

        checkWriterOrAdmin(user, correspondingComment);

        commentService.deleteComment(commentId);

        return ResponseEntity.ok().build();
    }

    private void checkWriter(Member member, Comment correspondingComment) {
        if (!member.getUUID().equals(correspondingComment.getMember().getUUID())) {
            throw new UnauthorizedException("작성자가 아닙니다.");
        }
    }

    private void checkWriterOrAdmin(Member user, Comment correspondingComment) {
        if (!correspondingComment.getMember().getUUID().equals(user.getUUID()) && !user.isAdmin()) {
            throw new UnauthorizedException("접근할 수 없습니다.");
        }
    }
}
