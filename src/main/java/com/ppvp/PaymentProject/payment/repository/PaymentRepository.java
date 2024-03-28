package com.ppvp.PaymentProject.payment.repository;

import com.ppvp.PaymentProject.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


  Payment save(Payment payment);

  void deletePaymentByOrders_OrderId(String order_id);

}
