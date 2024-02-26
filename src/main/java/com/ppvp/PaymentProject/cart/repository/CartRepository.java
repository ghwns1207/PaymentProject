package com.ppvp.PaymentProject.cart.repository;

import com.ppvp.PaymentProject.cart.cartModel.CartDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartDto, Long> {
  Optional<CartDto> findCartDtoByUser_UserId (Long userId);

  Optional<CartDto> findCartDtoByUser_Id(Long id);

  boolean deleteByUser_Id(Long id);

  CartDto save(CartDto cartDto);
}
