package org.wingstudio.service;

import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.User;
import org.wingstudio.vo.CartVo;

public interface ICartService {
    ServerResponse<CartVo> add(User user, Long productId, Integer count);

    ServerResponse<CartVo> update(Long id, Long productId, Integer count);

    ServerResponse<CartVo> deleteProduct(Long id, String productIds);

    ServerResponse<CartVo> list(Long id);

    ServerResponse<CartVo> selectOrUnSelectAll(Long id, byte checked);

    ServerResponse<CartVo> selectOrUnSelectProduct(Long id, Long productId, byte checked);

    ServerResponse<Long> getCartProductCount(Long id);
}
