package org.poolc.api.board.domain;

import lombok.Getter;
import lombok.Setter;
import org.poolc.api.common.domain.TimestampEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = "Board")
@Getter
@Setter
public class Board extends TimestampEntity {

    @Id
    @GeneratedValue
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

    public Board() {
    }

    public Board(String name, String urlPath, String readPermission, String writePermission) {
        this.name = name;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
