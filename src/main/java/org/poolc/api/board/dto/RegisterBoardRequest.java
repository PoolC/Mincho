package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

@Getter
public class RegisterBoardRequest {
    private final String name;
    private final String URLPath;
    private final String readPermission;
    private final String writePermission;

    @JsonCreator
    public RegisterBoardRequest(String name, String URLPath, String readPermission, String writePermission) {
        this.name = name;
        this.URLPath = URLPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
