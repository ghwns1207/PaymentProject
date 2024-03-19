package com.ppvp.PaymentProject.kakaoLogin.controller;


import com.ppvp.PaymentProject.kakaoLogin.service.KakaoLoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/rest-api/kakao")
public class kakaoRestController {


  @Value("${jwt.accessToken.secretKey}")
  private String jwtSecretKey;

  private final KakaoLoginService kakaoLoginService;


  /*
  * fecth 비동기
  * 토큰 로그아웃
  * */
  @GetMapping("/rlogout")
  public ResponseEntity<?> kakaoLogout(@RequestHeader("Authorization") String authorizationHeader
      , HttpServletRequest request ) {

    try {

      String jwtToken = authorizationHeader.substring(7); // "Bearer " 이후의 문자열 추출


      Claims claims = Jwts.parser()
          .setSigningKey(jwtSecretKey)
          .parseClaimsJws(jwtToken)
          .getBody();
      log.info("claims : {}", claims.getSubject());
      ResponseEntity response = kakaoLoginService.kakaoLogout(claims.getSubject());
//    ResponseEntity response = kakaoLoginService.kakaoOauthLogout();
      if (response.getStatusCode().is2xxSuccessful() ){
        log.info("response :{}", response);
        log.info("session : {}", request.getSession() );
        // 세션 무효화
        request.getSession().invalidate();
        return ResponseEntity.ok(response);
      }else{
        log.info("status :{}", response.getStatusCode());
      }
      log.info("test : {}" , response);
//    return "redirect:index";
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response.getStatusCode());
    }catch (Exception e){
      log.error(e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
  }



  /*
  * 카카오 회원탈퇴
  *
  * */
  @GetMapping("/unlink")
  public ResponseEntity<?> kakaoUnlink(@RequestHeader("Authorization") String authorizationHeader,
                                       HttpServletRequest request ) throws Exception {

    String jwtToken = authorizationHeader.substring(7); // "Bearer " 이후의 문자열 추출

    Claims claims = Jwts.parser()
        .setSigningKey(jwtSecretKey)
        .parseClaimsJws(jwtToken)
        .getBody();

    ResponseEntity<?> response = kakaoLoginService.kakaoUnlink(claims.getSubject());
    request.getSession().invalidate();
    return response;
  }

  /*
  * 비동기 통신 크로스 오리진 error
  *
  *
  * */
  @GetMapping("/Oathlogout")
  public ResponseEntity<?> kakaoOathLogout(HttpServletRequest request) {

    request.getSession().invalidate();
    ResponseEntity response = kakaoLoginService.kakaoOauthLogout();

    return ResponseEntity.ok(response);


  }

}



