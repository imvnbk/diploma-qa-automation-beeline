package api;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.landings.request.LandingQueryRequest;
import models.landings.response.LandingQueryResponse;

import static io.restassured.RestAssured.given;
import static specs.ResponseSpecs.okResponseSpec;

public class LandingsApi {

    @Step("POST /api/beeline-landings/query (страница {request.filters.group.eq}/{request.filters.subGroup.eq}/{request.filters.slug.eq})")
    public static LandingQueryResponse getLanding(LandingQueryRequest request) {
        return given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/beeline-landings/query")
                .then()
                .spec(okResponseSpec)
                .extract().as(LandingQueryResponse.class);
    }

    /** Запрос без проверки статуса и произвольным телом — для негативных сценариев. */
    @Step("POST /api/beeline-landings/query с произвольным телом (без проверки статуса)")
    public static Response rawQuery(Object body) {
        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/beeline-landings/query");
    }
}
