package org.poolc.api.comment.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("commentTest")
@RequiredArgsConstructor
public class CommentDataLoader implements CommandLineRunner {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    private Long noticePostId = 9L;
    private Long freePostId = 10L;

    private String MEMBER_ID = "MEMBER_ID";
    private String ADMIN_ID = "ADMIN_ID";

    @Override
    public void run(String... args) {
        Member member = memberRepository.findByLoginID(MEMBER_ID).get();
        Member admin = memberRepository.findByLoginID(ADMIN_ID).get();

        Post noticePost = postRepository.findById(noticePostId).get();
        Post freePost = postRepository.findById(freePostId).get();

        commentRepository.save(new Comment(noticePost, member, "test1"));
        commentRepository.save(new Comment(freePost, admin, "test2"));
        commentRepository.save(new Comment(noticePost, member, "test3"));

    }

}
