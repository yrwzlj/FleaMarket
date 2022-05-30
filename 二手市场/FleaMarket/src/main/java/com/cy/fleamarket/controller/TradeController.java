package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.User;
import com.cy.fleamarket.util.ImgDecoder;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.TradeMapper;
import com.cy.fleamarket.pojo.Commodity;
import com.cy.fleamarket.pojo.Trade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TradeController {

    @Autowired
    TradeMapper tradeMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommodityMapper commodityMapper;

    //查看用户售出记录
    @RequestMapping("/user/soldCommodity")
    @ResponseBody
    public List<Map<Object, Object>> selectSoldTrade(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Map<Object, Object>> maps = null;
        try {
            maps = tradeMapper.selectSoldTradeByphone(userDetails.getUsername());

            for (Map<Object, Object> map : maps) {
                //JSON数据中double无法直接转String
                String price = String.valueOf(map.get("price"));

                map.replace("price", price);

                ImgDecoder.singleImgDecederMap(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;
    }

    //查看用户买入记录
    @RequestMapping("/user/buyCommodity")
    @ResponseBody
    public List<Map<Object, Object>> selectBuyTrade(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Map<Object, Object>> maps = null;
        try {
            maps = tradeMapper.selectBuyTradeByphone(userDetails.getUsername());

            for (Map<Object, Object> map : maps) {

                String price = String.valueOf(map.get("price"));

                map.replace("price", price);

                ImgDecoder.singleImgDecederMap(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return maps;
    }

    //查看交易记录中的商品
    @RequestMapping("/user/tradeCommodity")
    @ResponseBody
    public Map<Object, Object> selectTradeCommodity(@RequestParam("id") int id){
        Map<Object, Object> map = tradeMapper.selectTradeCommodity(id);

        try {
            String price = String.valueOf(map.get("price"));

            map.replace("price",price);

            ImgDecoder.singleImgDecederMap(map);

        } catch (Exception e) {
            e.printStackTrace();
        }

        Object seller=map.get("seller");

        QueryWrapper<User> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",seller.toString());

        User user=userMapper.selectOne(wrapper);

        map.replace("seller",user.getName());

        return map;
    }

    //将商品挂起
    @RequestMapping("/user/addHungTrade")
    @ResponseBody
    public void addHungTrade(@RequestParam("id") int id){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();

        QueryWrapper<Commodity> commodityQueryWrapper=new QueryWrapper<>();

        commodityQueryWrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(commodityQueryWrapper);

        updateWrapper.eq("id",id)
                .set("status","挂起");

        commodityMapper.update(null,updateWrapper);

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        simpleDateFormat.format(date);

        Trade trade = new Trade(commodity.getSeller(),userDetails.getUsername(),id,date,"挂起");

        tradeMapper.insert(trade);
    }

    //卖家私下交易
    @RequestMapping("/user/addTrade")
    @ResponseBody
    public void addTrade(@RequestParam("id") int id){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id",id)
                .set("status","售出");

        commodityMapper.update(null,updateWrapper);

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();

        userQueryWrapper.eq("phone",userDetails.getUsername());

        User user=userMapper.selectOne(userQueryWrapper);

        user.setIncome(user.getIncome()+commodity.getPrice());

        userMapper.updateById(user);

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        simpleDateFormat.format(date);

        Trade trade = new Trade(userDetails.getUsername(),"私下交易",id,date,"交易成功");

        tradeMapper.insert(trade);
    }

    //我出售的_挂起更改状态
    @RequestMapping("/user/hungToSold")
    @ResponseBody
    public void hungToSold(@RequestParam("id") int id){

        UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id",id)
                .set("status","售出");

        commodityMapper.update(null,updateWrapper);

        UpdateWrapper<Trade> tradeUpdateWrapper = new UpdateWrapper<>();

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        simpleDateFormat.format(date);

        tradeUpdateWrapper.eq("id",id)
                .set("sellstatus","交易成功")
                .set("trade_time",date);

        UserDetails userDetails=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();

        userQueryWrapper.eq("phone",commodity.getSeller());

        User user=userMapper.selectOne(userQueryWrapper);

        user.setIncome(user.getIncome()+commodity.getPrice());

        QueryWrapper<User> queryWrapper=new QueryWrapper<>();

        queryWrapper.eq("phone",userDetails.getUsername());

        User user2=userMapper.selectOne(queryWrapper);

        user.setConsumption(user.getConsumption()+commodity.getPrice());

        userMapper.updateById(user2);

        userMapper.updateById(user);

        tradeMapper.update(null,tradeUpdateWrapper);
    }


    //挂起改正在售出
    @RequestMapping("/user/hungToSell")
    @ResponseBody
    public void hungToSell(@RequestParam("id") int id){

        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("id",id);

        tradeMapper.delete(queryWrapper);

        UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id",id)
                .set("status","正在出售");

        commodityMapper.update(null,updateWrapper);
    }

    //获得买家名字
    @RequestMapping("/user/getBuyerName")
    @ResponseBody
    public Map<String,Object> getBuyerName(@RequestParam("id") int id){
        QueryWrapper<Trade> tradeQueryWrapper=new QueryWrapper<>();

        tradeQueryWrapper.eq("id",id);

        Trade trade=tradeMapper.selectOne(tradeQueryWrapper);

        QueryWrapper<User> userQueryWrapper=new QueryWrapper<>();

        userQueryWrapper.eq("phone",trade.getBuyer());

        User user=userMapper.selectOne(userQueryWrapper);

        Map<String,Object> map=new HashMap<>();

        map.put("buyer",user.getName());

        return map;
    }
}
