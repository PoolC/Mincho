package org.poolc.api.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Comment")
@Getter
public class Comment extends TimestampEntity {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postID", referencedColumnName = "ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorUUID", referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;
}
