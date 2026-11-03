package com.quannet.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Builder.Default
    @Column(nullable = false)
    private Double balance = 0.0;
    @Builder.Default
    @Column(nullable = false)
    private String tier = "NORMAL";
    @Builder.Default
    @Column(nullable = false)
    private String role = "USER";
}
