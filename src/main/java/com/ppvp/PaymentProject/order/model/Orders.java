package com.ppvp.PaymentProject.order.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Data
@Entity
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Orders {

  @Id
  @Column(name = "order_id", nullable = false)
  private String orderId;

  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false) // UserRole과 연동하는 컬럼
  private User user;

  @Column(name = "order_status")
  private String order_status = "결제 준비 중";

  @Column(name = "delivery_cost", nullable = false)
  private Integer deliveryCost;                       // 배송비

  @Column(name = "total_product_quantity")
  private Integer totalProductQuantity;               // 총 상품 수량

  @Column(name = "total_payment_amount")
  private Integer totalPaymentAmount;                 // 총 금액


  // 주문 상세 정보를 계산하여 필드에 설정하는 메서드
//  public void calculateOrderDetails(List<OrderDetails> orderDetails) {
//    int totalQuantity = 0;
//    int totalAmount = 0;
//
//    // 주문에 속한 상품들의 수량과 가격을 계산하여 총 상품 수량과 총 결제 금액을 구함
//    for (OrderDetails orderDetail : orderDetails) {
//      totalQuantity += orderDetail.getProductQuantity();
//      totalAmount += orderDetail.getTotalPrice() * orderDetail.getProductQuantity();
//    }
//
//    // 계산된 값들을 필드에 설정
//    this.totalProductQuantity = totalQuantity;
//    this.totalPaymentAmount = totalAmount;
//  }


}
