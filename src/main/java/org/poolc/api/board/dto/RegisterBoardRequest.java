package org.poolc.api.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import org.poolc.api.member.domain.MemberRole;

@Getter
public class RegisterBoardRequest {
    private final String name;
    private final String urlPath;
    private final MemberRole readPermission;
    private final MemberRole writePermission;

    @JsonCreator
    public RegisterBoardRequest(String name, String URLPath, MemberRole readPermission, MemberRole writePermission) {
        this.name = name;
        this.urlPath = URLPath;
        this.readPermission = readPermission;
        this.writePermission = writePermission;
    }
}
