package com.intent.interview.scanner.test;

import com.com.intent.interview.scanner.catalog.CatalogService;
import com.com.intent.interview.scanner.catalog.CatalogServiceImpl;
import com.com.intent.interview.scanner.catalog.exception.InvalidPriceTierException;
import com.com.intent.interview.scanner.order.OrderService;
import com.com.intent.interview.scanner.order.OrderServiceImpl;
import com.com.intent.interview.scanner.order.model.Order;
import com.com.intent.interview.scanner.terminal.TerminalService;
import com.com.intent.interview.scanner.terminal.TerminalServiceImpl;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.PriceTierType;
import com.com.intent.interview.scanner.catalog.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TerminalServiceTest {

    TerminalService terminal;
    OrderService orderService;
    CatalogService catalog;

    @Before
    public void setUp() throws ProductNotFoundException, InvalidPriceTierException {

        terminal = new TerminalServiceImpl();
        orderService = new OrderServiceImpl();
        catalog = CatalogServiceImpl.getInstance();

        Product A = new Product();
        A.setProductCode("A");
        A.setProductName("PRODUCT A");
        A.setBasePrice(new Double(2.00));
        catalog.insertProduct(A);

        PriceTier a_tier_1 = new PriceTier();
        a_tier_1.setQuantity(4);
        a_tier_1.setPrice(new Double(7.00));
        a_tier_1.setType(PriceTierType.UNIT);
        catalog.insertPriceTier(A.getProductCode(), a_tier_1);

        Product B = new Product();
        B.setProductCode("B");
        B.setProductName("Product B");
        B.setBasePrice(new Double(12.00));
        catalog.insertProduct(B);

        Product C = new Product();
        C.setProductCode("C");
        C.setProductName("Product C");
        C.setBasePrice(new Double(1.25));
        catalog.insertProduct(C);

        PriceTier c_tier_1 = new PriceTier();
        c_tier_1.setType(PriceTierType.UNIT);
        c_tier_1.setQuantity(6);
        c_tier_1.setPrice(new Double(6.00));
        catalog.insertPriceTier(C.getProductCode(), c_tier_1);

        Product D = new Product();
        D.setProductCode("D");
        D.setProductName("Product D");
        D.setBasePrice(new Double(0.15));
        catalog.insertProduct(D);

        Product E = new Product();
        E.setProductCode("E");
        E.setProductName("Product E");
        E.setBasePrice(new Double(5));
        catalog.insertProduct(E);

        PriceTier e_tier_1 = new PriceTier();
        e_tier_1.setType(PriceTierType.THRESHOLD);
        e_tier_1.setQuantity(5);
        e_tier_1.setPrice(new Double(4.5));
        catalog.insertPriceTier(E.getProductCode(), e_tier_1);

        PriceTier e_tier_2 = new PriceTier();
        e_tier_2.setType(PriceTierType.THRESHOLD);
        e_tier_2.setQuantity(10);
        e_tier_2.setPrice(new Double(4));
        catalog.insertPriceTier(E.getProductCode(), e_tier_2);

    }

    /**
     * Scan these items in this order: ABCDABAA; Verify the total price is $32.40.
     */
    @Test
    public void testTotal1() {
        System.out.println("testTotal1: Scan these items in this order: ABCDABAA; Verify the total price is $32.40.");
        terminal.createOrder();
        terminal.scan("A");
        terminal.scan("B");
        terminal.scan("C");
        terminal.scan("D");
        terminal.scan("A");
        terminal.scan("B");
        terminal.scan("A");
        terminal.scan("A");
        Order order = terminal.completeOrder();
        assertEquals(new Double(32.40), order.total());
        order.print();
    }

    /**
     * Scan these items in this order: CCCCCCC; Verify the total price is $7.25.
     */
    @Test
    public void testTotal2() {
        System.out.println("testTotal2: Scan these items in this order: CCCCCCC; Verify the total price is $7.25.");
        terminal.createOrder();
        terminal.scan("C");
        terminal.scan("C");
        terminal.scan("C");
        terminal.scan("C");
        terminal.scan("C");
        terminal.scan("C");
        terminal.scan("C");
        Order order = terminal.completeOrder();
        assertEquals(new Double(7.25f), order.total());
        order.print();
    }

    /**
     * Scan these items in this order: ABCD; Verify the total price is $15.40.
     */
    @Test
    public void testTotal3() {
        System.out.println("testTotal3: Scan these items in this order: ABCD; Verify the total price is $15.40.");
        terminal.createOrder();
        terminal.scan("A");
        terminal.scan("B");
        terminal.scan("C");
        terminal.scan("D");
        Order order = terminal.completeOrder();
        assertEquals(new Double(15.40), order.total());
        order.print();

    }

    /**
     * Scan these items in this order EEEEEEEEEEE (11x); Verify that the cost is 22.50 after 5 scans,
     * 27 after 6 scans, 40.00 after 10 scans, and 44.00 after 11 scans.
     *
     * This is to test that the THRESHHOLD price type, which is a simple volume discount.
     */
    @Test
    public void testTotal4() {
        System.out.println("testTotal4: Scan these items in this order EEEEEEEEEEE (11x); Verify that the final cost in $44.00");
        terminal.createOrder();
        terminal.scan("E");
        terminal.scan("E");
        terminal.scan("E");
        terminal.scan("E");
        terminal.scan("E");
        assertEquals(new Double(22.5), terminal.total());
        terminal.scan("E");
        assertEquals(new Double(27), terminal.total());
        terminal.scan("E");
        terminal.scan("E");
        terminal.scan("E");
        terminal.scan("E");
        assertEquals(new Double(40), terminal.total());
        terminal.scan("E");
        Order order = terminal.completeOrder();
        assertEquals(new Double(44), order.total());
        order.print();
    }

    /**
     * Verify that the total is 0.0f after clearing the terminal
     */
    @Test
    public void testClear() {
        System.out.println("testClear: Verify that the total is 0.0f after clearing the terminal");
        terminal.createOrder();
        assertEquals(new Double(0), terminal.total());
        terminal.scan("A");
        assertNotEquals(new Double(0), terminal.total());
        terminal.clear();
        terminal.completeOrder();
        assertEquals(new Double(0), terminal.total());
        terminal.getOrder().print();
    }

    /**
     * Test removing an item
     */
    @Test
    public void testRemove() {
        terminal.createOrder();
        terminal.scan("A");
        assertEquals(new Double(2), terminal.total());
        terminal.removeItem("A");
        assertEquals(new Double(0), terminal.total());
        assertEquals(terminal.getOrder().getContents().size(), 0);
        terminal.completeOrder();
    }

    /**
     * Test calculating a UNIT price.
     * In this instance, a single item is $10, a set of 5 is $9
     */
    @Test
    public void testUnitPriceCalculation() {
        Double price = null;
        Double basePrice = new Double(10);
        Integer unitQantity = 5;
        Double unitPrice = new Double(45);

        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(1, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(10));
        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(4, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(40));
        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(5, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(45));
        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(6, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(55));
        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(10, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(90));
        price = ((OrderServiceImpl)orderService).calculateUnitTierPrice(12, basePrice, unitQantity, unitPrice);
        assertEquals(price, new Double(110));
    }


    @After
    public void tearDown() {
        catalog.deleteProduct("A");
        catalog.deleteProduct("B");
        catalog.deleteProduct("C");
        catalog.deleteProduct("D");
        catalog.deleteProduct("E");
    }

}

