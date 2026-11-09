package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpSearch {
    private int id;
    private String placeholder;
    private String variantsTitle;
    private List<SearchVariant> variants;
}
