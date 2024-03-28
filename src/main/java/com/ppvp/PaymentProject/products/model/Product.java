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
@Table(name = "product")
public class Product {

  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Column(name = "product_name" , nullable = false)
  private String productName;    // 상품명


  @Column(name = "product_price", nullable = false)
  private Integer productPrice;      // 상품 가격

  @Column(name = "detail_description")
  private String detailDescription;   // 상품 상세 설명

  @Column(name = "created_by" , nullable = false)
  private String created_by;

  @Column(name = "created_at",nullable = false ,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime created_at;


}
