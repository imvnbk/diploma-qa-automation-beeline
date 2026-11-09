package tests.ui;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
}
