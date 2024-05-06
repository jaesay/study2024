package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.repository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.repository.RestaurantRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
class OrderApplicationServiceTest {

    @Autowired
    OrderApplicationService orderApplicationService;

    @Autowired
    OrderDataMapper orderDataMapper;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    CreateOrderCommand createOrderCommand;
    CreateOrderCommand createOrderCommandWrongPrice;
    CreateOrderCommand createOrderCommandWrongProductPrice;
    CreateOrderCommand createOrderCommandPassiveRestaurant;

    private static final UUID CUSTOMER_ID = UUID.randomUUID();
    private static final UUID RESTAURANT_ID = UUID.randomUUID();
    private static final UUID RESTAURANT_ID_PASSIVE = UUID.randomUUID();
    private static final UUID PRODUCT_ID = UUID.randomUUID();
    private static final UUID ORDER_ID = UUID.randomUUID();
    private static final BigDecimal PRICE = new BigDecimal("200.00");

    @BeforeAll
    void setUp() {
        createOrderCommand = new CreateOrderCommand(
                CUSTOMER_ID,
                RESTAURANT_ID,
                PRICE,
                List.of(
                        new OrderItem(PRODUCT_ID, 1, new BigDecimal("50.00"), new BigDecimal("50.00")),
                        new OrderItem(PRODUCT_ID, 3, new BigDecimal("50.00"), new BigDecimal("150.00"))
                ),
                new OrderAddress("street_1", "1000AB", "Paris")
        );

        createOrderCommandWrongPrice = new CreateOrderCommand(
                CUSTOMER_ID,
                RESTAURANT_ID,
                new BigDecimal("250.00"),
                List.of(
                        new OrderItem(PRODUCT_ID, 1, new BigDecimal("50.00"), new BigDecimal("50.00")),
                        new OrderItem(PRODUCT_ID, 3, new BigDecimal("50.00"), new BigDecimal("150.00"))
                ),
                new OrderAddress("street_1", "1000AB", "Paris")
        );

        createOrderCommandWrongProductPrice = new CreateOrderCommand(
                CUSTOMER_ID,
                RESTAURANT_ID,
                new BigDecimal("210.00"),
                List.of(
                        new OrderItem(PRODUCT_ID, 1, new BigDecimal("60.00"), new BigDecimal("60.00")),
                        new OrderItem(PRODUCT_ID, 3, new BigDecimal("50.00"), new BigDecimal("150.00"))
                ),
                new OrderAddress("street_1", "1000AB", "Paris")
        );

        createOrderCommandPassiveRestaurant = new CreateOrderCommand(
                CUSTOMER_ID,
                RESTAURANT_ID_PASSIVE,
                new BigDecimal("210.00"),
                List.of(
                        new OrderItem(PRODUCT_ID, 1, new BigDecimal("60.00"), new BigDecimal("60.00")),
                        new OrderItem(PRODUCT_ID, 3, new BigDecimal("50.00"), new BigDecimal("150.00"))
                ),
                new OrderAddress("street_1", "1000AB", "Paris")
        );

        Customer customer = new Customer();
        customer.setId(new CustomerId(CUSTOMER_ID));

        Restaurant restaurantResponse = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))))
                .active(true)
                .build();

        Restaurant restaurantResponsePassive = Restaurant.builder()
                .restaurantId(new RestaurantId(createOrderCommandPassiveRestaurant.restaurantId()))
                .products(List.of(new Product(new ProductId(PRODUCT_ID), "product-1", new Money(new BigDecimal("50.00"))),
                        new Product(new ProductId(PRODUCT_ID), "product-2", new Money(new BigDecimal("50.00")))))
                .active(false)
                .build();

        Order order = orderDataMapper.createOrderCommandToOrder(createOrderCommand);
        order.setId(new OrderId(ORDER_ID));

        given(customerRepository.findById(CUSTOMER_ID)).willReturn(Optional.of(customer));
        given(restaurantRepository.findByRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommand)))
                .willReturn(Optional.of(restaurantResponse));
        given(orderRepository.save(any(Order.class))).willReturn(order);
        given(restaurantRepository.findByRestaurantInformation(orderDataMapper.createOrderCommandToRestaurant(createOrderCommandPassiveRestaurant)))
                .willReturn(Optional.of(restaurantResponsePassive));
    }

    @Test
    void shouldCreateOrder() {
        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        assertThat(createOrderResponse.orderStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(createOrderResponse.message()).isEqualTo("Order Created Successfully");
        assertThat(createOrderResponse.orderTrackingId()).isNotNull();
    }

    @Test
    void failToCreateOrderWithWrongTotalPrice() {
        assertThatThrownBy(() -> orderApplicationService.createOrder(createOrderCommandWrongPrice))
                .isInstanceOf(OrderDomainException.class)
                .hasMessage("Total price: 250.00 is not equal to Order items total: 200.00!");
    }

    @Test
    void failToCreateOrderWithWrongProductPrice() {
        assertThatThrownBy(() -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice))
                .isInstanceOf(OrderDomainException.class)
                .hasMessage("Order item price: 60.00 is not valid for product: %s".formatted(PRODUCT_ID));
    }

    @Test
    void failToCreateOrderWithPassiveRestaurant() {
        assertThatThrownBy(() -> orderApplicationService.createOrder(createOrderCommandPassiveRestaurant))
                .isInstanceOf(OrderDomainException.class)
                .hasMessage("Restaurant with id: %s is not currently active!".formatted(RESTAURANT_ID_PASSIVE));
    }
}
