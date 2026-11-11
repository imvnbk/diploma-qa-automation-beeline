package tests.ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.MainPage;
import pages.components.LanguageSwitcher;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("beeline.kz UI")
@Feature("Переключение языка")
@DisplayName("UI: Переключение языка")
public class LanguageSwitchTests extends TestBase {

    /** Пункт меню, который есть только в русской версии интерфейса. */
    private static final String RUSSIAN_NAVIGATION_ITEM = "Тарифы и услуги";

    private final LanguageSwitcher languageSwitcher = new LanguageSwitcher();
    private final MainPage mainPage = new MainPage();

    @DisplayName("Переключение языка меняет локаль в URL и язык интерфейса")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    @ParameterizedTest(name = "Открыта {0}, переключение на \"{1}\" → URL содержит /{1}")
    @CsvSource({
            "/kk, ru, true",
            "/ru, kk, false"
    })
    void shouldSwitchLanguage(String startUrl, String targetLang, boolean russianInterface) {
        open(startUrl);

        languageSwitcher
                .checkVisible()
                .switchTo(targetLang)
                .checkSelected(targetLang);

        mainPage.checkNavigationItemPresence(RUSSIAN_NAVIGATION_ITEM, russianInterface);
        webdriver().shouldHave(urlContaining("/" + targetLang));
    }
}
