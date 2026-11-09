package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.util.List;
import java.util.regex.Pattern;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class HelpPage {

    private final SelenideElement searchInput = $(".help__search-input");
    private final SelenideElement variantsBlock = $(".help__search-variant-block");
    private final SelenideElement variantsTitle = variantsBlock.$(".fw-bold");
    private final ElementsCollection variants = $$(".help__search-variant");

    @Step("Открыть страницу помощи")
    public HelpPage openPage() {
        open("/ru/help");
        return this;
    }

    @Step("Проверить, что на странице отображается заголовок «{title}»")
    public HelpPage checkTitleVisible(String title) {
        $(".help__title").shouldBe(visible).shouldHave(exactText(title));
        return this;
    }

    @Step("Проверить, что на странице отображается категория «{categoryTitle}»")
    public HelpPage checkCategoryVisible(String categoryTitle) {
        $$("body *").filterBy(text(categoryTitle)).first().shouldBe(visible);
        return this;
    }

    @Step("Проверить, что поле поиска отображается с подсказкой «{placeholder}»")
    public HelpPage checkSearchFieldVisible(String placeholder) {
        searchInput.shouldBe(visible).shouldHave(attribute("placeholder", placeholder));
        return this;
    }

    @Step("Проверить, что отображается блок быстрых вариантов «{title}»")
    public HelpPage checkQuickVariantsTitleVisible(String title) {
        variantsTitle.shouldBe(visible).shouldHave(exactText(title));
        return this;
    }

    @Step("Проверить, что быстрые варианты поиска: {labels}")
    public HelpPage checkQuickVariants(List<String> labels) {
        variants.shouldHave(exactTexts(labels));
        return this;
    }

    @Step("Проверить, что ссылка варианта «{label}» содержит «{hrefPart}»")
    public HelpPage checkQuickVariantLink(String label, String hrefPart) {
        variants.findBy(exactText(label))
                .shouldHave(attributeMatching("href", ".*" + Pattern.quote(hrefPart) + ".*"));
        return this;
    }

    @Step("Нажать на быстрый вариант поиска «{label}»")
    public HelpPage clickQuickVariant(String label) {
        variants.findBy(exactText(label)).click();
        return this;
    }
}
