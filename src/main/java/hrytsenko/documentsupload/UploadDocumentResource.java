package hrytsenko.documentsupload;

import hrytsenko.documentsupload.model.Document;
import hrytsenko.documentsupload.model.Ticket;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UploadDocumentResource {

    UploadDocumentService uploadDocumentService;

    @PostMapping("api/_upload")
    public Ticket upload(
            @RequestBody Document document) {
        return uploadDocumentService.createTicket(document);
    }

}
