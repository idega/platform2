/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package com.idega.block.basket.data;

/**
 * A simple container class to hold the item and quantity for items put into the basket.
 * 
 * @author palli
 */
public class BasketEntry {

    /**
     * 
     * @uml.property name="item"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected BasketItem item;

    protected int quantity;
    
    public BasketEntry() {
        this(null, 0);
    }
    
    public BasketEntry(BasketItem item) {
        this(item, 1);
    }
    
    public BasketEntry(BasketItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * 
     * @uml.property name="item"
     */
    public void setItem(BasketItem item) {
        this.item = item;
    }

    /**
     * 
     * @uml.property name="item"
     */
    public BasketItem getItem() {
        return this.item;
    }

    /**
     * 
     * @uml.property name="quantity"
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * 
     * @uml.property name="quantity"
     */
    public int getQuantity() {
        return this.quantity;
    }

}