package com.com.intent.interview.scanner.catalog.model;

/**
 * This is the representation of a volume discount on a Product. A product can have
 * multiple price tiers, provided that they are all of the same PriceTierType and
 * are assigned to different quantities.
 *
 * @see com.com.intent.interview.scanner.catalog.model.PriceTierType
 */
public class PriceTier {

    private Integer quantity;
    private Double price;
    private PriceTierType type;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PriceTierType getType() {
        return type;
    }

    public void setType(PriceTierType type) {
        this.type = type;
    }
}
