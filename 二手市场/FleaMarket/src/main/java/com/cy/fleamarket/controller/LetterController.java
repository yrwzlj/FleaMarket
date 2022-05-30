package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.LetterMapper;
import com.cy.fleamarket.pojo.Letter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class LetterController {

    @Autowired
    LetterMapper letterMapper;

    @Autowired
    CommodityMapper commodityMapper;

    //查看所有私信
    @RequestMapping("/user/letter")
    @ResponseBody
    public List<Letter> showLetter(){
        QueryWrapper<Letter> wrapper=new QueryWrapper<>();

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        wrapper.eq("phone",userDetails.getUsername());

        return letterMapper.selectList(wrapper);
    }

    //私信详情
    @RequestMapping("/user/letter/detail")
    @ResponseBody
    public Letter letterDetail(@RequestParam("number") int number){
        QueryWrapper<Letter> wrapper=new QueryWrapper<>();

        wrapper.eq("number",number);

        Letter letter=letterMapper.selectOne(wrapper);

        letter.setRead(true);

        letterMapper.updateById(letter);

        return letter;
    }


    //是否有未读信息
    @RequestMapping("/user/getIsRead")
    @ResponseBody
    public Map<String,Object> getIsRead(){
        UserDetails userDetails=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<Letter> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",userDetails.getUsername());

        wrapper.eq("is_read",false);

        List<Letter> letterList=letterMapper.selectList(wrapper);

        Boolean isRead = letterList.isEmpty();

        Map<String,Object> map=new HashMap<>();

        map.put("isRead",isRead);

        return map;
    }

}
