package api;

import io.qameta.allure.Step;
import models.HelpResponse;

import static io.restassured.RestAssured.given;
import static specs.BaseSpec.okResponseSpec;
import static specs.BaseSpec.requestSpec;

public class HelpApi {

    @Step("GET /api/help?locale={locale}")
    public static HelpResponse getHelp(String locale) {
        return given(requestSpec)
                .queryParam("locale", locale)
                .queryParam("populate[category][populate][subCategory]", true)
                .queryParam("populate[search][populate][variants]", true)
                .queryParam("populate[breadcrumbs]", true)
                .queryParam("populate[seo]", true)
                .when()
                .get("/help")
                .then()
                .spec(okResponseSpec)
                .extract().as(HelpResponse.class);
    }
}
