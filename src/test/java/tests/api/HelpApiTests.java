package tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.help.HelpCategory;
import models.help.HelpData;
import models.help.HelpSubCategory;
import models.help.SearchVariant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static api.HelpApi.getHelp;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Feature("GET /api/help")
@Owner("Bexultan Imanbek")
@DisplayName("API: Страница помощи (/api/help)")
public class HelpApiTests extends ApiTestBase {

    private static final String CATEGORY_LINK_PREFIX = "/help?category=";

    @Test
    @DisplayName("Заголовок страницы помощи — «Как мы можем помочь?»")
    @Severity(SeverityLevel.CRITICAL)
    void helpTitleShouldBeReturned() {
        HelpData data = getHelp("ru").getData();

        step("locale = ru, title = «Как мы можем помочь?»", () -> {
            assertThat(data.getLocale()).isEqualTo("ru");
            assertThat(data.getTitle()).isEqualTo("Как мы можем помочь?");
        });
    }

    @Test
    @DisplayName("Категории справки содержат основные разделы mobile и homeinternet")
    @Severity(SeverityLevel.NORMAL)
    void categoriesShouldContainMainSections() {
        HelpData data = getHelp("ru").getData();

        step("Среди slug категорий есть mobile и homeinternet", () ->
                assertThat(data.getCategory())
                        .extracting(HelpCategory::getSlug)
                        .contains("mobile", "homeinternet"));
    }

    @Test
    @DisplayName("У каждой категории есть название, slug и подкатегории")
    @Severity(SeverityLevel.NORMAL)
    void everyCategoryShouldHaveTitleSlugAndSubCategories() {
        HelpData data = getHelp("ru").getData();

        step("title, slug заполнены, subCategory не пуст", () ->
                assertThat(data.getCategory()).isNotEmpty().allSatisfy(category -> {
                    assertThat(category.getTitle()).isNotBlank();
                    assertThat(category.getSlug()).isNotBlank();
                    assertThat(category.getSubCategory()).isNotEmpty();
                }));
    }

    @Test
    @DisplayName("slug уникальны: среди категорий и среди подкатегорий внутри категории")
    @Severity(SeverityLevel.NORMAL)
    void slugsShouldBeUnique() {
        HelpData data = getHelp("ru").getData();

        step("slug категорий не повторяются", () ->
                assertThat(data.getCategory()).extracting(HelpCategory::getSlug).doesNotHaveDuplicates());

        // между разными категориями slug подкатегорий повторяются (topquestions, contacts…) — это нормально
        step("slug подкатегорий не повторяются внутри одной категории, title заполнены", () ->
                assertThat(data.getCategory()).allSatisfy(category -> {
                    assertThat(category.getSubCategory())
                            .as("подкатегории в «" + category.getSlug() + "»")
                            .extracting(HelpSubCategory::getSlug)
                            .doesNotHaveDuplicates();
                    assertThat(category.getSubCategory())
                            .extracting(HelpSubCategory::getTitle)
                            .allSatisfy(title -> assertThat(title).isNotBlank());
                }));
    }

    @Test
    @DisplayName("Блок поиска: placeholder и быстрые варианты заполнены")
    @Severity(SeverityLevel.NORMAL)
    void searchBlockShouldBeFilled() {
        HelpData data = getHelp("ru").getData();

        step("placeholder не пуст, variants не пуст, у каждого label и url заполнены", () -> {
            assertThat(data.getSearch().getPlaceholder()).isNotBlank();
            assertThat(data.getSearch().getVariants()).isNotEmpty().allSatisfy(variant -> {
                assertThat(variant.getLabel()).isNotBlank();
                assertThat(variant.getUrl()).isNotBlank();
            });
        });
    }

    @Test
    @DisplayName("Внутренние ссылки быстрых вариантов поиска ведут на существующие категории")
    @Severity(SeverityLevel.NORMAL)
    void searchVariantLinksShouldPointToExistingCategories() {
        HelpData data = getHelp("ru").getData();
        Set<String> categorySlugs = data.getCategory().stream()
                .map(HelpCategory::getSlug)
                .collect(Collectors.toSet());

        step("Для ссылок вида /help?category=<slug> такая категория существует", () ->
                assertThat(data.getSearch().getVariants())
                        .extracting(SearchVariant::getUrl)
                        .filteredOn(url -> url.startsWith(CATEGORY_LINK_PREFIX))
                        .allSatisfy(url -> assertThat(categorySlugs)
                                .as("категория из ссылки «" + url + "»")
                                .contains(url.substring(CATEGORY_LINK_PREFIX.length()).split("[&#]")[0])));
    }

    @Test
    @DisplayName("SEO-блок содержит заголовок")
    @Severity(SeverityLevel.MINOR)
    void seoShouldHaveTitle() {
        HelpData data = getHelp("ru").getData();

        step("seo.title заполнен", () ->
                assertThat(data.getSeo().getTitle()).isNotBlank());
    }
}
