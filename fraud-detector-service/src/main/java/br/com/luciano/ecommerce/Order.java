package br.com.luciano.ecommerce;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Order {

    private String id;
    private BigDecimal amount;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", amount=" + amount +
                ", email='" + email + '\'' +
                '}';
    }
}
