package com.food.ordering.system.order.service.domain.dto.track;

import com.food.ordering.system.domain.valueobject.OrderStatus;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TrackOrderResponse(
        @NotNull String orderTrackingId,
        @NotNull OrderStatus orderStatus,
        List<String> failureMessages) {
}
