package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandingFilters {
    private EqFilter slug;
    private EqFilter group;
    private EqFilter subGroup;
}
