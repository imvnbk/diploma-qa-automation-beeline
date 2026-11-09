package tests.ui;

import api.TariffsApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Step;
import models.Tariff;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.TariffsPage;

@Epic("beeline.kz UI")
@Feature("Страница тарифов")
@DisplayName("UI: Страница тарифов")
public class TariffsTests extends TestBase {

    private final TariffsPage tariffsPage = new TariffsPage();

    @Test
    @DisplayName("Страница тарифов открывается и показывает заголовок «Тарифы для смартфона»")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    void tariffsPageShouldBeOpened() {
        tariffsPage.openPage()
                .checkPageTitle();
    }

    @Test
    @DisplayName("Первый тариф из API отображается на странице тарифов")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void firstTariffFromApiShouldBeDisplayed() {
        String tariffTitle = firstSmartphoneTariffTitle();

        tariffsPage.openPage()
                .checkTariffDisplayed(tariffTitle);
    }

    @Step("Получить из API название первого тарифа группы «Тарифы для смартфона»")
    private String firstSmartphoneTariffTitle() {
        return TariffsApi.getTariffList("ru").getData().getTariffGroups().stream()
                .filter(group -> "Тарифы для смартфона".equals(group.getTitle()))
                .flatMap(group -> group.getTariffs().stream())
                .map(Tariff::getTitle)
                .findFirst()
                .orElseThrow(() -> new AssertionError("API не вернул тарифов в группе «Тарифы для смартфона»"));
    }
}
