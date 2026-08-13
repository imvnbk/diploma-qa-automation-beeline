package tests;

import org.junit.jupiter.api.Test;
import pages.HelpPage;

public class HelpTests extends TestBase {

    private final HelpPage helpPage = new HelpPage();

    @Test
    void topQuestionsShouldBeDisplayed() {
        helpPage.openPage()
                .checkTopQuestionsVisible();
    }
}