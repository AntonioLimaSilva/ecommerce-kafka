package br.com.luciano.ecommerce;

import java.math.BigDecimal;

public class Order {

    private final String id;
    private final BigDecimal amount;
    private final String email;

    public Order(String id, BigDecimal amount, String email) {
        this.id = id;
        this.amount = amount;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getEmail() {
        return email;
    }
}
