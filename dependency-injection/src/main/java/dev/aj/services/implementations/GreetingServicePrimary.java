package dev.aj.services.implementations;

import dev.aj.services.GreetingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = {"maximal", "war"})
@Service(value = "greetingService")
public class GreetingServicePrimary implements GreetingService {


    public static final String HELLO_FROM_PRIMARY_GREETINGS_SERVICE = "Hello from Primary Greetings Service";

    @Override
    public String greetings() {
        return HELLO_FROM_PRIMARY_GREETINGS_SERVICE;
    }
}
