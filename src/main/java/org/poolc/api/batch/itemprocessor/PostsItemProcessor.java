package org.poolc.api.batch.itemprocessor;

import org.poolc.api.batch.vo.post.PostDao;
import org.poolc.api.batch.vo.post.Posts;
import org.springframework.batch.item.ItemProcessor;

public class PostsItemProcessor implements ItemProcessor<Posts, PostDao> {
    @Override
    public PostDao process(final Posts posts) throws Exception {
        PostDao post = PostDao.of(posts);
        return post;
    }
}
