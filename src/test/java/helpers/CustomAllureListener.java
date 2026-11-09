package helpers;

import io.qameta.allure.restassured.AllureRestAssured;

/**
 * Allure-листенер для rest-assured с собственными шаблонами вложений
 * (src/test/resources/tpl/http-request.ftl и http-response.ftl).
 */
public class CustomAllureListener {

    private static final AllureRestAssured FILTER = new AllureRestAssured();

    static {
        FILTER.setRequestTemplate("http-request.ftl");
        FILTER.setResponseTemplate("http-response.ftl");
    }

    private CustomAllureListener() {
    }

    public static AllureRestAssured withCustomTemplates() {
        return FILTER;
    }
}
