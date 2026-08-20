package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.remote.DesiredCapabilities;
import pages.helpers.Attach;

import java.util.Map;

import static com.codeborne.selenide.Configuration.remote;
import static com.codeborne.selenide.Selenide.closeWebDriver;

public class TestBase {

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @BeforeAll
    static void setup() {
        Configuration.browser = System.getProperty("browser", "chrome");
        Configuration.browserVersion = System.getProperty("browserVersion", "149.0");
        Configuration.browserSize = System.getProperty("browserSize", "1920x1080");
        Configuration.baseUrl = System.getProperty("baseUrl","https://beeline.kz");
        Configuration.headless = Boolean.parseBoolean(System.getProperty("headless", "false"));
        Configuration.remote = System.getProperty("remote");
        if (remote == null || remote.isEmpty() || "null".equals(remote)) {
            remote = "https://user1:1234@selenoid.autotests.cloud/wd/hub";
        }
        Configuration.timeout = 10000;
        Configuration.pageLoadStrategy = "eager";

        // Selenoid records a session only when asked to; without enableVideo no
        // .mp4 is ever produced. Ignored by a local browser, so it is safe here.
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVideo", true,
                "enableVNC", true
        ));
        Configuration.browserCapabilities = capabilities;
    }

    @AfterEach
    void afterEach(){
        Attach.screenshotAs("Last screenshot");
        Attach.pageSource();
        Attach.browserConsoleLogs();
        Attach.addVideo();
        closeWebDriver();
    }
}