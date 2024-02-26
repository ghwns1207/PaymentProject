package com.ppvp.PaymentProject.kakaopay.service;

import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import com.ppvp.PaymentProject.kakaopay.model.KakaoReadyResponse;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelRequest;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
//@Transactional
public class KakaoPayService {

  static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
//  static final String admin_Key = "${ADMIN_KEY}"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요

  // 카카오 페이 구버전 key
//  @Value("${kakao.admin-key}")
//  private String adminKey;

  @Value("${kakaoPay.Secret.keyDev}")
  private String secretKet;

  private KakaoReadyResponse kakaoReadyResponse;

  private KakaoApproveResponse approveResponse;

  private KakaoApprovedCancelResponse kakaoApprovedCancelResponse;

  public ResponseEntity<Map<String, String>> kakaoPayReady() {

    String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
    // 카카오페이 요청 양식
//    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", cid);
    parameters.put("partner_order_id", "1001");
    parameters.put("partner_user_id", "testUser");
    parameters.put("item_name", "뷰쉐이크 1개 외 8개");
    parameters.put("quantity", "9");
    parameters.put("total_amount", "450000");
    parameters.put("vat_amount", "2100");
    parameters.put("tax_free_amount", "0");
    parameters.put("approval_url", "http://localhost:8080/kakao-pay/success");
    parameters.put("cancel_url", "http://localhost:8080/kakao-pay/cancel");
    parameters.put("fail_url", "http://localhost:8080/kakao-pay/fail");

    try {
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
      RestTemplate restTemplate = new RestTemplate();
      kakaoReadyResponse = restTemplate.postForObject(url, requestEntity, KakaoReadyResponse.class);

      log.info("" + kakaoReadyResponse);

      // 여기에서 approveResponse를 사용하거나 반환하거나 처리합니다.
      Map<String, String> responseMap = new HashMap<>();
      responseMap.put("redirectUrl", kakaoReadyResponse.getNext_redirect_pc_url());
      return ResponseEntity.ok().body(responseMap);

    } catch (RestClientException e) {
      // 오류 처리
      e.printStackTrace();
      return ResponseEntity.status(500).body(Collections.singletonMap("error", "Failed to get KakaoPay URL"));
    }
  }

  /**
   * 결제 완료 승인
   */
  public KakaoApproveResponse approveResponse(String pgToken) {

    // https://kapi.kakao.com/v1/payment/approve 구 버전

    // 신 버전
    String url = "https://open-api.kakaopay.com/online/v1/payment/approve";

    // 카카오 요청
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", cid);
    parameters.put("tid", kakaoReadyResponse.getTid());
    parameters.put("partner_order_id", "1001"); // 가맹점 주문 번호
    parameters.put("partner_user_id", "testUser");  // 가맹점 회원 ID
    parameters.put("pg_token", pgToken);

    try {
      // 파라미터, 헤더
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

      // 외부에 보낼 url
      RestTemplate restTemplate = new RestTemplate();
      approveResponse = restTemplate.postForObject( url, requestEntity, KakaoApproveResponse.class );
      log.info("approveResponse : {}" , approveResponse);
      return approveResponse;
    }catch (Exception e){
      e.printStackTrace();
      return null;
    }
  }

  public KakaoApprovedCancelResponse kakaoApprovedCancel(KakaoApprovedCancelRequest approvedCancelRequest){

    // https://kapi.kakao.com/v1/payment/cancel 구 버전

    String reqURL = "https://open-api.kakaopay.com/online/v1/payment/cancel";

    // 카카오 요청
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", approvedCancelRequest.getCid());
    parameters.put("tid", approvedCancelRequest.getTid());
    parameters.put("cancel_amount", String.valueOf(approvedCancelRequest.getCancel_amount())); // 가맹점 주문 번호
    parameters.put("cancel_tax_free_amount", String.valueOf(approvedCancelRequest.getCancel_tax_free_amount()));  // 가맹점 회원 ID
    parameters.put("cancel_vat_amount", String.valueOf(approvedCancelRequest.getCancel_vat_amount()));

    try {
      // 파라미터, 헤더
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
      // 외부에 보낼 url
      RestTemplate restTemplate = new RestTemplate();
      kakaoApprovedCancelResponse = restTemplate.postForObject(reqURL, requestEntity, KakaoApprovedCancelResponse.class);
      log.info("CancelResponse : {}", kakaoApprovedCancelResponse);

      return kakaoApprovedCancelResponse;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 카카오 요구 헤더값
   */
  private HttpHeaders getHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();


    // DEV_SECRET_KEY 테스트 버전
    // SECRET_KEY 실 운영 버전
    String auth = "DEV_SECRET_KEY " + secretKet;

    httpHeaders.set("Authorization", auth);

    // 구버전
//    httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    // 신 버전
    httpHeaders.set("Content-type", "application/json");

    return httpHeaders;
  }



}