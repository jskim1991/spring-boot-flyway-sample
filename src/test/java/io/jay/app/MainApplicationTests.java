package io.jay.app;

import io.jay.app.initializer.ContainerTest;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "spring.flyway.clean-disabled=false"
})
@ActiveProfiles("test")
@ContainerTest
class MainApplicationTests {

    @BeforeEach
    void setup(@Autowired Flyway flyway) {
        flyway.migrate();
    }

    @AfterEach
    void cleanup(@Autowired Flyway flyway) {
        // requires "spring.flyway.clean-disabled=false" property
        flyway.clean();
    }

    @Test
    void contextLoads() {

    }

}
