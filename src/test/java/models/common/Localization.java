package models.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/** Перевод услуги на другой язык (в ответе лежит в поле localizations). */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Localization {
    private int id;
    private String title;
    private String locale;
    private String slug;
}
