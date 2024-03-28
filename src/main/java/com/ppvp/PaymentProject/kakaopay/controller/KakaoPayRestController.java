package com.ppvp.PaymentProject.kakaopay.controller;

import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.Jwt.JwtService;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelRequest;
import com.ppvp.PaymentProject.kakaopay.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kakao-pay")
@Slf4j
@RequiredArgsConstructor
//@RequiredArgsConstructor
public class KakaoPayRestController {

  private final KakaoPayService kakaoPayService;

  private final JwtService jwtService;

  /**
   * 결제요청
   */
  @PostMapping("/ready/{goduuid}/{delivery_address_id}")
  public ResponseEntity<?> readyToKakaoPay(@RequestHeader HttpHeaders headers,
                                           @PathVariable(name = "goduuid") String goduuid,@PathVariable(name = "delivery_address_id") String delivery_address_id, HttpSession session) {
    // JWT 토큰 추출
    String jwtToken = headers.getFirst(HttpHeaders.AUTHORIZATION).split(" ")[1];
    log.info("jwtToken : {}", jwtToken);
    session.setAttribute("delivery_address_id" , delivery_address_id);
    if (jwtService.isTokenExpired(jwtToken)) {

      return ResponseEntity.ok().body(Api.builder().resultCode("401").resultMessage("Unauthorized")
          .errorMessage("로그인 상태 확인해주세요.").build());
    }

    Long userId = jwtService.parseToken(jwtToken).get("userId", Long.class);
    return kakaoPayService.kakaoPayReady(userId,goduuid , session);
  }

  /**
  *  결제 취소 요청
  * */
  @PostMapping("/approvedCancel")
  public ResponseEntity<?> approvedCancel(@RequestBody KakaoApprovedCancelRequest approvedCancelRequest){

    return ResponseEntity.ok(kakaoPayService.kakaoApprovedCancel(approvedCancelRequest));
  }

//  @GetMapping("/rsuccess")
//  public ResponseEntity<?> afterPayRequest(@RequestParam("pg_token") String pgToken, Model model, @RequestHeader HttpHeaders headers,
//                                           HttpSession session) {
//
//    KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken, session);
////    model.addAttribute("kakaoApprove", kakaoApprove);
//
//    log.info(String.valueOf(headers));
//
//
//    return ResponseEntity.ok(model);
//  }


}
