package com.ppvp.PaymentProject.cart.cartModel;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartModel {

  private String itemName;
  private String itemType;
  private String itemQuantity;

}
