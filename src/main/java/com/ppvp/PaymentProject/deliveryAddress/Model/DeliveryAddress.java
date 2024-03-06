package com.ppvp.PaymentProject.deliveryAddress.Model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery_address")
public class DeliveryAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // user 연동하는 컬럼
  private User user;


  @Column(name = "road_address")
  private String roadAddress;        // 도로명

  @Column(name = "extra_address")
  private String extraAddress;        // 참고항목

  @Column(name = "detail_address")
  private String detailAddress;        // 상세주소

  @Column(name = "postal_code")
  private String postalCode;      // 우편번호

  @Column(name = "shipping_name")
  private String shippingName;    // 배송지 명

  @Column(name = "recipient_name")
  private String recipientName;   // 받는 이

  @Column(name = "contact_number")
  private String contactNumber;   // 연락처

  @Column(name = "default_address")
  private boolean defaultAddress;   // 기본 배송지

}
