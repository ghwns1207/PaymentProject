package com.ppvp.PaymentProject.products.service;


import com.ppvp.PaymentProject.products.model.Product;
import com.ppvp.PaymentProject.products.model.ProductCategory;
import com.ppvp.PaymentProject.products.model.ProductCategoryLink;
import com.ppvp.PaymentProject.products.repository.ProductCategoryLinkRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AdminProductCategoryLinkService {

  private final ProductCategoryLinkRepository productCategoryLinkRepository;

  public ProductCategoryLink addProductCategoryLink(Product product, ProductCategory productCategory){

    ProductCategoryLink productCategoryLink = ProductCategoryLink.builder()
        .product(product)
        .productCategory(productCategory)
        .build();

    return productCategoryLinkRepository.save(productCategoryLink);
  }

}
