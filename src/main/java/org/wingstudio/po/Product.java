package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter @Getter @Entity
public class Product {

    public Product(){}
    public Product(Long id){this.id=id;}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    @Column(length = 100,nullable = false)
    private String name;

    @Column(length = 200)
    private String subTitle;

    @Column(length = 500)
    private String mainImage;

    @Lob
    private String subImages;

    @Lob
    private String detail;

    private Long price;

    @Column(columnDefinition = "tinyint default '1'")
    private Byte status;

    private Integer stock;


    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;

}
