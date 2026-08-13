package tests;

import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.title;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.assertj.core.api.Assertions.assertThat;

public class MainPageTests extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    void mainPageShouldBeOpened() {
        mainPage.openPage()
                .checkHeaderVisible();

        assertThat(url())
                .contains("beeline.kz");
    }

    @Test
    void mainPageShouldHaveCorrectTitle() {
        mainPage.openPage();

        assertThat(title())
                .containsIgnoringCase("Beeline");
    }
}