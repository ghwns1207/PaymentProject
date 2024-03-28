package com.ppvp.PaymentProject.products.model;


import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOptionDto {

  @NotBlank
  private String product_id;

  @NotBlank
  private String option_name;

  @NotBlank
  private String option_detail_name;

  @NotBlank
  private String option_detail_cost;

}
