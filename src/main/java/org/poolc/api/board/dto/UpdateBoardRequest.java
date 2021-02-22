package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class UpdateBoardRequest {
    private final String name;
    private final String urlPath;
    private final String readPermission;
    private final String writePermission;

    @JsonCreator
    public UpdateBoardRequest(String name, String URLPath, String readPermission, String writePermission) {
        this.name = name;
        this.urlPath = URLPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
