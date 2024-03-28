package com.ppvp.PaymentProject.products.service;


import com.ppvp.PaymentProject.products.model.Product;
import com.ppvp.PaymentProject.products.model.ProductAddDto;
import com.ppvp.PaymentProject.products.model.ProductCategory;
import com.ppvp.PaymentProject.products.model.ProductCategoryLink;
import com.ppvp.PaymentProject.products.repository.ProductCategoryLinkRepository;
import com.ppvp.PaymentProject.products.repository.ProductCategoryRepository;
import com.ppvp.PaymentProject.products.repository.ProductRepository;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductService {

  private final ProductRepository productRepository;

  private final AdminProductCategoryService adminProductCategoryService;

  private final AdminProductCategoryLinkService adminProductCategoryLinkService;
  public Api addProduct(ProductAddDto productAddDto){

    try {
      Optional<ProductCategory> productCategoryOptional=
          adminProductCategoryService.retrieveCategory(Long.valueOf(productAddDto.getProduct_category()));
      log.info("productCategory : {}", productCategoryOptional.get());

      if (productCategoryOptional.isEmpty()){
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "카테고리 정보를 확인해주세요.");
      }

      Product product = Product.builder()
          .productName(productAddDto.getProduct_name())
          .productPrice(Integer.valueOf(productAddDto.getProduct_price()))
          .detailDescription(productAddDto.getDetail_description())
          .created_by("admin")
          .created_at(LocalDateTime.now())
          .build();

     Product checkproduct = productRepository.save(product);
      log.info("product : {}", checkproduct);

      if(checkproduct == null){
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "상품 입력 정보를 확인해주세요.");
      }

      ProductCategoryLink productCategoryLink =
          adminProductCategoryLinkService.addProductCategoryLink(checkproduct, productCategoryOptional.get());
      log.info("productCategoryLink : {}", productCategoryLink);

      if(productCategoryLink == null){
        return ApiResponseUtil.failureResponse(HttpStatus.UNPROCESSABLE_ENTITY, "상품 저장을 실패했습니다.");
      }

      return ApiResponseUtil.successResponse(HttpStatus.OK, checkproduct);

    }catch (Exception e){
      e.printStackTrace();
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 문제가 있습니다. 잠시 후 다시 시도해주세요.");
    }
  }

  public Api dividedRetrieveProduct(int page , int pageSize) {
    try {
      List<Product> products = productRepository.findAll(PageRequest.of(page, pageSize)).getContent();

      if (products.isEmpty()){
       return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "상품 조회 정보가 없습니다.");
      }

      long totalProducts = countProduct();
      int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

      Api api =ApiResponseUtil.successResponse(HttpStatus.OK, products);
      api.setErrorMessage(""+totalPages +" " +page);
      return api;


    }catch (Exception e){
      e.printStackTrace();
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR,
          "서버에 문제가 생겼습니다. 잠시 후 다시 ");
    }

  }

  public Long countProduct() {
    return productRepository.count();
  }

  public Api searchByProductId(Long product_id){
    try {
      Optional<Product> optionalProduct = productRepository.findProductByProductId(product_id);
      if(optionalProduct.isEmpty()){
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "조회한 상품 정보가 없습니다.");
      }
      return ApiResponseUtil.successResponse(HttpStatus.OK, optionalProduct.get());
    }catch (Exception e){
      e.printStackTrace();
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage().toString());
    }
  }

  public Api searchByProductName(String product_name){
    try {
      List<Product> productList = productRepository.findAllByProductNameContaining(product_name);
      if(productList.isEmpty()){
        return ApiResponseUtil.failureResponse(HttpStatus.NOT_FOUND, "조회한 상품 정보가 없습니다.");
      }
      return ApiResponseUtil.successResponse(HttpStatus.OK, productList);
    }catch (Exception e){
      e.printStackTrace();
      return ApiResponseUtil.failureResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage().toString());
    }
  }

}
