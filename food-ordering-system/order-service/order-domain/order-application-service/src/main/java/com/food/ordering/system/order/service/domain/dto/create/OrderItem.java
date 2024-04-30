package com.food.ordering.system.order.service.domain.dto.create;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OrderItem(
        @NotNull String productId,
        @NotNull Integer quantity,
        @NotNull BigDecimal price,
        @NotNull BigDecimal subTotal) {

}
