package org.poolc.api.batch.vo.board;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Boards {
    private Long id;
    private String name;
    private String urlPath;
    private String readPermission;
    private String writePermission;
    private String createdAt;
    private String updatedAt;

    public Boards() {
    }

    public Boards(Long id, String name, String urlPath, String readPermission, String writePermission, String createdAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.urlPath = urlPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
