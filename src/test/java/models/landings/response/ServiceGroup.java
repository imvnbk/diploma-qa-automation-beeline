package models.landings.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceGroup {
    private int id;
    private String documentId;
    private String title;
    private String locale;
    private Boolean showInGrid;
    private List<ServiceItem> services;
}
