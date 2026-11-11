package tests.ui;

import api.HelpApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import models.help.HelpSubCategory;
import models.help.HelpData;
import models.help.HelpSearch;
import models.help.SearchVariant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.HelpPage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static org.assertj.core.api.Assertions.assertThat;

@Epic("beeline.kz UI")
@Feature("Страница помощи")
@DisplayName("UI: Страница помощи")
public class HelpTests extends TestBase {

    private static final Pattern SECTION_LINK = Pattern.compile("^/help\\?category=([^&#]+)&section=([^&#]+)");

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
        assertThat(search.getVariants()).isNotEmpty().allSatisfy(variant ->
                helpPage.checkQuickVariantLink(variant.getLabel(), expectedHrefPart(variant.getUrl())));
    }

    @Test
    @DisplayName("Клик по быстрому варианту поиска открывает нужный раздел справки")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void quickVariantShouldOpenSection() {
        HelpData help = helpFromApi();
        // "/help?category=mobile&section=topquestions#tariff": категория mobile, раздел topquestions
        SearchVariant variant = help.getSearch().getVariants().stream()
                .filter(v -> SECTION_LINK.matcher(v.getUrl()).find())
                .findFirst()
                .orElseThrow(() -> new AssertionError("В API нет быстрых вариантов со ссылкой на раздел справки"));
        Matcher link = SECTION_LINK.matcher(variant.getUrl());
        link.find();
        String categorySlug = link.group(1);
        String sectionSlug = link.group(2);
        String sectionTitle = help.getCategory().stream()
                .filter(category -> category.getSlug().equals(categorySlug))
                .flatMap(category -> category.getSubCategory().stream())
                .filter(subCategory -> subCategory.getSlug().equals(sectionSlug))
                .map(HelpSubCategory::getTitle)
                .findFirst()
                .orElseThrow(() -> new AssertionError("В API нет раздела из ссылки " + variant.getUrl()));

        helpPage.openPage()
                .clickQuickVariant(variant.getLabel())
                .checkSectionOpened(sectionSlug, sectionTitle);

        webdriver().shouldHave(urlContaining("category=" + categorySlug + "&section=" + sectionSlug));
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
