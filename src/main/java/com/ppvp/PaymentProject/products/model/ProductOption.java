package com.ppvp.PaymentProject.products.model;


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

}
