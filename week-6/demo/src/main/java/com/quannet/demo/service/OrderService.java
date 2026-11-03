package com.quannet.demo.service;


import com.quannet.demo.dto.OrderRequest;
import com.quannet.demo.model.Order;

public interface OrderService {
    public Order placeOrder(OrderRequest request);
}
