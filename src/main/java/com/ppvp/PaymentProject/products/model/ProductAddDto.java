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
public class ProductAddDto {

  @NotBlank
  private String product_name;
  @NotBlank
  private String product_price;
  @NotBlank
  private String detail_description;
  @NotBlank
  private String product_category;
}
