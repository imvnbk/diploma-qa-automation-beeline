package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.components.LanguageSwitcher;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LanguageSwitchTests extends TestBase {

    private final LanguageSwitcher languageSwitcher = new LanguageSwitcher();

    @DisplayName("Переключение языка меняет локаль в URL")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    @ParameterizedTest(name = "Открыта {0}, переключение на \"{1}\" → URL содержит /{1}")
    @CsvSource({
            "/kk, ru",
            "/ru, kk"
    })
    void shouldSwitchLanguage(String startUrl, String targetLang) {
        open(startUrl);

        languageSwitcher
                .checkVisible()
                .switchTo(targetLang);

        webdriver().shouldHave(urlContaining("/" + targetLang));
    }
}
