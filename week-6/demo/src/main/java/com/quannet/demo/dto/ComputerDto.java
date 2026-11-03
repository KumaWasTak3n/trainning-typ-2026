package com.quannet.demo.dto;

import com.quannet.demo.model.Computer;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ComputerDto {
    private String area;
    private Double cost;
    @Builder.Default
    private String status = "AVAILABLE";
    public Computer convertToModel(){
        return Computer.builder().area(area).cost(cost).status(status).build();
    }
}
