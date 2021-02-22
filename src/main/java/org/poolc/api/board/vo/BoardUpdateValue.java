package org.poolc.api.board.vo;

import lombok.Getter;
import org.poolc.api.board.dto.UpdateBoardRequest;

@Getter
public class BoardUpdateValue {
    private final String name;
    private final String urlPath;
    private final String readPermission;
    private final String writePermission;

    public BoardUpdateValue(UpdateBoardRequest updateBoardRequest) {
        this.name = updateBoardRequest.getName();
        this.urlPath = updateBoardRequest.getUrlPath();
        this.readPermission = updateBoardRequest.getReadPermission();
        this.writePermission = updateBoardRequest.getWritePermission();
    }
}
