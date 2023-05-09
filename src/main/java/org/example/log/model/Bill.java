package org.example.log.model;

import java.time.LocalDateTime;

public record Bill(
        long orderId,
        long clientId,
        double totalAmount,
        LocalDateTime timestamp) {
}