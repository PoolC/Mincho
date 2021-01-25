package poolc.poolc.domain;

import lombok.Getter;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
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

}
