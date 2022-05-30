package com.cy.fleamarket.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

//收藏
@Data
@NoArgsConstructor
public class Favorite {

    //收藏的人的电话
    private String phone;

    //收藏的商品id
    private int id;

    private Boolean sign=false;

    public Favorite(String phone, int id) {
        this.phone = phone;
        this.id = id;
    }
}
