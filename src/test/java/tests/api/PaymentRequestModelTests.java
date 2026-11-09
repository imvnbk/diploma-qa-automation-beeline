package tests.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.PaymentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Проверка модели тела POST /telco/payment без обращения к сайту:
 * платёж — финансовая операция, а поле captchaCode защищает её от автоматических вызовов.
 */
@Tag("api")
@Epic("beeline.kz API")
@Feature("POST /telco/payment (модель тела запроса)")
@DisplayName("API: Модель тела запроса оплаты (/telco/payment)")
@Owner("Bexultan Imanbek")
public class PaymentRequestModelTests {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static PaymentRequest testRequest() {
        return PaymentRequest.builder()
                .msisdn("7000000000")
                .amount(2000)
                .captchaCode("abcde")
                .build();
    }

    @Test
    @DisplayName("Тело запроса сериализуется в JSON с полями msisdn, amount, captchaCode")
    @Severity(SeverityLevel.NORMAL)
    void requestShouldBeSerializedToExpectedJson() throws Exception {
        String json = step("Сериализовать PaymentRequest в JSON", () ->
                MAPPER.writeValueAsString(testRequest()));

        JsonNode node = MAPPER.readTree(json);

        step("В JSON ровно три поля с ожидаемыми значениями и типами", () -> {
            assertThat(node.fieldNames()).toIterable()
                    .containsExactlyInAnyOrder("msisdn", "amount", "captchaCode");
            assertThat(node.get("msisdn").isTextual()).isTrue();
            assertThat(node.get("msisdn").asText()).isEqualTo("7000000000");
            assertThat(node.get("amount").isInt()).isTrue();
            assertThat(node.get("amount").asInt()).isEqualTo(2000);
            assertThat(node.get("captchaCode").asText()).isEqualTo("abcde");
        });
    }

    @Test
    @DisplayName("JSON формата сайта десериализуется в модель PaymentRequest")
    @Severity(SeverityLevel.NORMAL)
    void siteJsonShouldBeDeserializedToModel() throws Exception {
        String siteJson = "{\"msisdn\":\"7000000000\",\"amount\":2000,\"captchaCode\":\"abcde\"}";

        PaymentRequest request = step("Десериализовать JSON формата сайта", () ->
                MAPPER.readValue(siteJson, PaymentRequest.class));

        step("Модель равна собранной через builder", () ->
                assertThat(request).isEqualTo(testRequest()));
    }
}
