package com.yangbin1.order_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.yangbin1.order_service.domain.ProductOrder;
import com.yangbin1.order_service.service.ProductClient;
import com.yangbin1.order_service.service.ProductOrderService;
import com.yangbin1.order_service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class ProductOrderServiceImpl implements ProductOrderService {

//    @Autowired
//    private RestTemplate restTemplate;

//    @Autowired
//    private LoadBalancerClient loadBalancerClient;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductClient productClient;

    @Override
    public ProductOrder save(int userId, int productId) {

//        Map<String,Object> productMap = restTemplate.getForObject("http://product-service/api/v1/product/find?id=" + productId, Map.class);

//        ServiceInstance instance = loadBalancerClient.choose("product-service");
//        String url = String.format("http://%s:%s/api/v1/product/find?id="+productId,instance.getHost(),instance.getPort());
//        RestTemplate restTemplate = new RestTemplate();
//
//        Map<String,Object> productMap = restTemplate.getForObject(url, Map.class);

        String response = productClient.findById(productId);

        logger.info("service save order");
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);


        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        return productOrder;
    }
}
