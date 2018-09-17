package org.wingstudio.vo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OrderItemVo {

    private Long orderNo;

    private Long productId;

    private String productName;
    private String productImage;

    private long currentUnitPrice;

    private Integer quantity;

    private long totalPrice;

    private String createTime;

}
