package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {

    private final SelenideElement header = $("header");
    private final SelenideElement personalAccountModal = $("#QRModalAuth");

    @Step("Открыть главную страницу")
    public MainPage openPage() {
        open("/");
        return this;
    }

    @Step("Открыть главную страницу на русском языке")
    public MainPage openRussianPage() {
        open("/ru");
        return this;
    }

    @Step("Нажать на кнопку «Личный кабинет»")
    public MainPage clickPersonalAccount() {
        // иконка личного кабинета открывает модальное окно #QRModalAuth;
        // таких иконок может быть несколько (десктоп/мобильное меню): берём видимую
        $$("[data-bs-target='#QRModalAuth']").findBy(visible).click();
        return this;
    }

    @Step("Проверить сообщение «{title}» о переезде личного кабинета")
    public MainPage checkPersonalAccountMovedMessage(String title, String descriptionPart) {
        personalAccountModal.shouldBe(visible)
                .shouldHave(text(title))
                .shouldHave(text(descriptionPart));
        return this;
    }

    @Step("Проверить, что в сообщении отображается QR-код")
    public MainPage checkPersonalAccountQrCodeVisible() {
        // хеш в имени файла (qr_code_auth_<hash>.svg) может меняться, поэтому ищем по части имени
        personalAccountModal.$("img[src*='qr_code_auth']").shouldBe(visible);
        return this;
    }

    @Step("Проверить, что шапка сайта отображается")
    public MainPage checkHeaderVisible() {
        header.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что заголовок страницы содержит \"{expected}\"")
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
