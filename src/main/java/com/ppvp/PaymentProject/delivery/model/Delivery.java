package com.ppvp.PaymentProject.delivery.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddress;
import com.ppvp.PaymentProject.deliveryAddress.Model.DeliveryAddressModel;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "delivery")
public class Delivery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "delivery_id")
  private Long deliveryId;

  @OneToOne
  @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
  private Orders orders;

  @ManyToOne
  @JoinColumn(name = "delivery_address_id", referencedColumnName = "id", nullable = false)
  private DeliveryAddress deliveryAddressId;

  @Column(name = "invoice_number")
  private Integer invoiceNumber;

  @Column(name = "delivery_status")
  private String deliveryStatus;

  @Column(name = "delivery_company")
  private String deliveryCompany;

  @Column(name = "delivery_memo")
  private String deliveryMemo;

  @Column(name = "delivery_start_datetime")
  private LocalDateTime deliveryStartDatetime;

  @Column(name = "delivery_completion_datetime")
  private LocalDateTime deliveryCompletionDatetime;



}
