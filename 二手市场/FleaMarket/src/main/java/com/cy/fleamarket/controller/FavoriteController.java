package com.cy.fleamarket.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.util.ImgDecoder;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.FavoriteMapper;
import com.cy.fleamarket.pojo.Commodity;
import com.cy.fleamarket.pojo.Favorite;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FavoriteController {

    @Autowired
    FavoriteMapper favoriteMapper;

    @Autowired
    CommodityMapper commodityMapper;

    //收藏列表
    @RequestMapping("/user/favorite")
    @ResponseBody
    public List<Commodity> showFavorite(){

        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<Favorite> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",userDetails.getUsername());

        System.out.println(userDetails.getUsername());

        List<Favorite> favoriteList = favoriteMapper.selectList(wrapper);

        List<Commodity> commodityList = new ArrayList<>();

        for (Favorite favorite : favoriteList) {

            QueryWrapper<Commodity> queryWrapper=new QueryWrapper<>();

            queryWrapper.eq("id",favorite.getId());

            commodityList.add(commodityMapper.selectOne(queryWrapper));
        }
        try {
            ImgDecoder.Imgdecoder(commodityList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodityList;
    }

    //商品收藏
    @RequestMapping("/user/addFavorite")
    @ResponseBody
    public void addFavorite(@RequestParam("id") int id){

        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        favoriteMapper.insert(new Favorite(userDetails.getUsername(),id));

    }

    //是否收藏
    @RequestMapping("/user/isFavorite")
    @ResponseBody
    public Map<String,Object> isFavorite(@RequestParam("id") int id){
        Map<String,Object> map=new HashMap<>();

        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<Favorite> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",userDetails.getUsername());

        wrapper.eq("id",id);

        if(favoriteMapper.selectOne(wrapper)!=null){
            map.put("isFavorite",true);
        }else {
            map.put("isFavorite",false);
        }

        return map;
    }

    //取消收藏
    @RequestMapping("/user/cancelF")
    @ResponseBody
    public void cancelFavorite(@RequestParam("id") int id){

        QueryWrapper<Favorite> wrapper=new QueryWrapper<>();

        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        wrapper.eq("phone",userDetails.getUsername());

        wrapper.eq("id",id);

        favoriteMapper.delete(wrapper);
    }

    //批量取消收藏
    @RequestMapping("/user/cancelF/arr")
    @ResponseBody
    public void cancelFavoriteArr(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<Favorite> wrapper=new QueryWrapper<>();

            wrapper.eq("id",Integer.parseInt(i.toString()));

            UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            wrapper.eq("phone",userDetails.getUsername());

            favoriteMapper.delete(wrapper);
        }

    }

    //用户收藏的商品搜索
    @RequestMapping("/user/FSelectName")
    @ResponseBody
    public List<Commodity> userSelectCommodityByName(@RequestParam("name") String name){

        UserDetails userDetails =(UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        QueryWrapper<Favorite> wrapper=new QueryWrapper<>();

        wrapper.eq("phone",userDetails.getUsername());

        List<Favorite> favoriteList = favoriteMapper.selectList(wrapper);

        List<Commodity> commodityList = new ArrayList<>();

        for (Favorite favorite : favoriteList) {

            QueryWrapper<Commodity> queryWrapper=new QueryWrapper<>();

            queryWrapper.eq("id",favorite.getId());

            queryWrapper.like("name",name);

            Commodity commodity=commodityMapper.selectOne(queryWrapper);

            if(commodity!=null){
                commodityList.add(commodity);
            }
        }
        try {
            ImgDecoder.Imgdecoder(commodityList);
        }catch (Exception e){
            e.printStackTrace();
        }
        return commodityList;
    }
}
