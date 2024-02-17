package org.example.bootmonitoring.order.v5;

import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.aop.ObservedAspect;
import org.example.bootmonitoring.order.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV5 {

    @Bean
    public OrderService orderService() {
        return new OrderServiceV5();
    }

//    @Bean
//    public MyHandler myHandler() {
//        return new MyHandler();
//    }

    @Bean
    public ObservedAspect observedAspect(ObservationRegistry observationRegistry) {
        observationRegistry.observationConfig().observationHandler(new MyHandler());
        return new ObservedAspect(observationRegistry);
    }
}
