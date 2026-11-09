package models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Тело запроса POST /telco/payment (пополнение баланса), как его отправляет сайт.
 * Запрос на боевой сайт тестами не отправляется (финансовая операция, защищена капчей);
 * модель используется для сборки тела и проверки его формата.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String msisdn;
    private int amount;
    private String captchaCode;
}
