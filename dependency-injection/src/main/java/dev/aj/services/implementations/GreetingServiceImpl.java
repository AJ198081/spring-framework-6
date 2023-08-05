package dev.aj.services.implementations;

import dev.aj.services.GreetingService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile(value = {"minimal", "peace", "default"})
@Service(value = "greetingService")
public class GreetingServiceImpl implements GreetingService {

    public static final String HELLO_EVERYONE = "Hello everyone";

    @Override
    public String greetings() {
        return HELLO_EVERYONE;
    }
}
