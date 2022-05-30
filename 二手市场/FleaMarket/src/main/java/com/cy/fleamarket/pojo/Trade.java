package com.cy.fleamarket.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Trade {
    //卖家
    private String seller;

    //买家
    private String buyer;

    //交易的商品id
    @TableId(type = IdType.ASSIGN_ID)
    private int id;

    //交易的时间
    private Date tradeTime;

    //相对于卖家交易的状态 售出/挂起
    private String sellstatus;
}
