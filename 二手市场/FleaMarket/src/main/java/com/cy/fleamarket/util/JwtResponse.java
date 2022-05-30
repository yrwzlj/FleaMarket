package com.cy.fleamarket.util;

import lombok.Data;

import java.io.Serializable;

@Data
//响应的工具类
public class JwtResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String jwttoken;

    private Boolean isRead;

    private Boolean isBan;

    public JwtResponse(String jwttoken, Boolean isRead, Boolean isBan) {
        this.jwttoken = jwttoken;
        this.isRead = isRead;
        this.isBan = isBan;
    }
}

