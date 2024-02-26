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
@Table(name = "product_image")
public class ProductImage {

  @Id
  @Column(name = "image_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long imageId;

  @OneToOne
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false) // ProductCategory 연동하는 컬럼
  private Product product;

  @Column(name = "image_1")
  private String image1;     // 메인 이미지 1

  @Column(name = "image_2")
  private String image2;     // 메인 이미지 2

  @Column(name = "image_3")
  private String image3;     // 메인 이미지 3

  @Column(name = "image_4")
  private String image4;     // 서브 이미지

  @Column(name = "image_5")
  private String image5;     // 서브 이미지

}
