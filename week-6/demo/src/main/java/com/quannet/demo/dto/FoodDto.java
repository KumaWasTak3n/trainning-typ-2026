package com.quannet.demo.dto;

import com.quannet.demo.model.Food;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class FoodDto {
    private String name;
    private Double cost;
    @Builder.Default
    private Integer quantity = 0;
    public Food convertToModel(){
        return Food.builder().name(name).cost(cost).quantity(quantity).build();
    }
}
