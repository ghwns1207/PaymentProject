package com.ppvp.PaymentProject.cart.repository;


import com.ppvp.PaymentProject.cart.cartModel.CartItemDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemDto, Long> {

  List<CartItemDto> findAllByCartDto_Id (String cartId);

  Optional<CartItemDto> findCartItemDtoById (Long cartId);

 CartItemDto save(CartItemDto cartItem);

  void deleteById(Long cartItemId);
}
