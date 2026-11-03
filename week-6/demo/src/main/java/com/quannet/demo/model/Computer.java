package com.quannet.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table (name = "computers")
public class Computer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String area;
    @Column(nullable = false)
    private Double cost;
    @Column(nullable = false)
    @Builder.Default
    private String status = "AVAILABLE";
}
