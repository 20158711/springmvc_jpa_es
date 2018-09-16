package org.wingstudio.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CartVo {

    private List<CartProductVo> cartProductVoList;
    private Long cartTotalPrice;
    private Boolean allChecked;//是否已经都勾选
    private String imageHost;

}
