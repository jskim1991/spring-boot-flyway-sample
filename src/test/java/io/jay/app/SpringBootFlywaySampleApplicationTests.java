package io.jay.app;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.flyway.clean-disabled=false"})
class SpringBootFlywaySampleApplicationTests extends TestContainerInitializer {

    @BeforeEach
    void setup(@Autowired Flyway flyway) {
        flyway.migrate();
    }

    @AfterEach
    void cleanup(@Autowired Flyway flyway) {
        flyway.clean();
    }

    @Test
    void contextLoads() {

    }

}
