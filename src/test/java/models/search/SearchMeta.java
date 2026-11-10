package models.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchMeta {
    private int total;
    private String query;
    private String locale;
    private List<String> types;
    private List<String> searchedIndices;
    private List<String> availableTypes;
    private SearchPagination pagination;
}
