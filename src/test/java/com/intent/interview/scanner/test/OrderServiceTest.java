package com.intent.interview.scanner.test;

import com.com.intent.interview.scanner.order.OrderService;
import com.com.intent.interview.scanner.order.OrderServiceImpl;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by tmcelreath on 11/28/14.
 */
public class OrderServiceTest {

    OrderService orderService;

    @Before
    public void setup() {
        orderService = new OrderServiceImpl();
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

}
