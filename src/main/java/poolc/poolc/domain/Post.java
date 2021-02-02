package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Post")
@Getter
public class Post {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "boardID",referencedColumnName = "ID", nullable = false)
    private Board board;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorUUID",referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "title", columnDefinition = "char(255)", nullable = false)
    private String title;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Post() {
    }

    public Post(Board board, Member member, String title, String body, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.board = board;
        this.member = member;
        this.title = title;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", board=" + board +
                ", member=" + member +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
