package org.poolc.api.comment.controller;


import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.service.CommentService;
import org.poolc.api.member.service.MemberService;
import org.poolc.api.post.service.PostService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final PostService postService;
    private final CommentService commentService;
    private final MemberService memberService;
    

}
