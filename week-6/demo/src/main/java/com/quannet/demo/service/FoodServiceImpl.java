package com.quannet.demo.service;

import com.quannet.demo.model.Food;
import com.quannet.demo.repository.FoodRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FoodServiceImpl implements FoodService{
    @Autowired
    FoodRepository foodRepository;

    @Transactional
    @Override
    public void save(Food food){
        foodRepository.save(food);
    }

    @Transactional
    @Override
    public Food update(String name, Double cost, Integer quantity){
        Optional<Food> optionalFood = foodRepository.findByName(name);
        if(optionalFood.isEmpty()){
            throw new RuntimeException("Food " + name + " is not found!!!");
        }
        Food food = optionalFood.get();
        if(cost != null){
            food.setCost(cost);
        }
        if(quantity != null){
            food.setQuantity(quantity);
        }
        return foodRepository.save(food);
    }

    @Transactional
    @Override
    public void delete(String name){
        Optional<Food> optionalFood = foodRepository.findByName(name);
        if(optionalFood.isEmpty()){
            throw new RuntimeException("Food " + name + " is not found!!!");
        }
        Food food = optionalFood.get();
        foodRepository.delete(food);
    }

}
