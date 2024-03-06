package com.ppvp.PaymentProject.deliveryAddress.service;

import com.ppvp.PaymentProject.Api;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddressModel;
import com.ppvp.PaymentProject.deliveryAddress.repository.DeliveryAddressRepository;
import com.ppvp.PaymentProject.kakaoLogin.repository.KakaoLoginRepository;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryAddressService {

  private final KakaoLoginRepository kakaoLoginRepository;

  private final DeliveryAddressRepository deliveryAddressRepository;

  public Api appendDeliveryAddress(Long userId, DeliveryAddressModel deliveryAddressModel) {

    try {
      Optional<User> user = kakaoLoginRepository.findByIdAndWithdrawnIsFalse(userId);
      if (user.isEmpty()){
        return Api.builder()
            .resultCode("401 ")
            .resultMessage("Unauthorized")
            .errorMessage("유저 정보를 찾을 수 없습니다.")
            .build();
      }

      log.info("deliveryAddressModel :{}" , deliveryAddressModel);

      log.info("boolean : {}", deliveryAddressModel.isDefault_address());

      Optional<DeliveryAddress> defaultDeliveryAddress = deliveryAddressRepository.findDeliveryAddressByDefaultAddressIsTrueAndUser_Id(userId);
      log.info(" defaultDeliveryAddress : {}", defaultDeliveryAddress);

      if(defaultDeliveryAddress.isPresent()){
        defaultDeliveryAddress.get().setDefaultAddress(false);
        log.info("check :{}",defaultDeliveryAddress);
        deliveryAddressRepository.save(defaultDeliveryAddress.get());
      }

      DeliveryAddress deliveryAddress = deliveryAddressRepository.save(DeliveryAddress.builder().user(user.get())
          .roadAddress(deliveryAddressModel.getRoadAddress())
          .detailAddress(deliveryAddressModel.getDetailAddress())
          .extraAddress(deliveryAddressModel.getExtraAddress())
          .postalCode(deliveryAddressModel.getPostcode())
          .contactNumber(deliveryAddressModel.getContact_number())
          .recipientName(deliveryAddressModel.getRecipient_name())
          .defaultAddress(deliveryAddressModel.isDefault_address())
          .build());

      if (deliveryAddress == null) {
        return Api.builder()
            .resultCode("409 ")
            .resultMessage("Conflict")
            .errorMessage("저장 실패")
            .build();
      }
      log.info("deliveryAddress : {}", deliveryAddress);
      return Api.builder()
          .resultCode("200")
          .resultMessage("Success")
          .data(defaultDeliveryAddress)
          .build();
    }catch (Exception e){
      log.error("error : {}", e);
      return Api.builder()
          .resultCode("400")
          .errorMessage(e.getMessage())
          .build();
    }

  }

  public Api retrieveDeliveryAddressList(Long userId){

    List<DeliveryAddress> deliveryAddresses = deliveryAddressRepository.findAllByUser_IdAndUser_WithdrawnIsFalse(userId);

    log.info("deliveryAddresses :{}", deliveryAddresses);

    if (deliveryAddresses.isEmpty()){
      return Api.builder()
          .resultCode("404")
          .resultMessage("Not Found")
          .errorMessage("배송지 주소가 없습니다.")
          .build();
    }else {
      return Api.builder()
          .resultCode("200")
          .resultMessage("Success")
          .data(deliveryAddresses)
          .build();
    }
  }


}


