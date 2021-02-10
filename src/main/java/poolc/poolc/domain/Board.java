package poolc.poolc.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
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
