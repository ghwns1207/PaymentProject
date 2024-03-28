package com.ppvp.PaymentProject.products.repository;

import com.ppvp.PaymentProject.products.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

  ProductCategory save(ProductCategory productCategory);

  Optional<ProductCategory> findProductCategoriesByCategoryId(Long category_id);
  List<ProductCategory> findProductCategoriesBy();


}
