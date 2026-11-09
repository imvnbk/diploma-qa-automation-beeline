package tests.ui;

import api.HelpApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import models.HelpData;
import models.HelpSearch;
import models.SearchVariant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HelpPage;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("beeline.kz UI")
@Feature("Страница помощи")
@DisplayName("UI: Страница помощи")
public class HelpTests extends TestBase {

    private final HelpPage helpPage = new HelpPage();

    @Test
    @DisplayName("Заголовок страницы помощи из API отображается на странице")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void helpTitleFromApiShouldBeDisplayed() {
        HelpData help = helpFromApi();

        helpPage.openPage()
                .checkTitleVisible(help.getTitle());
    }

    @Test
    @DisplayName("Первая категория справки из API отображается на странице")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void firstCategoryFromApiShouldBeDisplayed() {
        String categoryTitle = helpFromApi().getCategory().get(0).getTitle();

        helpPage.openPage()
                .checkCategoryVisible(categoryTitle);
    }

    @Test
    @DisplayName("Поле поиска и быстрые варианты из API отображаются на странице помощи")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void searchBlockFromApiShouldBeDisplayed() {
        HelpSearch search = helpFromApi().getSearch();

        helpPage.openPage()
                .checkSearchFieldVisible(search.getPlaceholder())
                .checkQuickVariantsTitleVisible(search.getVariantsTitle())
                .checkQuickVariants(search.getVariants().stream()
                        .map(SearchVariant::getLabel)
                        .toList());
    }

    @Test
    @DisplayName("Ссылки быстрых вариантов поиска на странице совпадают с API")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void quickVariantLinksShouldMatchApi() {
        HelpSearch search = helpFromApi().getSearch();

        helpPage.openPage();
        for (SearchVariant variant : search.getVariants()) {
            helpPage.checkQuickVariantLink(variant.getLabel(), expectedHrefPart(variant.getUrl()));
        }
    }

    @Test
    @DisplayName("Клик по быстрому варианту поиска открывает нужную категорию справки")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void quickVariantShouldOpenCategory() {
        SearchVariant variant = helpFromApi().getSearch().getVariants().stream()
                .filter(v -> v.getUrl().startsWith("/help?"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("В API нет внутренних быстрых вариантов поиска"));
        // "/help?category=mobile&section=topquestions#tariff" -> "category=mobile&section=topquestions"
        String query = variant.getUrl().substring("/help?".length()).split("#")[0];

        helpPage.openPage()
                .clickQuickVariant(variant.getLabel());

        webdriver().shouldHave(urlContaining(query));
    }

    /** Внутренние ссылки API приходят без локали (/help?...), а на странице — с ней (/ru/help?...). */
    private static String expectedHrefPart(String apiUrl) {
        return apiUrl.startsWith("/help") ? "/ru" + apiUrl : apiUrl.split("\\?")[0];
    }

    @Step("Получить данные страницы помощи из API")
    private HelpData helpFromApi() {
        return HelpApi.getHelp("ru").getData();
    }
}
