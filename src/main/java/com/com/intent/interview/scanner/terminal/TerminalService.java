package com.com.intent.interview.scanner.terminal;

import com.com.intent.interview.scanner.order.model.Order;

/**
 * Interface for the terminal. I've tried to keep this simple because customer
 * service workers have enough to deal with without having to navigate some
 * insanely complicated register, amirite?
 */
public interface TerminalService {

    public void createOrder();
    public void scan(String productCode);
    public void scan(String productCode, ScanAction action);
    public Order getOrder();
    public Double total();
    public void clear();
    public Order completeOrder();

}
