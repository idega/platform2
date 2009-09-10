/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 *
 * This software is the proprietary information of Idega software.
 * Use is subject to license terms.
 *
 */
package com.idega.block.basket.data;

import com.idega.data.IDOPrimaryKey;

/**
 * @author palli
 */

public interface BasketItem {

    /**
     * Get the unique id for this item.
     * 
     * @return A unique key represented by a IDOPrimaryKey.
     * 
     * @uml.property name="itemID"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    public IDOPrimaryKey getItemID();

    /**
     * 
     * @uml.property name="itemName"
     */
    public String getItemName();

    /**
     * 
     * @uml.property name="itemDescription"
     */
    public String getItemDescription();

    /**
     * 
     * @uml.property name="itemPrice"
     */
    public Double getItemPrice();

}
