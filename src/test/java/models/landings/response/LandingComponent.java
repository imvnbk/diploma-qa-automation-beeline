package models.landings.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LandingComponent {

    @JsonProperty("__component")
    private String component;

    private int id;
    private String detailsButton;

    @JsonProperty("service_groups")
    private List<ServiceGroup> serviceGroups;
}
