package com.cy.fleamarket.service;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.mapper.AdminMapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.Admin;
import com.cy.fleamarket.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//实现接口UserDetailsService
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        UserDetails userDetails=null;

        //区分user与admin
        if(username.startsWith("user")){

            QueryWrapper<User> wrapper=new QueryWrapper<>();

            wrapper.eq("phone",username);

            userDetails=userMapper.selectOne(wrapper);

        }
        else if(username.startsWith("admin")){

            QueryWrapper<Admin> wrapper=new QueryWrapper<>();

            wrapper.eq("phone",username);

            userDetails= adminMapper.selectOne(wrapper);

        }

        return userDetails;
    }
}
