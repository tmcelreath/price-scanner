package com.com.intent.interview.scanner.catalog.exception;

/**
 * Thrown when a PriceTier cannot be added to a product.
 */
public class InvalidPriceTierException extends Exception {

    public InvalidPriceTierException(String message) {
        super(message);
    }
}
