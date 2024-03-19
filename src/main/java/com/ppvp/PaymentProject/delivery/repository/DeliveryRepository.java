package com.ppvp.PaymentProject.delivery.repository;

import com.ppvp.PaymentProject.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

  Delivery save(Delivery delivery);

  Integer deleteDeliveryByOrders_OrderId(String order_id);

}
