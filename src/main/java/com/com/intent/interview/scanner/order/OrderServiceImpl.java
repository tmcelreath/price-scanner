package com.com.intent.interview.scanner.order;

import com.com.intent.interview.scanner.catalog.CatalogService;
import com.com.intent.interview.scanner.catalog.CatalogServiceImpl;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.PriceTierType;
import com.com.intent.interview.scanner.catalog.model.Product;
import com.com.intent.interview.scanner.order.model.Order;
import com.com.intent.interview.scanner.order.model.OrderRow;
import com.com.intent.interview.scanner.order.model.OrderStatus;

import java.util.List;
import java.util.Map;

/**
 * The business logic for processing an order. Includes
 */
public class OrderServiceImpl implements OrderService {

    private CatalogService catalogService = CatalogServiceImpl.getInstance();

    public Order createOrder(String terminalId) {
        return new Order(terminalId);
    }

    public Order addProduct(Order order, String productCode) {
        order.add(productCode);
        total(order, productCode);
        return order;
    }

    /**
     * Remove one instance of the product from the order. In other words, if the order
     * currently contains Product A with a quantity of 2, this method will reduce the
     * quantity to 1. If the quantity is reduced to 0, then the product will be removed from
     * the order.
     *
     * @param order
     * @param productCode
     * @return
     */
    public Order removeProduct(Order order, String productCode) {
        order.remove(productCode);
        return order;
    }

    /**
     * Re-calculates the subtotal of a product on an order, taking into account the current
     * quantity and product Price Tiers.
     *
     * @param order
     * @param productCode
     * @return
     */
    public Order total(Order order, String productCode) {
        Map<String, OrderRow> contents = order.getContents();
        contents.values().stream()
                .filter(row -> row.getProductCode().equals(productCode))
                .forEach(row -> {
                    try {
                        Product product = catalogService.getProductByProductCode(row.getProductCode());
                        PriceTier tier = selectPriceTier(product.getPriceTiers(), row.getQuantity());
                        if(tier == null) {
                            row.setSubTotal(product.getBasePrice() * row.getQuantity());
                        } else {
                            if(PriceTierType.UNIT.equals(tier.getType())) {
                                row.setSubTotal(calculateUnitTierPrice(row.getQuantity(),  product.getBasePrice(), tier.getQuantity(), tier.getPrice()));
                            } else
                            if(PriceTierType.THRESHOLD.equals(tier.getType())) {
                                row.setSubTotal(tier.getPrice() * row.getQuantity());
                            }
                        }
                    } catch (ProductNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                });
        return order;
    }

    /**
     * Cancel the order. COWARD!
     *
     * @param order
     * @return
     */
    public Order cancelOrder(Order order) {
        order.setOrderStatus(OrderStatus.CANCELLED);
        // TODO: Persist order. Maybe I'll tackle this once that SWEET SWEET VC CASH comes pouring in.
        return order;
    }

    /**
     * Finish the order. GET PAID.
     *
     * @param order
     * @return
     */
    public Order completeOrder(Order order) {
        order.setOrderStatus(OrderStatus.COMPLETED);
        // TODO: Persist order. AS ABOVE.
        return order;
    }


    /**
     * A UNIT price tier indicates that the tier price applies only to units of the
     * exact tier quantity, and that any additional items will be charged at the product base
     * price. For example, a six-pack of soda will be charged at the tier price, but a seventh
     * can will be charged at the base, or non-tier, price.
     *
     * @param quantity
     * @param basePrice
     * @param unitSize
     * @param unitPrice
     * @return java.lang.Double
     */
    public Double calculateUnitTierPrice(Integer quantity,  Double basePrice, Integer unitSize, Double unitPrice) {
        int leftovers = quantity % unitSize;
        int units = (quantity - leftovers) / unitSize;
        return (unitPrice * units) + (leftovers * basePrice);
    }

    /**
     * Select the correct price tier (if any) from the product record for the given quantity.
     * NOTE: This assumes that the product object has correctly sorted the PriceTiers by quantity (ascending)
     *
     * @param priceTiers java.util.List<PriceTier>
     * @param quantity java.lang.Integer
     * @return PriceTier
     */
    public PriceTier selectPriceTier(List<PriceTier> priceTiers, Integer quantity) {
        PriceTier tier = null;
        for(PriceTier pt: priceTiers) {
            if(pt.getQuantity() <= quantity) {
                tier = pt;
            } else {
                // Since the tiers are sorted, the given quantity is below
                // the quantity value of the remaining tiers and we can exit.
                break;
            }
        }
        return tier;
    }

}
