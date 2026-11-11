package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebElementCondition;
import io.qameta.allure.Step;

import java.time.Duration;
import java.util.Arrays;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

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
        SelenideElement icon = $$("[data-bs-target='#QRModalAuth']").findBy(visible);
        // сайт на Nuxt: клик до завершения гидратации страницы не открывает модальное окно (на медленном CI-агенте
        // так бывает), поэтому при необходимости повторяем клик, пока окно не появится
        for (int attempt = 1; attempt <= 3; attempt++) {
            icon.click();
            try {
                personalAccountModal.shouldBe(visible, Duration.ofSeconds(4));
                return this;
            } catch (AssertionError e) {
                if (attempt == 3) {
                    throw e;
                }
            }
        }
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

    @Step("Нажать на пункт меню «{label}»")
    public MainPage clickNavigationItem(String label) {
        $$(".navigation-list a.navigation-button").findBy(exactText(label)).click();
        return this;
    }

    @Step("Проверить, что меню навигации отображается, а пункт «{russianLabel}» {present}")
    public MainPage checkNavigationItemPresence(String russianLabel, boolean present) {
        ElementsCollection items = $$(".navigation-list a.navigation-button").shouldHave(sizeGreaterThan(0));
        if (present) {
            items.findBy(exactText(russianLabel)).shouldBe(visible);
        } else {
            items.findBy(exactText(russianLabel)).shouldNot(exist);
        }
        return this;
    }

    @Step("Нажать на плитку быстрого действия «{label}»")
    public MainPage clickQuickTile(String label) {
        $$("a.box").findBy(exactText(label)).click();
        return this;
    }

    @Step("Проверить контакты поддержки в подвале: {contacts}")
    public MainPage checkFooterContacts(String... contacts) {
        $(".contact-list").scrollIntoView(true)
                .shouldHave(Arrays.stream(contacts).map(Condition::text).toArray(WebElementCondition[]::new));
        return this;
    }

    @Step("Проверить, что шапка сайта отображается")
    public MainPage checkHeaderVisible() {
        header.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что заголовок страницы содержит \"{expected}\"")
    public MainPage checkTitleContains(String expected) {
        $("title").shouldHave(attributeMatching("textContent", "(?is).*" + Pattern.quote(expected) + ".*"));
        return this;
    }
}
