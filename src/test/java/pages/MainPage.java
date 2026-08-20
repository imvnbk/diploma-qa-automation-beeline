package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement header = $("header");

    public MainPage openPage() {
        open("/");
        return this;
    }

    public MainPage checkHeaderVisible() {
        header.shouldBe(visible);
        return this;
    }

    // The homepage sets its <title> via Nuxt around hydration, so with eager
    // page-load a one-shot read can catch an empty title. Poll until it appears.
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