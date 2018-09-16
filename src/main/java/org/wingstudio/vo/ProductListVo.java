package org.wingstudio.vo;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProductListVo {

    private Long id;
    private Integer categoryId;
    private String name;
    private String subTitle;
    private String mainImage;
    private Long price;
    private Byte status=1;
    private String imageHost;

}