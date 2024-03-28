package com.ppvp.PaymentProject.kakaopay.service;

import com.ppvp.PaymentProject.payment.model.Payment;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.delivery.model.Delivery;
import com.ppvp.PaymentProject.delivery.repository.DeliveryRepository;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import com.ppvp.PaymentProject.deliveryAddress.repository.DeliveryAddressRepository;
import com.ppvp.PaymentProject.kakaopay.model.KakaoApproveResponse;
import com.ppvp.PaymentProject.kakaopay.model.KakaoReadyResponse;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelRequest;
import com.ppvp.PaymentProject.kakaopay.model.cancel.KakaoApprovedCancelResponse;
import com.ppvp.PaymentProject.order.model.OrderDetails;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.order.repository.OrderDetailRepository;
import com.ppvp.PaymentProject.order.repository.OrderRepository;
import com.ppvp.PaymentProject.payment.repository.PaymentRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class KakaoPayService {

  static final String cid = "TC0ONETIME"; // 가맹점 테스트 코드
//  static final String admin_Key = "${ADMIN_KEY}"; // 공개 조심! 본인 애플리케이션의 어드민 키를 넣어주세요

  // 카카오 페이 구버전 key
//  @Value("${kakao.admin-key}")
//  private String adminKey;

  @Value("${kakaoPay.Secret.keyDev}")
  private String secretKet;

  private KakaoReadyResponse kakaoReadyResponse;

//  private final KakaoLoginRepository kakaoLoginRepository;

  private final OrderRepository orderRepository;

  private final OrderDetailRepository orderDetailRepository;

  private final PaymentRepository paymentRepository;

  private final DeliveryAddressRepository deliveryAddressRepository;

  private final DeliveryRepository deliveryRepository;

  /**
   * 결제 준비 (요청)
   */
  public ResponseEntity<?> kakaoPayReady(Long user_id, String goduuid, HttpSession session) {

    log.info(goduuid);

//   Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(user_id);
//
//   if (userOptional.isEmpty()){
//     return ResponseEntity.ok().body(Api.builder().resultCode("404").resultMessage("Not Found")
//         .errorMessage("사용자 정보가 없습니다.").build());
//   }

    Optional<Orders> orders = orderRepository.findOrdersByUser_IdAndOrderId(user_id, goduuid);

    if (orders.isEmpty()) {
      return ResponseEntity.ok().body(Api.builder().resultCode("404").resultMessage("Not Found")
          .errorMessage("주문조회 중 오류 발생.").build());
    }

    List<OrderDetails> orderDetails = orderDetailRepository.findAllByOrders_OrderId(goduuid);
    String productName = orderDetails.get(0).getProductId().toString();

    String url = "https://open-api.kakaopay.com/online/v1/payment/ready";
    // 카카오페이 요청 양식
//    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", cid);
    parameters.put("partner_order_id", orders.get().getOrderId());
    parameters.put("partner_user_id", String.valueOf(user_id));
    parameters.put("item_name", productName + " 외 " + (orderDetails.size() - 1) + "건");
    parameters.put("quantity", String.valueOf(orders.get().getTotalProductQuantity()));
    parameters.put("total_amount", String.valueOf(orders.get().getTotalPaymentAmount() + orders.get().getDeliveryCost()));
    parameters.put("vat_amount", "2100");
    parameters.put("tax_free_amount", "0");
    parameters.put("approval_url", "http://localhost:8080/kakao-pay/success");
    parameters.put("cancel_url", "http://localhost:8080/kakao-pay/cancel");
    parameters.put("fail_url", "http://localhost:8080/kakao-pay/fail");

    session.setAttribute("partner_order_id", orders.get().getOrderId());
    session.setAttribute("partner_user_id", String.valueOf(user_id));

    try {
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
      RestTemplate restTemplate = new RestTemplate();
      kakaoReadyResponse = restTemplate.postForObject(url, requestEntity, KakaoReadyResponse.class);

      log.info("kakaoReadyResponse" + kakaoReadyResponse);

      // 여기에서 approveResponse를 사용하거나 반환하거나 처리합니다.
      Map<String, String> responseMap = new HashMap<>();
      responseMap.put("redirectUrl", kakaoReadyResponse.getNext_redirect_pc_url());
      return ResponseEntity.ok().body(responseMap);

    } catch (RestClientException e) {
      // 오류 처리
      e.printStackTrace();
      return ResponseEntity.status(500).body(Collections.singletonMap("error", "Failed to get KakaoPay URL"));
    }
  }

  /**
   * 결제 완료 승인
   */
  public Api approveResponse(String pgToken, HttpSession session) {

    // https://kapi.kakao.com/v1/payment/approve 구 버전

    // 신 버전
    String url = "https://open-api.kakaopay.com/online/v1/payment/approve";
    // 카카오 요청
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", cid);
    parameters.put("tid", kakaoReadyResponse.getTid());
    parameters.put("partner_order_id", session.getAttribute("partner_order_id").toString()); // 가맹점 주문 번호
    parameters.put("partner_user_id", session.getAttribute("partner_user_id").toString());  // 가맹점 회원 ID
    parameters.put("pg_token", pgToken);

    try {
      // 파라미터, 헤더
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());

      // 외부에 보낼 url
      RestTemplate restTemplate = new RestTemplate();
      KakaoApproveResponse approveResponse = restTemplate.postForObject(url, requestEntity, KakaoApproveResponse.class);
      log.info("approveResponse : {}", approveResponse);
      Optional<Orders> orders =
          orderRepository.findOrdersByUser_IdAndOrderId(Long.valueOf(session.getAttribute("partner_user_id").toString()),
              session.getAttribute("partner_order_id").toString());
      orders.get().setOrder_status("결제 완료");

      Payment payment = Payment.builder()
          .orders(orders.get())
          .tid(approveResponse.getTid())
          .amount_total(approveResponse.getAmount().getTotal())
          .amount_tax_free(approveResponse.getAmount().getTax_free())
          .amount_vat(approveResponse.getAmount().getVat())
          .approved_at(approveResponse.getApproved_at())
          .purchase_corp((approveResponse.getCard_info() == null ?
              approveResponse.getPayment_method_type() : approveResponse.getCard_info().getKakaopay_purchase_corp()))
          .payment_method_type(approveResponse.getPayment_method_type())
          .build();

      log.info("payment : {}", payment);

      if (paymentRepository.save(payment) == null) {
        // 결제 테이블 저장 중 오류 발생 시 결제 취소 처리
        log.info("실패");
        KakaoApprovedCancelRequest kakaoApprovedCancelRequest = KakaoApprovedCancelRequest.builder()
            .tid(approveResponse.getTid())
            .cid(approveResponse.getCid())
            .cancel_amount(approveResponse.getAmount().getTotal())
            .cancel_vat_amount(approveResponse.getAmount().getVat())
            .cancel_tax_free_amount(approveResponse.getAmount().getTax_free())
            .build();
        Api api = kakaoApprovedCancel(kakaoApprovedCancelRequest);
        api.setErrorMessage("결제 중 에러가 발생했습니다. 결제가 취소 되었습니다.");
        session.invalidate();
        return api;
      }

      Optional<DeliveryAddress> deliveryAddress =
          deliveryAddressRepository.findDeliveryAddressByIdAndUser_Id(Long.valueOf(session.getAttribute("delivery_address_id").toString()),
              Long.valueOf(session.getAttribute("partner_user_id").toString()));
      log.info("deliveryAddress: {}", deliveryAddress);

      Delivery delivery = Delivery.builder().deliveryAddressId(deliveryAddress.get())
          .deliveryStatus("CS_Unchecked").orders(orders.get()).build();
      log.info("delivery :{}", delivery);

      deliveryRepository.save(delivery);
      session.invalidate();
      return Api.builder().resultCode("200").resultMessage("Success").data(approveResponse).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Api.builder().resultMessage("500").resultMessage("서버 에러").errorMessage(e.getMessage()).build();
    }
  }

  /**
   * 결제 취소
   */
  public Api kakaoApprovedCancel(KakaoApprovedCancelRequest approvedCancelRequest) {

    // https://kapi.kakao.com/v1/payment/cancel 구 버전

    String reqURL = "https://open-api.kakaopay.com/online/v1/payment/cancel";

    // 카카오 요청
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", approvedCancelRequest.getCid());
    parameters.put("tid", approvedCancelRequest.getTid());
    parameters.put("cancel_amount", String.valueOf(approvedCancelRequest.getCancel_amount())); // 가맹점 주문 번호
    parameters.put("cancel_tax_free_amount", String.valueOf(approvedCancelRequest.getCancel_tax_free_amount()));  // 가맹점 회원 ID
    parameters.put("cancel_vat_amount", String.valueOf(approvedCancelRequest.getCancel_vat_amount()));

    try {
      // 파라미터, 헤더
      HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, this.getHeaders());
      // 외부에 보낼 url
      RestTemplate restTemplate = new RestTemplate();
      KakaoApprovedCancelResponse kakaoApprovedCancelResponse = restTemplate.postForObject(reqURL, requestEntity, KakaoApprovedCancelResponse.class);
      log.info("CancelResponse : {}", kakaoApprovedCancelResponse);

      if (kakaoApprovedCancelResponse == null) {
        return Api.builder().resultCode("400").resultMessage("Bad Request").errorMessage("결제 취소 실패").build();
      }

      Optional<Orders> orders = orderRepository.findOrdersByOrderId(kakaoApprovedCancelResponse.getPartner_order_id());

      deliveryRepository.deleteDeliveryByOrders_OrderId(kakaoApprovedCancelResponse.getPartner_order_id());
      paymentRepository.deletePaymentByOrders_OrderId(kakaoApprovedCancelResponse.getPartner_order_id());


      if (orders.isEmpty()) {
        return Api.builder().resultCode("400").resultMessage("Bad Reques").errorMessage("로그인 정보를 확인해주세요.").build();
      }

      orders.get().setOrder_status("결제 취소");

      orderRepository.save(orders.get());

      return Api.builder().resultCode("200").resultMessage("Success").data(kakaoApprovedCancelResponse).build();
    } catch (Exception e) {
      e.printStackTrace();
      return Api.builder().resultCode("400").resultMessage("Bad Request").errorMessage("결제 취소 실패").build();
    }
  }

  /**
   * 카카오 요구 헤더값
   */
  private HttpHeaders getHeaders() {
    HttpHeaders httpHeaders = new HttpHeaders();

    // DEV_SECRET_KEY 테스트 버전
    // SECRET_KEY 실 운영 버전
    String auth = "DEV_SECRET_KEY " + secretKet;

    httpHeaders.set("Authorization", auth);

    // 구버전
//    httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
    // 신 버전
    httpHeaders.set("Content-type", "application/json");
    return httpHeaders;
  }


}