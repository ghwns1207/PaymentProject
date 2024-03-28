package com.ppvp.PaymentProject.products.repository;


import com.ppvp.PaymentProject.products.model.ProductCategoryLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductCategoryLinkRepository extends JpaRepository<ProductCategoryLink , Long> {

  ProductCategoryLink save(ProductCategoryLink productCategoryLink);

}
