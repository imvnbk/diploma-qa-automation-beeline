package specs;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;

import static helpers.CustomAllureListener.withCustomTemplates;

/**
 * Общая настройка REST Assured: адрес, базовый путь, заголовки, Allure-фильтр.
 * Вызывается базовыми классами и API-тестов, и UI-тестов (UI берёт ожидаемые данные из API).
 */
public final class RestAssuredSetup {

    private RestAssuredSetup() {
    }

    public static void configure() {
        RestAssured.baseURI = System.getProperty("baseUrl", "https://beeline.kz");
        RestAssured.basePath = "/api";
        // спецификация пересоздаётся целиком, поэтому фильтр Allure не дублируется при повторных вызовах
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setAccept(ContentType.JSON)
                .addHeader("User-Agent", "diploma-qa-automation-beeline/1.0")
                .addFilter(withCustomTemplates())
                .build();
    }
}
