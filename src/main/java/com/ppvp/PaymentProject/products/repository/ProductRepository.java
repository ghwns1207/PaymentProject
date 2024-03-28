package com.ppvp.PaymentProject.products.repository;

import com.ppvp.PaymentProject.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long > {
  Product save(Product product);

  Optional<Product>findProductByProductId(Long product_id);

  List<Product> findAllByProductNameContaining(String product_name);

}
