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

    public Board findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new NoSuchElementException("no board found with given name"));
    }

    public Board findBoardByUrlPath(String urlPath) {
        return boardRepository.findByUrlPath(urlPath)
                .orElseThrow(() -> new NoSuchElementException("no board found with given urlPath"));
    }

    public void update(Long boardId, BoardUpdateValue boardUpdateValue) {
        Board findBoard = findBoardById(boardId);
        checkDuplicateNameAndUrlPath(findBoard, boardUpdateValue);

        findBoard.update(boardUpdateValue);
        boardRepository.flush();
    }

    public void delete(Long boardId) {
        boardRepository.delete(findBoardById(boardId));
    }

    private void checkDuplicateNameOrUrlPath(String name, String urlPath) {
        boolean hasDuplicate = boardRepository.existsByNameOrUrlPath(name, urlPath);

        if (hasDuplicate) {
            throw new DuplicateBoardException("중복된 이름입니다.");
        }
    }

    private void checkDuplicateNameAndUrlPath(Board board, BoardUpdateValue boardUpdateValue) {
        String updateName = boardUpdateValue.getName();
        if (!board.isSameName(updateName)) {
            checkDuplicateName(updateName);
        }

        String updateUrlPath = boardUpdateValue.getUrlPath();
        if (!board.isSameUrlPath(updateUrlPath)) {
            checkDuplicateUrlPath(updateUrlPath);
        }
    }

    private void checkDuplicateName(String name) {
        boolean hasDuplicate = boardRepository.existsByName(name);

        if (hasDuplicate) {
            throw new DuplicateBoardException("중복된 이름입니다.");
        }
    }

    private void checkDuplicateUrlPath(String urlPath) {
        boolean hasDuplicate = boardRepository.existsByUrlPath(urlPath);

        if (hasDuplicate) {
            throw new DuplicateBoardException("중복된 urlPath입니다.");
        }
    }
}
