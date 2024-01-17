package com.ppvp.PaymentProject.kakaopay.controller;

import com.ppvp.PaymentProject.kakaopay.service.KakaoPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/kakao-pay")
@Slf4j
//@RequiredArgsConstructor
public class KakaoPayController {

  @Autowired
  private  KakaoPayService kakaoPayService;

  /**
   * 결제요청
   */
  @PostMapping("/ready")
  public ResponseEntity<Map<String, String>> readyToKakaoPay() {

    return kakaoPayService.kakaoPayReady();
  }

  /**
   * 결제 진행 중 취소
   */
  @GetMapping("/cancel")
  public void cancel() {

//    throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
  }

  /**
   * 결제 성공
   */
//  @GetMapping("/success")
//  public ResponseEntity afterPayRequest(@RequestParam("pg_token") String pgToken) {
//
//    KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);
//
//    return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
//  }


  /**
   * 결제 실패
   */
  @GetMapping("/fail")
  public void fail() {

//    throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
  }
}
