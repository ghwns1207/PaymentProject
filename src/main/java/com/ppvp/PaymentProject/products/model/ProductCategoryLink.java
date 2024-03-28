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
@Table(name = "product_category_link")
public class ProductCategoryLink {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id", referencedColumnName = "product_id", nullable = false)
  private Product product;

  @ManyToOne
  @JoinColumn(name = "category_id", referencedColumnName = "category_id", nullable = false)
  private ProductCategory  productCategory;

}
