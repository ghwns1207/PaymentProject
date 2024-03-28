package com.ppvp.PaymentProject.products.service;

import com.ppvp.PaymentProject.products.model.Product;
import com.ppvp.PaymentProject.products.model.ProductOption;
import com.ppvp.PaymentProject.products.model.ProductOptionDetails;
import com.ppvp.PaymentProject.products.model.ProductOptionDto;
import com.ppvp.PaymentProject.products.repository.ProductOptionDetailsRepository;
import com.ppvp.PaymentProject.products.repository.ProductOptionRepository;
import com.ppvp.PaymentProject.products.repository.ProductRepository;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductOptionService {

  private final ProductOptionRepository productOptionRepository;

  private final ProductOptionDetailsRepository productOptionDetailsRepository;

  private final ProductRepository productRepository;

  public Api addOption(ProductOptionDto productOptionDto) {

    try {
      Optional<Product> optionalProduct =
          productRepository.findProductByProductId(Long.valueOf(productOptionDto.getProduct_id()));
      if (optionalProduct.isEmpty()) {
        log.error("없다");
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "상품 코드가 틀렸습니다.");
      }

      ProductOption productOption = ProductOption.builder().product(optionalProduct.get())
          .optionName(productOptionDto.getOption_name()).created_by("Admin")
          .created_at(LocalDateTime.now()).build();

      ProductOption  saveProductOption= productOptionRepository.save(productOption);

      if(saveProductOption == null){
        log.error("옵션 저장 실패 ");
        return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "옵션 저장을 실패 했습니다. 잠시 후 다시 시도해주세요.");
      }

      ProductOptionDetails productOptionDetails = ProductOptionDetails.builder().productOption(saveProductOption)
          .detailOptionName(productOptionDto.getOption_detail_name())
          .additional_fee(Integer.valueOf(productOptionDto.getOption_detail_cost())).build();

      ProductOptionDetails saveOptionDetails = productOptionDetailsRepository.save(productOptionDetails);

      if(saveOptionDetails == null){
        log.error("옵션 디테일 저장 실패");
        return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "옵션 디테일 저장을 실패 했습니다. 잠시 후 다시 시도해주세요.");
      }

      return ApiResponseUtil.successResponse(HttpStatus.OK, "상품 옵션 저장을 성공했습니다.");

    } catch (Exception e) {
      e.printStackTrace();
      log.error(e.getMessage());
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 문제가 생겼습니다. 잠시 후 다시 시도해주세요.");
    }
  }


}
