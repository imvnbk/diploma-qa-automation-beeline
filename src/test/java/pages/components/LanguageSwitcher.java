package pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.WebDriverRunner.url;

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
        for (int attempt = 0; attempt < 15 && !url().contains(urlPart); attempt++) {
            if (attempt > 0) {
                languagePicker.selectOptionByValue(otherLang);
            }
            languagePicker.selectOptionByValue(targetLang);
            try {
                Wait().withTimeout(Duration.ofSeconds(1))
                        .until(driver -> driver.getCurrentUrl().contains(urlPart));
            } catch (RuntimeException notHydratedYet) {
            }
        }
        return this;
    }
}
