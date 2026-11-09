package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.UiTextItem;
import models.UiTextsResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static api.UiTextsApi.getUiTexts;
import static api.UiTextsApi.getUiTextsRaw;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("beeline.kz API")
@Feature("GET /api/ui-texts")
@Owner("Bexultan Imanbek")
@DisplayName("API: UI-тексты (/api/ui-texts)")
public class UiTextsApiTests {

    @ParameterizedTest(name = "Ответ ui-texts соответствует JSON-схеме (locale={0})")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Ответ соответствует JSON-схеме")
    @Severity(SeverityLevel.CRITICAL)
    void responseShouldMatchJsonSchema(String locale) {
        getUiTextsRaw(locale)
                .body(matchesJsonSchemaInClasspath("schemas/ui-texts-schema.json"));
    }

    @ParameterizedTest(name = "Все элементы ui-texts имеют locale={0}, непустые code и text")
    @ValueSource(strings = {"ru", "kk"})
    @DisplayName("Тексты возвращаются для запрошенной локали")
    @Severity(SeverityLevel.CRITICAL)
    void allItemsShouldHaveRequestedLocale(String locale) {
        UiTextsResponse response = getUiTexts(locale);

        step("Список текстов не пустой", () ->
                assertThat(response.getData()).isNotEmpty());

        step("У каждого элемента locale = " + locale + ", code и text заполнены", () -> {
            for (UiTextItem item : response.getData()) {
                assertThat(item.getLocale()).isEqualTo(locale);
                assertThat(item.getCode()).isNotBlank();
                assertThat(item.getText()).isNotBlank();
            }
        });
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
            Set<String> duplicates = new HashSet<>();
            response.getData().forEach(item -> {
                if (!seen.add(item.getCode())) {
                    duplicates.add(item.getCode());
                }
            });
            duplicates.removeAll(KNOWN_DUPLICATE_CODES);
            assertThat(duplicates).as("новые дубли code").isEmpty();
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
