package com.ppvp.PaymentProject.cart.controller;


import com.ppvp.PaymentProject.Jwt.JwtService;
import com.ppvp.PaymentProject.cart.cartModel.CartDto;
import com.ppvp.PaymentProject.cart.cartModel.CartItemDto;
import com.ppvp.PaymentProject.cart.cartModel.CartModel;
import com.ppvp.PaymentProject.cart.service.CartItemService;
import com.ppvp.PaymentProject.cart.service.CartService;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

  @Value("${jwt.accessToken.secretKey}")
  private String jwtSecretKey;

  private final CartService cartService;

  private final CartItemService cartItemService;

  private final JwtService jwtService;


  @PostMapping("/add_items")
  public ResponseEntity<CartDto> addItems(@RequestBody CartModel cartModel, @RequestHeader HttpHeaders headers) {
    log.info("cartModel : {}", cartModel);

    // 헤더에서 JWT 토큰 값을 가져옵니다.
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION);

    if (jwtToken == null || !jwtToken.startsWith("Bearer ")) {
      // JWT 토큰이 유효하지 않은 경우 에러 처리
      throw new IllegalArgumentException("Invalid JWT token");
    }

    // JWT 토큰에서 실제 토큰 값만 추출합니다.
    String token = jwtToken.substring(7); // "Bearer " 이후의 값만 추출
    if (jwtService.isTokenExpired(token)) throw new IllegalArgumentException("Expired JWT token");
    Claims claims = jwtService.parseToken(token);

    log.info("claims : {}",  claims);

    if (claims == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    Long userId = claims.get("userId", Long.class);

    log.info("Id : {}", userId);
    // 프로덕트 테이블에서 아이템 이름으로 검색 아이템 아이디 받아오는 부분 추가!!
//    findbyitemId(cartModel.getItemName());

    CartDto cartDto = cartService.addCartTable(cartModel, userId);
    cartDto.setUser(null);
    log.info("Controller cartDto : {}", cartDto);
    if (cartDto != null) {
      return ResponseEntity.ok().body(cartDto);
    } else {
      // API 클래스 느낌으로 포장후 다시 보내야함
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
  }

  @GetMapping("/retrieveBasket")
  public ResponseEntity<?> retrieveBasket(@RequestHeader HttpHeaders headers) {
    log.info("headers : {}", headers);

    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    Claims claims = jwtService.parseToken(jwtToken);
    log.info("claims : {}", claims);
    Long userId = claims.get("userId", Long.class);
    log.info("userid : {}", userId);

//    // UUID 추출
    String uuid = headers.getFirst("uuid");
    if(uuid == null){
      CartDto cartDto = cartService.getCartId(userId);
      if (cartDto == null){
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
      }
      uuid = cartDto.getId();
      log.info("uuid : {}", cartDto.getId());
    }

    Optional<List<CartItemDto>> cartItemDto1List = Optional.ofNullable(cartItemService.retrieveBasketList(uuid, userId));


    log.info("cartItemDto1List : {}", cartItemDto1List);
    if (cartItemDto1List.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    } else {
      for (CartItemDto cartItem : cartItemDto1List.get()) cartItem.getCartDto().setUser(null);
      return ResponseEntity.ok(cartItemDto1List);
    }

  }

  @GetMapping("/updateCart/{cartId}/{itemCount}")
  public ResponseEntity<?> updateCart(@PathVariable(name = "cartId") String cartId,@PathVariable(name = "itemCount") String count,@RequestHeader HttpHeaders headers){

    log.info(count);
    Long cartItemId = Long.valueOf(cartId);
    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    if (jwtService.isTokenExpired(jwtToken)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 해주세요.");
    }
    Claims claims = jwtService.parseToken(jwtToken);
    if(claims.isEmpty()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃 되었습니다. 다시 로그인 해주세요.");
    }

    String status = cartItemService.udateCartItem(Long.valueOf(cartId), Integer.valueOf(count));

    if("200".equals(status)){
      return ResponseEntity.status(HttpStatus.OK).body("success");
    }else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("아이템 정보가 없습니다.");
    }

  }


  @GetMapping("/deleteItem/{cartId}")
  public ResponseEntity<String> deleteCartItem(@PathVariable(name = "cartId") String cartId, @RequestHeader HttpHeaders headers) {

    Long cartItemId = Long.valueOf(cartId);
    log.info("cartItemId : {}", cartItemId);
    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).body("로그인 해주세요.");
    }

    Claims claims = jwtService.parseToken(jwtToken);
    if(claims.isEmpty()){
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃 되었습니다. 다시 로그인 해주세요.");
    }

    String status = cartItemService.deleteCartItem(cartItemId);

    if("200".equals(status)){
      return ResponseEntity.ok("장바구니에서 해당 상품을 제거 했습니다.");
    }else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("상품 제거 실패");
    }
  }


}
