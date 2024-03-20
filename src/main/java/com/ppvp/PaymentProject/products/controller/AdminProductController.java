package com.ppvp.PaymentProject.products.controller;


import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/product")
public class AdminProductController {

  @GetMapping("/addCategory")
  public void addCategory(@RequestParam(name = "categoryName") String categoryName){

    log.info(categoryName);


  }




}
