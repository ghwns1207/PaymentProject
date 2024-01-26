package com.ppvp.PaymentProject.kakaoLogin.model;
import lombok.Data;

@Data
public class KakaoProfile {

  private Long id;
  private String connected_at;
  private Properties properties;
  private KakaoAccount kakao_account;

  @Data
  public class Properties {

    private String nickname;
    public String profile_image;
    public String thumbnail_image;

  }

  @Data
  public class KakaoAccount {

    private Boolean profile_nickname_needs_agreement;
    private Profile profile;
    private Boolean has_email;
    private Boolean email_needs_agreement;
    public Boolean is_email_valid;
    public Boolean is_email_verified;
    public String email;

    @Data
    class Profile {

      private String nickname;
      public String thumbnail_image_url;
      public String profile_image_url;

    }
  }
}