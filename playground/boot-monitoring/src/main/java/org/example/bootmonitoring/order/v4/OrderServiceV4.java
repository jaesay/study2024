package org.example.bootmonitoring.order.v4;

import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.example.bootmonitoring.order.OrderService;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Timed(value = "my.order", percentiles = {0.5, 0.95, 0.99})
@Slf4j
public class OrderServiceV4 implements OrderService {

    private AtomicInteger stock = new AtomicInteger(100);

    @Override
    public void order() {
        log.info("주문");
        stock.decrementAndGet();
        sleep(500);
    }

    @Override
    public void cancel() {
        log.info("취소");
        stock.incrementAndGet();
        sleep(200);
    }

    private static void sleep(int l) {
        try {
            Thread.sleep(l + new Random().nextInt(200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AtomicInteger getStock() {
        return stock;
    }
}
