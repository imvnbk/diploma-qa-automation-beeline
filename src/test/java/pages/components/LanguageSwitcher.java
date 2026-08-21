package pages.components;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LanguageSwitcher {

    private final SelenideElement languagePicker = $(".language-picker");

    public LanguageSwitcher checkVisible() {
        languagePicker.shouldBe(visible);
        return this;
    }

    public LanguageSwitcher selectKazakh() {
        switchLanguage("kk", "ru", "/kk");
        return this;
    }

    public LanguageSwitcher selectRussian() {
        switchLanguage("ru", "kk", "/ru");
        return this;
    }

    private void switchLanguage(String targetValue, String otherValue, String urlPart) {
        for (int attempt = 0; attempt < 15 && !url().contains(urlPart); attempt++) {
            if (attempt > 0) {
                languagePicker.selectOptionByValue(otherValue);
            }
            languagePicker.selectOptionByValue(targetValue);
            try {
                Wait().withTimeout(Duration.ofSeconds(1))
                        .until(driver -> driver.getCurrentUrl().contains(urlPart));
            } catch (RuntimeException notHydratedYet) {
            }
        }
    }
}
