package com.ppvp.PaymentProject.cart.service;

import com.ppvp.PaymentProject.cart.cartModel.Cart;
import com.ppvp.PaymentProject.cart.cartModel.CartItem;
import com.ppvp.PaymentProject.cart.cartModel.CartModel;
import com.ppvp.PaymentProject.cart.repository.CartItemRepository;
import com.ppvp.PaymentProject.cart.repository.CartRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.userModel.User;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public Api addCartTable(CartModel model, Long userId) {

    Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);

    log.info("userid : {}" ,userOptional.get().getUserId());
    if(userOptional.isEmpty()){
      return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "로그인을 확인해주세요.");
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
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "장바구니 저장 실패했습니다. 잠시 후 다시 시도해주세요.");
    }

    // 아이템 이름,타입으로 프로덕트 테이블에서 id 검색
    String itemId = model.getItemName() + model.getItemType();

    CartItem cartItem = CartItem.builder().cart(saveCart).itemId(6546L).count(Integer.valueOf(model.getItemQuantity())).build();

    CartItem savedCartItem = cartItemRepository.save(cartItem);
    if(savedCartItem == null){
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 에러가 있습니다. 잠시 후 다시 시도해주세요.");
    }
//    cart.getCartItemDto().add(savedCartItem);
    log.info("savedCartItem : {}" , savedCartItem);
    return ApiResponseUtil.successResponse(HttpStatus.OK,saveCart);
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
