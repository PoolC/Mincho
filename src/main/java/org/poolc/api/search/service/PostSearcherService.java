package org.poolc.api.search.service;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.joda.time.DateTime;
import org.poolc.api.post.domain.Post;
import org.poolc.api.post.dto.PostSearchRequest;
import org.poolc.api.post.vo.PostSearchValues;
import org.poolc.api.search.domain.PostSearcher;
import org.poolc.api.search.domain.Searcher;
import org.poolc.api.search.infra.ElasticsearchEngine;
import org.poolc.api.search.vo.PostSearchResult;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostSearcherService {
    private final ElasticsearchEngine elasticsearchEngine;

    public PostSearcherService(ElasticsearchEngine elasticsearchEngine) throws IOException {
        this.elasticsearchEngine = elasticsearchEngine;
        this.createIndices();
    }

    public PostSearchResult search(PostSearchValues query) {
        Searcher postSearcher = new PostSearcher();
        SearchRequest searchQuery = (SearchRequest) postSearcher.search(query);

        SearchResponse response = elasticsearchEngine.searchRequest(searchQuery);
        return PostSearchResult.of(response);
    }

    private void createIndices() throws IOException {
        try {
            this.elasticsearchEngine.createIndex("posts", getPostsSettings(), getPostsMappings());
        } catch(ElasticsearchStatusException e) {
            if(!e.getMessage().contains("resource_already_exists_exception")) {
                throw new ElasticsearchException("posts index couldn't be created");
            }
        }
    }

    private XContentBuilder getPostsSettings() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                    .field("number_of_shards", 1)
                    .field("number_of_replicas", 0)
                    .startObject("analysis")
                        .startObject("char_filter")
                            .startObject("base64_image_filter")
                                .field("type", "pattern_replace")
                                .field("pattern", "!\\[(.*)?\\]\\(data:image\\/jpeg;base64,.*\\)")
                                .field("replacement", "")
                            .endObject()
                        .endObject()
                        .startObject("tokenizer")
                            .startObject("ko_tokenizer")
                                .field("type", "nori_tokenizer")
                                .field("decompound_mode", "mixed")
                                .field("user_dictionary", "analysis/user_dict.txt")
                            .endObject()
                        .endObject()
                        .startObject("filter")
                            .startObject("ko_postfilter")
                                .field("type", "nori_part_of_speech")
                                .startArray("stoptags")
                                    .value("E").value("IC").value("J").value("MAG").value("MM").value("NA").value("NR")
                                    .value("SC").value("SE").value("SF").value( "SH").value("SP")
                                    .value("SSC").value("SSO").value("SY").value( "UNA").value("UNKNOWN")
                                    .value("VA").value("VCN").value("VCP").value("VSV").value("VV").value("VX")
                                    .value("XPN").value("XR").value("XSA").value("XSN").value("XSV")
                                .endArray()
                            .endObject()
                            .startObject("synonyms")
                                .field("type", "synonym")
                                .field("synonyms_path", "analysis/synonyms.txt")
                            .endObject()
                            .startObject("stops")
                                .field("type", "stop")
                                .field("stopwords_path", "analysis/stopwords.txt")
                            .endObject()
                        .endObject()
                        .startObject("analyzer")
                            .startObject("ko_analyzer")
                                .field("type", "custom")
                                .startArray("char_filter")
                                    .value("html_strip")
                                    .value("base64_image_filter")
                                .endArray()
                                .field("tokenizer", "ko_tokenizer")
                                .startArray("filter")
                                    .value("ko_postfilter")
                                    .value("lowercase")
                                    .value("synonyms")
                                    .value("stops")
                                    .value("word_delimiter")
                                .endArray()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
    }

    private XContentBuilder getPostsMappings() throws IOException {
        return XContentFactory.jsonBuilder()
                .startObject()
                    .field("dynamic", "strict")
                    .startObject("properties")
                        .startObject("board")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("writerName")
                            .field("type", "keyword")
                        .endObject()
                        .startObject("title")
                            .field("type", "text")
                            .field("analyzer", "ko_analyzer")
                        .endObject()
                        .startObject("body")
                            .field("type", "text")
                            .field("analyzer", "ko_analyzer")
                        .endObject()
                        .startObject("updatedAt")
                            .field("type", "date")
                        .endObject()
                    .endObject()
                .endObject();
    }
}
