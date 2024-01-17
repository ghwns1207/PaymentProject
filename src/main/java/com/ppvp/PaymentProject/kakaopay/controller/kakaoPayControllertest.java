//package com.ppvp.PaymentProject.Controller.kakaopay.controller;
//
//import com.ppvp.PaymentProject.Controller.kakaopay.model.KakaoReadyResponse;
//import lombok.extern.slf4j.Slf4j;
//
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//
//import org.springframework.http.ResponseEntity;
//
//
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import org.springframework.web.client.RestClientException;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@RestController
//@RequestMapping("/kakao-pay")
//public class kakaoPayControllertest {
//
//  static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
//
//  private String adminKey = "61583fac17118ad0997bab511d8b3608"; // 어드민 키
//
//  private KakaoReadyResponse kakaoReadyResponse;
//
//  @PostMapping("/ready")
//  public  ResponseEntity<Map<String, String>> kakaoPayReady() {
//    String url = "https://kapi.kakao.com/v1/payment/ready";
//
//    // 요청 헤더 설정
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("Authorization", "KakaoAK " + adminKey);
//    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//    // 요청 파라미터 설정
//    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//    params.add("cid", cid);
//    params.add("partner_order_id", "1001");
//    params.add("partner_user_id", "testUser");
//    params.add("item_name", "testitem");
//    params.add("quantity", "1");
//    params.add("total_amount", "2200");
////    params.add("vat_amount", "200");
//    params.add("tax_free_amount", "0");
//    params.add("approval_url", "http://localhost:8080/kakaosuccess");
//    params.add("cancel_url", "http://localhost:8080/kakaocancel");
//    params.add("fail_url", "http://localhost:8080/kakaofail");
//
//
//    try {
//      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
//      RestTemplate restTemplate = new RestTemplate();
//      kakaoReadyResponse = restTemplate.postForObject(url, requestEntity, KakaoReadyResponse.class);
//
//      log.info("" + kakaoReadyResponse);
//
//      // 여기에서 approveResponse를 사용하거나 반환하거나 처리합니다.
//      Map<String, String> responseMap = new HashMap<>();
//      responseMap.put("redirectUrl", kakaoReadyResponse.getNext_redirect_pc_url());
//      return ResponseEntity.ok().body(responseMap);
//
//    } catch (RestClientException e) {
//      // 오류 처리
//      e.printStackTrace();
//      return ResponseEntity.status(500).body(Collections.singletonMap("error", "Failed to get KakaoPay URL"));
//    }
//  }
//
//
//
//}
