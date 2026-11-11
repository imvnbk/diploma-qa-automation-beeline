package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.uitexts.UiTextsResponse;

import static io.restassured.RestAssured.given;
import static specs.ResponseSpecs.okResponseSpec;

public class UiTextsApi {

    private static final String PATH = "/ui-texts";

    /** Ответ со статусом и временем, уже проверенными спецификацией; нужен для проверки по JSON Schema. */
    @Step("GET /api/ui-texts?locale={locale} (сырой ответ)")
    public static Response getUiTextsResponse(String locale) {
        return given()
                .queryParam("locale", locale)
                .when()
                .get(PATH)
                .then()
                .spec(okResponseSpec)
                .extract().response();
    }

    @Step("GET /api/ui-texts?locale={locale}&pagination[pageSize]={pageSize}")
    public static UiTextsResponse getUiTexts(String locale, int pageSize) {
        return given()
                .queryParam("locale", locale)
                .queryParam("pagination[pageSize]", pageSize)
                .when()
                .get(PATH)
                .then()
                .spec(okResponseSpec)
                .extract().as(UiTextsResponse.class);
    }

    @Step("GET /api/ui-texts?locale={locale} (десериализация в модель)")
    public static UiTextsResponse getUiTexts(String locale) {
        return getUiTextsResponse(locale).as(UiTextsResponse.class);
    }
}
