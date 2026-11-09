package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TariffsPage {

    private final SelenideElement pageTitle =
            $$("body *")
                    .filterBy(text("Тарифы для смартфона"))
                    .first();

    /** Карточка тарифа: id карточки совпадает со slug тарифа из API. */
    private SelenideElement card(String slug) {
        return $("[id='" + slug + "']");
    }

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

    @Step("Проверить карточку тарифа «{title}» ({slug}): название и цена {price}")
    public TariffsPage checkTariffCard(String slug, String title, String price) {
        // тарифы лежат в карусели: карточки за границей видимой области Selenium считает неотображаемыми,
        // поэтому сверяем textContent, а не видимый текст
        SelenideElement tariffCard = card(slug).should(exist);
        tariffCard.$(".heading-5")
                .shouldHave(attributeMatching("textContent", containing(title)));
        tariffCard.$(".product-card__cost")
                .shouldHave(attributeMatching("textContent", containing(price)));
        return this;
    }

    @Step("Проверить, что карточка тарифа «{slug}» отображается")
    public TariffsPage checkCardVisible(String slug) {
        card(slug).shouldBe(visible);
        return this;
    }

    @Step("Нажать «Подробнее» в карточке тарифа «{slug}»")
    public TariffsPage clickDetails(String slug) {
        card(slug).$(".product-card__button-left").click();
        return this;
    }

    @Step("Нажать «Подключить» в карточке тарифа «{slug}»")
    public TariffsPage clickConnect(String slug) {
        card(slug).$("button[data-bs-target]").click();
        return this;
    }

    @Step("Проверить, что открылось окно подключения тарифа «{slug}»")
    public TariffsPage checkConnectModalVisible(String slug) {
        $("[id='modal-QR-" + slug + "']").shouldBe(visible);
        return this;
    }

    @Step("Перейти на вкладку «{tabName}»")
    public TariffsPage clickTab(String tabName) {
        $$(".product__tab-item").findBy(exactText(tabName)).click();
        return this;
    }

    @Step("Нажать «Архив тарифов»")
    public TariffsPage clickArchive() {
        $(".tariff-list__archive-button a").click();
        return this;
    }

    @Step("Раскрыть группу тарифов «{groupTitle}»")
    public TariffsPage expandGroup(String groupTitle) {
        SelenideElement header = $$(".product__group-nav .heading-3[role='button']")
                .findBy(text(groupTitle));
        header.click();
        header.shouldHave(attribute("aria-expanded", "true"));
        return this;
    }

    private static String containing(String text) {
        return "(?s).*" + Pattern.quote(text) + ".*";
    }
}
