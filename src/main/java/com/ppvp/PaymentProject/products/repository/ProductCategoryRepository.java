package com.ppvp.PaymentProject.products.repository;

import com.ppvp.PaymentProject.products.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

  ProductCategory save(ProductCategory productCategory);



}
