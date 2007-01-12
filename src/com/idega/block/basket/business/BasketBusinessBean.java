/*
 * Copyright (C) 2004 Idega software. All Rights Reserved.
 * 
 * This software is the proprietary information of Idega software. Use is
 * subject to license terms.
 *  
 */
package com.idega.block.basket.business;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
public class BasketBusinessBean extends IBOSessionBean
		implements
			BasketBusiness {

	protected boolean isCookieBased = false; //Is the basket cookie or session
											 // based.

	protected Map basket = null; //A map to hold the
								 // com.idega.block.basket.data.BasketEntry
								 // entries.
	
	protected List extraData = null; //A list to additional data for the basket.
	
	protected int quantity = 0; // Total quantity for every entry in the basket

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
		if (item == null || quantity < 1) {
			return;
		}

		if (this.isCookieBased) {
			//@TODO implement :)
		} else {
			if (this.basket == null) {
				this.basket = new LinkedHashMap();
			}

			BasketEntry entry = null;
			if (this.basket.containsKey(item.getItemID())) {
				entry = (BasketEntry) this.basket.get(item.getItemID());
				entry.setQuantity(entry.getQuantity() + quantity);
			} else {
				entry = new BasketEntry(item, quantity);
			}
			this.quantity += quantity;
			this.basket.put(item.getItemID(), entry);
		}
	}

	/**
	 * Removes an item from the basket.
	 * 
	 * @param The
	 */
	public void removeItem(BasketItem item) {
		if (item == null || this.basket == null) {
			return;
		}

		if (this.basket.containsKey(item.getItemID())) {
			BasketEntry entry = (BasketEntry) this.basket.get(item.getItemID());
			this.quantity -= entry.getQuantity();
			this.basket.remove(item.getItemID());
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
		if (item == null || quantity < 1) {
			return;
		}


		this.quantity += quantity;

		if (this.basket == null) {
			addItem(item, quantity);
		} else {
			if (this.basket.containsKey(item.getItemID())) {
				BasketEntry entry = (BasketEntry) this.basket.get(item.getItemID());
				this.quantity -= entry.getQuantity();
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
		checkoutBusiness.checkout(this.basket);
	}

	/**
	 * Get the map representing the basket.
	 * 
	 * @return A Map where the values are BasketEntry entries.
	 * 
	 * @uml.property name="basket"
	 */
	public Map getBasket() {
		if (this.basket == null) {
			this.basket = new LinkedHashMap();
		}
		return this.basket;
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
		if (this.basket == null || this.basket.isEmpty()) {
			return false;
		}

		if (this.basket.containsKey(item.getItemID())) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Clears all the data from the basket.
	 */
	public void emptyBasket() {
		if (this.basket != null && !this.basket.isEmpty()) {
			this.basket.clear();
		}
		
		if (this.extraData != null && !this.extraData.isEmpty()) {
			this.extraData.clear();
		}

		this.basket = null;
		this.extraData = null;
		this.quantity = 0;
	}
	
	/**
	 * 
	 */
	public List getExtraInfo() {
		return this.extraData;
	}
	
	/**
	 * 
	 */
	public void emptyExtraInfo() {
		if (this.extraData != null && !this.extraData.isEmpty()) {
			this.extraData.clear();
		}

		this.extraData = null;		
	}
	
	/**
	 * 
	 */
	public void addExtraInfo(Object info) {
		if (this.extraData == null) {
			this.extraData = new ArrayList();
		}
		
		this.extraData.add(info);
	}
	
	/**
	 * 
	 */
	public void removeExtraInfo(Object info) {
		if (this.extraData == null || this.extraData.isEmpty()) {
			return;
		}
		
		this.extraData.remove(info);
	}
	
	/**
	 * Returns the total quantity of every entry in the basket
	 * @return
	 */
	public int getQuantity() {
		return this.quantity;
	}
}