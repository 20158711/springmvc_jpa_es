package org.wingstudio.service;

import org.springframework.data.domain.Page;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.vo.OrderVo;

public interface IOrderService {
    ServerResponse create(User user, Long shippingId);

    ServerResponse cancel(User user, Long orderNo);

    ServerResponse getOrderCartProduct(User user);

    ServerResponse<OrderVo> getOrderDetail(User user, Long orderNo);

    ServerResponse<Page<OrderVo>> getOrderList(User user, Integer pageNum, Integer pageSize);

    ServerResponse<Page<OrderVo>> manageList(Integer pageNum, Integer pageSize);

    ServerResponse<OrderVo> manageDetail(Long orderNo);

    ServerResponse<Page<OrderVo>> manageSearch(Long orderNo, Integer pageNum, Integer pageSize);

    ServerResponse<String> sendGoods(Long orderNo);
}
