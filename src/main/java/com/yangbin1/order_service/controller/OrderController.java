package com.yangbin1.order_service.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yangbin1.order_service.service.ProductOrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @RequestMapping("save")
    @HystrixCommand(fallbackMethod = "saveOrderFail")
    public Object save(@RequestParam("user_id") int userId, @RequestParam("product_id") int productId, HttpServletRequest request){
        Map<String,Object> data = new HashMap<>();
        data.put("code",0);
        data.put("data",productOrderService.save(userId,productId));
        return data;
    }

    //方法签名和api方法一致
    private Object saveOrderFail(int userId,int productId, HttpServletRequest request){

        String saveOrderKey = "save-order";
        //监控报警
        String sendValue = redisTemplate.opsForValue().get(saveOrderKey);
        String ip = request.getRemoteAddr();
        new Thread( ()->{
            if (StringUtils.isBlank(sendValue)){
                System.out.println("发送短信,ip地址"+ ip);
                redisTemplate.opsForValue().set("saveOrderKey","save-order-fail",20, TimeUnit.SECONDS);
            }else{
                System.out.println("已经发送过短信");
            }
        }).start();


        Map<String,Object> msg = new HashMap<>();
        msg.put("code",-1);
        msg.put("msg","抢购人数太多");
        return msg;
    }



}
