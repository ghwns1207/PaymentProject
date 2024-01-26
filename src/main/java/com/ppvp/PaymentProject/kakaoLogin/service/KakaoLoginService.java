package com.ppvp.PaymentProject.kakaoLogin.service;

import com.ppvp.PaymentProject.kakaoLogin.model.KakaoLoginResponse;
import com.ppvp.PaymentProject.kakaoLogin.model.KakaoProfile;
import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KakaoLoginService {

  @Value("${kakao.clientTest.secret.key}")
  private String clientSecret;

  @Value("${kakao.restApi.key}")
  private String clientId;

  @Value("${kakao.redirectTest.uri}")
  private String kakaoRediretUri;

  public KakaoProfile getKakaoInfo(String code) throws Exception {
    if (code == null) throw new Exception("Failed get authorization code");
    String reqURL = "https://kauth.kakao.com/oauth/token";


    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-type", "application/x-www-form-urlencoded");

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("grant_type", "authorization_code");
      params.add("client_id", clientId);
      params.add("client_secret", clientSecret);
      params.add("code", code);
      params.add("redirect_uri", kakaoRediretUri);

      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

      RestTemplate restTemplate = new RestTemplate();

      KakaoLoginResponse kakaoLoginResponse = restTemplate.postForObject(reqURL, requestEntity, KakaoLoginResponse.class);
      log.info("kakaoLoginResponse : {}", kakaoLoginResponse);
      String accessToken = kakaoLoginResponse.getAccessToken();
      String refreshToken = kakaoLoginResponse.getRefreshToken();
      log.info("Object :{}", getUserInfoWithToken(accessToken));
      return getUserInfoWithToken(accessToken);
    } catch (Exception e) {
      log.error("error :{}", e);
      return null;
    }
  }



  private KakaoProfile getUserInfoWithToken(String accessToken) throws Exception {
    String reqURL = "https://kapi.kakao.com/v2/user/me";
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);
    headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

    //HttpHeader 담기
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(headers);

    KakaoProfile kakaoProfile = restTemplate.postForObject(reqURL, requestEntity, KakaoProfile.class);


    return kakaoProfile;
  }
}







