package com.ppvp.PaymentProject.products.controller;


import com.ppvp.PaymentProject.products.model.Product;
import com.ppvp.PaymentProject.products.model.ProductAddDto;
import com.ppvp.PaymentProject.products.model.ProductAddImageDto;
import com.ppvp.PaymentProject.products.model.ProductOptionDto;
import com.ppvp.PaymentProject.products.service.AdminProductCategoryService;
import com.ppvp.PaymentProject.products.service.AdminProductOptionService;
import com.ppvp.PaymentProject.products.service.AdminProductService;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductController {

  private final AdminProductService adminProductService;

  private final AdminProductCategoryService adminProductCategoryService;

  private final AdminProductOptionService adminProductOptionService;

  /**
  * @RequestBody: HTTP 요청의 본문(body) JSON 또는 XML과 같은 형식의 데이터를 자바 객체로 변환
   * @ModelAttribute: HTTP 요청의 파라미터(parameter)
   *                  HTML 폼(form)에서 사용자가 입력한 데이터를 컨트롤러로 전달할 때 사용
  * */
  @PostMapping(path = "/image/addImage",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public void addProductImage(@ModelAttribute ProductAddImageDto productAddImageDto){

    log.info( "productAddImageDto : {}", productAddImageDto);
  }

  @PostMapping("/option/addOption")
  public ResponseEntity<Api> addProductOption(@Valid @RequestBody ProductOptionDto productOptionDto) {

    log.info("productOptionDto : {}", productOptionDto);

     Api api= adminProductOptionService.addOption(productOptionDto);

    return ResponseEntity.ok(api);
  }


  @PostMapping("/product/addProduct")
  public ResponseEntity<Api> addProduct(@Valid @RequestBody ProductAddDto productAddDto) {
    log.info("productAddDto :{}", productAddDto);
    Api api = adminProductService.addProduct(productAddDto);
//  if ("500".equals(api.getResultCode())) {
//   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
//  }
    return ResponseEntity.ok(api);
  }

  @GetMapping("/product/dividedRetrieveProduct")
  public ResponseEntity<Api> dividedRetrieveProduct(@RequestParam(name = "page", defaultValue = "0") int page,
                                                    @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
    int defaultSize = pageSize;
    Api api = adminProductService.dividedRetrieveProduct(page, defaultSize);
//  if ("500".equals(api.getResultCode())) {
//   return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
//  }
    return ResponseEntity.ok(api);
  }


  @GetMapping("/product/searchProductBy/{searchType}/{searchInfo}")
  public ResponseEntity<Api> searchProductBy(@PathVariable(name = "searchType") String searchType,
                                             @PathVariable(name = "searchInfo") String searchInfo) {
    Api api;
    if ("1".equals(searchType)) {
      api = adminProductService.searchByProductId(Long.valueOf(searchInfo));
    } else {
      api = adminProductService.searchByProductName(searchInfo);
    }
    if ("500".equals(api.getResultCode())) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(api);
    }
    return ResponseEntity.ok(api);
  }

  @PostMapping("/category/addCategory")
  public ResponseEntity<Api> addProductCategory(@RequestParam(name = "categoryName") String categoryName) {
    if (categoryName.isEmpty()) {
      return ResponseEntity.ok(
          ApiResponseUtil.failureResponse(HttpStatus.BAD_REQUEST, "입력 정보를 확인해주세요."));
    }

    Api api = adminProductCategoryService.addCategory(categoryName);

    return ResponseEntity.ok(api);
  }

  @GetMapping("/category/retrieveCategory")
  public ResponseEntity<Api> retrieveCategory() {
    Api api = adminProductCategoryService.retrieveAllCategory();

    return ResponseEntity.ok(api);
  }

}
