package tests;

import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HelpPage;

public class HelpTests extends TestBase {

    private final HelpPage helpPage = new HelpPage();

    @Test
    @DisplayName("Блок «Как мы можем помочь?» отображается на странице помощи")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void topQuestionsShouldBeDisplayed() {
        helpPage.openPage()
                .checkTopQuestionsVisible();
    }
}
