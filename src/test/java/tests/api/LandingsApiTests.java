package tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.common.Localization;
import models.landings.response.LandingComponent;
import models.landings.response.LandingPage;
import models.landings.request.LandingQueryRequest;
import models.landings.response.ServiceGroup;
import models.landings.response.ServiceItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static api.LandingsApi.getLanding;
import static api.LandingsApi.queryLanding;
import static api.LandingsApi.rawQuery;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.greaterThan;

@Tag("api")
@Epic("beeline.kz API")
@Feature("POST /api/beeline-landings/query")
@DisplayName("API: Контент страницы (POST /api/beeline-landings/query)")
@Owner("Bexultan Imanbek")
public class LandingsApiTests {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final String SERVICE_GROUP_COMPONENT = "collection.service-group-landing";

    /** Тело запроса в том виде, в каком его отправляет страница «Архив услуг». */
    private static final String SITE_REQUEST_BODY = """
            {"filters":{"slug":{"$eq":"archive"},"group":{"$eq":"products"},"subGroup":{"$eq":"services"}},
             "pagination":{"pageSize":1},
             "populate":{"components":true,"breadcrumbs":true,"seo":true},
             "locale":"ru"}""";

    private static LandingQueryRequest archivePageRequest() {
        return LandingQueryRequest.forPage("products", "services", "archive", "ru");
    }

    private static LandingPage archivePage() {
        List<LandingPage> pages = getLanding(LandingQueryRequest.forServicesPage("archive", "ru")).getData();
        assertThat(pages).as("страниц в ответе").hasSize(1);
        return pages.get(0);
    }

    private static List<ServiceGroup> serviceGroups(LandingPage page) {
        assertThat(page.getComponents()).as("components страницы").isNotNull();
        return page.getComponents().stream()
                .filter(component -> SERVICE_GROUP_COMPONENT.equals(component.getComponent()))
                .map(LandingComponent::getServiceGroups)
                .filter(java.util.Objects::nonNull)
                .flatMap(List::stream)
                .toList();
    }

    private static List<ServiceItem> allServices(LandingPage page) {
        return serviceGroups(page).stream()
                .map(ServiceGroup::getServices)
                .filter(java.util.Objects::nonNull)
                .flatMap(List::stream)
                .toList();
    }

    @Test
    @DisplayName("Модель запроса сериализуется в тело, как у сайта")
    @Severity(SeverityLevel.NORMAL)
    void requestModelShouldMatchSiteRequestBody() throws Exception {
        JsonNode actual = step("Сериализовать LandingQueryRequest", () ->
                MAPPER.readTree(MAPPER.writeValueAsString(archivePageRequest())));
        JsonNode expected = MAPPER.readTree(SITE_REQUEST_BODY);

        step("JSON модели равен телу запроса сайта", () ->
                assertThat(actual).isEqualTo(expected));
    }

    @Test
    @DisplayName("Запрос контента страницы возвращает 200 и непустой JSON")
    @Severity(SeverityLevel.CRITICAL)
    void landingQueryShouldReturnJson() {
        queryLanding(archivePageRequest())
                .body("size()", greaterThan(0));
    }

    @Test
    @DisplayName("Ответ содержит запрошенную страницу (slug, group, subGroup, locale)")
    @Severity(SeverityLevel.CRITICAL)
    void responseShouldContainRequestedPage() {
        LandingPage page = archivePage();

        step("slug = archive, group = products, subGroup = services, locale = ru", () -> {
            assertThat(page.getSlug()).isEqualTo("archive");
            assertThat(page.getGroup()).isEqualTo("products");
            assertThat(page.getSubGroup()).isEqualTo("services");
            assertThat(page.getLocale()).isEqualTo("ru");
        });
    }

    @Test
    @DisplayName("Страница содержит группы услуг, в каждой есть название и услуги")
    @Severity(SeverityLevel.CRITICAL)
    void pageShouldContainServiceGroups() {
        LandingPage page = archivePage();
        List<ServiceGroup> groups = serviceGroups(page);
        List<String> componentInfo = page.getComponents().stream()
                .map(c -> c.getComponent() + " (service_groups=" + c.getServiceGroups() + ")")
                .toList();

        step("Группы услуг не пусты, у каждой title и services заполнены", () -> {
            assertThat(groups).as("группы услуг; компоненты страницы: " + componentInfo).isNotEmpty();
            for (ServiceGroup group : groups) {
                assertThat(group.getTitle()).isNotBlank();
                assertThat(group.getServices()).isNotEmpty();
            }
        });
    }

