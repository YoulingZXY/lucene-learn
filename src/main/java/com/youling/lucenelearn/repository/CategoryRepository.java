package com.youling.lucenelearn.repository;

import com.youling.lucenelearn.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
}
