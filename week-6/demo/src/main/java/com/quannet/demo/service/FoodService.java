package com.quannet.demo.service;


import com.quannet.demo.model.Food;

public interface FoodService {
    public void save(Food food);
    public Food update(String name, Double cost, Integer quantity);
    public void delete(String name);
}
