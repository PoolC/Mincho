package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.vo.PostCreateValues;
import org.poolc.api.post.vo.PostUpdateValues;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Post")
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ"
)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post extends TimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "ID", nullable = false)
    private Board board;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "title", columnDefinition = "char(255)", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comment> commentList = new ArrayList<>();

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "post_file_list", joinColumns = @JoinColumn(name = "post_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "file_uri"})})
    @Column(name = "file_uri")
    private List<String> fileList = new ArrayList<>();

    protected Post() {
    }

    public Post(Board board, Member member, String title, String body, List<Comment> commentList, List<String> fileList) {
        this.board = board;
        this.member = member;
        this.title = title;
        this.body = body;
        this.commentList = commentList;
        this.fileList = fileList;
    }

    public Post(PostCreateValues postCreateValues) {
        checkDuplicateFileList(postCreateValues.getFileList());
        this.board = postCreateValues.getBoard();
        this.member = postCreateValues.getMember();
        this.title = postCreateValues.getTitle();
        this.body = postCreateValues.getBody();
        this.fileList = postCreateValues.getFileList();
        this.commentList = null;
    }


    public void update(PostUpdateValues postUpdateValues) {
        this.title = postUpdateValues.getTitle();
        this.body = postUpdateValues.getBody();
        this.fileList = postUpdateValues.getFileList();
    }

    private void checkDuplicateFileList(List<String> fileList) {
        boolean duplicated = fileList.stream().distinct().count() != fileList.size();
        if (duplicated) {
            throw new IllegalArgumentException("파일 URI가 중복되었습니다.");
        }
    }
}
