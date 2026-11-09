package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Тело запроса POST /api/beeline-landings/query (получение контента страницы по фильтрам).
 * Запрос только читает данные.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LandingQueryRequest {
    private LandingFilters filters;
    private RequestPagination pagination;
    private LandingPopulate populate;
    private String locale;

    /** Запрос страницы лендинга так же, как его отправляет сайт. */
    public static LandingQueryRequest forPage(String group, String subGroup, String slug, String locale) {
        return LandingQueryRequest.builder()
                .filters(LandingFilters.builder()
                        .slug(EqFilter.eq(slug))
                        .group(EqFilter.eq(group))
                        .subGroup(EqFilter.eq(subGroup))
                        .build())
                .pagination(RequestPagination.builder().pageSize(1).build())
                .populate(LandingPopulate.builder().components(true).breadcrumbs(true).seo(true).build())
                .locale(locale)
                .build();
    }

    /** Страница услуг: компонент с группами услуг подгружается вместе с услугами (вложенный populate). */
    public static LandingQueryRequest forServicesPage(String slug, String locale) {
        Object deep = java.util.Map.of("on", java.util.Map.of(
                "collection.service-group-landing", java.util.Map.of("populate", java.util.Map.of(
                        "service_groups", java.util.Map.of("populate", java.util.Map.of(
                                "services", java.util.Map.of("populate", java.util.Map.of(
                                        "costBlock", true,
                                        "accordion", true,
                                        "localizations", true))))))));
        LandingQueryRequest request = forPage("products", "services", slug, locale);
        request.getPopulate().setComponents(deep);
        return request;
    }
}
