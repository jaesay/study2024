package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.event.publisher.DomainEventPublisher;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

/**
 * 이벤트 발행은 어디서 해야할까?
 * - 엔티티의 비즈니스 로직을 통해 비즈니스 로직을 수행한 후 주문 이벤트를 생성한다.
 * - 도메인 이벤트를 발행하기 전에 데이터베이스에 영속화해야 하기 떄문에 이벤트 발생 시기와 방법은 Application Service를 통해 수행된다.
 * - 비즈니스로직에만 집중해야 하는 도메인 코어 모듈에서 불필요한 로직을 방지할 수 할 수 있기 때문에 애플리케이션 서비스에서 영속화하는 게 좋을 수 있다.
 * 도메인 이벤트는 누가 생성해야 할까?
 * - 도메인 엔티티와 도메인 서비스는 애플리케이션 서비스에서 호출할 수 있기 때문에 모두 관련 이벤트를 생성할 책임이 있다.
 * - DDD에서 도메인 서비스는 필수가 아니다. 비즈니스 로직에서 여러 Agrregate에 접근해야 하거나 비즈니스 로직이 어느 엔티티에도 맞지 않을 떄  필요하다.
 * - 여기서는 관련 이벤트를 생성하기 위해 모두 도메인 서비스를 사용하도록 통합한다. 따라서 해당 애플리케이션에서는 애플리케이션 서비스가 도메인과 직접 통신하지 않는다.
 * 도메인 서비스는 Clean Architecture 의 엔티티의 비즈니스 로직을 수행하는 구성요소인 Use Case와 비슷하다.
 */
public interface OrderDomainService {

    OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant, DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher);

    OrderPaidEvent payOrder(Order order, DomainEventPublisher<OrderPaidEvent> orderPaidEventDomainEventPublisher);

    void approveOrder(Order order);

    OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages, DomainEventPublisher<OrderCancelledEvent> orderCancelledEventDomainEventPublisher);

    void cancelOrder(Order order, List<String> failureMessages);
}
