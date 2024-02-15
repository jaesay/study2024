package org.example.bootmonitoring.order.v3;

import io.micrometer.core.instrument.MeterRegistry;
import org.example.bootmonitoring.order.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderConfigV3 {

    @Bean
    OrderService orderService(MeterRegistry registry) {
        return new OrderServiceV3(registry);
    }
}
