package com.com.intent.interview.scanner.order.model;

import java.sql.Timestamp;

/**
 * Represents a single instance of a product that was either added to or removed from an order. Order items
 * are aggregated to an oder as a linked list, maintaining the scan history.
 */
public class OrderItem {

    private OrderItemStatus status;
    private String productCode;
    private Timestamp timestamp;

    public OrderItem(String productCode, OrderItemStatus status) {
        this.setStatus(status);
        this.setProductCode(productCode);
        this.setTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public OrderItemStatus getStatus() {
        return status;
    }

    public void setStatus(OrderItemStatus status) {
        this.status = status;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}


