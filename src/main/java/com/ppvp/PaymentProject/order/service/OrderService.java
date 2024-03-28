package com.ppvp.PaymentProject.order.service;

import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import com.ppvp.PaymentProject.deliveryAddress.repository.DeliveryAddressRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.order.model.OrderDetails;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.order.model.Purchaser;
import com.ppvp.PaymentProject.order.repository.OrderDetailRepository;
import com.ppvp.PaymentProject.order.repository.OrderRepository;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

  private final KakaoLoginRepository kakaoLoginRepository;

  private final OrderRepository orderRepository;

  private final OrderDetailRepository orderDetailRepository;

  private final DeliveryAddressRepository deliveryAddressRepository;

  public ResponseEntity<?> createOrderSheet(Long userId, List<Map<String, String>> selectedItems) {

    Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);

    String uuid = UUID.randomUUID().toString();

    if (userOptional.isEmpty()) {
      return ResponseEntity.ok().body(Api.builder().resultCode("401 ").resultMessage("Unauthorized")
          .errorMessage("회원정보가 없습니다.").build());
    }

    Orders orders = orderRepository.save(Orders.builder()
        .orderId(uuid)
        .user(userOptional.get())
        .totalPaymentAmount(0)
        .totalProductQuantity(0)
        .deliveryCost(3000)
        .order_status("결제 준비 중")
        .build());


    log.info("orders1", orders);
    if (selectedItems == null) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("500").resultMessage("Internal Server Error")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("데이터 전달 실패")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("500").resultMessage("Internal Server Error")
          .errorMessage("잠시 후 다시 시도해주세요.").build());
    }

//    orderDetails.forEach(orderDetail ->{
//      orderDetail.getOrders().setTotalProductQuantity(orderDetail.getOrders().getTotalProductQuantity() + orderDetail.getProductQuantity());
//      orderDetail.getOrders().setTotalPaymentAmount(orderDetail.getOrders().getTotalPaymentAmount()+ orderDetail.getTotalPrice());
//    } );

    selectedItems.forEach(item -> {
      OrderDetails orderDetail = orderDetailRepository.save(
          OrderDetails.builder()
              .orders(orders)
              .productId(Long.parseLong(item.get("itemId")))
              .productQuantity(Integer.parseInt(item.get("itemCount")))
              .totalPrice(Integer.parseInt(item.get("itemCount")) * 2500)
              .build());
      // orderDetail을 사용하여 추가적인 작업을 수행할 수 있습니다.
      // 저장된 주문 상세 정보의 ID를 로그에 출력
      log.info("orderDetail : {}", orderDetail);

      orders.setTotalProductQuantity(orders.getTotalProductQuantity() + orderDetail.getProductQuantity());
      orders.setTotalPaymentAmount(orders.getTotalPaymentAmount() + orderDetail.getTotalPrice());
    });

    Orders savedOrders = orderRepository.save(orders);

    if (savedOrders == null) {
      return ResponseEntity.ok().body(Api.builder().resultCode("422").resultMessage("Unprocessable Entity")
          .errorMessage("상품 불러오기 중 오류.").build());
    }

    log.info("orders2 : {}", orders);
    List<OrderDetails> orderDetails = orderDetailRepository.findAllByOrders_OrderId(orders.getOrderId());
    log.info("orderDetails : {}", orderDetails);

    if (orderDetails.isEmpty()) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("404").data(null).resultMessage("Not Found")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("OrderDetails.isEmpty")).build()));
      return ResponseEntity.ok().body(Api.builder().resultCode("404").resultMessage("Not Found")
          .errorMessage("주문 상품이 없습니다.").build());
    }

    return ResponseEntity.ok().body(Api.builder().resultCode("200").resultMessage("Success").data(orders.getOrderId()).build());
  }

  public Api retrieveOrderDetail(Long userId, String ordersId) {
    Optional<User> userOptional = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);

    if (userOptional.isEmpty()) {
      return Api.builder().resultCode("401 ").resultMessage("Unauthorized")
          .errorMessage("회원정보가 없습니다.").build();
    }

    List<OrderDetails> orderDetails = orderDetailRepository.findAllByOrders_OrderId(ordersId);
    log.info("orderDetails : {}", orderDetails);

    Optional<Orders> ordersOptional = orderRepository.findOrdersByUser_IdAndOrderId(userId, ordersId);

    if (ordersOptional.isEmpty()) {
      return Api.builder().resultCode("404").resultMessage("Not Found")
          .errorMessage("주문정보가 없습니다.").build();
    }

    Orders orders = ordersOptional.get();
    log.info("orders : {}", orders);

    if (orderDetails.isEmpty()) {
//      return ResponseEntity.ok().body(Api.builder().resultCode("404").data(null).resultMessage("Not Found")
//          .error(Api.Error.builder().errorMessage(Collections.singletonList("OrderDetails.isEmpty")).build()));
      return Api.builder().resultCode("404").resultMessage("Not Found")
          .errorMessage("주문 상품이 없습니다.").build();
    }

    String phoneNumber = userOptional.get().getPhoneNumber();

    // 전화번호를 하이푼을 기준으로 나누기
    String[] parts = phoneNumber.split("-");

    // 마스킹된 전화번호 생성
    String maskedPhoneNumber = parts[0] + "-" + parts[1].substring(0, 1) + parts[1].substring(1).replaceAll("\\d", "*")
        + "-" + parts[2].substring(0, 1) + parts[2].substring(1).replaceAll("\\d", "*");


    String email = userOptional.get().getEmail();
    int atIndex = email.indexOf('@');
    int dotIndex = email.indexOf('.', atIndex + 1); // '@' 다음부터 '.' 문자의 인덱스 찾기
    String maskedEmail = email.substring(0, 2) + email.substring(2, atIndex).replaceAll("\\S", "*") + "@" +
        email.substring(atIndex + 1, atIndex + 2) + email.substring(atIndex + 2, dotIndex).replaceAll("\\S", "*") + email.substring(dotIndex);


    Optional<DeliveryAddress> deliveryAddress = deliveryAddressRepository.findDeliveryAddressByDefaultAddressIsTrueAndUser_Id(userId);
    if (deliveryAddress.isEmpty()) {
      Purchaser purchaser = Purchaser.builder()
          .purchaserName(userOptional.get().getName())
          .purchaserEmail(maskedEmail)
          .orderSheetId(ordersId)
          .orderDetailsList(orderDetails)
          .phoneNumber(maskedPhoneNumber)
          .sellerRegisterType("CART_ORDER")
          .build();
      return Api.builder().resultCode("206").resultMessage("Partial Content").data(purchaser)
          .errorMessage("배송지 정보가 없습니다.").build();
    }
    log.info("deliveryAddresses : {}", deliveryAddress.get());

    Purchaser purchaser = Purchaser.builder()
        .purchaserName(userOptional.get().getName())
        .purchaserEmail(maskedEmail)
        .orderSheetId(ordersId)
        .orderDetailsList(orderDetails)
        .deliveryAddress(deliveryAddress.get())
        .phoneNumber(maskedPhoneNumber)
        .sellerRegisterType("CART_ORDER")
        .build();


    return Api.builder().resultCode("200").resultMessage("Success").data(purchaser).build();
  }

}
