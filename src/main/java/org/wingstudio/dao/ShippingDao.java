package org.wingstudio.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.wingstudio.po.Shipping;
import org.wingstudio.po.User;

import javax.transaction.Transactional;

public interface ShippingDao extends JpaRepository<Shipping,Long> {
    @Transactional
    @Modifying
    @Query("delete from Shipping s where s.user=:user and s.id=:shippingId")
    int deleteByUserAndId(@Param("user") User user,@Param("shippingId") Long shippingId);
    Shipping findByUserAndId(User user,Long shippingId);
    Page<Shipping> findByUser(User user, Pageable pageable);
}
