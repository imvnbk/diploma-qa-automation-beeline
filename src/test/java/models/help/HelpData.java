package models.help;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.common.Seo;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HelpData {
    private int id;
    private String documentId;
    private String title;
    private String categoryTitle;
    private String choseSubCategoryTitle;
    private String locale;
    private List<HelpCategory> category;
    private HelpSearch search;
    private Seo seo;
}
