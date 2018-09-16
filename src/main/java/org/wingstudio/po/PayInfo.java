package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter @Setter @Entity
public class PayInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private Long orderNo;

    @Column(columnDefinition = "tinyint")
    private Short payPlatform;

    @Column(length = 200)
    private String platformNumber;

    @Column(length = 20)
    private String platformStatus;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;

}
