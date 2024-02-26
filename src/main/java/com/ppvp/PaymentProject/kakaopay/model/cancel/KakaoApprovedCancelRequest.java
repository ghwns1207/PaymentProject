package com.ppvp.PaymentProject.kakaopay.model.cancel;

import lombok.Data;

@Data
public class KakaoApprovedCancelRequest {
  private String cid;
  private String tid;
  private Integer cancel_amount;
  private Integer cancel_tax_free_amount;
  private Integer cancel_vat_amount;
//  private int cancel_available_amount;
}
