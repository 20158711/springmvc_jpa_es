package org.wingstudio.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class ProductDetailVo {

    private Long id;
    private Integer categoryId;
    private String name;
    private String subTitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private Long price;
    private Integer stock;
    private Byte status=1;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    private String imageHost;

    private Integer parentCategoryId;

}

