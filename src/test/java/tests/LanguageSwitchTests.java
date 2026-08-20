package tests;

import org.junit.jupiter.api.Test;
import pages.components.LanguageSwitcher;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LanguageSwitchTests extends TestBase {

    private final LanguageSwitcher languageSwitcher =
            new LanguageSwitcher();

    @Test
    void shouldSwitchLanguageToRussian() {
        open("/kk");

        languageSwitcher
                .checkVisible()
                .selectRussian();

        webdriver().shouldHave(urlContaining("/ru"));
    }

    @Test
    void shouldSwitchLanguageToKazakh() {
        open("/ru");

        languageSwitcher
                .checkVisible()
                .selectKazakh();

        webdriver().shouldHave(urlContaining("/kk"));
    }
}
