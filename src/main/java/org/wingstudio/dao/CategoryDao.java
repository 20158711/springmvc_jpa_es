package org.wingstudio.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.po.Category;

public interface CategoryDao extends JpaRepository<Category,Integer> {


}
