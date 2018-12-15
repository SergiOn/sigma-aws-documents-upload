package hrytsenko.documentsupload;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import hrytsenko.documentsupload.model.Document;
import hrytsenko.documentsupload.model.Ticket;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
@Component
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(UploadDocumentService.Properties.class)
class UploadDocumentService {

    private static final DateTimeFormatter EXPIRATION_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withZone(ZoneId.systemDefault());

    Clock clock;
    Properties properties;
    AmazonS3 storageClient;

    Ticket createTicket(Document document) {
        log.info("Generate URL for '{}'", document);

        String bucket = properties.getBucket();
        String key = document.getOwner() + "/" + document.getId();
        log.info("Use key '{}' and bucket '{}'", key, bucket);

        Instant expiration = clock.instant().plusSeconds(properties.getUploadTimeout());
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.PUT)
                .withExpiration(Date.from(expiration));
        String url = storageClient.generatePresignedUrl(request).toString();

        Ticket ticket = Ticket.builder()
                .url(url)
                .expiration(EXPIRATION_FORMATTER.format(expiration))
                .build();
        log.info("Generated ticket '{}'", ticket);

        return ticket;
    }

    @Getter
    @Setter
    @ConfigurationProperties("documents")
    static class Properties {
        String bucket;
        int uploadTimeout;
    }

}
