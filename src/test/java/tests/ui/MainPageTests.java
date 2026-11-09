package tests.ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("beeline.kz UI")
@Feature("Главная страница")
@DisplayName("UI: Главная страница")
public class MainPageTests extends TestBase {

    private final MainPage mainPage = new MainPage();

    @Test
    @DisplayName("Главная страница открывается, шапка видна, URL содержит beeline.kz")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.BLOCKER)
    void mainPageShouldBeOpened() {
        mainPage.openPage()
                .checkHeaderVisible();

        webdriver().shouldHave(urlContaining("beeline.kz"));
    }

    @Test
    @DisplayName("Кнопка «Личный кабинет» показывает сообщение о переезде в приложение «Janymda»")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    void personalAccountShouldShowMovedToJanymdaMessage() {
        mainPage.openRussianPage()
                .clickPersonalAccount()
                .checkPersonalAccountMovedMessage(
                        "Личный кабинет переехал в приложение «Janymda»",
                        "В приложении можно смотреть детализацию")
                .checkPersonalAccountQrCodeVisible();
    }

    @Test
    @DisplayName("Заголовок главной страницы содержит «Beeline»")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void mainPageShouldHaveCorrectTitle() {
        mainPage.openPage()
                .checkTitleContains("Beeline");
    }

    @ParameterizedTest(name = "Пункт меню «{0}» ведёт на {1}")
    @DisplayName("Пункты меню навигации ведут на нужные страницы")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    @CsvSource(delimiter = '|', value = {
            "Тарифы и услуги | /ru/products/tariffs",
            "Интернет дома + TV | /ru/internet-home-tv-mobile",
            "Помощь | /ru/help"
    })
    void navigationItemShouldOpenPage(String label, String expectedPath) {
        mainPage.openRussianPage()
                .clickNavigationItem(label);

        webdriver().shouldHave(urlContaining(expectedPath));
    }

    @ParameterizedTest(name = "Плитка «{0}» ведёт на {1}")
    @DisplayName("Плитки быстрых действий ведут на нужные страницы")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    @CsvSource(delimiter = '|', value = {
            "Пополнить баланс | /ru/top-up",
            "Тарифы | /ru/products/tariffs",
            "Замена SIM | /ru/sim-restore"
    })
    void quickTileShouldOpenPage(String label, String expectedPath) {
        mainPage.openRussianPage()
                .clickQuickTile(label);

        webdriver().shouldHave(urlContaining(expectedPath));
    }

    @Test
    @DisplayName("В подвале отображаются контакты поддержки")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.MINOR)
    void footerShouldShowSupportContacts() {
        mainPage.openRussianPage()
                .checkFooterContacts("116", "3131", "infodesk@beeline.kz");
    }
}
