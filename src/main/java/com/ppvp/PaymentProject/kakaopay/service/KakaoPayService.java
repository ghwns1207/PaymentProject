package com.ppvp.PaymentProject.kakaopay.service;

import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import com.ppvp.PaymentProject.kakaopay.model.KakaoReadyResponse;
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

  @Value("${kakao.admin-key}")
  private String adminKey;

  private KakaoReadyResponse kakaoReadyResponse;

  public ResponseEntity<Map<String, String>> kakaoPayReady() {

    String url = "https://kapi.kakao.com/v1/payment/ready";
    // 카카오페이 요청 양식
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", cid);
    parameters.add("cid", cid);
    parameters.add("partner_order_id", "1001");
    parameters.add("partner_user_id", "testUser");
    parameters.add("item_name", "testitem");
    parameters.add("quantity", "2");
    parameters.add("total_amount", "45000");
    parameters.add("vat_amount", "2100");
    parameters.add("tax_free_amount", "0");
    parameters.add("approval_url", "http://localhost:8080/kakaosuccess");
    parameters.add("cancel_url", "http://localhost:8080/kakaocancel");
    parameters.add("fail_url", "http://localhost:8080/kakaofail");

    // 파라미터, 헤더
//    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

    // 외부에 보낼 url
//    RestTemplate restTemplate = new RestTemplate();

    try {
      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
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
  public KakaoApproveResponse ApproveResponse(String pgToken) {

    String url = "https://kapi.kakao.com/v1/payment/approve";

    // 카카오 요청
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", cid);
    parameters.add("tid", kakaoReadyResponse.getTid());
    parameters.add("partner_order_id", "가맹점 주문 번호");
    parameters.add("partner_user_id", "가맹점 회원 ID");
    parameters.add("pg_token", pgToken);

    // 파라미터, 헤더
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

    // 외부에 보낼 url
    RestTemplate restTemplate = new RestTemplate();

    KakaoApproveResponse approveResponse = restTemplate.postForObject( url, requestEntity, KakaoApproveResponse.class );

    return approveResponse;
  }

  /**
   * 카카오 요구 헤더값
   */
  private HttpHeaders getHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();

    String auth = "KakaoAK " + adminKey;

    httpHeaders.set("Authorization", auth);
    httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    return httpHeaders;
  }

}