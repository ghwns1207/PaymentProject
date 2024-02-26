package com.ppvp.PaymentProject.cart.service;



import com.ppvp.PaymentProject.cart.cartModel.CartDto;
import com.ppvp.PaymentProject.cart.cartModel.CartItemDto;
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

  public List<CartItemDto> retrieveBasketList(String uuid, Long userId) {
    log.info("userId : {}", userId);

    List<CartItemDto> cartItemDtos = cartItemRepository.findAllByCartDto_Id(uuid);
    log.info("cartItemDtos : {}", cartItemDtos);
    log.info("uuid : {}", uuid);

    if (cartItemDtos.isEmpty()) {
      return null;
    } else {
      return cartItemDtos;
    }
  }

  public String deleteCartItem(Long cartItemId) {

    cartItemRepository.deleteById(cartItemId);

    Optional<CartItemDto> cartItemDto = cartItemRepository.findCartItemDtoById(cartItemId);
    if (cartItemDto.isPresent()){
      return "409";

    }else{
      return "200";
    }

  }

  public String udateCartItem(Long cartItemId,Integer count ){
    Optional<CartItemDto> optionalCartItemDto = cartItemRepository.findCartItemDtoById(cartItemId);
    if (optionalCartItemDto.isPresent()){
      CartItemDto  cartItemDto= optionalCartItemDto.get();
      cartItemDto.setCount(count);
      cartItemRepository.save(cartItemDto);
      return "200";
    }else{
      return "404";
    }
  }
}