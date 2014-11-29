package com.com.intent.interview.scanner.order.model;

/**
 * The representation of a line item (single product) on an order.
 */
public class OrderRow {

    private String productCode;
    private Integer quantity;
    private Double subTotal;

    public OrderRow(String productCode) {
        this.productCode = productCode;
        this.quantity = 1;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public void increment() {
        this.quantity++;
    }

    public void decrement() {
        this.quantity--;
    }
}
