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
    public User convertToModel(){
        return User.builder().username(username).password(password).build();
    }
}
