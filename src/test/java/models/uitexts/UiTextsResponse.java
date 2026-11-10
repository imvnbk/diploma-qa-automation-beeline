package models.uitexts;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.common.Meta;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UiTextsResponse {
    private List<UiTextItem> data;
    private Meta meta;
}
