package com.manav.ecommerce.miniapi.repository;

import com.manav.ecommerce.miniapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
