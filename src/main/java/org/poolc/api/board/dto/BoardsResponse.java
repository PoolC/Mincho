package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardsResponse {
    private final List<BoardResponse> data;

    @JsonCreator
    public BoardsResponse(List<BoardResponse> data) {
        this.data = data;
    }
}
