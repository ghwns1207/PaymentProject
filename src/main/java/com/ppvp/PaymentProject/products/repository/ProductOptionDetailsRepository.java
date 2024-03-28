package com.ppvp.PaymentProject.products.repository;

import com.ppvp.PaymentProject.products.model.ProductOptionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionDetailsRepository extends JpaRepository<ProductOptionDetails, Long> {




}
