package com.ppvp.PaymentProject.kakaopay.controller;

import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelRequest;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelResponse;
import com.ppvp.PaymentProject.kakaopay.service.KakaoPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/kakao-pay")
@Slf4j
@RequiredArgsConstructor
//@RequiredArgsConstructor
public class KakaoPayRestController {

  private final KakaoPayService kakaoPayService;

  /**
   * 결제요청
   */
  @PostMapping("/ready")
  public ResponseEntity<Map<String, String>> readyToKakaoPay() {

    return kakaoPayService.kakaoPayReady();
  }

  @PostMapping("/approvedCancel")
  public ResponseEntity<KakaoApprovedCancelResponse> approvedCancel(@RequestBody KakaoApprovedCancelRequest approvedCancelRequest){

    return ResponseEntity.ok(kakaoPayService.kakaoApprovedCancel(approvedCancelRequest));
  }


}
