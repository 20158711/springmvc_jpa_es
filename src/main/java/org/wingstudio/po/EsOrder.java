package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@Entity
public class EsOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 订单号
     */
    @Column(unique = true)
    private Long orderNo;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToMany
    @JsonIgnore
    private List<OrderItem> orderItems;

    @ManyToOne
    private Shipping shipping;

    private Long payment;

    @Column(columnDefinition = "tinyint")
    private Byte paymentType;

    private Long postage;

    @Column(columnDefinition = "tinyint")
    private Byte status;

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
