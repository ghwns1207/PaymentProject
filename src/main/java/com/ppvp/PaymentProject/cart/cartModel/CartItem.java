package com.ppvp.PaymentProject.cart.cartModel;

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
@Table(name = "cart_item")
public class CartItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cart_id", referencedColumnName = "id", nullable = false)
  private Cart cart;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

//  @OneToOne
//  @JoinColumn(name = "item_id", referencedColumnName = "product_id", nullable = false)
//  private Product product;

  @Column(name = "count", nullable = false)
  private Integer count;

}
