package tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.tariffs.Tariff;
import models.tariffs.TariffGroup;
import models.tariffs.TariffListData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static api.TariffsApi.getTariffList;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("GET /api/tariff-list")
@Owner("Bexultan Imanbek")
@DisplayName("API: Список тарифов (/api/tariff-list)")
public class TariffListApiTests extends ApiTestBase {

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

        step("title группы заполнен, tariffs не пуст", () ->
                assertThat(groups).isNotEmpty().allSatisfy(group -> {
                    assertThat(group.getTitle()).isNotBlank();
                    assertThat(group.getTariffs()).isNotEmpty();
                }));
    }

    @Test
    @DisplayName("У каждого тарифа есть название, slug и неотрицательная стоимость")
    @Severity(SeverityLevel.CRITICAL)
    void everyTariffShouldHaveTitleSlugAndCost() {
        List<Tariff> tariffs = allTariffs();

        step("title, slug и costBlock.newCost заполнены корректно", () ->
                assertThat(tariffs).isNotEmpty().allSatisfy(tariff -> {
                    assertThat(tariff.getTitle()).isNotBlank();
                    assertThat(tariff.getSlug()).isNotBlank();
                    assertThat(tariff.getCostBlock()).isNotNull();
                    assertThat(tariff.getCostBlock().getNewCost()).isNotNull().isGreaterThanOrEqualTo(0.0);
                }));
    }

    @Test
    @DisplayName("slug тарифов уникальны")
    @Severity(SeverityLevel.NORMAL)
    void tariffSlugsShouldBeUnique() {
        List<Tariff> tariffs = allTariffs();

        step("Среди slug тарифов нет повторов", () ->
                assertThat(tariffs).extracting(Tariff::getSlug).doesNotHaveDuplicates());
    }

    private static List<Tariff> allTariffs() {
        return getTariffList("ru").getData().getTariffGroups().stream()
                .flatMap(group -> group.getTariffs().stream())
                .toList();
    }
}
