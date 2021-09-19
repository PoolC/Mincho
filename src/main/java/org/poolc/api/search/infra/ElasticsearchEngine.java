package org.poolc.api.search.infra;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class ElasticsearchEngine {
    private final RestHighLevelClient client;

    public ElasticsearchEngine(RestHighLevelClient client) {
        this.client = client;
    }

    public void createIndex(String indexName, XContentBuilder settings, XContentBuilder mappings) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(settings);
        request.mapping(mappings);
        client.indices().create(request, RequestOptions.DEFAULT);
    }

    public void indexDocument() {

    }

    public void updateDocument() {

    }

    public void deleteDocument() {

    }

    public SearchResponse searchRequest(SearchRequest searchRequest) {
        try {
            return client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException ignore) {
            throw new RuntimeException("Elasticsearch failed to send search request");
        }
    }
}
