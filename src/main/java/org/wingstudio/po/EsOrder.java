package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
public class EsOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long orderNo;

    @ManyToOne
    private User user;

    @ManyToOne
    private Shipping shipping;

    private Long payment;

    @Column(columnDefinition = "tinyint")
    private Short paymentType;

    private Long postage;

    @Column(columnDefinition = "tinyint")
    private Short status;

    private Date paymentTime;

    private Date sendTime;

    private Date endTime;

    private Date closeTime;


    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;


}
