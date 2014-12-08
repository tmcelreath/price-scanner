package com.com.intent.interview.scanner.order;

import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.order.model.Order;

public interface OrderService {

    public Order createOrder(String terminalId);

    public Order addProduct(Order order, String productCode);

    public Order removeProduct(Order order, String productCode);

    public Order total(Order order);

    public Order completeOrder(Order order);
}
