package com.ppvp.PaymentProject.nicePay.model;

import lombok.Data;

@Data
public class NicePayReadyAuthResult {
  private String authResultCode;
  private String authResultMsg;
  private String tid;
  private String clientId;
  private String orderId;
  private String amount;
  private String mallReserved;
  private String authToken;
  private String signature;
}
