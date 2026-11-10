package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.search.GlobalSearchResponse;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.okResponseSpec;
import static specs.BaseSpec.requestSpec;

public class GlobalSearchApi {

    @Step("GET /api/global-search/search?q={query}&limit={limit}&locale={locale}")
    public static GlobalSearchResponse search(String query, int limit, String locale) {
        return given(requestSpec)
                .queryParam("q", query)
                .queryParam("limit", limit)
                .queryParam("locale", locale)
                .when()
                .get("/global-search/search")
                .then()
                .spec(okResponseSpec)
                .extract().as(GlobalSearchResponse.class);
    }

    /** Запрос без проверки статуса — для негативных сценариев. */
    @Step("GET /api/global-search/search?q={query} (без проверки статуса)")
    public static Response rawSearch(String query) {
        return given(requestSpec)
                .queryParam("q", query)
                .queryParam("limit", 10)
                .queryParam("locale", "ru")
                .when()
                .get("/global-search/search");
    }
}
