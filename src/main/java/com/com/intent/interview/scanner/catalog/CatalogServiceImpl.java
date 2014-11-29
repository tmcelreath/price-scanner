package com.com.intent.interview.scanner.catalog;

import com.com.intent.interview.scanner.catalog.exception.InvalidPriceTierException;
import com.com.intent.interview.scanner.catalog.exception.ProductNotFoundException;
import com.com.intent.interview.scanner.catalog.model.PriceTier;
import com.com.intent.interview.scanner.catalog.model.Product;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of the CatalogService interface. Not a particularly GOOD one, mind you,
 * but still . . .
 */
public class CatalogServiceImpl implements CatalogService {

    /*
        Yeah. I'm using a singleton here. So sue me.
     */
    private static CatalogServiceImpl _instance = null;

    /*
        My little fake database. Adorable.
     */
    private Map<String, Product> products;

    public static CatalogServiceImpl getInstance() {
        return CatalogServiceImplHolder._instance;
    }

    /**
     * At LEAST I'm using Bill Pugh's "Initialization-on-demand holder idiom" to
     * avoid concurrency issues on my little fake database here. So that should count for
     * something, right?
     */
    private static class CatalogServiceImplHolder {
        private static final CatalogServiceImpl _instance = new CatalogServiceImpl();
    }

    private CatalogServiceImpl() {
        products = new ConcurrentHashMap<String, Product>();
    }

    /**
     * Fetch the product from the stupid little database-ette.
     *
     * @param productCode
     * @return
     * @throws ProductNotFoundException
     */
    public Product getProductByProductCode(String productCode) throws ProductNotFoundException {
        if(products.containsKey(productCode)) {
            return products.get(productCode);
        } else {
            throw new ProductNotFoundException(productCode);
        }
    }

    /**
     * Load up a product into the Moron-Mongo.
     * @param product
     */
    public void insertProduct(Product product) {
        products.put(product.getProductCode(), product);
    }

    /**
     * A little logic. We'll throw an exception if you try to insert a
     * second PriceTier for the same quantity, since that makes no sense.
     *
     * @param productCode
     * @param priceTier
     * @throws ProductNotFoundException
     * @throws InvalidPriceTierException
     */
    public void insertPriceTier(String productCode, PriceTier priceTier)
            throws ProductNotFoundException, InvalidPriceTierException {
        Product product = getProductByProductCode(productCode);
        // determine if there is an existing price tier for the quantity
        long count = product.getPriceTiers().stream()
                .filter(tier -> tier.getQuantity() == priceTier.getQuantity())
                .count();
        if(count>0) {
            throw new InvalidPriceTierException(
                    String.format(
                            "A price tier for quantity %d already exists on product %s",
                            priceTier.getQuantity(), productCode));
        }
        product.getPriceTiers().add(priceTier);
    }

    /**
     * Dump the product. Sell it on eBay. Shit, I don't care.
     * @param productCode
     */
    public void deleteProduct(String productCode) {
        products.remove(productCode);
    }

    /**
     * Black Friday's Over. FULL PRICE FOR YOU!
     *
     * @param productCode
     * @param quantity
     * @throws ProductNotFoundException
     */
    public void deletePriceTier(String productCode, Integer quantity) throws ProductNotFoundException {
        Product product = getProductByProductCode(productCode);
        for(PriceTier tier: product.getPriceTiers()) {
            if(quantity.equals(tier.getQuantity())) {
                product.getPriceTiers().remove(tier);
            }
        }
    }

}
