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
@Table(name = "product_option")
public class ProductOption {

  @Id
  @Column(name = "option_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer optionId;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false) // ProductCategory 연동하는 컬럼
  private Product product;

  @Column(name = "option_name")
  private String optionName;      // 옵션 명

  @Column(name = "created_by" , nullable = false)
  private String created_by;

  @Column(name = "created_at",nullable = false ,columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime created_at;

}
