package com.ppvp.PaymentProject.products.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductAddImageDto {

  private String image_name;

  private MultipartFile main_image;

  private MultipartFile sub_image1;

  private MultipartFile sub_image2;

  private MultipartFile sub_image3;

  private MultipartFile sub_image4;

}
