package org.poolc.api.comment.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.infra.PasswordHashProvider;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.comment.repository.CommentRepository;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.repository.MemberRepository;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.repository.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;

@Component
@Profile("commentTest")
@RequiredArgsConstructor
public class CommentDataLoader implements CommandLineRunner {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final PasswordHashProvider passwordHashProvider;


    private Long noticePostId = 9L;
    private Long freePostId = 10L;

    private final String COMMENTER = "COMMENT_WRITER_ID";

    @Override
    public void run(String... args) {
        memberRepository.save(
                Member.builder()
                        .UUID(UUID.randomUUID().toString())
                        .loginID(COMMENTER)
                        .passwordHash(passwordHashProvider.encodePassword("COMMENT_WRITER_ID"))
                        .email("COMMENT@email.com")
                        .phoneNumber("010-4444-4444")
                        .name("COMMENT_WRITER")
                        .department("exampleDepartment")
                        .studentID("2015147514")
                        .passwordResetToken(null)
                        .passwordResetTokenValidUntil(null)
                        .profileImageURL(null)
                        .introduction("")
                        .isExcepted(false)
                        .roles(new HashSet<>() {{
                            add(MemberRole.MEMBER);
                        }})
                        .build());
        Member member = memberRepository.findByLoginID(COMMENTER).get();
        Post noticePost = postRepository.findById(noticePostId).get();
        Post freePost = postRepository.findById(freePostId).get();

        commentRepository.save(new Comment(noticePost, member, "test1"));
        commentRepository.save(new Comment(freePost, member, "test2"));
        commentRepository.save(new Comment(noticePost, member, "test3"));
    }

}
