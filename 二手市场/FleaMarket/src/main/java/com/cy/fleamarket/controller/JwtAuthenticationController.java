package com.cy.fleamarket.controller;

import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cy.fleamarket.util.JwtTokenUtil;
import com.cy.fleamarket.util.JwtRequest;
import com.cy.fleamarket.util.JwtResponse;
import com.cy.fleamarket.mapper.LetterMapper;
import com.cy.fleamarket.mapper.UserMapper;
import com.cy.fleamarket.pojo.Letter;
import com.cy.fleamarket.pojo.User;
import com.cy.fleamarket.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    LetterMapper letterMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserMapper userMapper;

    @Value("${jwt.header}")
    private String tokenHeader;

    @PostMapping("${jwt.route.authentication.path}")
    public ResponseEntity<?> createAuthenticationToken(@RequestParam String username,@RequestParam String password ) throws Exception {

        JwtRequest authenticationRequest=new JwtRequest(username,password);

        //验证
        String flag=authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        //验证通过
        if(flag.equals("true")){

            final String token = jwtTokenUtil.generateToken(userDetails);

            //判断是否有私信未读
            QueryWrapper<Letter> wrapper=new QueryWrapper<>();

            wrapper.eq("phone",userDetails.getUsername());

            wrapper.eq("is_read",false);

            List<Letter> letterList=letterMapper.selectList(wrapper);

            Boolean isRead = letterList.isEmpty();

            //查看是否封禁
            boolean isBan=false;

            QueryWrapper<User> queryWrapper=new QueryWrapper<>();

            queryWrapper.eq("phone",userDetails.getUsername());

            if(userDetails.getUsername().startsWith("user")){
                User user=userMapper.selectOne(queryWrapper);

                if(user.getBanNumber()>0){
                    Date date = new Date(System.currentTimeMillis());

                    double interval=(date.getTime() - user.getBanTime().getTime())/(3600*24*1000.0);

                    if(user.getBanNumber()>1000){

                        isBan=true;

                    } else if(interval<30){

                        isBan=true;

                    }
                }
            }
            return ResponseEntity.ok(new JwtResponse(token,isRead,isBan));
        }//失败
        else {
            return ResponseEntity.ok(flag);
        }
    }

    private String authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            return "true";

        } catch (DisabledException | BadCredentialsException e) {
            return "false";
        }
    }

    //前端若需要user可通过这里获取
    @GetMapping("/token")
    public User getAuthenticatedUser(HttpServletRequest request) {

        String token = request.getHeader(tokenHeader).substring(7);

        String username = jwtTokenUtil.getUsernameFromToken(token);

        return (User) userDetailsService.loadUserByUsername(username);
    }

}

