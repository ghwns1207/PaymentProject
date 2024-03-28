package com.ppvp.PaymentProject.products.repository;

import com.ppvp.PaymentProject.products.model.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductOptionRepository extends JpaRepository<ProductOption , Long> {
}
