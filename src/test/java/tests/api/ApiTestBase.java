package tests.api;

import io.qameta.allure.Epic;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import specs.RestAssuredSetup;

/**
 * Базовый класс API-тестов: единая настройка REST Assured перед запуском класса.
 * Клиенты из пакета {@code api} вызывают просто {@code given()} и берут настройки отсюда.
 */
@Tag("api")
@Epic("beeline.kz API")
public abstract class ApiTestBase {

    @BeforeAll
    static void setUpRestAssured() {
        RestAssuredSetup.configure();
    }
}
