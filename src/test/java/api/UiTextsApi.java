package api;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.UiTextsResponse;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.okResponseSpec;
import static specs.BaseSpec.requestSpec;

public class UiTextsApi {

    private static final String PATH = "/ui-texts";

    @Step("GET /api/ui-texts?locale={locale}")
    public static ValidatableResponse getUiTextsRaw(String locale) {
        return given(requestSpec)
                .queryParam("locale", locale)
                .when()
                .get(PATH)
                .then()
                .spec(okResponseSpec);
    }

    @Step("GET /api/ui-texts?locale={locale}&pagination[pageSize]={pageSize}")
    public static UiTextsResponse getUiTexts(String locale, int pageSize) {
        return given(requestSpec)
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
        return getUiTextsRaw(locale).extract().as(UiTextsResponse.class);
    }
}
