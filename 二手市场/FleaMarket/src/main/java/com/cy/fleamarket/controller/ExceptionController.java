package com.cy.fleamarket.controller;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;

//异常页面的Controller
@RestController
public class ExceptionController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value=ERROR_PATH,produces = {MediaType.TEXT_HTML_VALUE})
    public String handleError(HttpServletRequest request){

        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");

        if(statusCode == 400){
            return "400";
        }else if(statusCode == 404){
            return "404";
        }else if(statusCode == 401){
            return "401";
        }else if(statusCode == 403){
            return "403";
        }else{
            return "500";
        }
    }

}
