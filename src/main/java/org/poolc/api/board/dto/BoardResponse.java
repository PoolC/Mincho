package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardResponse {
    private final Long ID;
    private final String name;
    private final String urlPath;
    private final Long postCount;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    @JsonCreator
    public BoardResponse(Board board) {
        this.ID = board.getId();
        this.name = board.getName();
        this.urlPath = board.getUrlPath();
        this.postCount = board.getPostCount();
        this.readPermission = board.getReadPermission();
        this.writePermission = board.getWritePermission();
    }
}
