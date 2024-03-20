package com.ppvp.PaymentProject.order.model;


import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import com.ppvp.PaymentProject.products.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import java.util.List;

@Data
@Builder
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class Purchaser {

  private String purchaserName;  // 주문 고객 이름
  private String purchaserEmail;   // 주문 고객 이메일
  private String phoneNumber;    // 주문 고객 전화번호

  private String orderSheetId;   // 주문 번호
  private String sellerRegisterType;  // 주문 타입

  private List<Product> productList;    //상품 목록

  private DeliveryAddress deliveryAddress;   // 배송지

  private List<OrderDetails> orderDetailsList;  // 주문상품 목록
  

}
