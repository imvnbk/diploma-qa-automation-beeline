package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

    @Step("Открыть страницу тарифов")
    public TariffsPage openPage() {
        open("/ru/products/tariffs");
        return this;
    }

    @Step("Проверить, что отображается заголовок «Тарифы для смартфона»")
    public TariffsPage checkPageTitle() {
        pageTitle.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что отображается тариф «Премиум Семья х6»")
    public TariffsPage checkPremiumFamilyTariff() {
        premiumFamilyTariff.shouldBe(visible);
        return this;
    }
}
