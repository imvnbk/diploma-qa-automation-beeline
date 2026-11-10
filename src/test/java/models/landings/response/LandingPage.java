package models.landings.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingPage {
    private int id;
    private String documentId;
    private String slug;
    private String locale;
    private String group;
    private String subGroup;
    private List<LandingComponent> components;
}
