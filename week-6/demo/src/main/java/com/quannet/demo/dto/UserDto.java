package com.quannet.demo.dto;

import com.quannet.demo.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class UserDto {
    private String username;
    private String password;
    @Builder.Default
    private Double balance = 0.0;
    @Builder.Default
    private String tier = "NORMAL";
    public User convertToModel(){
        return User.builder().username(username).password(password)
                .balance(balance).tier(tier).build();
    }
}
