package it.unimib.sd2024.responses;

import java.util.List;

import it.unimib.sd2024.models.Order;

public class OrdersResponse {
    private String userId;
    private List<Order> orders;

    public OrdersResponse(String userId, List<Order> orders) {
        this.userId = userId;
        this.orders = orders;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
