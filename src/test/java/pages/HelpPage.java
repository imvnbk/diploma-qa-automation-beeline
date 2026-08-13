package pages;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class HelpPage {

    private final SelenideElement topQuestions =
            $$("body *")
                    .filterBy(text("Как мы можем помочь?"))
                    .first();

    public HelpPage openPage() {
        open("/ru/help");
        return this;
    }

    public HelpPage checkTopQuestionsVisible() {
        topQuestions.shouldBe(visible);
        return this;
    }
}