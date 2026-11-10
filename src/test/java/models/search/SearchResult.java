package models.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;
import java.util.Map;

/** Один результат глобального поиска. Состав поля source зависит от type (услуга, тариф, новость и т.д.). */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {
    private String id;
    private String type;
    private double score;
    private Map<String, Object> source;
    private Map<String, List<String>> highlight;
}
