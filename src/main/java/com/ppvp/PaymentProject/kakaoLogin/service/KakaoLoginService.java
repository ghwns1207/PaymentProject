package com.ppvp.PaymentProject.kakaoLogin.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppvp.PaymentProject.cart.repository.CartRepository;
import com.ppvp.PaymentProject.kakaoLogin.model.KakaoLoginResponse;
import com.ppvp.PaymentProject.kakaoLogin.model.KakaoProfile;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.UserRoleRepository;
import com.ppvp.PaymentProject.userModel.User;
import com.ppvp.PaymentProject.userModel.UserRole;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class KakaoLoginService {

  @Value("${kakao.clientTest.secret.key}")
  private String clientSecret;

  @Value("${kakao.restApi.key}")
  private String clientId;

  @Value("${kakao.redirectTest.uri}")
  private String kakaoRediretUri;

  @Value("${kakao.redirectLogoutTest.uri}")
  private String kakaoRediretLogoutUri;


  @Value("${kakao.adminTest.key}")
  private String kakaoAdminTestKey;

  private final KakaoLoginRepository kakaoLoginRepository;

  private final UserRoleRepository userRoleRepository;

  private final CartRepository cartRepository;


  public Optional<User> getUser(Long userid) throws Exception {

    return kakaoLoginRepository.findByUserIdAndAndWithdrawnIsFalse(userid);
  }

  /*
   * 카카오 서버 측으로 받은 인가 코드로 로그인 요청
   *
   * */
  public Map<String, Object> getKakaoInfo(String code) throws Exception {
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
//      String accessToken = kakaoLoginResponse.getAccessToken();
//      String refreshToken = kakaoLoginResponse.getRefreshToken();
      KakaoProfile kakaoProfile = getUserInfoWithToken(kakaoLoginResponse.getAccessToken());
      log.info("kakaoProfile :{}", kakaoProfile);
      // 사용자 정보와 액세스 토큰을 하나의 객체로 묶어서 응답 본문에 추가
      Map<String, Object> response = new HashMap<>();
      response.put("kakaoProfile", kakaoProfile);
      response.put("kakaoLoginResponse", kakaoLoginResponse);
      if (kakaoLoginRepository.findByUserIdAndAndWithdrawnIsFalse(kakaoProfile.getId()).isEmpty()) {
        if (kakaoProfile.getId() != null) {
          String checkPhoneNumber = Arrays.stream(kakaoProfile.getKakao_account().getPhone_number().split(" ")).map(
              c -> c.replaceAll("-", "")).collect(Collectors.joining());
          log.info("PhoneNumber : {}", checkPhoneNumber);
          if (checkPhoneNumber.contains("+82")) {
            String userPhoneNumber = checkPhoneNumber.replaceAll("\\+82", "0");
            log.info("userPhoneNumber : {}", userPhoneNumber);
            // UserRole 객체 조회
            UserRole userRole = userRoleRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("Role not found"));

            User user = User.builder()
                .userId(kakaoProfile.getId())
                .name(kakaoProfile.getKakao_account().getName())
                .email(kakaoProfile.getKakao_account().getEmail())
                .image(kakaoProfile.getProperties().getThumbnail_image())
                .phoneNumber(userPhoneNumber)
                .role(userRole)
                .withdrawn(false)
                .suspended(false)
                .build();
            kakaoLoginRepository.save(user);
            log.info("user : {}", user);
          }
        }

      }
      return response;
    } catch (Exception e) {
      log.error("error :{}", e);
      return null;
    }
  }

  /*
   * accessToken으로 사용자 정보 요청
   *
   * */
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

  /*
   *  accessToken 토큰으로 로그아웃
   *
   * */
