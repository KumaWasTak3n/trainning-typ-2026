package com.quannet.demo.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity

@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "computer_id", nullable = false)
    private Long computerId;
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "full_cost", nullable = false)
    @Builder.Default
    private Double fullCost = 0.0;
    @Builder.Default
    private String status = "COMPLETED";
    // nho them PENDING, COMPLETED, CANCELLED
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();
}
