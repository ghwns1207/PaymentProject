package com.ppvp.PaymentProject.cart.service;

import com.ppvp.PaymentProject.cart.cartModel.Cart;
import com.ppvp.PaymentProject.cart.cartModel.CartItem;
import com.ppvp.PaymentProject.cart.cartModel.CartModel;
import com.ppvp.PaymentProject.cart.repository.CartItemRepository;
import com.ppvp.PaymentProject.cart.repository.CartRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CartService {

  private final CartRepository cartRepository;

  private final KakaoLoginRepository kakaoLoginRepository;

  private final CartItemRepository cartItemRepository;

  public Cart addCartTable(CartModel model, Long userId) {

    Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);

    log.info("userid : {}" ,userOptional.get().getUserId());
    if(userOptional.isEmpty()){
      // 유저 정보가 없는 경우
      throw new RuntimeException("유저 정보가 없습니다.");
    }
    log.info("userOptional : {}" ,userOptional.get());
    // userId를 사용하여 해당 유저의 카트를 찾습니다.

    Optional<Cart> optionalCart = cartRepository.findCartByUser_Id(userOptional.get().getId());
    Cart cart;

    // 카트가 이미 존재하는 경우 해당 카트를 사용합니다.
    if (optionalCart.isPresent()) {
      cart = optionalCart.get();
      log.info("cart : {}" , cart);
    } else {
      // 카트가 없는 경우
      // cartid 랜덤 UUID 생성
      String uuid = UUID.randomUUID().toString();
      log.info("uuid : {}", uuid);
      cart = Cart.builder().id(uuid).user(userOptional.get()).build();
//      cart = Cart.builder().id(uuid).build();
    }
    Cart saveCart = cartRepository.save(cart);

    if (saveCart == null) {
      throw new RuntimeException("Cart save error");
    }

    // 아이템 이름,타입으로 프로덕트 테이블에서 id 검색
    String itemId = model.getItemName() + model.getItemType();

    CartItem cartItem = CartItem.builder().cart(saveCart).itemId(6546L).count(Integer.valueOf(model.getItemQuantity())).build();

    CartItem savedCartItem = cartItemRepository.save(cartItem);
    if(savedCartItem == null){
      throw new RuntimeException("아이템 저장 에러");
    }
//    cart.getCartItemDto().add(savedCartItem);
    log.info("savedCartItem : {}" , savedCartItem);
    return saveCart;
  }

  public Cart getCartId(Long userId){

//    Optional<User> userOptional = kakaoLoginRepository.findByUserId(userId);
   Optional<Cart> cartDto = cartRepository.findCartByUser_Id(userId);
    log.info("cart : {}", cartDto);
    if (cartDto.isEmpty()) {
      return null;
    }
    return cartDto.get();
  }


}
