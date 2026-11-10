package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.tariffs.Tariff;
import models.tariffs.TariffGroup;
import models.tariffs.TariffListData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static api.TariffsApi.getTariffList;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("beeline.kz API")
@Feature("GET /api/tariff-list")
@Owner("Bexultan Imanbek")
@DisplayName("API: Список тарифов (/api/tariff-list)")
public class TariffListApiTests {

    @Test
    @DisplayName("Список тарифов возвращается для локали ru")
    @Severity(SeverityLevel.CRITICAL)
    void tariffListShouldBeReturnedForRussianLocale() {
        TariffListData data = getTariffList("ru").getData();

        step("locale = ru, есть группы тарифов", () -> {
            assertThat(data.getLocale()).isEqualTo("ru");
            assertThat(data.getTariffGroups()).isNotEmpty();
        });
    }

    @Test
    @DisplayName("Вкладка «Тарифы для смартфона» ведёт на /ru/products/tariffs")
    @Severity(SeverityLevel.NORMAL)
    void smartphoneTabShouldPointToTariffsPage() {
        TariffListData data = getTariffList("ru").getData();

        step("Среди вкладок есть «Тарифы для смартфона» со ссылкой /ru/products/tariffs", () ->
                assertThat(data.getTabs())
                        .anySatisfy(tab -> {
                            assertThat(tab.getName()).isEqualTo("Тарифы для смартфона");
                            assertThat(tab.getUrl()).isEqualTo("/ru/products/tariffs");
                        }));
    }

    @Test
    @DisplayName("В каждой группе есть название и хотя бы один тариф")
    @Severity(SeverityLevel.NORMAL)
    void everyGroupShouldHaveTitleAndTariffs() {
        List<TariffGroup> groups = getTariffList("ru").getData().getTariffGroups();

        step("title группы заполнен, tariffs не пуст", () -> {
            for (TariffGroup group : groups) {
                assertThat(group.getTitle()).isNotBlank();
                assertThat(group.getTariffs()).isNotEmpty();
            }
        });
    }

    @Test
    @DisplayName("У каждого тарифа есть название, slug и неотрицательная стоимость")
    @Severity(SeverityLevel.CRITICAL)
    void everyTariffShouldHaveTitleSlugAndCost() {
        List<TariffGroup> groups = getTariffList("ru").getData().getTariffGroups();

        step("title, slug и costBlock.newCost заполнены корректно", () -> {
            for (TariffGroup group : groups) {
                for (Tariff tariff : group.getTariffs()) {
                    assertThat(tariff.getTitle()).isNotBlank();
                    assertThat(tariff.getSlug()).isNotBlank();
                    assertThat(tariff.getCostBlock()).isNotNull();
                    assertThat(tariff.getCostBlock().getNewCost()).isNotNull().isGreaterThanOrEqualTo(0.0);
                }
            }
        });
    }

    @Test
    @DisplayName("slug тарифов уникальны")
    @Severity(SeverityLevel.NORMAL)
    void tariffSlugsShouldBeUnique() {
        List<TariffGroup> groups = getTariffList("ru").getData().getTariffGroups();

        step("Количество уникальных slug совпадает с количеством тарифов", () -> {
            Set<String> slugs = new HashSet<>();
            int total = 0;
            for (TariffGroup group : groups) {
                for (Tariff tariff : group.getTariffs()) {
                    slugs.add(tariff.getSlug());
                    total++;
                }
            }
            assertThat(slugs).hasSize(total);
        });
    }
}
