package org.wingstudio.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wingstudio.po.Cart;
import org.wingstudio.po.Product;
import org.wingstudio.po.User;

import javax.transaction.Transactional;
import java.util.List;

public interface CartDao extends JpaRepository<Cart,Long> {
    Cart findByUserAndProduct(User user, Product product);
    List<Cart> findByUser(User user);
    Integer countByUserAndChecked(User user,byte checked);
    @Transactional
    @Modifying
    @Query("delete from Cart c where c.product in (:products)")
    int deleteByIds(@Param("products") List<Product> products);
    @Transactional
    @Modifying
    @Query("update Cart c set c.checked=:checked where c.user=:user")
    int updateCheckedByUser(@Param("user")User user,@Param("checked")Byte checked);
    @Transactional
    @Modifying
    @Query("update Cart c set c.checked=:checked where c.user=:user and c.product=:product")
    int updateCheckByUserAndProduct(@Param("user")User user,@Param("product")Product product,@Param("checked")Byte checked);
}
