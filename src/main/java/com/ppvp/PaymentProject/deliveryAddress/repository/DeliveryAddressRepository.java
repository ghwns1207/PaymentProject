package com.ppvp.PaymentProject.deliveryAddress.repository;

import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryAddressRepository extends JpaRepository<DeliveryAddress, Long> {

  DeliveryAddress save(DeliveryAddress deliveryAddressDto);

  List<DeliveryAddress> findAllByUser_IdAndUser_WithdrawnIsFalse(Long userId);

  Optional<DeliveryAddress> findDeliveryAddressByDefaultAddressIsTrueAndUser_Id(Long userId);

  Integer deleteDeliveryAddressByIdAndUserId(Long addressId, Long userId);

  Optional<DeliveryAddress> findDeliveryAddressByIdAndUser_Id(Long addressId, Long userId);


}
