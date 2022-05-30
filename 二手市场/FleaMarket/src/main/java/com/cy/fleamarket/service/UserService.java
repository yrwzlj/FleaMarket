package com.cy.fleamarket.service;

import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public Boolean emailSend(String email,String contents) throws MessagingException {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.qq.com");

        mailSender.setUsername("2582085906@qq.com");

        mailSender.setPassword("wsmjyrjyucgfdjgi");

        mailSender.setPort(465);

        mailSender.setProtocol("smtps");

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("2582085906@qq.com");//发送的

        message.setTo(email);

        message.setSubject("【二手市场】邮箱验证登录");

        message.setText(contents);

        try {
            mailSender.send(message);
        }catch (MailException ignored){
            return false;
        }

        return true;
    }

}
