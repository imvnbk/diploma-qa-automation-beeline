package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceItem {
    private int id;
    private String documentId;
    private String title;
    private String slug;
    private String locale;
    private String description;
    private String fixedStyle;
    private List<TariffItem> items;
    private CostBlock costBlock;
    private List<Localization> localizations;
}
