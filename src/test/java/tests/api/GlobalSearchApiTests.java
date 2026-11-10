package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.search.GlobalSearchResponse;
import models.search.SearchResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static api.GlobalSearchApi.rawSearch;
import static api.GlobalSearchApi.search;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("beeline.kz API")
@Feature("GET /api/global-search/search")
@Owner("Bexultan Imanbek")
@DisplayName("API: Глобальный поиск (/api/global-search/search)")
public class GlobalSearchApiTests {

    private static final String QUERY = "телефон";

    @Test
    @DisplayName("Поиск по слову «телефон» возвращает результаты и метаданные запроса")
    @Severity(SeverityLevel.CRITICAL)
    void searchShouldReturnResults() {
        GlobalSearchResponse response = search(QUERY, 10, "ru");

        step("data не пуст, meta.query и meta.locale совпадают с запросом", () -> {
            assertThat(response.getData()).isNotEmpty();
            assertThat(response.getMeta().getQuery()).isEqualTo(QUERY);
            assertThat(response.getMeta().getLocale()).isEqualTo("ru");
        });
    }

    @ParameterizedTest(name = "limit={0}")
    @DisplayName("Параметр limit ограничивает число результатов")
    @Severity(SeverityLevel.NORMAL)
    @ValueSource(ints = {1, 5, 10})
    void limitShouldRestrictResultsCount(int limit) {
        GlobalSearchResponse response = search(QUERY, limit, "ru");

        step("Результатов не больше limit, meta.pagination.limit = " + limit, () -> {
            assertThat(response.getData()).hasSizeLessThanOrEqualTo(limit);
            assertThat(response.getMeta().getPagination().getLimit()).isEqualTo(limit);
        });
    }

    @Test
    @DisplayName("У каждого результата есть id, type и source той же локали")
    @Severity(SeverityLevel.NORMAL)
    void everyResultShouldHaveBasicFields() {
        List<SearchResult> results = search(QUERY, 10, "ru").getData();

        step("id, type заполнены; source.id = id; source.locale = ru", () -> {
            for (SearchResult result : results) {
                assertThat(result.getId()).as("id результата").isNotBlank();
                assertThat(result.getType()).as("type результата " + result.getId()).isNotBlank();
                assertThat(result.getSource()).as("source результата " + result.getId()).isNotNull();
                assertThat(result.getSource().get("id")).isEqualTo(result.getId());
                assertThat(result.getSource().get("locale")).isEqualTo("ru");
            }
        });
    }

    @Test
    @DisplayName("Результаты отсортированы по убыванию релевантности (score)")
    @Severity(SeverityLevel.NORMAL)
    void resultsShouldBeSortedByScoreDescending() {
        List<SearchResult> results = search(QUERY, 10, "ru").getData();

        step("score каждого следующего результата не больше предыдущего", () -> {
            for (int i = 1; i < results.size(); i++) {
                assertThat(results.get(i).getScore())
                        .as("score результата №" + (i + 1))
                        .isLessThanOrEqualTo(results.get(i - 1).getScore());
            }
        });
    }

    @Test
    @DisplayName("У каждого результата есть подсветка совпадений (highlight с тегом mark)")
    @Severity(SeverityLevel.MINOR)
    void everyResultShouldHaveHighlight() {
        List<SearchResult> results = search(QUERY, 10, "ru").getData();

        step("highlight не пуст и содержит <mark>", () -> {
            for (SearchResult result : results) {
                assertThat(result.getHighlight()).as("highlight результата " + result.getId()).isNotEmpty();
                assertThat(result.getHighlight().values().stream().flatMap(List::stream))
                        .anyMatch(fragment -> fragment.contains("<mark>"));
            }
        });
    }

    @Test
    @DisplayName("meta: total не меньше числа результатов, типы результатов входят в availableTypes")
    @Severity(SeverityLevel.NORMAL)
    void metaShouldBeConsistentWithResults() {
        GlobalSearchResponse response = search(QUERY, 10, "ru");

        step("total >= data.size(), список availableTypes непуст", () -> {
            assertThat(response.getMeta().getTotal()).isGreaterThanOrEqualTo(response.getData().size());
            assertThat(response.getMeta().getAvailableTypes()).isNotEmpty();
        });
        step("Каждый type результата вида api::<тип>.<тип> есть в availableTypes", () -> {
            for (SearchResult result : response.getData()) {
                String type = result.getType();
                if (type.startsWith("api::")) {
                    String name = type.substring("api::".length(), type.indexOf('.'));
                    assertThat(response.getMeta().getAvailableTypes()).contains(name);
                }
            }
        });
    }

    @ParameterizedTest(name = "q=\"{0}\"")
    @DisplayName("Пустой и бессмысленный запрос не приводят к ошибке 5xx")
    @Severity(SeverityLevel.NORMAL)
    @ValueSource(strings = {"", "zzzqqqxxx999", "<script>alert(1)</script>"})
    void unusualQueryShouldNotCauseServerError(String query) {
        var response = rawSearch(query);

        step("Статус не 5xx", () -> assertThat(response.statusCode()).isLessThan(500));
    }
}
