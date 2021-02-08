package poolc.poolc.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(name = "Board")
@Getter @Setter
public class Board {

    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar(40)", nullable = false, unique = true)
    private String name;

    @Column(name = "URLPath", columnDefinition = "varchar(40)", nullable = false, unique = true)
    private String urlPath;

    @Column(name = "readPermission", columnDefinition = "varchar(10)", nullable = false)
    private String readPermission;

    @Column(name = "writePermission", columnDefinition = "varchar(10)", nullable = false)
    private String writePermission;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    List<Post> posts = new ArrayList<>();

    public Board() {
    }

    public Board(String name, String urlPath, String readPermission, String writePermission, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.name = name;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public void updateBoard(String name, String URLPath, String readPermission, String writePermission){
        this.name = name;
        this.urlPath = URLPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return Objects.equals(getId(), board.getId()) &&
                Objects.equals(getName(), board.getName()) &&
                Objects.equals(getUrlPath(), board.getUrlPath()) &&
                Objects.equals(getReadPermission(), board.getReadPermission()) &&
                Objects.equals(getWritePermission(), board.getWritePermission()) &&
                Objects.equals(getCreatedAt(), board.getCreatedAt()) &&
                Objects.equals(getUpdatedAt(), board.getUpdatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getUrlPath(), getReadPermission(), getWritePermission(), getCreatedAt(), getUpdatedAt());
    }
}
