package org.example.greetingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    private final Logger logger = LoggerFactory.getLogger(GreetingController.class);

    @GetMapping("/greeting/{user}")
    public String greeting(@PathVariable String user) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error("GreetingController#greeting: ", e);
        }

        return "Hello! " + user;
    }
}
