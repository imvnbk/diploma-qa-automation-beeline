package models.tariffs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import models.common.CostBlock;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Tariff {
    private int id;
    private String documentId;
    private String title;
    private String slug;
    private String description;
    private String connectUniqueUrl;
    private String detailsUniqueUrl;
    private List<TariffItem> items;
    private CostBlock costBlock;
}
