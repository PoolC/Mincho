package org.poolc.api.board.controller;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.board.domain.Board;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.dto.BoardsResponse;
import org.poolc.api.board.dto.RegisterBoardRequest;
import org.poolc.api.board.dto.UpdateBoardRequest;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.poolc.api.member.domain.Member;
import org.poolc.api.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;

    private final String PUBLIC = "PUBLIC";
    private final String MEMBER = "MEMBER";
    private final String ADMIN = "ADMIN";

    @PostMapping
    public ResponseEntity<Void> createBoard(HttpServletRequest request,
                                            @RequestBody RegisterBoardRequest registerBoardRequest) {
        checkAdmin(request);

        System.out.println("registerBoardRequest.getURLPath() = " + registerBoardRequest.getUrlPath());
        boardService.create(new BoardCreateValues(registerBoardRequest));

        return ResponseEntity.accepted().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardsResponse> getAllBoard(HttpServletRequest request) {
        if (request.getAttribute("UUID") == null) {
            List<Board> filteredBoards = boardService.getAllBoards().stream()
                    .filter(board -> board.getReadPermission().equals(PUBLIC))
                    .collect(Collectors.toList());

            List<BoardResponse> transformBoardResponse = filteredBoards.stream()
                    .map(board -> new BoardResponse(board))
                    .collect(Collectors.toList());
            BoardsResponse boardsResponse = new BoardsResponse(transformBoardResponse);
            return ResponseEntity.ok().body(boardsResponse);
        }

        String uuid = request.getAttribute("UUID").toString();
        Member user = memberService.findMember(uuid);
        if (user.getIsAdmin().equals(false)) {
            List<Board> filteredBoards = boardService.getAllBoards().stream()
                    .filter(board -> !board.getReadPermission().equals(ADMIN))
                    .collect(Collectors.toList());

            List<BoardResponse> transformBoardResponse = filteredBoards.stream()
                    .map(board -> new BoardResponse(board))
                    .collect(Collectors.toList());
            BoardsResponse boardsResponse = new BoardsResponse(transformBoardResponse);
            return ResponseEntity.ok().body(boardsResponse);
        }

        List<BoardResponse> boards = boardService.getAllBoards().stream()
                .map(board -> new BoardResponse(board))
                .collect(Collectors.toList());
        ;

        BoardsResponse boardsResponse = new BoardsResponse(boards);

        return ResponseEntity.ok().body(boardsResponse);
    }


    private List<Board> getPublicBoard() {
        return boardService.getAllBoards().stream()
                .filter(board -> board.getReadPermission().equals(PUBLIC))
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/{boardId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {

        BoardResponse boardResponse = new BoardResponse(boardService.get(boardId));

        return ResponseEntity.ok().body(boardResponse);
    }

    @PutMapping(value = "/{boardId}")
    public ResponseEntity<Void> updateBoard(@PathVariable Long boardId,
                                            @RequestBody UpdateBoardRequest updateBoardRequest,
                                            HttpServletRequest httpServletRequest) {
        checkAdmin(httpServletRequest);

        BoardUpdateValue boardUpdateValue = new BoardUpdateValue(updateBoardRequest);
        boardService.update(boardId, boardUpdateValue);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId,
                                            HttpServletRequest httpServletRequest) {
        checkAdmin(httpServletRequest);

        boardService.delete(boardId);

        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<String> unauthenticatedHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    public ResponseEntity<String> noSuchElementHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private void checkAdmin(HttpServletRequest request) {
        if (request.getAttribute("isAdmin").equals("false")) {
            throw new UnauthenticatedException("임원진이 아닙니다");
        }
    }
}
