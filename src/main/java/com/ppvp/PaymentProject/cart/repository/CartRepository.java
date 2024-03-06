package com.ppvp.PaymentProject.cart.repository;

import com.ppvp.PaymentProject.cart.cartModel.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
  Optional<Cart> findCartByUser_UserId (Long userId);

  Optional<Cart> findCartByUser_Id(Long id);

  void deleteCartByUser_Id(Long id);

  Cart save(Cart cart);
}
