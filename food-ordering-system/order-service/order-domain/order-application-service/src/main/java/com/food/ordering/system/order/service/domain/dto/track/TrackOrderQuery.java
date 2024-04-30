package com.food.ordering.system.order.service.domain.dto.track;

import jakarta.validation.constraints.NotNull;

/**
 * 주문 최신 상태를 조회 DTO
 */
public record TrackOrderQuery(
        @NotNull String orderTrackingId) {
}
