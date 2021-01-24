package poolc.poolc.domain;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Board {
    @Id @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "Name", length = 40, nullable = false, unique = true)
    private String name;

    @Column(name = "URLPath", length = 40, nullable = false, unique = true)
    private String urlPath;

    @Column(name = "ReadPermission",length = 10, nullable = false)
    private String readPermission;

    @Column(name = "WritePermission",length = 10, nullable = false)
    private String writePermission;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;


}
