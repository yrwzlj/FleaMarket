package com.cy.fleamarket.pojo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    //电话
    @TableId(type = IdType.ASSIGN_ID)
    private String phone;

    //密码
    private String password;

    //消费
    private double consumption;

    //收入
    private double income;

    //诚信积分
    private int integrity;

    //用户名
    private String name;

    //封禁时间
    private Date banTime;

    //封禁次数
    private int banNumber;

    //标记
    private Boolean sign;

    //权限设置为user
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();

        GrantedAuthority grantedAuthority=new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return "ROLE_user";
            }
        };
        auths.add(grantedAuthority);
        return auths;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}