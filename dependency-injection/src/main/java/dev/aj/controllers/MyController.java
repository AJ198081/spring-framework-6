package dev.aj.controllers;

import dev.aj.services.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
//@RequiredArgsConstructor
public class MyController {

    public MyController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    private final GreetingService greetingService;

    public String sayHello() {
        return greetingService.greetings();
    }

}
