package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.util.ImgDecoder;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.LetterMapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.Commodity;
import com.cy.fleamarket.pojo.Letter;
import com.cy.fleamarket.pojo.User;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

@Controller
public class AuditController {

    @Autowired
    CommodityMapper commodityMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    LetterMapper letterMapper;

    //显示所有需要审核的商品
    @RequestMapping("/admin/commodity")
    @ResponseBody
    public List<Commodity> commodityNeedAudit(){
        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("status","审核中");

        List<Commodity> commodities = commodityMapper.selectList(wrapper);

        //解码
        try {
            ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodities;
    }

    //商品审核通过
    @GetMapping("/admin/commodity/pass")
    @ResponseBody
    public void commodityAuditPass(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

            wrapper.eq("id",Integer.parseInt(i.toString()));

            Commodity commodity=commodityMapper.selectOne(wrapper);

            commodity.setStatus("正在出售");

            commodityMapper.updateById(commodity);
        }
    }

    //商品审核不通过
    @RequestMapping("/admin/commodity/fail")
    @ResponseBody
    public void commodityAuditFail(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

            wrapper.eq("id",Integer.parseInt(i.toString()));

            Commodity commodity=commodityMapper.selectOne(wrapper);

            commodity.setStatus("审核未通过");

            letterMapper.insert(new Letter(commodity.getSeller(),"您要出售的商品\""+commodity.getName()+"\"未通过审核，请重新编辑后再次提交审核。",false));

            commodityMapper.updateById(commodity);
        }
    }

    //所有信誉分为0的用户
    @RequestMapping("/admin/auditUser")
    @ResponseBody
    public List<User> showUser(){
        QueryWrapper<User> wrapper=new QueryWrapper<>();

        wrapper.eq("integrity",0);

        List<User> list=userMapper.selectList(wrapper);

        for (User user : list) {
            user.setPhone(user.getPhone().substring(4));
        }

        return list;
    }

    //封禁账号30天
    @RequestMapping("/admin/banUser/temporary")
    @ResponseBody
    public void banUserTemporary(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<User> wrapper=new QueryWrapper<>();

            wrapper.eq("phone","user"+i.toString());

            User user=userMapper.selectOne(wrapper);

            user.setBanNumber(user.getBanNumber()+1);

            user.setIntegrity(100);

            user.setBanTime(new Date(System.currentTimeMillis()));

            userMapper.updateById(user);

        }
    }

    //封禁账号，永久
    @RequestMapping("/admin/banUser/permanent")
    @ResponseBody
    public void banUserPermanent(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<User> wrapper=new QueryWrapper<>();

            wrapper.eq("phone","user"+i.toString());

            User user=userMapper.selectOne(wrapper);

            user.setBanNumber(1001);

            user.setIntegrity(100);

            userMapper.updateById(user);

        }
    }
}
