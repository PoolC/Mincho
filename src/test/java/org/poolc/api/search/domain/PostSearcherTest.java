package org.poolc.api.search.domain;

import org.elasticsearch.action.search.SearchRequest;
import org.junit.jupiter.api.Test;
import org.poolc.api.post.vo.PostSearchValues;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class PostSearcherTest {
    @Test
    void testPostSearchByQueryParam() {
        PostSearcher searcher = new PostSearcher();

        PostSearchValues values = new PostSearchValues("testTitle", "testBody", "testAuthor", Arrays.asList("notice", "free"));
        SearchRequest request = (SearchRequest) searcher.search(values);

        assertThat(request.source().toString()).contains("{\"match\":{\"title\":{\"query\":\"testTitle\"");
        assertThat(request.source().toString()).contains("{\"match\":{\"body\":{\"query\":\"testBody\"");
        assertThat(request.source().toString()).contains("{\"term\":{\"writerName\":{\"value\":\"testAuthor\",\"boost\":4.0}}");
        assertThat(request.source().toString()).contains("\"gauss\":{\"updatedAt\":{\"origin\"");
    }
}
