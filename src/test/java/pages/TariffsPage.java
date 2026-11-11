package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TariffsPage {

    /** Тарифы лежат в карусели: чтобы карточка стала видимой, к ней нужно доскроллить. */
    private static final String CAROUSEL_SCROLL = "{behavior: \"instant\", block: \"center\", inline: \"center\"}";

    /** Карточка тарифа: id карточки совпадает со slug тарифа из API. */
    private SelenideElement card(String slug) {
        return $("[id='" + slug + "']");
    }

    /** В API перед названием может стоять декоративный символ (например, «✦ »), в тексте страницы его нет. */
    static String visibleTitle(String apiTitle) {
        return apiTitle.replaceFirst("^[^\\p{L}\\p{N}]+", "");
    }

    @Step("Открыть страницу тарифов")
    public TariffsPage openPage() {
        open("/ru/products/tariffs");
        return this;
    }

    @Step("Проверить, что отображается заголовок «Тарифы для смартфона»")
    public TariffsPage checkPageTitle() {
        checkGroupTitleVisible("Тарифы для смартфона");
        return this;
    }

    @Step("Проверить, что отображается тариф «{tariffTitle}»")
    public TariffsPage checkTariffDisplayed(String tariffTitle) {
        $$(".product-card .heading-5")
                .findBy(text(visibleTitle(tariffTitle)))
                .scrollIntoView(CAROUSEL_SCROLL)
                .shouldBe(visible);
        return this;
    }

    @Step("Проверить карточку тарифа «{title}» ({slug}): название и цена {price}")
    public TariffsPage checkTariffCard(String slug, String title, String price) {
        SelenideElement tariffCard = card(slug).scrollIntoView(CAROUSEL_SCROLL).shouldBe(visible);
        tariffCard.$(".heading-5").shouldHave(text(visibleTitle(title)));
        tariffCard.$(".product-card__cost").shouldHave(text(price));
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

    @Step("Открыть страницу тарифов для роутера")
    public TariffsPage openRoutersPage() {
        open("/ru/products/tariffs/for-routers");
        return this;
    }

    @Step("Проверить, что выбрана вкладка «{tabName}»")
    public TariffsPage checkSelectedTab(String tabName) {
        $(".product__tab-item.selected").shouldHave(exactText(tabName));
        return this;
    }

    @Step("Проверить, что отображается группа тарифов «{groupTitle}»")
    public TariffsPage checkGroupTitleVisible(String groupTitle) {
        $$(".product__group-nav .heading-3").findBy(text(groupTitle)).shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в первой карточке есть название, цена и кнопки «Подробнее» и «Подключить»")
    public TariffsPage checkFirstCardHasContent() {
        SelenideElement first = $$(".product-card").first().shouldBe(visible);
        first.$(".heading-5").shouldNotBe(empty);
        first.$(".product-card__cost").shouldBe(visible).shouldNotBe(empty);
        first.$(".product-card__button-left").shouldBe(visible);
        first.$("button[data-bs-target]").shouldBe(visible);
        return this;
    }
}
