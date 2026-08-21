package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.MainPage;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

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
    @DisplayName("Заголовок главной страницы содержит «Beeline»")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void mainPageShouldHaveCorrectTitle() {
        mainPage.openPage()
                .checkTitleContains("Beeline");
    }
}
