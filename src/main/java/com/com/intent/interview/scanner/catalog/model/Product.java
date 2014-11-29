package com.com.intent.interview.scanner.catalog.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The object representation of a Product, included all embedded
 * price tiers.
 *
 * @see com.com.intent.interview.scanner.catalog.model.PriceTier
 */
public class Product {

    private String productCode;
    private String productName;
    private Double basePrice;
    private List<PriceTier> priceTiers;

    public Product() {
        this.priceTiers = new ArrayList<PriceTier>();
    }

    public List<PriceTier> getPriceTiers() {
        return priceTiers;
    }

    public void setPriceTiers(List<PriceTier> priceTiers) {
        priceTiers.sort((a, b) -> a.getQuantity().compareTo(b.getQuantity()));
        this.priceTiers = priceTiers;
    }

    public String getProductCode() {

        return productCode;
    }

    public void setProductCode(String productCode) {

        this.productCode = productCode;
    }

    public String getProductName() {

        return productName;
    }

    public void setProductName(String productName) {

        this.productName = productName;
    }

    public Double getBasePrice() {

        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

}
