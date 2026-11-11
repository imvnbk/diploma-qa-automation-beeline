package pages.components;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Заголовок страницы: подтверждает, что после перехода отрисовался контент, а не только сменился URL.
 * На сайте заголовки бывают и тегами h1–h4, и блоками с классами heading-N / *__title,
 * поэтому для каждой страницы можно указать свой селектор.
 */
public class PageHeading {

    private static final String ANY_HEADING = "h1, h2, h3, h4, [class*='heading-']";

    @Step("Проверить, что на странице отображается заголовок «{heading}»")
    public PageHeading checkVisible(String heading) {
        return checkVisible(ANY_HEADING, heading);
    }

    @Step("Проверить, что элемент {selector} с текстом «{heading}» отображается")
    public PageHeading checkVisible(String selector, String heading) {
        $$(selector).findBy(text(heading)).shouldBe(visible);
        return this;
    }
}
