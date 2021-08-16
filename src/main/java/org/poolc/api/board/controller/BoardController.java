package org.poolc.api.board.controller;

import org.poolc.api.auth.exception.UnauthorizedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.dto.BoardsResponse;
import org.poolc.api.board.dto.RegisterBoardRequest;
import org.poolc.api.board.dto.UpdateBoardRequest;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.domain.MemberRole;
import org.poolc.api.member.domain.MemberRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(@RequestBody RegisterBoardRequest registerBoardRequest) {
        boardService.create(new BoardCreateValues(registerBoardRequest));

        return ResponseEntity.accepted().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardsResponse> getAllBoard(@AuthenticationPrincipal Member member) {
        //TODO: MEMBER DEFAULT 값이 PUBLIC으로 만들시 삭제
        List<BoardResponse> boards = new ArrayList<>();
        if (member == null) {
            boards = boardService.getAllBoards().stream()
                    .filter(board -> board.isPublicReadPermission())
                    .map(BoardResponse::new)
                    .collect(Collectors.toList());
        } else {
            boards = boardService.getAllBoards().stream()
                    .filter(board -> board.memberHasReadPermissions(member.getRoles()))
                    .map(BoardResponse::new)
                    .collect(Collectors.toList());
        }
        boards.sort(Comparator.comparing(BoardResponse::getName));

        BoardsResponse response = new BoardsResponse(boards);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> getBoard(@AuthenticationPrincipal Member member, @PathVariable Long boardId) {
        Board board = boardService.findBoardById(boardId);

        //TODO: MEMBER DEFAULT 값이 PUBLIC으로 만들시 삭제
        if (member == null) {
            checkMemberPermissions(board, new MemberRoles(Set.of(MemberRole.PUBLIC)));
            return ResponseEntity.ok().body(new BoardResponse(board));
        }

        checkMemberPermissions(board, member.getRoles());
        return ResponseEntity.ok().body(new BoardResponse(board));
    }

    @PutMapping(value = "/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long boardId,
                                            @RequestBody UpdateBoardRequest updateBoardRequest) {
        boardService.update(boardId, new BoardUpdateValue(updateBoardRequest));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.delete(boardId);

        return ResponseEntity.ok().build();
    }

    private void checkMemberPermissions(Board board, MemberRoles roles) {
        if (!board.memberHasReadPermissions(roles)) {
            throw new UnauthorizedException("접근할 수 없습니다");
        }
    }
}
