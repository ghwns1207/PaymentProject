package com.ppvp.PaymentProject.kakaopay.model.cancel;

import com.ppvp.PaymentProject.kakaopay.model.Amount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoApprovedCancelResponse {
  private String aid; // 요청 고유 번호
  private String tid; // 결제 고유번호, 20자
  private String cid; // 가맹점 코드, 10자
  private String status; // 결제 상태
  private String partner_order_id; // 가맹점 주문번호
  private String partner_user_id; // 가맹점 회원 id
  private String payment_method_type; // 결제 수단, CARD 또는 MONEY 중 하나
  private Amount amount; // 결제 금액
  private ApprovedCancelAmount approved_cancel_amount; // 이번 요청으로 취소된 금액
  private CanceledAmount canceled_amount; // 누계 취소 금액
  private CancelAvailableAmount cancel_available_amount; // 남은 취소 가능 금액
  private String item_name; // 상품 이름, 최대 100자
  private String item_code; // 상품 코드, 최대 100자
  private Integer quantity; // 상품 수량
  private LocalDateTime created_at; // 결제 준비 요청 시각
  private LocalDateTime approved_at; // 결제 승인 시각
  private LocalDateTime canceled_at; // 결제 취소 시각
  private String payload; // 취소 요청 시 전달한 값

  // 생성자, getter 및 setter 생략

  @Data
  class ApprovedCancelAmount {
    private Integer total; // 이번 요청으로 취소된 전체 결제 금액
    private Integer tax_free; // 이번 요청으로 취소된 비과세 금액
    private Integer vat; // 이번 요청으로 취소된 부가세 금액
    private Integer point; // 이번 요청으로 취소된 사용한 포인트 금액
    private Integer discount; // 이번 요청으로 취소된 할인 금액
    private Integer green_deposit; // 이번 요청으로 취소된 컵 보증금

    // 생성자, getter 및 setter 생략
  }

  @Data
  class CanceledAmount {
    private Integer total; // 취소된 전체 누적 금액
    private Integer tax_free; // 취소된 비과세 누적 금액
    private Integer vat; // 취소된 부가세 누적 금액
    private Integer point; // 취소된 포인트 누적 금액
    private Integer discount; // 취소된 할인 누적 금액
    private Integer green_deposit; // 취소된 누적 컵 보증금

    // 생성자, getter 및 setter 생략
  }

  @Data
  class CancelAvailableAmount {
    private Integer total; // 전체 취소 가능 금액
    private Integer tax_free; // 취소 가능한 비과세 금액
    private Integer vat; // 취소 가능한 부가세 금액
    private Integer point; // 취소 가능한 포인트 금액
    private Integer discount; // 취소 가능한 할인 금액
    private Integer green_deposit; // 취소 가능한 컵 보증금

    // 생성자, getter 및 setter 생략
  }

}