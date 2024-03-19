package com.ppvp.PaymentProject.payment.repository;

import com.ppvp.PaymentProject.payment.model.PaymentDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentDto , Long> {


  PaymentDto save(PaymentDto paymentDto);

  void deletePaymentDtoByOrders_OrderId(String order_id);

}
