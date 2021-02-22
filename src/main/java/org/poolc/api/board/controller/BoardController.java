package org.poolc.api.board.controller;

import org.poolc.api.auth.exception.UnauthenticatedException;
import org.poolc.api.board.dto.BoardResponse;
import org.poolc.api.board.dto.BoardsResponse;
import org.poolc.api.board.dto.RegisterBoardRequest;
import org.poolc.api.board.dto.UpdateBoardRequest;
import org.poolc.api.board.service.BoardService;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.springframework.beans.factory.annotation.Autowired;
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
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<Void> createBoard(HttpServletRequest request,
                                            @RequestBody RegisterBoardRequest registerBoardRequest) {
        checkAdmin(request);

        System.out.println("registerBoardRequest.getURLPath() = " + registerBoardRequest.getUrlPath());
        boardService.create(new BoardCreateValues(registerBoardRequest));

        return ResponseEntity.accepted().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardsResponse> getAllBoard() {
        List<BoardResponse> boards = boardService.getAllBoards().stream()
                .map(board -> new BoardResponse(board))
                .collect(Collectors.toList());

        BoardsResponse boardsResponse = new BoardsResponse(boards);

        return ResponseEntity.ok().body(boardsResponse);
    }

    @GetMapping(value = "/{boardName}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BoardResponse> getBoard(@PathVariable String boardName) {
        BoardResponse boardResponse = new BoardResponse(boardService.get(boardName));

        return ResponseEntity.ok().body(boardResponse);
    }

    @PutMapping(value = "/{boardName}")
    public ResponseEntity<Void> updateBoard(@PathVariable String boardName,
                                            @RequestBody UpdateBoardRequest updateBoardRequest,
                                            HttpServletRequest httpServletRequest) {
        checkAdmin(httpServletRequest);

        BoardUpdateValue boardUpdateValue = new BoardUpdateValue(updateBoardRequest);
        boardService.update(boardName, boardUpdateValue);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{boardName}")
    public ResponseEntity<Void> deleteBoard(@PathVariable String boardName,
                                            HttpServletRequest httpServletRequest) {
        checkAdmin(httpServletRequest);

        boardService.deleteBoard(boardName);

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
