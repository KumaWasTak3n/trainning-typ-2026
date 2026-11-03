package com.quannet.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "foods")
public class Food {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false)
    private Double cost;
    @Column(nullable = false)
    @Builder.Default
    private Integer quantity = 0;
}
