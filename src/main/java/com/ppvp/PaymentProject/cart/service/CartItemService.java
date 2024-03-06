package com.ppvp.PaymentProject.cart.service;



import com.ppvp.PaymentProject.cart.cartModel.CartItem;
import com.ppvp.PaymentProject.cart.repository.CartItemRepository;
import com.ppvp.PaymentProject.cart.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartItemService {

  private final CartRepository cartRepository;

  private final CartItemRepository cartItemRepository;

  public List<CartItem> retrieveBasketList(String uuid, Long userId) {
    log.info("userId : {}", userId);

    List<CartItem> cartItems = cartItemRepository.findAllByCart_Id(uuid);
    log.info("cartItems : {}", cartItems);
    log.info("uuid : {}", uuid);

    if (cartItems.isEmpty()) {
      return null;
    } else {
      return cartItems;
    }
  }

  public String deleteCartItem(Long cartItemId) {

    cartItemRepository.deleteById(cartItemId);

    Optional<CartItem> cartItemDto = cartItemRepository.findCartItemById(cartItemId);
    if (cartItemDto.isPresent()){
      return "409";

    }else{
      return "200";
    }

  }

  public String udateCartItem(Long cartItemId,Integer count ){
    Optional<CartItem> optionalCartItemDto = cartItemRepository.findCartItemById(cartItemId);
    if (optionalCartItemDto.isPresent()){
      CartItem cartItem = optionalCartItemDto.get();
      cartItem.setCount(count);
      cartItemRepository.save(cartItem);
      return "200";
    }else{
      return "404";
    }
  }
}