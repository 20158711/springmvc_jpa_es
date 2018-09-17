package org.wingstudio.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.po.EsOrder;
import org.wingstudio.po.User;

public interface OrderDao extends JpaRepository<EsOrder,Long> {
    EsOrder findByUserAndOrderNo(User user,Long orderNo);
    Page<EsOrder> findByUser(User user, Pageable pageable);
    EsOrder findByOrderNo(Long orderNo);
    Page<EsOrder> findByOrderNo(Long orderNo,Pageable pageable);
}
