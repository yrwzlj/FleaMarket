package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserMapper userMapper;


    //用户个人信息
    @RequestMapping("/user/userDetails")
    @ResponseBody
    public User userDetails(){

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        String phone=userDetails.getUsername();

        wrapper.eq("phone",phone);

        User user=userMapper.selectOne(wrapper);

        user.setPhone(user.getPhone().substring(4));

        return user;
    }

    //获得用户收入
    @RequestMapping("/user/getIncome")
    @ResponseBody
    public Map<String,Object> getIncome(){
        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        String phone=userDetails.getUsername();

        wrapper.eq("phone",phone);

        User user=userMapper.selectOne(wrapper);

        user.setPhone(user.getPhone().substring(4));

        Map<String,Object> map=new HashMap<>();

        map.put("income",user.getIncome());

        map.put("consumption",user.getConsumption());

        return map;
    }

}
