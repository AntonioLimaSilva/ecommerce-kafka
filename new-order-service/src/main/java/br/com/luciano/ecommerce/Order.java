package br.com.luciano.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String id;
    private final String userId;
    private final BigDecimal amount;

    public Order(String id, String userId, BigDecimal amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
