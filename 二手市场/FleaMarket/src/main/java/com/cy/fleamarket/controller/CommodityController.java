package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.cy.fleamarket.util.ImgDecoder;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.TradeMapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.Commodity;
import com.cy.fleamarket.pojo.Trade;
import com.cy.fleamarket.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class CommodityController {

    @Autowired
    CommodityMapper commodityMapper;

    @Autowired
    TradeMapper tradeMapper;

    @Autowired
    UserMapper userMapper;

    //管理员查看所有商品
    @RequestMapping("/admin/allCommodity")
    @ResponseBody
    public List<Commodity> showAllCommodity(){

        List<Commodity> commodities = commodityMapper.selectList(null);

        //解码
        try {
           ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
                e.printStackTrace();
            }
        return commodities;

    }

    //全部商品
    @RequestMapping("/allCommodityOnSale")
    @ResponseBody
    public List<Commodity> showAllCommodityOnSale(){

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("status","正在出售");

        List<Commodity> commodities = commodityMapper.selectList(wrapper);

        //解码
        try {
            ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodities;

    }

    //搜索
    @RequestMapping("/selectCommodityByName")
    @ResponseBody
    public List<Commodity> selectCommodityByName(@RequestParam("name") String name){
        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("status","正在出售").like("name",name);

        List<Commodity> commodities = commodityMapper.selectList(wrapper);

        //解码
        try {
            ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodities;
    }

    //用户的商品搜索
    @RequestMapping("/user/selectByName")
    @ResponseBody
    public List<Commodity> userSelectCommodityByName(@RequestParam("name") String name){
        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.like("name",name);

        UserDetails userDetails=(UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        wrapper.eq("seller",userDetails.getUsername());

        List<Commodity> commodities = commodityMapper.selectList(wrapper);

        //解码
        try {
            ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodities;
    }


    //用户的所有商品
    @RequestMapping("/user/commodity")
    @ResponseBody
    public List<Commodity> usersCommodity(){

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        wrapper.eq("seller",userDetails.getUsername());

        List<Commodity> commodities = commodityMapper.selectList(wrapper);

        //解码
        try {
            ImgDecoder.Imgdecoder(commodities);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodities;

    }

    //单个商品详情，通过id查
    @RequestMapping("/commodityDetail")
    @ResponseBody
    public Commodity CommodityDetails(@RequestParam("id") int id){

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        byte[] image1 = (byte[])commodity.getPicture1();

        byte[] image2 = (byte[])commodity.getPicture2();

        byte[] image3 = (byte[])commodity.getPicture3();

        try {
            ImgDecoder.singleImgDecederMap(commodity,image1,image2,image3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        QueryWrapper<User> queryWrapper=new QueryWrapper<User>();

        queryWrapper.eq("phone",commodity.getSeller());

        User user=userMapper.selectOne(queryWrapper);

        commodity.setSeller(user.getName());

        return commodity;
    }


    //添加商品
    @PostMapping("/user/addCommodity")
    @ResponseBody
    public String addCommodity(@RequestParam("name")String name, @RequestParam("price")String price, @RequestParam("introduction")String introduction, @RequestParam("contact")String contact,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3){

        String image1 = new String();

        String image2 = new String();

        String image3 = new String();

        BASE64Encoder encoder = new BASE64Encoder();

        try {
            if (!file1.isEmpty()){
                image1 = encoder.encode(file1.getBytes());
            }
            if (!file2.isEmpty()){
                image2 = encoder.encode(file2.getBytes());
            }
            if (!file3.isEmpty()){
                image3 = encoder.encode(file3.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String seller =userDetails.getUsername();

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        simpleDateFormat.format(date);

        Commodity commodity = new Commodity(name,Double.parseDouble(price),seller,introduction,date,contact,image1,image2,image3,"审核中");

        commodityMapper.insert(commodity);

        return "true";
    }

    //编辑商品
    @PostMapping("/user/updateCommodity")
    @ResponseBody
    public void updateCommodity(@RequestParam("id") int id,@RequestParam("name")String name, @RequestParam("price")String price, @RequestParam("introduction")String introduction, @RequestParam("contact")String contact,@RequestParam("file1") MultipartFile file1,@RequestParam("file2") MultipartFile file2,@RequestParam("file3") MultipartFile file3){

        String image1 = new String();

        String image2 = new String();

        String image3 = new String();

        BASE64Encoder encoder = new BASE64Encoder();;

        try {
            if (!file1.isEmpty()){
                image1 = encoder.encode(file1.getBytes());
            }
            if (!file2.isEmpty()){
                image2 = encoder.encode(file2.getBytes());
            }
            if (!file3.isEmpty()){
                image3 = encoder.encode(file3.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        UserDetails userDetails=(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String seller =userDetails.getUsername();

        Date date = new Date();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        simpleDateFormat.format(date);

        Commodity commodity = new Commodity(name,Double.parseDouble(price),seller,introduction,date,contact,image1,image2,image3,"审核中");

        commodity.setId(id);

        commodityMapper.updateById(commodity);
    }


    //根据价格区间查
    @RequestMapping("/showByPrice")
    @ResponseBody
    public List<Commodity> selectCommodityByPrice(@RequestParam("begin") String begin,@RequestParam("end") String end){

        QueryWrapper<Commodity> queryWrapper = new QueryWrapper<>();

        queryWrapper.between("price",Double.parseDouble(begin),Double.parseDouble(end));

        queryWrapper.eq("status","正在出售");

        List<Commodity> commodities = commodityMapper.selectList(queryWrapper);

        ImgDecoder.Imgdecoder(commodities);

        return commodities;
    }

    //售出更改为正在出售
    @RequestMapping("/user/soldToSelling")
    @ResponseBody
    public void soldToSelling(@RequestParam("id") int id){

        QueryWrapper<Trade> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("id",id);

        tradeMapper.delete(queryWrapper);

        UpdateWrapper<Commodity> updateWrapper = new UpdateWrapper<>();

        updateWrapper.eq("id",id)
                .set("status","审核中");

        commodityMapper.update(null,updateWrapper);
    }
}
