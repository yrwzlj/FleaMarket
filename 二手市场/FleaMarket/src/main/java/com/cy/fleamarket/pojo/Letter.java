package com.cy.fleamarket.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Letter {
    //电话
    private String phone;

    //内容
    private String content;

    //是否已读
    private boolean isRead;

    //私信编号
    @TableId(type = IdType.ASSIGN_ID)
    private int number;

    public Letter(String phone, String content, boolean isRead) {
        this.phone = phone;
        this.content = content;
        this.isRead = isRead;
    }
}
