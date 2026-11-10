package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static org.hamcrest.Matchers.lessThan;

public class BaseSpec {

    public static final String BASE_URI = System.getProperty("baseUrl", "https://beeline.kz");
    public static final String BASE_PATH = "/api";

    public static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setBasePath(BASE_PATH)
            .setAccept(ContentType.JSON)
            .addHeader("User-Agent", "diploma-qa-automation-beeline/1.0")
            .addFilter(withCustomTemplates())
            .build();

    public static final ResponseSpecification okResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .expectResponseTime(lessThan(15000L))
            .build();
}
