package com.com.intent.interview.scanner.order.model;

import com.com.intent.interview.scanner.order.model.OrderRow;

import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;

public class Order {

    /**
     * We're US-only for now. I'll throw Internationalization into the Product Backlog
     * in the event that this thing goes GLOBAL.
     */
    public static final Locale CURRENT_LOCALE = Locale.US;
    private static final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance(CURRENT_LOCALE);

    /*
       It was at the point when I decided that we needed to record the terminal ID
       on the order that it became clear to me that I was over-thinking this
       exercise.
     */
    private String terminalId;

    /*
        OPEN, CANCELLED, CLOSED. The usual.
     */
    private OrderStatus orderStatus;

    /*
        The map of all of the products, along with the quantities and subtotals.
     */
    private Map<String, OrderRow> contents;

    private List<OrderItem> orderItems;

    public Order(String terminalId) {
        orderItems = new LinkedList<OrderItem>();
        contents = new HashMap<String, OrderRow>();
        this.terminalId = terminalId;
        this.orderStatus = orderStatus.OPEN;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Map<String, OrderRow> getContents() {
        return contents;
    }

    public List<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void add(String productCode) {

        OrderItem item = new OrderItem();
        item.setProductCode(productCode);
        item.setStatus(OrderItemStatus.ADD);
        item.setTimestamp(new Timestamp(System.currentTimeMillis()));
        orderItems.add(item);

        if(contents.containsKey(productCode)) {
            contents.get(productCode).increment();
        } else {
            contents.put(productCode, new OrderRow(productCode));
        }
    }

    public void remove(String productCode) {

        OrderItem item = new OrderItem();
        item.setProductCode(productCode);
        item.setStatus(OrderItemStatus.REMOVE);
        item.setTimestamp(new Timestamp(System.currentTimeMillis()));
        orderItems.add(item);

        if(contents.containsKey(productCode)) {
            contents.get(productCode).decrement();
            if(contents.get(productCode).getQuantity()<1) {
                contents.remove(productCode);
            }
        }
    }

    public void clear() {
        orderItems.clear();
        contents.clear();
    }

    public Double total() {
        Double total = new Double(0);
        for(OrderRow row: contents.values()) {
            total += row.getSubTotal() == null ? 0 : row.getSubTotal() ;
        }
        return total;
    }

    /**
     * Print out a receipt. Since I'm barred from using logging, here we are.
     */
    public void print() {
        System.out.println("\n--------------- RECEIPT --------------\n");
        this.contents.values().stream()
                .forEach(row -> {
                    System.out.println(
                            "  ITEM: " + row.getProductCode() + "\t"
                            + "QUANTITY: " + row.getQuantity() + "\t"
                            + "SUBTOTAL: " + CURRENCY_FORMATTER.format(row.getSubTotal()) + "\n"
                    );

                });
        System.out.println("  TOTAL: " + CURRENCY_FORMATTER.format(this.total()));
        System.out.println("  STATUS: " + this.getOrderStatus());
        System.out.println("\n-------------------------------------\n");
    }

}
