package org.wingstudio.service;

import org.springframework.data.domain.Page;
import org.wingstudio.common.ServerResponse;
import org.wingstudio.po.Product;
import org.wingstudio.vo.ProductDetailVo;
import org.wingstudio.vo.ProductListVo;

public interface IProductService {
    ServerResponse save(Product product, Integer categoryId);

    ServerResponse setSaleStatus(Long productId, Byte status);

    ServerResponse<ProductDetailVo> managerProductDetail(Long productId);

    ServerResponse<Page<ProductListVo>> getProductList(Integer pageNum, Integer pageSize);

    ServerResponse<Page<ProductListVo>> searchProductList(String productName, Long productId, Integer pageNum, Integer pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Long productId);

    ServerResponse<Page<ProductListVo>> searchProductDetail(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);
}
