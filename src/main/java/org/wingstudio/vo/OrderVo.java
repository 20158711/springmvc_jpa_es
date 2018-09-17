package org.wingstudio.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class OrderVo {

    private Long orderNo;

    private long payment;

    private Byte paymentType;

    private String paymentTypeDesc;
    private Long postage;

    private Byte status;


    private String statusDesc;

    private String paymentTime;

    private String sendTime;

    private String endTime;

    private String closeTime;

    private String createTime;

    //订单的明细
    private List<OrderItemVo> orderItemVoList;

    private String imageHost;
    private Long shippingId;
    private String receiverName;

    private ShippingVo shippingVo;
}
