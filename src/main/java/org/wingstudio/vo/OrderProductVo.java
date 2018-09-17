package org.wingstudio.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderProductVo {
    private List<OrderItemVo> orderItemVoList;
    private long productTotalPrice;
    private String imageHost;
}
