package org.poolc.api.domain;

import lombok.Getter;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Post")
@Getter
public class Post extends TimestampEntity {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "boardID", referencedColumnName = "ID", nullable = false)
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorUUID", referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "title", columnDefinition = "char(255)", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> commentList = new ArrayList<>();

    public Post() { // TODO: JPA를 위한 기본 생성자는 의도치않은 생성을 피하기 위해 protected으로 선언해주세요
    }

    public Post(Long id, Board board, Member member, String title, String body, List<Comment> commentList) {
        this.id = id;
        this.board = board;
        this.member = member;
        this.title = title;
        this.body = body;
        this.commentList = commentList;
    }
}
