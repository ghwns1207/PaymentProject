package com.ppvp.PaymentProject.kakaoLogin.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoLoginResponse {

  //토큰 타입, bearer로 고정
  @JsonProperty("token_type")
  private String tokenType;

  // 사용자 액세스 토큰 값
  @JsonProperty("access_token")
  private String accessToken;

  // 액세스 토큰과 ID 토큰의 만료 시간(초)
  @JsonProperty("expires_in")
  private Integer expiresIn;

  // 사용자 리프레시 토큰 값
  @JsonProperty("refresh_token")
  private String refreshToken;

  // 리프레시 토큰 만료 시간(초)
  @JsonProperty("refresh_token_expires_in")
  private Integer refreshTokenExpiresIn;

  // 인증된 사용자의 정보 조회 권한 범위 범위가 여러 개일 경우, 공백으로 구분
  private String scope;

  @JsonProperty("id_token")
  private String idTokenPayload;


  // OpenID Connect 확장 기능을 통해 발급되는 ID 토큰, Base64 인코딩 된 사용자 인증 정보 포함
//  @Data
//  class IdTokenPayload {
//
//    private String iss;
//
//    private String aud;
//
//    private String sub;
//
//    private Integer iat;
//
//    private Integer exp;
//
//    @JsonProperty("auth_time")
//    private Integer authTime;
//
//    private String nonce;
//
//    private String nickname;
//
//    private String picture;
//
//    private String email;
//
//  }

}
