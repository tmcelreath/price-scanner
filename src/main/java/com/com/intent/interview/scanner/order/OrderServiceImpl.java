package com.com.intent.interview.scanner.order;

import com.com.intent.interview.scanner.catalog.CatalogService;
import com.com.intent.interview.scanner.catalog.CatalogServiceImpl;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.PriceTierType;
import com.com.intent.interview.scanner.catalog.model.Product;
import com.com.intent.interview.scanner.order.model.*;

import java.util.List;
import java.util.Map;

/**
 * The business logic for processing an order. Includes
 */
public class OrderServiceImpl implements OrderService {

    private CatalogService catalogService = CatalogServiceImpl.getInstance();

    public Order createOrder(String terminalId) {
        return new Order(terminalId);
    }

    public Order addProduct(Order order, String productCode) {
        addOrderItem(productCode, order, OrderItemStatus.ADD);
        return order;
    }

    /**
     * Remove one instance of the product from the order. In other words, if the order
     * currently contains Product A with a quantity of 2, this method will reduce the
     * quantity to 1. If the quantity is reduced to 0, then the product will be removed from
     * the order.
     *
     * @param order
     * @param productCode
     * @return
     */
    public Order removeProduct(Order order, String productCode) {
        addOrderItem(productCode, order, OrderItemStatus.REMOVE);
        return order;
    }

    private void addOrderItem(String productCode, Order order, OrderItemStatus status) {
        if(OrderItemStatus.ADD.equals(status)) {
            order.add(productCode);
        } else if (OrderItemStatus.REMOVE.equals(status)) {
            order.remove(productCode);
        }
        //total(order, productCode);
    }

    /**
     * Recalculate the totals on all scanned items.
     *
     * @param order
     * @return
     */
    public Order total(Order order) {

        order.getContents().clear();

        // Apply the ADDs
        order.getOrderItems().stream()
                .filter(item -> OrderItemStatus.ADD.equals(item.getStatus()))
                .forEach(item -> {
                    if(order.getContents().containsKey(item.getProductCode())) {
                        order.getContents().get(item.getProductCode()).increment();
                    } else {
                        OrderRow row = new OrderRow(item.getProductCode());
                        order.getContents().put(item.getProductCode(), row);
                    }
                });

        // Apply the REMOVES
        order.getOrderItems().stream()
                .filter(item -> OrderItemStatus.REMOVE.equals(item.getStatus()))
                .forEach(item -> {
                    if(order.getContents().containsKey(item.getProductCode())) {
                        order.getContents().get(item.getProductCode()).decrement();
                        if(order.getContents().get(item.getProductCode()).getQuantity()<1) {
                            order.getContents().remove(item.getProductCode());
                        }
                    }
                });

        order.getContents().keySet().stream().forEach(productCode -> total(order, productCode));

        return order;
    }

    /**
     * Re-calculates the subtotal of a product on an order, taking into account the current
     * quantity and product Price Tiers.
     *
     * @param order
     * @param productCode
     * @return
     */
    private Order total(Order order, String productCode) {
        Map<String, OrderRow> contents = order.getContents();
        contents.values().stream()
                .filter(row -> row.getProductCode().equals(productCode))
                .forEach(row -> {
                    row.setSubTotal(catalogService.calculateSubtotal(row.getProductCode(), row.getQuantity()));
                });
        return order;
    }

    /**
     * Cancel the order. COWARD!
     *
     * @param order
     * @return
     */
    public Order cancelOrder(Order order) {
        order.setOrderStatus(OrderStatus.CANCELLED);
        // TODO: Persist order. Maybe I'll tackle this once that SWEET SWEET VC CASH comes pouring in.
        return order;
    }

    /**
     * Finish the order. GET PAID.
     *
     * @param order
     * @return
     */
    public Order completeOrder(Order order) {
        total(order);
        order.setOrderStatus(OrderStatus.COMPLETED);
        // TODO: Persist order. AS ABOVE.
        return order;
    }



}
