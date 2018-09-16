package org.wingstudio.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.po.Category;
import org.wingstudio.po.Product;

public interface ProductDao extends JpaRepository<Product,Long> {
    Page<Product> findAllByNameContainsAndCategory(String name, Category category, Pageable pageable);
    Page<Product> findAllByNameContainsAndCategoryAndStatus(String name, Category category, Byte status,Pageable pageable);
    Page<Product> findAllByNameContains(String name,Pageable pageable);
    Page<Product> findAllByNameContainsAndStatus(String name,Byte status,Pageable pageable);
}
