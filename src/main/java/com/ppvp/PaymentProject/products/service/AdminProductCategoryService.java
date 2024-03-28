package com.ppvp.PaymentProject.products.service;


import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.products.model.ProductCategory;
import com.ppvp.PaymentProject.products.repository.ProductCategoryRepository;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductCategoryService {

  private final ProductCategoryRepository productCategoryRepository;



  public Api addCategory(String categoryName) {

    try {
      ProductCategory category = ProductCategory.builder()
          .categoryName(categoryName)
          .createdBy("admin")
          .build();

      ProductCategory productCategory = productCategoryRepository.save(category);

      if (productCategory == null) {
        return ApiResponseUtil.failureResponse(HttpStatus.UNPROCESSABLE_ENTITY, "카테고리 저장을 실패했습니다.");
      }
      log.info("Category : {}", productCategory);
      return ApiResponseUtil.successResponse(HttpStatus.OK, "카테고리 저장을 성공했습니다.");

    } catch (Exception e) {
      e.printStackTrace();

      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 에러가 있습니다. 잠시 후 다시 시도해주세요.");
    }
  }

  public Api retrieveAllCategory() {
    try {
      List<ProductCategory> productCategories = productCategoryRepository.findProductCategoriesBy();

      if(productCategories.isEmpty()){
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "카테고리 조회 실패");
      }
      return ApiResponseUtil.successResponse(HttpStatus.OK, productCategories);

    }catch (Exception e){
      e.printStackTrace();
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 문제가 생겼습니다. 잠시 후 다시 시도해주세요.");
    }
  }

  public Optional<ProductCategory> retrieveCategory(Long categoryId){

    Optional<ProductCategory> productCategoryOptional =
        productCategoryRepository.findProductCategoriesByCategoryId(categoryId);
    return productCategoryOptional;

  }

}
