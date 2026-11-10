package models.help;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpCategory {
    private int id;
    private String title;
    private String slug;
    private List<HelpSubCategory> subCategory;
}
