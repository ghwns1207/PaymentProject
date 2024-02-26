package com.ppvp.PaymentProject.productModel;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "detail_option")
public class DetailOption {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "option_id", referencedColumnName = "option_id", nullable = false)
  private ProductOption productOption;

  @Column(name = "detail_option_name")
  private String detailOptionName;      // 옵션 상세 이름

  @Column(name = "additional_cost")
  private Integer additionalCost;       // 추가 금액



}
