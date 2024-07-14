package com.food.ordering.system.springbootk8sexample.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProbeController {

    private final ApplicationAvailability applicationAvailability;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping("hello")
    public String hello() {
        return "Application is now " + applicationAvailability.getLivenessState() + " " + applicationAvailability.getReadinessState();
    }

    @GetMapping("/block")
    public String block() {
        AvailabilityChangeEvent.publish(this.applicationEventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
        return "Blocked";
    }

    @GetMapping("/break")
    public String doBreak() {
        AvailabilityChangeEvent.publish(this.applicationEventPublisher, this, LivenessState.BROKEN);
        return "Broken";
    }

    @Async
    @EventListener
    public void onStateChanged(AvailabilityChangeEvent<ReadinessState> readiness) throws InterruptedException {
        System.out.println("State is changed to " + readiness.getState());
        if (readiness.getState() == ReadinessState.REFUSING_TRAFFIC) {
            Thread.sleep(15_000);
            AvailabilityChangeEvent.publish(applicationEventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
        }
    }
}
