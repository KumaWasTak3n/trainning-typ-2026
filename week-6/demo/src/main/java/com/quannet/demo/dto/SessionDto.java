package com.quannet.demo.dto;

import com.quannet.demo.model.Computer;
import com.quannet.demo.model.Session;
import com.quannet.demo.model.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class SessionDto {
    private Computer computer;
    private User user;
    private LocalDateTime s_t;
    private LocalDateTime e_t;

    @Builder.Default
    private Long totalTime = 0L;
    @Builder.Default
    private Double totalCost = 0.0;
    @Builder.Default
    private String status = "ACTIVE";

    public Session convertToModel(){
        return Session.builder().computer(computer).user(user).startTime(s_t).endTime(e_t).totalTime(totalTime).
                totalCost(totalCost).status(status).build();
    }
}
