package org.poolc.api.search.domain;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.poolc.api.post.vo.PostSearchValues;

public class PostSearcher extends Searcher {
    @Override
    public Object search(Object request) {
        PostSearchValues values = (PostSearchValues) request;

        return new SearchRequest("posts").source(
                new SearchSourceBuilder().query(QueryBuilders.functionScoreQuery(QueryBuilders.boolQuery()
                                .filter(QueryBuilders.termsQuery("board", values.getBoards()))
                                .should(QueryBuilders.matchQuery("title", values.getTitle()).boost(3.0f))
                                .should(QueryBuilders.matchQuery("body", values.getBody()))
                                .should(QueryBuilders.termQuery("writerName", values.getAuthor()).boost(4.0f)),
                        ScoreFunctionBuilders.gaussDecayFunction("updatedAt", new DateTime(), "365d",
                                "30d", 0.8))));
    }
}
