package com.ppvp.PaymentProject.products.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_image")
public class ProductImage {

  @Id
  @Column(name = "product_image_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long productImageId;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false) // ProductCategory 연동하는 컬럼
  private Product product;

  @Column(name = "image_description")
  private String imageDescription;     // 이미지 설명

  @Column(name = "created_by")
  private Date createdBy;     // 메인 이미지 2

}
