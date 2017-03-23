package com.demo.model;

public class Exchange {

    private String currency;
    private double rate;

    public Exchange(){}

    public Exchange(String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }


}
