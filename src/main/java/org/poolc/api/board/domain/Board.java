package org.poolc.api.board.domain;

import lombok.Getter;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.poolc.api.common.domain.TimestampEntity;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;

import javax.persistence.*;

@Entity(name = "Board")
@SequenceGenerator(
        name = "BOARD_SEQ_GENERATOR",
        sequenceName = "BOARD_SEQ"
)
@Getter
public class Board extends TimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GENERATOR")
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String name;

    @Column(name = "url_path", unique = true, nullable = false, columnDefinition = "varchar(40)")
    private String urlPath;

    @Column(name = "post_count")
    private Long postCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_permission", nullable = false, columnDefinition = "varchar(10)")
    private MemberRole readPermission;

    @Enumerated(EnumType.STRING)
    @Column(name = "write_permission", nullable = false, columnDefinition = "varchar(10)")
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
        this.postCount = 0L;
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

    public boolean memberHasWritePermissions(Member user) {
        if (onlyAdminAllowed(writePermission, user.getRoles())) {
            return false;
        }

        return !onlyMemberAllowed(writePermission, user.getRoles());
    }

    public boolean memberHasReadPermissions(MemberRoles roles) {
        if (onlyAdminAllowed(readPermission, roles)) {
            return false;
        }

        return !onlyMemberAllowed(readPermission, roles);
    }

    public void addPostCount() {
        this.postCount++;
    }

    public void deletePostCount() {
        this.postCount--;
    }

    private boolean onlyAdminAllowed(MemberRole permission, MemberRoles roles) {
        return permission.equals(MemberRole.ADMIN) && !roles.isAdmin();
    }

    private boolean onlyMemberAllowed(MemberRole permission, MemberRoles roles) {
        return permission.equals(MemberRole.MEMBER) && !roles.isMember();
    }
}
