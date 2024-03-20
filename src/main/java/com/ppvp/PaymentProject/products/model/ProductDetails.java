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
@Table(name = "product_details")
public class ProductDetails {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false) // ProductCategory 연동하는 컬럼
  private Product product;

  @Column(name = "attribute_name" , nullable = false)
  private String attributeName;      // 상품 가격

  @Column(name = "attribute_value" , nullable = false)
  private String attributeValue;   // 상품 상세 설명

}
