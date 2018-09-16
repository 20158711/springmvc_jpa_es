package org.wingstudio.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Setter @Getter @Entity
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Category {

    public Category(Integer id){
        this.id=id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonIgnore
    @ManyToOne
    private Category parent;

    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private Set<Category> children=new HashSet<>();

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<Product> products=new HashSet<>();

    @Column(length = 50,unique = true)
    private String name;

//    @Column(columnDefinition = "tinyint default 1")
    private boolean status;

    @Column(columnDefinition = "tinyint")
    private short orders;


    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp")
    private Date createTime;

    @Temporal(value = TemporalType.TIMESTAMP)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(columnDefinition = "timestamp default current_timestamp on update current_timestamp")
    private Date updateTime;


}
