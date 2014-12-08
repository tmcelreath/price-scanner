package com.com.intent.interview.scanner.terminal;

import com.com.intent.interview.scanner.order.OrderService;
import com.com.intent.interview.scanner.order.OrderServiceImpl;
import com.com.intent.interview.scanner.order.model.Order;

/**
 * A terminal for creating orders and scanning products. This service
 * communicates with the OrderService, which in turn communicates with
 * the CatalogService.
 */
public class TerminalServiceImpl implements TerminalService {

    private Order order;
    private OrderService orderService;
    private String terminalId;

    public TerminalServiceImpl() {
        orderService = new OrderServiceImpl();
        terminalId = this.toString();
    }

    public void createOrder() {
        order = orderService.createOrder(terminalId);
    }


    /**
     * Scans a single instance of a product into the current order, defaults to ScanAction.ADD
     * @param productCode
     */
    public void scan(String productCode) {
       scan(productCode, ScanAction.ADD);
    }


    /**
     * Scans a single instance of a product into the current order, along with an ADD/REMOVE flag
     * @param productCode
     */
    public void scan(String productCode, ScanAction action) {
        if(ScanAction.ADD.equals(action)) {
            orderService.addProduct(order, productCode);
        } else if (ScanAction.REMOVE.equals(action))
           orderService.removeProduct(order, productCode);
    }

    /**
     * Returns the complete order.
     * @return
     */
    public Order getOrder() {
        return order;
    }

    /**
     * Calculates the price total of all items, taking into account the product
     * pricing tiers.
     *
     * @return java.lang.Double
     */
    public Double total() {

        return orderService.total(order).total();
    }

    /**
     * Lock 'er down.
     * @return
     */
    public Order completeOrder() {

        return orderService.completeOrder(order);
    }

    /**
     * Removes all items from the order.
     */
    public void clear() {
        if(order != null) {
            order.clear();
        }
    }

}
