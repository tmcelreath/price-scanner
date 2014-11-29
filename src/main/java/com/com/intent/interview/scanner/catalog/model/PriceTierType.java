package com.com.intent.interview.scanner.catalog.model;

/**
 * Constants representing the varieties of PriceTiers that can be applied to a
 * product.
 *
 * UNIT: This is a discount that is applied to a specific multiple of a product.
 *       Common examples of this is a six-pack of beer or a 12-can 'case' of motor oil.
 *       Any items outside of the 'unit' will be charged at the base price - i.e. If one
 *       purchases a six-pack and a single can, the single can will be charged at the
 *       single-unit price.
 *
 * THRESHOLD: This is a simple volume discount. Once the number of items equals or
 *       exceeds the price tier quantity, the discount price will apply to all of the
 *       items.
 *
 */
public enum PriceTierType {
    UNIT, THRESHOLD
}
