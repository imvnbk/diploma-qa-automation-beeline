package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TariffGroup {
    private int id;
    private String title;
    private Boolean collapsable;
    private List<Tariff> tariffs;
}
