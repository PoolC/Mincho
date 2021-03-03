package org.poolc.api.comment.vo;

import lombok.Getter;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;

@Getter
public class CommentCreateValues {
    private final Post post;
    private final Member member;
    private final String body;

    public CommentCreateValues(Post post, Member member, String body) {
        this.post = post;
        this.member = member;
        this.body = body;
    }

}
