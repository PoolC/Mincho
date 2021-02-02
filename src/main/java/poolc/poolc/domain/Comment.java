package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity(name = "Comment")
@Getter
public class Comment {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "postID", referencedColumnName = "ID", nullable = false)
    private Post post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "authorUUID",referencedColumnName = "UUID", nullable = false)
    private Member member;

    @Column(name = "body", columnDefinition = "text", nullable = false)
    private String body;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Comment() {
    }

    public Comment(Post post, Member member, String body, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.post = post;
        this.member = member;
        this.body = body;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return Objects.equals(getId(), comment.getId()) &&
                Objects.equals(getPost(), comment.getPost()) &&
                Objects.equals(getMember(), comment.getMember()) &&
                Objects.equals(getBody(), comment.getBody()) &&
                Objects.equals(getCreatedAt(), comment.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), comment.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getPost(), getMember(), getBody(), getCreatedAt(), getUpdatedAt());
    }
}
