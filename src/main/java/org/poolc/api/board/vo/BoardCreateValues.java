package org.poolc.api.board.vo;

import lombok.Getter;
import org.poolc.api.board.dto.RegisterBoardRequest;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class BoardCreateValues {
    private final String name;
    private final String URLPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    public BoardCreateValues(RegisterBoardRequest registerBoardRequest) {
        this.name = registerBoardRequest.getName();
        this.URLPath = registerBoardRequest.getUrlPath();
        this.readPermission = registerBoardRequest.getReadPermission();
        this.writePermission = registerBoardRequest.getWritePermission();
    }

}
