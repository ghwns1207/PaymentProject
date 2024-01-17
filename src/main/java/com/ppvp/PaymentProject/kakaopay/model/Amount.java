package com.ppvp.PaymentProject.kakaopay.model;

import lombok.Data;

@Data
public class Amount {
    private int total;       // 총 결제 금액
    private int tax_free;    // 비과세 금액
    private int vat;         // 부가세 금액
    private int point;       // 포인트 금액
    private int discount;    // 할인 금액

}
