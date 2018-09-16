package org.wingstudio.service;

import org.springframework.data.domain.Page;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.Shipping;
import org.wingstudio.po.User;

public interface IShippingService {
    ServerResponse add(User user, Shipping shipping);

    ServerResponse<String> del(User user, Long shippingId);

    ServerResponse<String> update(User user, Shipping shipping);

    ServerResponse<Shipping> select(User user, Long shippingId);

    ServerResponse<Page<Shipping>> select(User user, int pageNum, int pageSize);
}
