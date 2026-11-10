package models.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CostBlock {
    private int id;
    private Double baseCost;
    private Double newCost;
    private String currency;
    private String textUnderCost;
    private Boolean hideConnectButton;
    private Boolean hideDetailsButton;
}
