package tests;

import org.junit.jupiter.api.Test;
import pages.TariffsPage;

public class TariffsTests extends TestBase {

    private final TariffsPage tariffsPage = new TariffsPage();

    @Test
    void tariffsPageShouldBeOpened() {
        tariffsPage.openPage()
                .checkPageTitle();
    }

    @Test
    void premiumFamilyTariffShouldBeDisplayed() {
        tariffsPage.openPage()
                .checkPremiumFamilyTariff();
    }
}