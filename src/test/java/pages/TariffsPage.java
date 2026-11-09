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

    @Step("Проверить, что отображается тариф «{tariffTitle}»")
    public TariffsPage checkTariffDisplayed(String tariffTitle) {
        // в API перед названием может стоять декоративный символ (например, «✦ »), в тексте страницы его нет
        String visibleTitle = tariffTitle.replaceFirst("^[^\\p{L}\\p{N}]+", "");
        $$("body *")
                .filterBy(text(visibleTitle))
                .first()
                .shouldBe(visible);
        return this;
    }
}
