package com.ppvp.PaymentProject.order.repository;


import com.ppvp.PaymentProject.order.model.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetails , Long> {

  List<OrderDetails> findAllByOrders_OrderId(String order_id);

  OrderDetails save(OrderDetails orderDetails);


}
