package com.ppvp.PaymentProject.order.model;

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
@Table(name = "order_details")
public class OrderDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_detail_id")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false) // UserRole과 연동하는 컬럼
  private Orders orders;

  @Column(name = "product_id", nullable = false)
  private Long productId;

  @Column(name = "product_quantity", nullable = false)
  private Integer productQuantity;        // 상품 수량

  @Column(name = "total_price", nullable = false)
  private Integer totalPrice;

}