    @Test
    @DisplayName("У каждой услуги есть название, slug, локаль ru и стиль archive")
    @Severity(SeverityLevel.NORMAL)
    void everyServiceShouldHaveBasicFields() {
        List<ServiceItem> services = allServices(archivePage());

        step("title, slug, locale = ru, fixedStyle = archive, costBlock не null", () -> {
            for (ServiceItem service : services) {
                assertThat(service.getTitle()).isNotBlank();
                assertThat(service.getSlug()).isNotBlank();
                assertThat(service.getLocale()).isEqualTo("ru");
                assertThat(service.getFixedStyle()).isEqualTo("archive");
                assertThat(service.getCostBlock()).as("costBlock услуги " + service.getSlug()).isNotNull();
            }
        });
    }

    @Test
    @DisplayName("slug услуг уникальны")
    @Severity(SeverityLevel.NORMAL)
    void serviceSlugsShouldBeUnique() {
        List<ServiceItem> services = allServices(archivePage());

        step("Количество уникальных slug совпадает с количеством услуг", () -> {
            Set<String> slugs = new HashSet<>();
            services.forEach(service -> slugs.add(service.getSlug()));
            assertThat(slugs).hasSameSizeAs(services);
        });
    }

    @Test
    @DisplayName("Стоимость услуг (если указана) неотрицательна")
    @Severity(SeverityLevel.NORMAL)
    void servicePricesShouldBeNonNegative() {
        List<ServiceItem> services = allServices(archivePage());

        step("costBlock.newCost >= 0, когда он задан", () -> {
            for (ServiceItem service : services) {
                Double price = service.getCostBlock().getNewCost();
                if (price != null) {
                    assertThat(price).as("цена услуги " + service.getSlug()).isGreaterThanOrEqualTo(0.0);
                }
            }
        });
    }

    @Test
    @DisplayName("Переводы услуг имеют тот же slug и другую локаль")
    @Severity(SeverityLevel.MINOR)
    void localizationsShouldMatchServices() {
        List<ServiceItem> services = allServices(archivePage());

        step("У каждого перевода slug равен slug услуги, а locale отличается", () -> {
            for (ServiceItem service : services) {
                for (Localization localization : service.getLocalizations()) {
                    assertThat(localization.getSlug()).isEqualTo(service.getSlug());
                    assertThat(localization.getLocale()).isNotEqualTo(service.getLocale());
                    assertThat(localization.getTitle()).isNotBlank();
                }
            }
        });
    }

    // ---------- негативные сценарии ----------

    @ParameterizedTest(name = "group={0}, subGroup={1}, slug={2}")
    @DisplayName("Несуществующая страница: пустой data, а не ошибка")
    @Severity(SeverityLevel.NORMAL)
    @CsvSource({
            "products, services, no-such-page-xyz",
            "products, no-such-subgroup, archive",
            "no-such-group, services, archive"
    })
    void unknownPageShouldReturnEmptyData(String group, String subGroup, String slug) {
        List<LandingPage> pages = getLanding(LandingQueryRequest.forPage(group, subGroup, slug, "ru")).getData();

        step("data пуст", () -> assertThat(pages).isEmpty());
    }

    @Test
    @DisplayName("Неизвестная локаль: сервер не падает и не возвращает страниц")
    @Severity(SeverityLevel.NORMAL)
    void unknownLocaleShouldNotReturnPages() {
        var response = rawQuery(LandingQueryRequest.forPage("products", "services", "archive", "xx"));

        step("Статус не 5xx", () -> assertThat(response.statusCode()).isLessThan(500));
        step("Если статус 200 — страниц в ответе нет", () -> {
            if (response.statusCode() == 200) {
                assertThat(response.jsonPath().getList("data")).isNullOrEmpty();
            }
        });
    }

    @Test
    @DisplayName("Некорректный JSON в теле: сервер не отвечает ошибкой 5xx")
    @Severity(SeverityLevel.NORMAL)
    void malformedBodyShouldNotCauseServerError() {
        var response = rawQuery("{not a json");

        step("Статус не 5xx", () -> assertThat(response.statusCode()).isLessThan(500));
    }
}
