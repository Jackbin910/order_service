package com.yangbin1.order_service.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yangbin1.order_service.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id") int userId,@RequestParam("product_id") int productId){
        Map<String,Object> data = new HashMap<>();
        data.put("code",0);
        data.put("data",productOrderService.save(userId,productId));
        return data;
    }

    //方法签名和api方法一致
    private Object saveOrderFail(int userId,int productId){
        Map<String,Object> msg = new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","抢购人数太多");
        return msg;
    }



}
