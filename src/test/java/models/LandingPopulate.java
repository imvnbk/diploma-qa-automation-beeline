package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandingPopulate {
    /** true или вложенное описание populate (Strapi: {"on": {...}}). */
    private Object components;
    private boolean breadcrumbs;
    private boolean seo;
}
