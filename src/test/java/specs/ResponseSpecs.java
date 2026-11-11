package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;

import static org.hamcrest.Matchers.lessThan;

public class ResponseSpecs {

    /** Максимально допустимое время ответа API, мс. Можно переопределить через -DresponseTimeoutMs. */
    public static final long RESPONSE_TIMEOUT_MS = Long.getLong("responseTimeoutMs", 5000L);

    public static final ResponseSpecification okResponseSpec = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .expectContentType(ContentType.JSON)
            .expectResponseTime(lessThan(RESPONSE_TIMEOUT_MS))
            .build();
}
