package tests.api;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.HelpCategory;
import models.HelpData;
import models.HelpSubCategory;
import models.SearchVariant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static api.HelpApi.getHelp;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("api")
@Epic("beeline.kz API")
@Feature("GET /api/help")
@Owner("Bexultan Imanbek")
@DisplayName("API: Страница помощи (/api/help)")
public class HelpApiTests {

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

        step("title, slug заполнены, subCategory не пуст", () -> {
            for (HelpCategory category : data.getCategory()) {
                assertThat(category.getTitle()).isNotBlank();
                assertThat(category.getSlug()).isNotBlank();
                assertThat(category.getSubCategory()).isNotEmpty();
            }
        });
    }

    @Test
    @DisplayName("slug уникальны: среди категорий и среди подкатегорий внутри категории")
    @Severity(SeverityLevel.NORMAL)
    void slugsShouldBeUnique() {
        HelpData data = getHelp("ru").getData();

        step("slug категорий не повторяются", () -> {
            Set<String> slugs = new HashSet<>();
            data.getCategory().forEach(c -> slugs.add(c.getSlug()));
            assertThat(slugs).hasSameSizeAs(data.getCategory());
        });

        // между разными категориями slug подкатегорий повторяются (topquestions, contacts…) — это нормально
        step("slug подкатегорий не повторяются внутри одной категории", () -> {
            for (HelpCategory category : data.getCategory()) {
                Set<String> slugs = new HashSet<>();
                category.getSubCategory().forEach(s -> slugs.add(s.getSlug()));
                assertThat(slugs).as("slug подкатегорий в «" + category.getSlug() + "»")
                        .hasSameSizeAs(category.getSubCategory());
                category.getSubCategory().forEach(s -> assertThat(s.getTitle()).isNotBlank());
            }
        });
    }

    @Test
    @DisplayName("Блок поиска: placeholder и быстрые варианты заполнены")
    @Severity(SeverityLevel.NORMAL)
    void searchBlockShouldBeFilled() {
        HelpData data = getHelp("ru").getData();

        step("placeholder не пуст, variants не пуст, у каждого label и url заполнены", () -> {
            assertThat(data.getSearch().getPlaceholder()).isNotBlank();
            assertThat(data.getSearch().getVariants()).isNotEmpty();
            for (SearchVariant variant : data.getSearch().getVariants()) {
                assertThat(variant.getLabel()).isNotBlank();
                assertThat(variant.getUrl()).isNotBlank();
            }
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

        step("Для ссылок вида /help?category=<slug> такая категория существует", () -> {
            for (SearchVariant variant : data.getSearch().getVariants()) {
                if (variant.getUrl().startsWith(CATEGORY_LINK_PREFIX)) {
                    String slug = variant.getUrl().substring(CATEGORY_LINK_PREFIX.length()).split("[&#]")[0];
                    assertThat(categorySlugs).as("категория из ссылки «" + variant.getUrl() + "»").contains(slug);
                }
            }
        });
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
