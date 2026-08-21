package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HelpPage {

    private final SelenideElement topQuestions =
            $$("body *")
                    .filterBy(text("Как мы можем помочь?"))
                    .first();

    @Step("Открыть страницу помощи")
    public HelpPage openPage() {
        open("/ru/help");
        return this;
    }

    @Step("Проверить, что отображается блок «Как мы можем помочь?»")
    public HelpPage checkTopQuestionsVisible() {
        topQuestions.shouldBe(visible);
        return this;
    }
}
