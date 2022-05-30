package com.cy.fleamarket.util;

import com.cy.fleamarket.pojo.Commodity;
import sun.misc.BASE64Decoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ImgDecoder {

    //多商品图片解码
    public static void Imgdecoder(List<Commodity> commodities){
        //解码
        try {
            for (Commodity commodity : commodities) {
                singleImgDecederMap(commodity, (byte[]) commodity.getPicture1(), (byte[]) commodity.getPicture2(), (byte[]) commodity.getPicture3());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //对商品的三张图解码
    public static void singleImgDecederMap(Commodity commodity, byte[] image1, byte[] image2, byte[] image3) throws  Exception{

        String value1 = new String(image1, StandardCharsets.UTF_8);

        String value2 = new String(image2, StandardCharsets.UTF_8);

        String value3 = new String(image3, StandardCharsets.UTF_8);

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] bytes1 = decoder.decodeBuffer(value1);

        byte[] bytes2 = decoder.decodeBuffer(value2);

        byte[] bytes3 = decoder.decodeBuffer(value3);

        for(int i=0;i<bytes1.length;i++){
            if(bytes1[i]<0){
                bytes1[i]+=256;
            }
        }

        commodity.setPicture1(bytes1);

        for(int i=0;i<bytes2.length;i++){
            if(bytes2[i]<0){
                bytes2[i]+=256;
            }
        }

        commodity.setPicture2(bytes2);

        for(int i=0;i<bytes3.length;i++){
            if(bytes3[i]<0){
                bytes3[i]+=256;
            }
        }

        commodity.setPicture3(bytes3);
    }

    public static void singleImgDecederMap(Map<Object,Object> map) throws  Exception{

        String value1 = new String((byte[]) map.get("picture1"), StandardCharsets.UTF_8);

        String value2 = new String((byte[]) map.get("picture2"), StandardCharsets.UTF_8);

        String value3 = new String((byte[]) map.get("picture3"),StandardCharsets.UTF_8);

        BASE64Decoder decoder = new BASE64Decoder();

        byte[] bytes1 = decoder.decodeBuffer(value1);

        byte[] bytes2 = decoder.decodeBuffer(value2);

        byte[] bytes3 = decoder.decodeBuffer(value3);

        for(int i=0;i<bytes1.length;i++){
            if(bytes1[i]<0){
                bytes1[i]+=256;
            }
        }
        map.replace("picture1",bytes1);
        for(int i=0;i<bytes2.length;i++){
            if(bytes2[i]<0){
                bytes2[i]+=256;
            }
        }
        map.replace("picture2",bytes2);
        for(int i=0;i<bytes3.length;i++){
            if(bytes3[i]<0){
                bytes3[i]+=256;
            }
        }
        map.replace("picture3",bytes3);
    }
}
