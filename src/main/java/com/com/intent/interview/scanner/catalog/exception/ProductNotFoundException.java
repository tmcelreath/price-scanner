package com.com.intent.interview.scanner.catalog.exception;

/**
 * Exception to be used by the CatalogService in cases where an
 * expected product code is not found in the database.
 */
public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String productCode) {
        super(productCode + " not found.");
    }

}
