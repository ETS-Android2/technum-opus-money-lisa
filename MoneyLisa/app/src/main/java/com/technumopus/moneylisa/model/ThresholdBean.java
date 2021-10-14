package com.technumopus.moneylisa.model;

public class ThresholdBean {
    private String currency;
    private float amount;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ThresholdBean{" +
                "currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}
