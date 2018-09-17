package org.wingstudio.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wingstudio.po.OrderItem;

import java.util.List;

public interface OrderItemDao extends JpaRepository<OrderItem,Long> {
}
