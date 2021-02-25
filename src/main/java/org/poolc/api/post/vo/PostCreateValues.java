package org.poolc.api.post.vo;

import lombok.Getter;
import org.poolc.api.board.domain.Board;
import org.poolc.api.member.domain.Member;
import org.poolc.api.post.dto.RegisterPostRequest;

@Getter
public class PostCreateValues {
    private final Member member;
    private final Board board;
    private final String title;
    private final String body;

    public PostCreateValues(Member member, Board board, RegisterPostRequest registerPostRequest) {
        this.member = member;
        this.board = board;
        this.title = registerPostRequest.getTitle();
        this.body = registerPostRequest.getBody();
    }
}
