package edu.icet.shopsphere.entity.enums;

public enum OrderStatus {
    PENDING, // Order created but not yet paid
    PAID, // Payment received, order is being processed
    PROCESSING, // Order is being prepared for shipment
    SHIPPED, // Order has been shipped to the customer
    DELIVERED, // Order has been delivered to the customer
    CANCELLED // Order has been cancelled by the customer or admin
}
