package com.intent.interview.scanner.test;

import com.com.intent.interview.scanner.catalog.CatalogService;
import com.com.intent.interview.scanner.catalog.CatalogServiceImpl;
import com.com.intent.interview.scanner.catalog.exception.InvalidPriceTierException;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.PriceTierType;
import com.com.intent.interview.scanner.catalog.model.Product;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CatalogServiceTest {

    private final CatalogService catalog = CatalogServiceImpl.getInstance();

    @Before
    public void setUp() {

    }


    @Test
    public void testInsertProduct() throws ProductNotFoundException {
        Product product = new Product();
        Long ts = System.currentTimeMillis();
        product.setProductName("PRODUCT NAME" + ts);
        product.setProductCode("" + ts);
        product.setBasePrice(new Double(1.5));
        catalog.insertProduct(product);
        Product persistedProduct = catalog.getProductByProductCode(product.getProductCode());
        assertEquals(product.getBasePrice(), persistedProduct.getBasePrice());
        assertEquals(product.getProductName(), persistedProduct.getProductName());
        assertEquals(product.getPriceTiers().size(), 0);
        catalog.deleteProduct(product.getProductCode());
    }

    @Test
    public void testProductNotFoundException() {
        Long ts = System.currentTimeMillis();
        Product product = null;
        try {
            catalog.getProductByProductCode("" + ts);
        } catch(Exception e) {
            assertTrue(e instanceof ProductNotFoundException);
            assertTrue(e.getMessage().toUpperCase().equals("" + ts + " NOT FOUND."));
        }
        assertNull(product);
    }

    @Test
    public void testInsertPriceTier() throws ProductNotFoundException, InvalidPriceTierException {
        Product product = new Product();
        String ts = "" + System.currentTimeMillis();
        product.setProductName("PRODUCT NAME" + ts);
        product.setProductCode(ts);
        product.setBasePrice(new Double(1.5));
        catalog.insertProduct(product);
        PriceTier tier = new PriceTier();
        tier.setQuantity(10);
        tier.setType(PriceTierType.THRESHOLD);
        tier.setPrice(new Double(10));
        catalog.insertPriceTier(product.getProductCode(), tier);
        product = null;

        product = catalog.getProductByProductCode(ts);
        assertEquals(product.getPriceTiers().size(), 1);
        assertEquals(product.getPriceTiers().get(0).getQuantity(), new Integer(10));
        assertEquals(product.getPriceTiers().get(0).getType(), PriceTierType.THRESHOLD);

        catalog.deleteProduct(ts);


    }

    @After
    public void tearDown() {

    }

}
