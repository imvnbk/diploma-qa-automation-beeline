package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Фильтр Strapi вида {"$eq": "значение"}. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EqFilter {

    @JsonProperty("$eq")
    private String eq;

    public static EqFilter eq(String value) {
        return new EqFilter(value);
    }
}
