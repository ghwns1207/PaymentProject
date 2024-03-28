package com.ppvp.PaymentProject.products.controller;

import com.ppvp.PaymentProject.products.service.AdminProductCategoryService;
import com.ppvp.PaymentProject.utils.Api;
import com.ppvp.PaymentProject.utils.ApiResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminProductCategoryController {

  private final AdminProductCategoryService adminProductCategoryService;

/*  @PostMapping("/category/addCategory")
  public ResponseEntity<Api> addCategory(@RequestParam(name = "categoryName") String categoryName){
    if(categoryName.isEmpty()){
      return ResponseEntity.ok(
          ApiResponseUtil.failureResponse(HttpStatus.BAD_REQUEST, "입력 정보를 확인해주세요."));
    }

    Api api =  adminProductCategoryService.addCategory(categoryName);

    return ResponseEntity.ok(api);
  }

  @GetMapping("/category/retrieveCategory")
  public ResponseEntity<Api> retrieveCategory(){
    Api api = adminProductCategoryService.retrieveAllCategory();

    return ResponseEntity.ok(api);

  }*/

}
