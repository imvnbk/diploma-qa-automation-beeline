package models.uitexts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UiTextItem {
    private int id;
    private String documentId;
    private String code;
    private String text;
    private String createdAt;
    private String updatedAt;
    private String publishedAt;
    private String locale;
}
