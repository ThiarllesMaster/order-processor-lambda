package com.example.orderprocessor;

import java.math.BigDecimal;

public record Order(
        String orderId,
        String customerId,
        String product,
        Integer quantity,
        BigDecimal total
) {
}
