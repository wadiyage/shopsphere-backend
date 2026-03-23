package edu.icet.shopsphere.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double totalAmount;

    @Enumerated(EnumType.STRING)
    private String status;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
}
