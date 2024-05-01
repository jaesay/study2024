package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval;

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;

public interface RestaurantApprovalMessageListener {

    void restaurantApproved(RestaurantApprovalResponse restaurantApprovalResponse);

    void restaurantRejected(RestaurantApprovalResponse restaurantApprovalResponse);
}
