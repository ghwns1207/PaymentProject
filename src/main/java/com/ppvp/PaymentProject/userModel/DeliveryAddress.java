package com.ppvp.PaymentProject.userModel;


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

  @ManyToOne
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // user 연동하는 컬럼
  private User user;

  @Column(name = "address_line_1")
  private String address1;        // 도로명

  @Column(name = "address_line_2")
  private String address2;        // 상세주소

  @Column(name = "address_line_3")
  private String address3;        // 우편번호

  @Column(name = "shipping_name")
  private String shippingName;    // 배송지 명

  @Column(name = "recipient_name")
  private String recipientName;   // 받는 이

  @Column(name = "contact_number")
  private String contactNumber;   // 연락처

}
