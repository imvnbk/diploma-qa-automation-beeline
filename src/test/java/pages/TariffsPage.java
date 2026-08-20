package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TariffsPage {

    private final SelenideElement pageTitle =
            $$("body *")
                    .filterBy(text("Тарифы для смартфона"))
                    .first();

    private final SelenideElement premiumFamilyTariff =
            $$("body *")
                    .filterBy(text("Премиум Семья х6"))
                    .first();

    public TariffsPage openPage() {
        open("/ru/products/tariffs");
        return this;
    }

    public TariffsPage checkPageTitle() {
        pageTitle.shouldBe(visible);
        return this;
    }

    public TariffsPage checkPremiumFamilyTariff() {
        premiumFamilyTariff.shouldBe(visible);
        return this;
    }
}