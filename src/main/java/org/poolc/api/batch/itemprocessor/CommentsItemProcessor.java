package org.poolc.api.batch.itemprocessor;

import org.poolc.api.batch.vo.comment.CommentDao;
import org.poolc.api.batch.vo.comment.Comments;
import org.springframework.batch.item.ItemProcessor;

public class CommentsItemProcessor implements ItemProcessor<Comments, CommentDao> {
    @Override
    public CommentDao process(final Comments comments) throws Exception {
        CommentDao comment = CommentDao.of(comments);
        return comment;
    }
}
