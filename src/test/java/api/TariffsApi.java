package api;

import io.qameta.allure.Step;
import models.tariffs.TariffListResponse;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.okResponseSpec;
import static specs.BaseSpec.requestSpec;

public class TariffsApi {

    private static final String PATH = "/tariff-list";
    private static final String TARIFFS = "populate[tariffGroups][populate][tariffs][populate]";

    /** Те же параметры populate, что использует страница тарифов на сайте. */
    private static Map<String, Object> populateParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("populate[tabs]", true);
        params.put(TARIFFS + "[0]", "bonus.bonusItems.icon");
        params.put(TARIFFS + "[1]", "items");
        params.put(TARIFFS + "[2]", "costBlock");
        params.put("populate[breadcrumbs]", true);
        params.put("populate[seo]", true);
        return params;
    }

    @Step("GET /api/tariff-list?locale={locale}")
    public static TariffListResponse getTariffList(String locale) {
        return given(requestSpec)
                .queryParam("locale", locale)
                .queryParams(populateParams())
                .when()
                .get(PATH)
                .then()
                .spec(okResponseSpec)
                .extract().as(TariffListResponse.class);
    }
}
