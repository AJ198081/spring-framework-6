package dev.aj;

import dev.aj.controllers.MyController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class DIApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(DIApplication.class, args);
        MyController controller = applicationContext.getBean(MyController.class);
        System.out.println(controller.sayHello());
    }
}