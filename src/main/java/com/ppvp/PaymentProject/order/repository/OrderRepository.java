package com.ppvp.PaymentProject.order.repository;

import com.ppvp.PaymentProject.order.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {


  Optional<Orders> findOrdersByUser_Id (Long id);

  Optional<Orders> findOrdersByOrderId(String order_id);

  Optional<Orders> findOrdersByUser_IdAndOrderId(Long user_id, String order_id);
  Orders save(Orders orders);

}
