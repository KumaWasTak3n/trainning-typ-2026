package com.quannet.demo.controller;

import com.quannet.demo.dto.FoodDto;
import com.quannet.demo.model.Food;
import com.quannet.demo.repository.FoodRepository;
import com.quannet.demo.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/foods")

public class FoodController {
    @Autowired
    FoodRepository foodRepository;
    @Autowired
    FoodService foodService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllFood(){
        List<Food> foodList = foodRepository.findAll();
        return new ResponseEntity<>(foodList, HttpStatus.OK);
    }
    // nen phan trang?
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id){
        Optional<Food> food = foodRepository.findById(id);
        if(food.isPresent()) return new ResponseEntity<>(food, HttpStatus.OK);
        else return new ResponseEntity<>("Id Not Found!", HttpStatus.BAD_REQUEST);
    }
    @PostMapping("/add")
    public ResponseEntity<?> saveFood(@RequestBody FoodDto foodDto){
        foodService.save(foodDto.convertToModel());
        return new ResponseEntity<>("oke", HttpStatus.OK);
    }
    @PostMapping("/add-all")
    public ResponseEntity<?> saveAllFood(@RequestBody FoodDto[] foodDtos){
        for (FoodDto foodDto : foodDtos) {
            foodService.save(foodDto.convertToModel());
        }
        return new ResponseEntity<>("oke", HttpStatus.OK);
    }
    @PutMapping("/{name}")
    public ResponseEntity<?> updateFood(@PathVariable(name = "name") String name,
                                        @RequestParam(name = "cost", required = false) Double cost,
                                        @RequestParam(name = "quantity", required = false) Integer quantity){
        try{
            Food updatedFood = foodService.update(name, cost, quantity);
            return new ResponseEntity<>(updatedFood, HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }
    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteFood(@PathVariable(name = "name") String name){
        try {
            foodService.delete(name);
            return new ResponseEntity<>("Food deteled", HttpStatus.OK);
        }
        catch (RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
