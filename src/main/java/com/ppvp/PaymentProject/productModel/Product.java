package com.ppvp.PaymentProject.productModel;


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
@Table(name = "product")
public class Product {

  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productId;

  @Column(name = "product_name")
  private String productName;    // 상품명

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false) // ProductCategory 연동하는 컬럼
  private ProductCategory productCategory;   // 상품 카테고리

  @Column(name = "product_price")
  private Integer productPrice;      // 상품 가격

  @Column(name = "detail_description")
  private String detailDescription;   // 상품 상세 설명




}
