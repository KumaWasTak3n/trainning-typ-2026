package com.quannet.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequest {
    private String username;
    private Long computerId;
    private List<OrderItemRequest> items;
}
