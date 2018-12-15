package hrytsenko.documentsupload;

import hrytsenko.documentsupload.model.Document;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(IndexMetadataService.Properties.class)
class IndexMetadataService {

    Properties properties;
    RestHighLevelClient indexClient;

    @SneakyThrows
    void index(Document document) {
        IndexRequest request = new IndexRequest(properties.getIndex())
                .type("_doc")
                .create(true)
                .id(document.getOwner() + "/" + document.getId())
                .source("owner", document.getOwner(), "id", document.getId(), "title", document.getTitle());

        IndexResponse response = indexClient.index(request);
        if (response.status() != RestStatus.CREATED) {
            throw new IOException("Cannot index document");
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties("documents")
    static class Properties {
        String index;
    }

}
