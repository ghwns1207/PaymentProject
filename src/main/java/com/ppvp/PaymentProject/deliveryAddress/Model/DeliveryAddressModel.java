package com.ppvp.PaymentProject.deliveryAddress.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressModel {

  private String postcode;
  private String roadAddress;
  private String detailAddress;
  private String extraAddress;
  private String recipient_name;
  private String contact_number;
  private boolean default_address;
  private String shipping_name;

}
