package models.tariffs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TariffItem {
    private int id;
    private String value;
    private String label;
}
