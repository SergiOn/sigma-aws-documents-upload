package hrytsenko.documentsupload;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class IndexClientConfiguration {

    @Bean
    RestHighLevelClient indexClient(
            @Value("${es.url}") String url) {
        RestClientBuilder clientBuilder = RestClient
                .builder(HttpHost.create(url));
        return new RestHighLevelClient(clientBuilder);
    }

}
