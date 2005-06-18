/*
 * $Id: SupplierBrowserPlugin.java,v 1.5 2005/06/18 18:02:36 gimmi Exp $
 * Created on 19.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import java.util.Collection;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;


public interface SupplierBrowserPlugin {

	public static final String OBJECT_NAME = "travel.supplier_browser_plugin";

	/**
	 * Show supplier results or go directly to product list
	 */
	public boolean displaySupplierResults();

	/**
	 * Returns true if product search has been made.
	 * @param iwc
	 * @return
	 */
	public boolean isProductSearchCompleted(IWContext iwc);
	
	/**
	 * Returns the parameters used by the plugin, they will be maintained by the SupplierBrowser
	 * @return
	 */
	public String[] getParameters();
	
	/**
	 * Returns an array of collections. Collection[0] contains the Strings and Collection[1] the InterfaceObjects
	 * @param iwc
	 * @param iwrb
	 * @return
	 */
	public Collection[] getSupplierSearchInputs(IWContext iwc, IWResourceBundle iwrb);

	/**
	 * Returns an array of collections. Collection[0] contains the Strings and Collection[1] the InterfaceObjects
	 * @param iwc
	 * @param iwrb
	 * @return
	 */
	public Collection[] getProductSearchInputs(IWContext iwc, IWResourceBundle iwrb);

	/**
	 * Returns a collection of Criterias use to find the
	 * @return
	 * @throws IDOException 
	 * @throws IDOCompositePrimaryKeyException 
	 * @throws IDORelationshipException 
	 */
	public Collection getSuppplierSearchCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException, IDORelationshipException;
	

	/**
	 * Returns a collection of the products to display in the SupplierBrower
	 * @param iwc
	 * @param iwrb
	 * @param postalCodes postalCodes[0] contains FROM codes, and postalCodes[1] contains TO codes.
	 * @return
	 */
	public Collection getProducts(Supplier supplier, Group supplierManager, IWContext iwc, String[][] postalCodes) throws IDOLookupException, FinderException;

	/**
	 * Get the extra (if any) fields needed for the booking form.
	 * Returns Array of Collection ... arr[0] contains string and arr[1] the InterfaceObjects
	 */
	public Collection[] getExtraBookingFormElements(Product product, IWResourceBundle iwrb);
}
