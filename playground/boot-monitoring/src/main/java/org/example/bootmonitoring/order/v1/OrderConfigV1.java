package org.example.bootmonitoring.order.v1;

import io.micrometer.core.instrument.MeterRegistry;
import org.example.bootmonitoring.order.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV1 {

    @Bean
    OrderService orderService(MeterRegistry registry) {
        return new OrderServiceV1(registry);
    }
}
