package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement header = $("header");

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("/");
        return this;
    }

    @Step("Проверить, что шапка сайта отображается")
    public MainPage checkHeaderVisible() {
        header.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что заголовок страницы содержит \"{expected}\"")
    public MainPage checkTitleContains(String expected) {
        Wait().withMessage("page title to contain \"" + expected + "\"")
                .until(driver -> {
                    String actual = driver.getTitle();
                    return actual != null
                            && actual.toLowerCase().contains(expected.toLowerCase());
                });
        return this;
    }
}
