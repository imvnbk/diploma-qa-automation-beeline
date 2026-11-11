package pages;

import io.qameta.allure.Step;
import pages.components.PageHeading;

public class TariffDetailsPage {

    private final PageHeading heading = new PageHeading();

    @Step("Проверить, что открыта страница тарифа «{tariffTitle}»")
    public TariffDetailsPage checkOpened(String tariffTitle) {
        heading.checkVisible(TariffsPage.visibleTitle(tariffTitle));
        return this;
    }
}
