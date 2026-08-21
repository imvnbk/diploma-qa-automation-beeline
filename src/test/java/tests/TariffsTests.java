package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.TariffsPage;

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
    @DisplayName("Тариф «Премиум Семья х6» отображается на странице тарифов")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void premiumFamilyTariffShouldBeDisplayed() {
        tariffsPage.openPage()
                .checkPremiumFamilyTariff();
    }
}
