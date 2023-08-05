package dev.aj;

import dev.aj.controllers.MyController;
import dev.aj.services.GreetingService;
import dev.aj.services.implementations.GreetingServiceImpl;
import dev.aj.services.implementations.GreetingServicePrimary;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("peace")
@DisplayName("Test Dependency Injection")
@SpringBootTest
class DIApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MyController controller;

    @Autowired
    private Environment environment;

    private String helloString;

    @Test
    void testContextLoads() {

        String sayHelloString = controller.sayHello();

        if (environment.getActiveProfiles().length != 0 && environment.getActiveProfiles()[0].equalsIgnoreCase("war")) {
            Assertions.assertThat(sayHelloString)
                    .isEqualTo(GreetingServicePrimary.HELLO_FROM_PRIMARY_GREETINGS_SERVICE);
        } else {
            Assertions.assertThat(sayHelloString)
                    .isEqualTo(GreetingServiceImpl.HELLO_EVERYONE);
        }
    }

    @Test
    void testManualBeanRetrievalFromContext() {
        Object greetingService = applicationContext.getBean("greetingService");

        if (greetingService instanceof GreetingService) {
            helloString = ((GreetingService) greetingService).greetings();
        }

        if (environment.getActiveProfiles().length != 0 && environment.getActiveProfiles()[0].equalsIgnoreCase("war")) {
            Assertions.assertThat(helloString)
                    .isEqualTo(GreetingServicePrimary.HELLO_FROM_PRIMARY_GREETINGS_SERVICE);
        } else {
            Assertions.assertThat(helloString)
                    .isEqualTo(GreetingServiceImpl.HELLO_EVERYONE);
        }
    }
}