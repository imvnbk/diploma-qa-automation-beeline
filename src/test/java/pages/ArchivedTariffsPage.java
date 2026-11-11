package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class ArchivedTariffsPage {

    /** Группы архива подписаны диапазонами букв: «А-В», «Г-Ж», «D-F». */
    private static final String ALPHABET_RANGE = "(?s).*\\p{L}\\s*-\\s*\\p{L}.*";

    @Step("Проверить, что открыт архив тарифов: тарифы сгруппированы по алфавиту, есть карточка архивного тарифа")
    public ArchivedTariffsPage checkOpened() {
        // у страницы архива нет заголовка-названия, её отличают алфавитные группы и карточки с модификатором _archive
        $$("h2.heading-3").first().shouldBe(visible).shouldHave(attributeMatching("textContent", ALPHABET_RANGE));
        $$(".product-card_archive").shouldHave(sizeGreaterThan(0));
        $$(".product-card_archive").first().shouldBe(visible)
                .$(".heading-5").shouldBe(visible).shouldNotBe(empty);
        return this;
    }
}
