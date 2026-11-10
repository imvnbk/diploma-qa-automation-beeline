package models.tariffs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TariffListData {
    private int id;
    private String documentId;
    private String locale;
    private String toPayLabel;
    private String connectButton;
    private String detailsButton;
    private String archiveButton;
    private List<Tab> tabs;
    private List<TariffGroup> tariffGroups;
}
