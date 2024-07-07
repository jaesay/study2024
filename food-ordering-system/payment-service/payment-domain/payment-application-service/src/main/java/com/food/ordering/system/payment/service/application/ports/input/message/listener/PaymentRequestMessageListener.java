package com.food.ordering.system.payment.service.application.ports.input.message.listener;


import com.food.ordering.system.payment.service.application.dto.PaymentRequest;

public interface PaymentRequestMessageListener {

    void completePayment(PaymentRequest paymentRequest);

    void cancelPayment(PaymentRequest paymentRequest);
}
