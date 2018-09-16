package org.wingstudio.vo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CartProductVo {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String productSubtitle;
    private String productMainImage;
    private Integer quantity;//购物车中此商品的数量
    private Long productPrice;
    private Byte productStatus;
    private Long productTotalPrice;
    private Integer productStock;
    private Byte productChecked;//此商品是否勾选
    private String limitQuantity;//限制数量的一个返回结果


}
