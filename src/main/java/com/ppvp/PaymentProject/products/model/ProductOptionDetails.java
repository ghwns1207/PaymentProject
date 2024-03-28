package com.ppvp.PaymentProject.products.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_option_details")
public class ProductOptionDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "option_id", referencedColumnName = "option_id", nullable = false)
  private ProductOption productOption;

  @Column(name = "detail_option_name", nullable = false)
  private String detailOptionName;      // 옵션 상세 이름

  @Column(name = "additional_fee" ,nullable = false)
  private Integer additional_fee;       // 추가 금액

}
