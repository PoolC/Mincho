package org.poolc.api.search.vo;

import lombok.Getter;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostSearchResult {
    private final List<Long> postIds;

    public PostSearchResult(List<Long> postIds) {
        this.postIds = postIds;
    }

    public static PostSearchResult of(SearchResponse response) {
        return new PostSearchResult(
                Arrays.stream(response.getHits().getHits())
                        .map(SearchHit::getId)
                        .map(Long::parseLong)
                        .collect(Collectors.toList()));
    }
}