//  public boolean kakaoLogout(String accessToken){
//    String reqURL = "https://kapi.kakao.com/v1/user/logout";
//
//    try {
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("Content-type", "application/x-www-form-urlencoded");
//      headers.add("Authorization", "Bearer "+ accessToken);
//      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
//
//      RestTemplate restTemplate = new RestTemplate();
//
//      ResponseEntity<String> response = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);
//      log.info("response : {}" ,response.getBody());
//      // ObjectMapper 생성
//      ObjectMapper objectMapper = new ObjectMapper();
//
//      try {
//        // JSON 문자열 파싱
//        JsonNode rootNode = objectMapper.readTree(response.getBody());
//
//        // id 값이 있는지 확인
//        if (rootNode.has("id")) {
//          // id 값이 존재하는 경우
//          // id 값에 대한 처리를 여기에 추가
//
//        } else {
//          // id 값이 존재하지 않는 경우
//          // 처리 로직을 여기에 추가
//        }
//      } catch (IOException e) {
//        // JSON 파싱 중 오류 발생 시 처리
//        e.printStackTrace();
//      }
//      return true;
//
//    }catch (Exception e){
//      return false;
//    }
//  }


  /*
   *  Rest 로그아웃
   *
   * */
  public ResponseEntity<?> kakaoLogout(String accessToken) {
    String reqURL = "https://kapi.kakao.com/v1/user/logout";

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.add("Content-type", "application/x-www-form-urlencoded");
      headers.add("Authorization", "Bearer " + accessToken);
      HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);

      RestTemplate restTemplate = new RestTemplate();

      ResponseEntity<String> response = restTemplate.exchange(reqURL, HttpMethod.POST, requestEntity, String.class);
      log.info("response : {}", response.getBody());
      // ObjectMapper 생성
      ObjectMapper objectMapper = new ObjectMapper();
      // JSON 문자열 파싱
      JsonNode rootNode = objectMapper.readTree(response.getBody());

      // id 값이 있는지 확인
      if (rootNode.has("id")) {
        // id 값이 존재하는 경우
        // id 값에 대한 처리를 여기에 추가

        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.status(response.getStatusCode()).body("로그아웃 실패!");
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 로그아웃 실패!");
    }

  }


  /*
   * 세션함께 로그아웃 처리
   * error
   * */
  public ResponseEntity<?> kakaoOauthLogout() {
    String reqURL = "https://kauth.kakao.com/oauth/logout?client_id=" + clientId + "&logout_redirect_uri=" + kakaoRediretLogoutUri;
    // RestTemplate 생성
    RestTemplate restTemplate = new RestTemplate();
    try {

      ResponseEntity<String> response = restTemplate.getForEntity(reqURL, String.class);


      Map<String, String> responseMap = new HashMap<>();
      responseMap.put("redirectUrl", response.getBody());
//        return ResponseEntity.ok().body(responseMap);
//      log.info("response :{}", response);
      if (response.getStatusCode().is2xxSuccessful()) {
//        URI redirectUri = response.getHeaders().getLocation();
//        log.info("redirectUri :{}", redirectUri);
        String redirectUri = response.getHeaders().getLocation().toString();
        return ResponseEntity.ok().body("{\"redirectUri\": \"" + redirectUri + "\"}");
      } else {
        // 로그아웃 실패한 경우
        return ResponseEntity.status(response.getStatusCode()).body("logout failed");
      }
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("logout error");
    }

  }

  public ResponseEntity<?> kakaoUnlink(String accessToken){
    String reqURL = "https://kapi.kakao.com/v1/user/unlink";

    try {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www-form-urlencoded");
    headers.add("Authorization", "Bearer " + accessToken);
    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<String> response = restTemplate.postForEntity(reqURL, requestEntity, String.class);
    log.info("response : {}", response.getBody());
    // ObjectMapper 생성
    ObjectMapper objectMapper = new ObjectMapper();
    // JSON 문자열 파싱
    JsonNode rootNode = objectMapper.readTree(response.getBody());

    // id 값이 있는지 확인
    if (rootNode.has("id")) {
      // 루트 노드에서 ID 추출
      Long userId = rootNode.get("id").asLong();
     Optional<User> userOptional = kakaoLoginRepository.findByUserIdAndAndWithdrawnIsFalse(userId);
     if (userOptional.isEmpty()){
       return ResponseEntity.status(response.getStatusCode()).body("회원탈퇴 ID를 확인해주세요 실패!");
     }

      User user = userOptional.get();
     user.setWithdrawn(true);
      User checkUser = kakaoLoginRepository.save(user);

      if (checkUser != null) {
        cartRepository.deleteByUser_Id(user.getId());
        return ResponseEntity.ok(response);
      } else {
        return ResponseEntity.status(response.getStatusCode()).body("회원탈퇴 실패!");
      }
    } else {
      return ResponseEntity.status(response.getStatusCode()).body("회원탈퇴 ID를 확인해주세요 실패!");
    }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러 탈퇴 실패!");
    }


  }

}







