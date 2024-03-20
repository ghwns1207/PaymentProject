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
@Table(name = "product_image_details")
public class ProductImageDetails {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_image_id", referencedColumnName = "product_image_id", nullable = false)
  private ProductImage productImage;

  @Column(name = "product_detail_image_1" , nullable = false)
  private String product_detailImage_1;

  @Column(name = "product_detail_image_2", nullable = false)
  private String product_detailImage_2;

  @Column(name = "product_detail_image_3")
  private String product_detailImage_3;

  @Column(name = "product_detail_image_4")
  private String product_detailImage_4;

  @Column(name = "product_detail_image_5")
  private String product_detailImage_5;



}
