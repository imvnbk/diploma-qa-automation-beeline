package tests.api;

import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.payment.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static helpers.Json.toJson;
import static io.qameta.allure.Allure.step;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;

/**
 * Проверка формата тела POST /telco/payment без обращения к сайту:
 * платёж — финансовая операция, а поле captchaCode защищает её от автоматических вызовов.
 * Формат описан JSON-схемой, модель запроса сериализуется и проверяется по ней.
 */
@Feature("POST /telco/payment (модель тела запроса)")
@DisplayName("API: Модель тела запроса оплаты (/telco/payment)")
@Owner("Bexultan Imanbek")
public class PaymentRequestModelTests extends ApiTestBase {

    private static final String SCHEMA = "schemas/payment-request-schema.json";

    @Test
    @DisplayName("Тело запроса, собранное из модели, соответствует JSON-схеме")
    @Severity(SeverityLevel.NORMAL)
    void requestBodyShouldMatchJsonSchema() {
        PaymentRequest request = PaymentRequest.builder()
                .msisdn("7000000000")
                .amount(2000)
                .captchaCode("abcde")
                .build();

        step("Сериализованная модель удовлетворяет схеме", () ->
                assertThat(toJson(request), matchesJsonSchemaInClasspath(SCHEMA)));
    }

    @ParameterizedTest(name = "msisdn=\"{0}\"")
    @DisplayName("Номер телефона неверного формата не проходит JSON-схему")
    @Severity(SeverityLevel.NORMAL)
    @ValueSource(strings = {"", "700000000", "87000000000", "7000abc000"})
    void invalidMsisdnShouldNotMatchJsonSchema(String msisdn) {
        PaymentRequest request = PaymentRequest.builder()
                .msisdn(msisdn)
                .amount(2000)
                .captchaCode("abcde")
                .build();

        step("Схема отклоняет тело с некорректным номером", () ->
                assertThat(toJson(request), not(matchesJsonSchemaInClasspath(SCHEMA))));
    }

    @Test
    @DisplayName("Тело запроса без captchaCode не проходит JSON-схему")
    @Severity(SeverityLevel.NORMAL)
    void requestWithoutCaptchaShouldNotMatchJsonSchema() {
        PaymentRequest request = PaymentRequest.builder()
                .msisdn("7000000000")
                .amount(2000)
                .build();

        step("Схема отклоняет тело без капчи", () ->
                assertThat(toJson(request), not(matchesJsonSchemaInClasspath(SCHEMA))));
    }
}
