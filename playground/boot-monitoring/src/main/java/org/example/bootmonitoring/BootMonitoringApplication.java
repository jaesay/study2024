package org.example.bootmonitoring;

import org.example.bootmonitoring.order.gauge.StockConfigV1;
import org.example.bootmonitoring.order.gauge.StockConfigV2;
import org.example.bootmonitoring.order.v0.OrderConfigV0;
import org.example.bootmonitoring.order.v1.OrderConfigV1;
import org.example.bootmonitoring.order.v2.OrderConfigV2;
import org.example.bootmonitoring.order.v3.OrderConfigV3;
import org.example.bootmonitoring.order.v4.OrderConfigV4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import({OrderConfigV4.class, StockConfigV2.class})
@SpringBootApplication(scanBasePackages = "org.example.bootmonitoring.controller")
public class BootMonitoringApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootMonitoringApplication.class, args);
  }

}
