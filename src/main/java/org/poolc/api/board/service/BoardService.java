package org.poolc.api.board.service;

import org.poolc.api.board.domain.Board;
import org.poolc.api.board.exception.DuplicateBoardException;
import org.poolc.api.board.repository.BoardRepository;
import org.poolc.api.board.vo.BoardCreateValues;
import org.poolc.api.board.vo.BoardUpdateValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void create(BoardCreateValues boardCreateValues) {
        checkDuplicateNameOrUrlPath(boardCreateValues.getName(), boardCreateValues.getURLPath());

        boardRepository.save(new Board(boardCreateValues));
    }


    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    public Board get(String boardName) {
        return boardRepository.findByName(boardName)
                .orElseThrow(() -> new NoSuchElementException("no board found with given name"));
    }

    public void update(String boardName, BoardUpdateValue boardUpdateValue) {
        Board findBoard = get(boardName);

        checkDuplicateNameOrUrlPath(boardUpdateValue.getName(), boardUpdateValue.getUrlPath());

        findBoard.update(boardUpdateValue);
        boardRepository.flush();
    }

    public void deleteBoard(String boardName) {
        boardRepository.delete(get(boardName));
    }

    private void checkDuplicateNameOrUrlPath(String name, String URLPath) {
        boolean hasDuplicate = boardRepository.existsByNameOrUrlPath(name, URLPath);

        if (hasDuplicate) {
            throw new DuplicateBoardException("중복된 게시판 이름입니다.");
        }
    }
}
