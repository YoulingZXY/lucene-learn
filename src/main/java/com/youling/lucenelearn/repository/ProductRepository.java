package com.youling.lucenelearn.repository;

import com.youling.lucenelearn.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Integer> {
}
