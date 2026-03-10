package com.saidavaas.literalura;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "DB_URL=jdbc:postgresql://localhost:5432/literalura_test",
        "DB_USERNAME=test",
        "DB_PASSWORD=test"
})
class LiteraluraApplicationTests {

    @Test
    void contextLoads() {
    }
}
