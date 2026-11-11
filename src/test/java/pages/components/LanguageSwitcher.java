package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LanguageSwitcher {

    private final SelenideElement languagePicker = $(".language-picker");

    @Step("Проверить, что переключатель языка отображается")
    public LanguageSwitcher checkVisible() {
        languagePicker.shouldBe(visible);
        return this;
    }

    @Step("Переключить язык на \"{targetLang}\"")
    public LanguageSwitcher switchTo(String targetLang) {
        String otherLang = targetLang.equals("ru") ? "kk" : "ru";
        String urlPart = "/" + targetLang;
        // сайт на Nuxt: до завершения гидратации страницы выбор языка не срабатывает,
        // поэтому повторяем выбор, пока URL не сменится
        for (int attempt = 0; attempt < 15; attempt++) {
            if (attempt > 0) {
                languagePicker.selectOptionByValue(otherLang);
            }
            languagePicker.selectOptionByValue(targetLang);
            try {
                webdriver().shouldHave(urlContaining(urlPart), Duration.ofSeconds(1));
                return this;
            } catch (AssertionError notHydratedYet) {
                // повторяем
            }
        }
        return this;
    }

    @Step("Проверить, что в переключателе выбран язык \"{lang}\"")
    public LanguageSwitcher checkSelected(String lang) {
        languagePicker.shouldHave(value(lang));
        return this;
    }
}
