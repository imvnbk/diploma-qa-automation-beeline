package tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import models.uitexts.UiTextItem;
import org.hamcrest.MatcherAssert;
import models.uitexts.UiTextsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static api.UiTextsApi.getUiTexts;
import static api.UiTextsApi.getUiTextsResponse;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("GET /api/ui-texts")
@Owner("Bexultan Imanbek")
@DisplayName("API: UI-тексты (/api/ui-texts)")
public class UiTextsApiTests extends ApiTestBase {

    @ParameterizedTest(name = "Ответ ui-texts соответствует JSON-схеме (locale={0})")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Ответ соответствует JSON-схеме")
    @Severity(SeverityLevel.CRITICAL)
    void responseShouldMatchJsonSchema(String locale) {
        Response response = getUiTextsResponse(locale);

        step("Тело ответа соответствует JSON-схеме", () ->
                MatcherAssert.assertThat(response.asString(),
                        matchesJsonSchemaInClasspath("schemas/ui-texts-schema.json")));
    }

    @ParameterizedTest(name = "Все элементы ui-texts имеют locale={0}, непустые code и text")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Тексты возвращаются для запрошенной локали")
    @Severity(SeverityLevel.CRITICAL)
    void allItemsShouldHaveRequestedLocale(String locale) {
        UiTextsResponse response = getUiTexts(locale);

        step("Список текстов не пустой", () ->
                assertThat(response.getData()).isNotEmpty());

        step("У каждого элемента locale = " + locale + ", code и text заполнены", () ->
                assertThat(response.getData()).allSatisfy(item -> {
                    assertThat(item.getLocale()).isEqualTo(locale);
                    assertThat(item.getCode()).isNotBlank();
                    assertThat(item.getText()).isNotBlank();
                }));
    }

    /**
     * Известные дубли в данных сайта (найдены этим тестом, локаль ru): записи с разными id,
     * но одинаковым code. Это замечание к данным CMS, а не к тестам; список нужен,
     * чтобы тест ловил только НОВЫЕ дубли.
     */
    private static final Set<String> KNOWN_DUPLICATE_CODES =
            Set.of("checkAddressCheckButton", "checkAddressConnectionAvailable");

    @ParameterizedTest(name = "Нет новых дублей code (locale={0})")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Поле code не дублируется (кроме известных дублей)")
    @Severity(SeverityLevel.NORMAL)
    void codesShouldNotHaveNewDuplicates(String locale) {
        UiTextsResponse response = getUiTexts(locale);

        step("Повторяющиеся code входят в список известных дублей", () -> {
            Set<String> seen = new HashSet<>();
            Set<String> duplicates = response.getData().stream()
                    .map(UiTextItem::getCode)
                    .filter(code -> !seen.add(code))
                    .collect(Collectors.toSet());
            assertThat(duplicates).as("новые дубли code").isSubsetOf(KNOWN_DUPLICATE_CODES);
        });
    }

    @ParameterizedTest(name = "meta.pagination согласована с data (locale={0})")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Пагинация согласована с содержимым data")
    @Severity(SeverityLevel.NORMAL)
    void paginationShouldBeConsistent(String locale) {
        UiTextsResponse response = getUiTexts(locale);

        step("Первая страница, total не меньше размера data", () -> {
            assertThat(response.getMeta().getPagination().getPage()).isEqualTo(1);
            assertThat(response.getMeta().getPagination().getTotal())
                    .isGreaterThanOrEqualTo(response.getData().size());
        });
    }

    @DisplayName("Параметр pagination[pageSize] ограничивает размер выдачи")
    @Severity(SeverityLevel.NORMAL)
    @ParameterizedTest(name = "pageSize={0}")
    @ValueSource(ints = {1, 5})
    void pageSizeShouldLimitResults(int pageSize) {
        UiTextsResponse response = getUiTexts("ru", pageSize);

        step("В data не больше " + pageSize + " элементов, pageSize в meta = " + pageSize, () -> {
            assertThat(response.getData()).hasSizeLessThanOrEqualTo(pageSize);
            assertThat(response.getMeta().getPagination().getPageSize()).isEqualTo(pageSize);
        });
    }
}
