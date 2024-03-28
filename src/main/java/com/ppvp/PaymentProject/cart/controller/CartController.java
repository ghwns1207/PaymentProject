package com.ppvp.PaymentProject.cart.controller;


import com.ppvp.PaymentProject.Jwt.JwtService;
import com.ppvp.PaymentProject.cart.cartModel.Cart;
import com.ppvp.PaymentProject.cart.cartModel.CartItem;
import com.ppvp.PaymentProject.cart.cartModel.CartModel;
import com.ppvp.PaymentProject.cart.service.CartItemService;
import com.ppvp.PaymentProject.cart.service.CartService;

import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
  public ResponseEntity<Api> addItems(@RequestBody CartModel cartModel, @RequestHeader HttpHeaders headers) {
    log.info("cartModel : {}", cartModel);

    // Spring Security를 사용하여 JWT 토큰 추출
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String jwtToken = (String) authentication.getCredentials();

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

    log.info("claims : {}", claims);

    if (claims == null) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    Long userId = claims.get("userId", Long.class);

    log.info("Id : {}", userId);
    // 프로덕트 테이블에서 아이템 이름으로 검색 아이템 아이디 받아오는 부분 추가!!
//    findbyitemId(cartModel.getItemName());

    Api cart = cartService.addCartTable(cartModel, userId);
    log.info("Controller cart : {}", cart);
      return ResponseEntity.ok(cart);
  }

  @GetMapping("/retrieveBasket")
  public ResponseEntity<Api> retrieveBasket(@RequestHeader HttpHeaders headers) {
    log.info("headers : {}", headers);

    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
      return ResponseEntity.ok(ApiResponseUtil.failureResponse(HttpStatus.FORBIDDEN, "로그인 정보를 확인해 주세요"));
    }

    Claims claims = jwtService.parseToken(jwtToken);
    log.info("claims : {}", claims);
    Long userId = claims.get("userId", Long.class);
    log.info("userid : {}", userId);

    Cart cart = cartService.getCartId(userId);
    if (cart == null) {
      return ResponseEntity.ok(ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "장바구니 정보가 없습니다."));
    }
    String uuid = cart.getId();
    log.info("uuid : {}", cart.getId());

    Optional<List<CartItem>> cartItemDtoList = Optional.ofNullable(cartItemService.retrieveBasketList(uuid, userId));

    log.info("cartItemDto1List : {}", cartItemDtoList);
    if (cartItemDtoList.isEmpty()) {
      return ResponseEntity.ok(ApiResponseUtil.successResponse( HttpStatus.NO_CONTENT, "장바구니 상품 정보가 없습니다. 추가해주세요"));
    } else {
      return ResponseEntity.ok(ApiResponseUtil.successResponse(HttpStatus.OK, cartItemDtoList.get()));
    }

  }

  @GetMapping("/updateCart/{cartId}/{itemCount}")
  public ResponseEntity<?> updateCart(@PathVariable(name = "cartId") String cartId,
                                      @PathVariable(name = "itemCount") String count, @RequestHeader HttpHeaders headers) {

    log.info(count);
    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    if (jwtService.isTokenExpired(jwtToken)) {
      return ResponseEntity.ok(ApiResponseUtil.failureResponse(HttpStatus.FORBIDDEN,"로그인 해주세요."));
    }
    Claims claims = jwtService.parseToken(jwtToken);
    if (claims.isEmpty()) {
      return ResponseEntity.ok(ApiResponseUtil.failureResponse(HttpStatus.UNAUTHORIZED,"로그아웃 되었습니다. 다시 로그인 해주세요."));
    }

    String status = cartItemService.udateCartItem(Long.valueOf(cartId), Integer.valueOf(count));

    if ("200".equals(status)) {
      return ResponseEntity.ok(ApiResponseUtil.successResponse(HttpStatus.OK, "상품 정보 수정 완료"));
    } else {
      return ResponseEntity.ok(ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "아이템 정보를 확인해주세요."));
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
    if (claims.isEmpty()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그아웃 되었습니다. 다시 로그인 해주세요.");
    }

    String status = cartItemService.deleteCartItem(cartItemId);

    if ("200".equals(status)) {
      return ResponseEntity.ok("장바구니에서 해당 상품을 제거 했습니다.");
    } else {
      return ResponseEntity.status(HttpStatus.CONFLICT).body("상품 제거 실패");
    }
  }


}
