package org.poolc.api.post.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.comment.domain.Comment;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.dto.RegisterPostRequest;
import org.poolc.api.post.dto.UpdatePostRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;

@Entity(name = "Post")
@SequenceGenerator(
        name = "POST_SEQ_GENERATOR",
        sequenceName = "POST_SEQ",
        allocationSize = 1
)
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post extends TimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "POST_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "board_id", nullable = false, referencedColumnName = "ID")
    private Board board;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_uuid", nullable = false, referencedColumnName = "UUID")
    private Member member;

    @Column(name = "title", nullable = false, columnDefinition = "char(255)")
    private String title;

    @Column(name = "body", nullable = false, columnDefinition = "text")
    private String body;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private List<Comment> commentList = new ArrayList<>();

    @ElementCollection(fetch = EAGER)
    @CollectionTable(name = "post_file_list", joinColumns = @JoinColumn(name = "post_id"), uniqueConstraints = {@UniqueConstraint(columnNames = {"post_id", "file_uri"})})
    @Column(name = "file_uri", columnDefinition = "varchar(1024)")
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

    public Post(Member writer, Board board, RegisterPostRequest request) {
        this.board = board;
        this.member = writer;
        this.title = request.getTitle();
        this.body = request.getBody();
        this.commentList = null;

        if (request.getFileList() != null) {
            checkDuplicateFileList(request.getFileList());
            this.fileList = request.getFileList();
        }
    }

    public void update(UpdatePostRequest value) {
        this.title = value.getTitle();
        this.body = value.getBody();

        if (value.getFileList() != null) {
            checkDuplicateFileList(value.getFileList());
            this.fileList = value.getFileList();
        }
    }

    private void checkDuplicateFileList(List<String> fileList) {
        boolean duplicated = fileList.stream().distinct().count() != fileList.size();
        if (duplicated) {
            throw new IllegalArgumentException("파일 URI가 중복되었습니다.");
        }
    }
}
