package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import models.LandingQueryRequest;
import models.LandingQueryResponse;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.okResponseSpec;
import static specs.BaseSpec.requestSpec;

public class LandingsApi {

    @Step("POST /api/beeline-landings/query (страница {request.filters.group.eq}/{request.filters.subGroup.eq}/{request.filters.slug.eq})")
    public static ValidatableResponse queryLanding(LandingQueryRequest request) {
        return given(requestSpec)
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/beeline-landings/query")
                .then()
                .spec(okResponseSpec);
    }

    @Step("Получить страницу лендинга (десериализация в модель)")
    public static LandingQueryResponse getLanding(LandingQueryRequest request) {
        return queryLanding(request).extract().as(LandingQueryResponse.class);
    }

    /** Запрос без проверки статуса и произвольным телом — для негативных сценариев. */
    @Step("POST /api/beeline-landings/query с произвольным телом (без проверки статуса)")
    public static Response rawQuery(Object body) {
        return given(requestSpec)
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/beeline-landings/query");
    }
}
