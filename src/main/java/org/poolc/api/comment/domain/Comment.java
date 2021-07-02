package org.poolc.api.comment.domain;

import lombok.Builder;
import lombok.Getter;
import org.poolc.api.comment.vo.CommentUpdateValues;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.domain.Post;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Comment")
@SequenceGenerator(
        name = "COMMENT_SEQ_GENERATOR",
        sequenceName = "COMMENT_SEQ"
)
@Builder
@Getter
public class Comment extends TimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "ID")
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    public Comment(Long id, Post post, Member member, String body) {
        this.id = id;
        this.post = post;
        this.member = member;
        this.body = body;
    }

    public Comment() {
    }

    public Comment(Post post, Member member, String body) {
        this.post = post;
        this.member = member;
        this.body = body;
    }

    public void updateComment(CommentUpdateValues values) {
        this.body = values.getBody();
    }
}
