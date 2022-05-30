package com.cy.fleamarket.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Commodity {

    //商品名
    private String name;

    //联系方式
    private String contact;

    //图1
    private Object picture1;

    //图2
    private Object picture2;

    //图3
    private Object picture3;

    //价格
    private double price;

    //卖家
    private String seller;

    //商品Id
    @TableId(type = IdType.ASSIGN_ID)
    private int id;

    //简介
    private String introduction;

    //上架日期
    private Date time;

    //商品状态
    private String status;

    private Boolean sign=false;


    //构造时不需要传入id
    public Commodity(String name, double price, String seller, String introduction, Date date, String contact,String picture1, String picture2, String picture3,String status) {
        this.name = name;
        this.price = price;
        this.seller = seller;
        this.introduction = introduction;
        this.time = date;
        this.contact=contact;
        this.picture1 = picture1;
        this.picture2 = picture2;
        this.picture3 = picture3;
        this.status=status;
    }
}
