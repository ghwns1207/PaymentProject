package com.ppvp.PaymentProject.cart.repository;


import com.ppvp.PaymentProject.cart.cartModel.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

  List<CartItem> findAllByCart_Id (String cartId);

  Optional<CartItem> findCartItemById (Long cartId);

 CartItem save(CartItem cartItem);

  void deleteById(Long cartItemId);
}
