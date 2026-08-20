package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class MainPageTests extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    void mainPageShouldBeOpened() {
        mainPage.openPage()
                .checkHeaderVisible();

        webdriver().shouldHave(urlContaining("beeline.kz"));
    }

    @Test
    void mainPageShouldHaveCorrectTitle() {
        mainPage.openPage()
                .checkTitleContains("Beeline");
    }

}
