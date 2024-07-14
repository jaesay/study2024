package com.food.ordering.system.springbootk8sexample.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GracefulShutdownController {

    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        log.info("got the request");
        Thread.sleep(5_000);
        return "slow";
    }
}
