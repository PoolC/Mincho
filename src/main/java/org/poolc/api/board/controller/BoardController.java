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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        List<BoardResponse> boards = boardService.getAllBoards().stream()
                .filter(board -> board.memberHasReadPermissions(member.getRoles()))
                .map(BoardResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(new BoardsResponse(boards));
    }

    @GetMapping(value = "/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> getBoard(@AuthenticationPrincipal Member member, @PathVariable Long boardId) {
        Board board = boardService.findBoardById(boardId);

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

    private void checkMemberPermissions(Board board, Set<MemberRole> roles) {
        if (!board.memberHasReadPermissions(roles)) {
            throw new UnauthorizedException("접근할 수 없습니다");
        }
    }
}
