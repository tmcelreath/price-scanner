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


}
