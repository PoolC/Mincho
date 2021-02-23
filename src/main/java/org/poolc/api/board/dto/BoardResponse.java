package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.board.domain.Board;

@Getter
public class BoardResponse {
    private final Long ID;
    private final String name;
    private final String urlPath;
    private final String readPermission;
    private final String writePermission;

    @JsonCreator
    public BoardResponse(Board board) {
        this.ID = board.getId();
        this.name = board.getName();
        this.urlPath = board.getUrlPath();
        this.readPermission = board.getReadPermission();
        this.writePermission = board.getWritePermission();
    }
}
