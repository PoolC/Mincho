package org.poolc.api.board.domain;

import lombok.Getter;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.MemberRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Set;

@Entity(name = "Board")
@Getter
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
    private MemberRole readPermission;

    @Column(name = "writePermission", columnDefinition = "varchar(10)", nullable = false)
    private MemberRole writePermission;

    public Board() {
    }

    public Board(String name, String urlPath, MemberRole readPermission, MemberRole writePermission) {
        this.name = name;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }

    public Board(BoardCreateValues boardCreateValues) {
        this.name = boardCreateValues.getName();
        this.urlPath = boardCreateValues.getURLPath();
        this.readPermission = boardCreateValues.getReadPermission();
        this.writePermission = boardCreateValues.getWritePermission();
    }

    public void update(BoardUpdateValue boardUpdateValue) {
        this.name = boardUpdateValue.getName();
        this.urlPath = boardUpdateValue.getUrlPath();
        this.readPermission = boardUpdateValue.getReadPermission();
        this.writePermission = boardUpdateValue.getWritePermission();
    }

    public boolean isSameName(String name) {
        return this.name.equals(name);
    }

    public boolean isSameUrlPath(String urlPath) {
        return this.urlPath.equals(urlPath);
    }

    public boolean isPublicReadPermission() {
        return this.readPermission.equals(MemberRole.PUBLIC);
    }

    public boolean memberHasWritePermissions(Set<MemberRole> roles) {
        if (onlyAdminAllowed(writePermission, roles)) {
            return false;
        }

        return !onlyMemberAllowed(writePermission, roles);
    }

    public boolean memberHasReadPermissions(Set<MemberRole> roles) {
        if (onlyAdminAllowed(readPermission, roles)) {
            return false;
        }

        return !onlyMemberAllowed(readPermission, roles);
    }

    private boolean onlyAdminAllowed(MemberRole permission, Set<MemberRole> roles) {
        return permission.equals(MemberRole.ADMIN) && !roles.contains(MemberRole.ADMIN);
    }

    private boolean onlyMemberAllowed(MemberRole permission, Set<MemberRole> roles) {
        return permission.equals(MemberRole.MEMBER) && !roles.contains(MemberRole.MEMBER);
    }
}
