package com.ppvp.PaymentProject.order.controller;


import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.Jwt.JwtService;
import com.ppvp.PaymentProject.order.service.OrderService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;



  private final JwtService jwtService;

  @PostMapping("/orderproducts")
  public ResponseEntity<?> createOrderSheet(@RequestBody List<Map<String, String>> selectedItems, @RequestHeader HttpHeaders headers) {
    log.info("headers : {}", headers);

    // Spring Security를 사용하여 JWT 토큰 추출
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String jwtToken = (String) authentication.getCredentials();

    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그인 상태 확인해주세요.")).build()));

      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그인 상태 확인해주세요.").build());
    }

    log.info("selectedItems : {}", selectedItems);


    var claims = jwtService.parseToken(jwtToken);
    if(claims == null){
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그아웃 되었습니다.")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그아웃 되었습니다.").build());
    }

    log.info("claims : {}", claims);
    Long userId = claims.get("userId", Long.class);

    if (selectedItems == null) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("400").data(null).resultMessage("Bad Request")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("장바구니에서 결제 요청 중 실패했습니다.")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("400").resultMessage("Bad Request")
          .errorMessage("장바구니에서 결제 요청 중 실패했습니다.").build());
    }

    ResponseEntity response = orderService.createOrderSheet(userId, selectedItems);

    return response;

    // 상품 아이디로 상품 정보 조회하는 부분

  }


  @GetMapping("/loadordersheet/{goduuid}")
  public ResponseEntity<?> getOrderDetailsList(@RequestHeader HttpHeaders headers, @PathVariable(name = "goduuid") String goduuid ){
    log.info("headers : {}", headers);

    // Spring Security를 사용하여 JWT 토큰 추출
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    String jwtToken = (String) authentication.getCredentials();

    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    log.info("goduuid : {}", goduuid);
    if (jwtService.isTokenExpired(jwtToken)) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그인 상태 확인해주세요.")).build()));

      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그인 상태 확인해주세요.").build());
    }

    Claims claims = jwtService.parseToken(jwtToken);
    if(claims == null){
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그아웃 되었습니다.")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그아웃 되었습니다.").build());
    }

    log.info("claims : {}", claims);
    Long userId = claims.get("userId", Long.class);

    Api api = orderService.retrieveOrderDetail(userId, goduuid);

    return ResponseEntity.ok().body(api);
  }


}
