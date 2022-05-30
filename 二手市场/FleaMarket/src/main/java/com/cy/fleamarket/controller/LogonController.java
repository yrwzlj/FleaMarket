package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.User;
import com.cy.fleamarket.service.UserDetailsServiceImpl;
import com.cy.fleamarket.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin
@RestController
public class LogonController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    //注册处理
    @GetMapping("/logon")
    public Map<String, Object> logon(@RequestParam("phone")String phone, @RequestParam("password")String password, @RequestParam("name")String name){

        phone="user"+phone;

        Map<String,Object> map = new HashMap<>();

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",phone);

        User user=userMapper.selectOne(wrapper);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if(user!=null){

            map.put("msg",false);

            return map;
        }
        else{
            userMapper.insert(new User(phone,encoder.encode(password),0,0,100,name,null,0,false));
        }
        map.put("msg",true);

        return map;
    }

    //发送验证码
    @RequestMapping("/emailVF")
    public Map<String, Object> sendEmail(@RequestParam("email")String email){

        Map<String,Object> map = new HashMap<>();

        //生成随机验证码
        int i = (int) ((Math.random() * 9 + 1) * 100000);

        String contents="您正在注册账号，本次的验证码为："+i;

        try {
            UserService userService = new UserService();

            Boolean res=userService.emailSend(email,contents);

            map.put("msg",res);

            if(res)
                map.put("emailInfo",i);//把生成的验证码传递到前端进行判断

        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
