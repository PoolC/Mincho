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


}
