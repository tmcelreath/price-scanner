package com.com.intent.interview.scanner.catalog;

import com.com.intent.interview.scanner.catalog.exception.InvalidPriceTierException;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.Product;

/**
 * Catalog interface.
 */
public interface CatalogService {

    public Product getProductByProductCode(String productCode) throws ProductNotFoundException;

    public void insertProduct(Product product);

    public void insertPriceTier(String productCode, PriceTier priceTier)
            throws ProductNotFoundException, InvalidPriceTierException;

    public void deleteProduct(String productCode);

    public void deletePriceTier(String productCode, Integer quantity) throws ProductNotFoundException;

}
