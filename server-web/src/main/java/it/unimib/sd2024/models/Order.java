package it.unimib.sd2024.models;

import java.time.Instant;

public class Order {
    private Instant date;
    private String domain;
    private String userId;
    private Instant start;
    private Instant end;
    private float price;
    

    public Order(Instant date, String domain, String userId, Instant start, Instant end, float price) {
        this.date = date;
        this.domain = domain;
        this.userId = userId;
        this.start = start;
        this.end = end;
        this.price = price;
    }
    
    public Order() {}

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getStart() {
        return start;
    }

    public void setStart(Instant start) {
        this.start = start;
    }

    public Instant getEnd() {
        return end;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }
    
    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

