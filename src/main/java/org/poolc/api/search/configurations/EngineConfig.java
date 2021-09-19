package org.poolc.api.search.configurations;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.poolc.api.search.infra.ElasticsearchEngine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineConfig {
    @Value("${search.post.url}")
    private String searchEngineUrl;
    @Value("${search.post.port}")
    private int searchEnginePort;
    @Value("${search.post.scheme}")
    private String searchEngineScheme;

    @Bean
    public ElasticsearchEngine elasticsearchEngine() {
        return new ElasticsearchEngine(new RestHighLevelClient(RestClient.builder(new HttpHost(
                searchEngineUrl, searchEnginePort, searchEngineScheme
        ))));
    }
}
