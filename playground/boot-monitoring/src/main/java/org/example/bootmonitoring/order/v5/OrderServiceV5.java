package org.example.bootmonitoring.order.v5;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.example.bootmonitoring.order.OrderService;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Observed(name = "my.order", contextualName = "my-order", lowCardinalityKeyValues = {"tag1", "1"})
public class OrderServiceV5 implements OrderService {

    private AtomicInteger stock = new AtomicInteger(100);

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
