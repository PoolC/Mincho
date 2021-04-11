package org.poolc.api.batch.itemprocessor;

import org.poolc.api.batch.vo.board.BoardDao;
import org.poolc.api.batch.vo.board.Boards;
import org.springframework.batch.item.ItemProcessor;

public class BoardsItemProcessor implements ItemProcessor<Boards, BoardDao> {
    @Override
    public BoardDao process(final Boards boards) throws Exception {
        BoardDao board = BoardDao.of(boards);
        return board;
    }
}
