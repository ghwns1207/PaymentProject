package com.ppvp.PaymentProject.deliveryAddress.controller;

import com.ppvp.PaymentProject.Api;
import com.ppvp.PaymentProject.Jwt.JwtService;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddressModel;
import com.ppvp.PaymentProject.deliveryAddress.service.DeliveryAddressService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/delivery_address")
public class DeliveryAddressController {

  private final JwtService jwtService;

  private final DeliveryAddressService deliveryAddressService;

  @PutMapping("/ordersheet/adddelivery")
  public ResponseEntity<Api> addDeliveryAddress(@RequestHeader HttpHeaders headers , @RequestBody DeliveryAddressModel deliveryAddressModel){

    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그인 상태 확인해주세요.")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그인 상태 확인해주세요.").build());
    }

    Long userId = jwtService.parseToken(jwtToken).get("userId", Long.class);
    if(userId == null){
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그아웃 되었습니다.")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그아웃 되었습니다.").build());
    }

    log.info(" Address : {}" , deliveryAddressModel);
    Api api =  deliveryAddressService.appendDeliveryAddress(userId,deliveryAddressModel );
    return ResponseEntity.ok().body(api);
  }

  @GetMapping("/ordersheet/retrieveDeliveryAddress")
  public ResponseEntity<Api> retrieveDeliveryAddress(@RequestHeader HttpHeaders headers ) {
    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    if (jwtService.isTokenExpired(jwtToken)) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("401").data(null).resultMessage("Unauthorized")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("로그인 상태 확인해주세요.")).build()));

      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그인 상태 확인해주세요.").build());
    }

    Long userId = jwtService.parseToken(jwtToken).get("userId", Long.class);

    if(userId == null){
      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그아웃 되었습니다.").build());
    }

    Api api =  deliveryAddressService.retrieveDeliveryAddressList(userId);

    return ResponseEntity.ok().body(api);
  }


}
