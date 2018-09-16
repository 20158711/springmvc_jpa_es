package org.wingstudio.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter @Entity
@DynamicInsert
@DynamicUpdate
public class User {

    public User(){}
    public User(Long id){this.id=id;}

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50,nullable = false,unique = true)
    private String username;
    @Column(length = 50,nullable = false)
    private String password;
    @Column(length = 50)
    private String email;
    @Column(length = 20)
    private String phone;
    @Column(length = 200)
    private String question;
    @Column(length = 100)
    private String answer;
    @Column(length = 4,nullable = false)
    private int role;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;

}
