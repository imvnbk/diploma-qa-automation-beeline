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
}