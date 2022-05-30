package com.cy.fleamarket.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.mapper.CommodityMapper;
import com.cy.fleamarket.mapper.ComplaintMapper;
import com.cy.fleamarket.mapper.LetterMapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.Commodity;
import com.cy.fleamarket.pojo.Complaint;
import com.cy.fleamarket.pojo.Letter;
import com.cy.fleamarket.pojo.User;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ComplaintController {

    @Autowired
    ComplaintMapper complaintMapper;

    @Autowired
    LetterMapper letterMapper;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CommodityMapper commodityMapper;

    //用户添加投诉
    @ResponseBody
    @RequestMapping("/user/addComplaint")
    public void addComplain(@RequestParam("id") int id,@RequestParam("content") String content){

        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        complaintMapper.insert(new Complaint(id,content,commodity.getName()));
    }

    //查看所有投诉
    @ResponseBody
    @RequestMapping("/admin/complaint")
    public List<Complaint> allComplaint(){
        return complaintMapper.selectList(null);
    }


    //投诉警告处理
    @RequestMapping("/admin/complaint/warning")
    @ResponseBody
    public void warn(@RequestParam("id") int id,@RequestParam("number") int number){
        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        QueryWrapper<User> wrapper2=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        commodity.setStatus("违规下架");

        commodityMapper.updateById(commodity);

        letterMapper.insert(new Letter(commodity.getSeller(),"警告：经审核，您的商品\""+commodity.getName()+"\"存在严重违规内容，已下架，已扣除10分信用分，请及时修改。",false));

        wrapper2.eq("phone",commodity.getSeller());

        User user=userMapper.selectOne(wrapper2);

        user.setIntegrity(user.getIntegrity()-100);

        userMapper.updateById(user);

        QueryWrapper<Complaint> queryWrapper=new QueryWrapper<>();

        queryWrapper.eq("number",number);

        complaintMapper.delete(queryWrapper);

    }

    //投诉提示处理
    @RequestMapping("/admin/complaint/hint")
    @ResponseBody
    public void hint(@RequestParam("id") int id,@RequestParam("number") int number){
        QueryWrapper<Commodity> wrapper=new QueryWrapper<>();

        wrapper.eq("id",id);

        Commodity commodity=commodityMapper.selectOne(wrapper);

        letterMapper.insert(new Letter(commodity.getSeller(),"提示：您的商品\""+commodity.getName()+"\"存在违规内容，请及时修改。",false));

        QueryWrapper<Complaint> queryWrapper=new QueryWrapper<>();

        queryWrapper.eq("number",number);

        complaintMapper.delete(queryWrapper);

    }

    //批量删除投诉
    @RequestMapping("/admin/complaint/delete")
    @ResponseBody
    public void deleteComplaint(@RequestParam("arr") String arr){

        arr="["+arr.substring(1,arr.length()-1)+"]";

        JSONArray jsonArray=JSONArray.fromObject(arr);

        for (Object i : jsonArray) {
            QueryWrapper<Complaint> wrapper=new QueryWrapper<>();

            wrapper.eq("number",Integer.parseInt(i.toString()));

            complaintMapper.delete(wrapper);
        }
    }
}
