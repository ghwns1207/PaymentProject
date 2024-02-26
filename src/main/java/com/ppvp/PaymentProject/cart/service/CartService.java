package com.ppvp.PaymentProject.cart.service;

import com.ppvp.PaymentProject.cart.cartModel.CartDto;
import com.ppvp.PaymentProject.cart.cartModel.CartItemDto;
import com.ppvp.PaymentProject.cart.cartModel.CartModel;
import com.ppvp.PaymentProject.cart.repository.CartItemRepository;
import com.ppvp.PaymentProject.cart.repository.CartRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

  public CartDto addCartTable(CartModel model, Long userId) {
    Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);

    log.info("userid : {}" ,userOptional.get().getUserId());
    if(userOptional.isEmpty()){
      // 유저 정보가 없는 경우
      throw new RuntimeException("유저 정보가 없습니다.");
    }
    log.info("userOptional : {}" ,userOptional.get());
    // userId를 사용하여 해당 유저의 카트를 찾습니다.
    Optional<CartDto> optionalCart = cartRepository.findCartDtoByUser_Id(userOptional.get().getId());
    CartDto cartDto;
    if (optionalCart.isPresent()) {
      // 카트가 이미 존재하는 경우 해당 카트를 사용합니다.
      cartDto = optionalCart.get();
      log.info("cartDto : {}" , cartDto);
    } else {
      // 카트가 없는 경우
      // cartid 랜덤 UUID 생성
      String uuid = UUID.randomUUID().toString();
      log.info("uuid : {}", uuid);
      cartDto = CartDto.builder().id(uuid).user(userOptional.get()).build();
//      cartDto = CartDto.builder().id(uuid).build();
    }
    CartDto saveCartDto = cartRepository.save(cartDto);

    if (saveCartDto == null) {
      throw new RuntimeException("CartDto save error");
    }

    // 아이템 이름,타입으로 프로덕트 테이블에서 id 검색
    String itemId = model.getItemName() + model.getItemType();

    CartItemDto cartItemDto = CartItemDto.builder().cartDto(saveCartDto).itemId(6546L).count(Integer.valueOf(model.getItemQuantity())).build();
    CartItemDto savedCartItemDto = cartItemRepository.save(cartItemDto);
    if(savedCartItemDto == null){
      throw new RuntimeException("아이템 저장 에러");
    }
//    cartDto.getCartItemDto().add(savedCartItemDto);
    log.info("savedCartItemDto : {}" , savedCartItemDto);
    return cartDto;
  }

  public CartDto getCartId(Long userId){

//    Optional<User> userOptional = kakaoLoginRepository.findByUserId(userId);
   Optional<CartDto> cartDto = cartRepository.findCartDtoByUser_Id(userId);
    log.info("cart : {}", cartDto);
    if (cartDto.isEmpty()) {
      return null;
    }
    return cartDto.get();
  }


}
