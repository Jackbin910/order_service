package com.yangbin1.order_service.fallback;

import com.yangbin1.order_service.service.ProductClient;
import org.springframework.stereotype.Component;

/**
 * 针对商品服务，降级处理
 */
@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public String findById(int id) {
        System.out.println("feign 调用product service异常");
        return null;
    }
}
