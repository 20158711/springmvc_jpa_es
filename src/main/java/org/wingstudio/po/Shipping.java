package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter @Getter @Entity
public class Shipping {

    public Shipping(){}
    public Shipping(Long id){
        this.id=id;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnore
    private User user;

    @Column(length = 20)
    private String receiverName;

    @Column(length = 20)
    private String receiverPhone;

    @Column(length = 20)
    private String receiverMobile;

    @Column(length = 20)
    private String receiverProvince;

    @Column(length = 20)
    private String receiverCity;

    @Column(length = 20)
    private String receiverDistrict;

    @Column(length = 200)
    private String receiverAddress;

    @Column(length = 6)
    private String receiverZip;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;

    public Shipping(Long id,String receiverName, String receiverPhone, String receiverMobile, String receiverProvince, String receiverCity, String receiverDistrict, String receiverAddress, String receiverZip) {
        this.id=id;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverMobile = receiverMobile;
        this.receiverProvince = receiverProvince;
        this.receiverCity = receiverCity;
        this.receiverDistrict = receiverDistrict;
        this.receiverAddress = receiverAddress;
        this.receiverZip = receiverZip;
    }
}
