package com.ppvp.PaymentProject.kakaopay.controller;

import com.ppvp.PaymentProject.Api;
import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import com.ppvp.PaymentProject.kakaopay.service.KakaoPayService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/kakao-pay")
public class kakaoPayControllertest {

  private final KakaoPayService kakaoPayService;

  /**
   * 결제 진행 중 취소
   */
  @GetMapping("/cancel")
  public String cancel() {

//    throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
    return "kakaocancel";
  }

  /**
   * 결제 성공
   */
  @GetMapping("/success")
  public String afterPayRequest(@RequestParam("pg_token") String pgToken, Model model, HttpSession session) {

    Api kakaoApprove = kakaoPayService.approveResponse(pgToken , session);
    model.addAttribute("kakaoApprove", kakaoApprove);
    return "kakaosuccess";
  }

  /**
   * 결제 실패
   */
  @GetMapping("/fail")
  public String fail() {

    return "kakaofail";
  }

}
