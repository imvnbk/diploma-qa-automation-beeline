package pages.components;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.Wait;
import static com.codeborne.selenide.WebDriverRunner.url;

public class LanguageSwitcher {

    // Native <select class="language-picker"> with <option value="kk|ru">.
    // Two exist (desktop + a d-lg-none mobile one); the first match is the
    // desktop switcher, which is the visible one at 1920x1080.
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

    // The switcher navigates via a Vue @change handler that attaches only after
    // client-side hydration. With eager page-load the first change can fire
    // before the handler is ready and gets lost, so re-trigger a genuine value
    // change (a same-value re-select emits no change event) and wait briefly for
    // the URL to reflect the chosen language between attempts.
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
                // handler not ready yet — re-trigger on the next attempt
            }
        }
    }
}
