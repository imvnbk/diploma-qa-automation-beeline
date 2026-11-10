package tests.ui;

import api.TariffsApi;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import models.tariffs.Tab;
import models.tariffs.Tariff;
import models.tariffs.TariffGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.TariffsPage;

import java.util.List;

import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

@Epic("beeline.kz UI")
@Feature("Страница тарифов")
@DisplayName("UI: Страница тарифов")
public class TariffsTests extends TestBase {

    private static final String SMARTPHONE_GROUP = "Тарифы для смартфона";
    private static final String SOCIAL_GROUP = "Социальные тарифы";

    private final TariffsPage tariffsPage = new TariffsPage();

    @Test
    @DisplayName("Страница тарифов открывается и показывает заголовок «Тарифы для смартфона»")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    void tariffsPageShouldBeOpened() {
        tariffsPage.openPage()
                .checkPageTitle();
    }

    @Test
    @DisplayName("Первый тариф из API отображается на странице тарифов")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void firstTariffFromApiShouldBeDisplayed() {
        String tariffTitle = firstTariff(SMARTPHONE_GROUP).getTitle();

        tariffsPage.openPage()
                .checkTariffDisplayed(tariffTitle);
    }

    @Test
    @DisplayName("Тарифы для смартфона из API отображаются с названием и ценой")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.CRITICAL)
    void smartphoneTariffsFromApiShouldBeDisplayedWithPrices() {
        List<Tariff> tariffs = group(SMARTPHONE_GROUP).getTariffs();

        tariffsPage.openPage();
        for (Tariff tariff : tariffs) {
            long price = tariff.getCostBlock().getNewCost().longValue();
            tariffsPage.checkTariffCard(tariff.getSlug(), tariff.getTitle(), String.valueOf(price));
        }
    }

    @Test
    @DisplayName("Кнопка «Подробнее» открывает страницу тарифа")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void detailsButtonShouldOpenTariffPage() {
        String slug = firstTariff(SMARTPHONE_GROUP).getSlug();

        tariffsPage.openPage()
                .clickDetails(slug);

        webdriver().shouldHave(urlContaining("/ru/products/tariffs/" + slug));
    }

    @Test
    @DisplayName("Кнопка «Подключить» открывает окно подключения тарифа")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void connectButtonShouldOpenModal() {
        String slug = firstTariff(SMARTPHONE_GROUP).getSlug();

        tariffsPage.openPage()
                .clickConnect(slug)
                .checkConnectModalVisible(slug);
    }

    @Test
    @DisplayName("Вкладки страницы тарифов из API ведут на свои страницы")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void tabsFromApiShouldOpenTheirPages() {
        List<Tab> tabs = tariffTabs();

        // первая вкладка («Тарифы для смартфона») уже выбрана, проверяем остальные
        for (Tab tab : tabs.subList(1, tabs.size())) {
            tariffsPage.openPage()
                    .clickTab(tab.getName());

            webdriver().shouldHave(urlContaining(tab.getUrl()));
        }
    }

    @Test
    @DisplayName("Кнопка «Архив тарифов» открывает архив")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.MINOR)
    void archiveButtonShouldOpenArchive() {
        tariffsPage.openPage()
                .clickArchive();

        webdriver().shouldHave(urlContaining("/ru/products/archived/tariffs"));
    }

    @Test
    @DisplayName("Группа «Социальные тарифы» раскрывается и показывает тариф из API")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void socialTariffsGroupShouldExpand() {
        String slug = firstTariff(SOCIAL_GROUP).getSlug();

        tariffsPage.openPage()
                .expandGroup(SOCIAL_GROUP)
                .checkCardVisible(slug);
    }

    @Test
    @DisplayName("Вкладка «Для роутера» открывается: выбрана вкладка, видна группа тарифов и карточка")
    @Owner("Bexultan Imanbek")
    @Severity(SeverityLevel.NORMAL)
    void routerTariffsPageShouldBeOpened() {
        tariffsPage.openRoutersPage()
                .checkSelectedTab("Для роутера")
                .checkGroupTitleVisible("Безлимитный интернет для роутера")
                .checkFirstCardHasContent();

        webdriver().shouldHave(urlContaining("/ru/products/tariffs/for-routers"));
    }

    @Step("Получить из API группу тарифов «{title}»")
    private TariffGroup group(String title) {
        return TariffsApi.getTariffList("ru").getData().getTariffGroups().stream()
                .filter(group -> title.equals(group.getTitle()))
                .findFirst()
                .orElseThrow(() -> new AssertionError("API не вернул группу «" + title + "»"));
    }

    @Step("Получить из API первый тариф группы «{groupTitle}»")
    private Tariff firstTariff(String groupTitle) {
        return group(groupTitle).getTariffs().stream()
                .findFirst()
                .orElseThrow(() -> new AssertionError("В группе «" + groupTitle + "» нет тарифов"));
    }

    @Step("Получить из API вкладки страницы тарифов")
    private List<Tab> tariffTabs() {
        return TariffsApi.getTariffList("ru").getData().getTabs();
    }
}
