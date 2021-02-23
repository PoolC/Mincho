package org.poolc.api.board.vo;

import lombok.Getter;
import org.poolc.api.board.dto.RegisterBoardRequest;

@Getter
public class BoardCreateValues {
    private final String name;
    private final String URLPath;
    private final String readPermission;
    private final String writePermission;

    public BoardCreateValues(RegisterBoardRequest registerBoardRequest) {
        this.name = registerBoardRequest.getName();
        this.URLPath = registerBoardRequest.getUrlPath();
        this.readPermission = registerBoardRequest.getReadPermission();
        this.writePermission = registerBoardRequest.getWritePermission();
    }

}
