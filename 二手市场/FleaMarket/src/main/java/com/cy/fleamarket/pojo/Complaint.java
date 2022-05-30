package com.cy.fleamarket.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Complaint {

    //投诉的商品id
    private int id;

    //投诉内容
    private String content;

    //投诉的商品名称
    private String name;

    //投诉单的编号
    @TableId(type = IdType.ASSIGN_ID)
    private int number;

    //助于前端标记
    private Boolean sign=false;

    public Complaint(int id, String content, String name) {
        this.id = id;
        this.content = content;
        this.name = name;
    }
}
