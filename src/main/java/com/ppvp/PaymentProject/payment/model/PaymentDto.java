package com.ppvp.PaymentProject.payment.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ppvp.PaymentProject.order.model.Orders;
import com.ppvp.PaymentProject.userModel.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "payment")
public class PaymentDto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long payment_id;


  @ManyToOne
  @JsonIgnore
  @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
  private Orders orders;

  @Column(name = "tid" , nullable = false)
  private String tid;

  @Column(name = "payment_method_type" , nullable = false)
  private String payment_method_type;

  @Column(name = "approved_at" , nullable = false)
  private String approved_at;

  @Column(name = "amount_total" , nullable = false)
  private Integer amount_total;

  @Column(name = "amount_tax_free" , nullable = false)
  private Integer amount_tax_free;

  @Column(name = "amount_vat" , nullable = false)
  private Integer amount_vat;

  @Column(name = "purchase_corp" , nullable = false)
  private String purchase_corp;

}
