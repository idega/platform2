/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package com.idega.block.basket.business;

import java.util.LinkedHashMap;
import java.util.Map;

import com.idega.block.basket.data.BasketEntry;
import com.idega.block.basket.data.BasketItem;
import com.idega.business.IBOSessionBean;

/**
 * A very simple session-based shopping basket. Defines a set of basic add,
 * remove, edit and view on the data.
 * 
 * @author palli
 */
public class BasketBusinessBean extends IBOSessionBean implements
        BasketBusiness {

    protected boolean isCookieBased = false; //Is the basket cookie or session

    // based.

    protected Map basket = null; //A map to hold the

    // com.idega.block.basket.data.BasketEntry
    // entries.

    /**
     * Increments the quantity of the specified item in the basket by one.
     * 
     * @param item
     *            The com.idega.block.basket.data.BasketItem that is being added
     *            to the basket.
     */
    public void addItem(BasketItem item) {
        addItem(item, 1);
    }

    /**
     * Increments the quantity of the specified item in the basket by the
     * quantity sent into the method.
     * 
     * @param item
     *            The {@link com.idega.block.basket.data.BasketItem BasketItem}
     *            that is being added to the basket.
     * @param quantity
     *            The quantity that the basket count is being incremented by.
     *            Will not do anything if this is less than one.
     */
    public void addItem(BasketItem item, int quantity) {
        if (item == null || quantity < 1) { return; }

        if (isCookieBased) {
            //@TODO implement :)
        } else {
            if (basket == null) {
                basket = new LinkedHashMap();
            }

            BasketEntry entry = null;
            if (basket.containsKey(item.getItemID())) {
                entry = (BasketEntry) basket.get(item.getItemID());
                entry.setQuantity(entry.getQuantity() + quantity);
            } else {
                entry = new BasketEntry(item, quantity);
            }
            basket.put(item.getItemID(), entry);
        }
    }

    /**
     * Removes an item from the basket.
     * 
     * @param The
     */
    public void removeItem(BasketItem item) {
        if (item == null || basket == null) { return; }

        if (basket.containsKey(item.getItemID())) {
            basket.remove(item.getItemID());
        }
    }

    /**
     * Changes the quantity for the specified item in the basket to the value
     * sent in. If the item is not in the basket it is added.
     * 
     * @param item
     * @param quantity
     *            The quantity
     */
    public void changeQuantity(BasketItem item, int quantity) {
        if (item == null || quantity < 1) { return; }

        if (basket == null) {
            addItem(item, quantity);
        } else {
            if (basket.containsKey(item.getItemID())) {
                BasketEntry entry = (BasketEntry) basket.get(item.getItemID());
                entry.setQuantity(quantity);
            } else {
                addItem(item, quantity);
            }
        }
    }

    /**
     * Perform a checkout function on the current basket. Will call the checkout
     * method in the BasketCheckoutBusiness implementation sent into the method
     * with the basket as a parameter.
     * 
     * @param checkoutBusiness
     */
    public void checkout(BasketCheckoutBusiness checkoutBusiness) {
        checkoutBusiness.checkout(basket);
    }

    /**
     * Get the map representing the basket.
     * 
     * @return A Map where the values are BasketEntry entries.
     * 
     * @uml.property name="basket"
     */
    public Map getBasket() {
        return basket;
    }

    /**
     * Check to see if a BasketItem is in the basket.
     * 
     * @param item
     *            The BasketItem we are looking for in the basket.
     * 
     * @return True, if an BasketItem with the same ID as the one sent in exists
     *         in the basket. False otherwise.
     */
    public boolean checkForItemInBasket(BasketItem item) {
        if (basket == null || basket.isEmpty()) {
            return false;
        }
        
        if (basket.containsKey(item.getItemID())) {
            return true;
        } else {
            return false;
        }
    }
    
    public void emptyBasket() {
        if (basket != null && !basket.isEmpty()) {
            basket.clear();
        }
        
        basket = null;
    }
}