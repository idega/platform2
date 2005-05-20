/*
 * $Id: SupplierBrowserPlugin.java,v 1.1 2005/05/20 04:05:03 gimmi Exp $
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
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;


public interface SupplierBrowserPlugin {

	public static final String OBJECT_NAME = "travel.supplier_browser_plugin";

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
	 * Method for calling com.idega.data.query.Criteria to be used when creating the SelectQuery, including join-criterias
	 * @return
	 * @throws IDOException 
	 * @throws IDOCompositePrimaryKeyException 
	 * @throws IDORelationshipException 
	 */
	public Collection getCriterias(IWContext iwc) throws IDOCompositePrimaryKeyException, IDORelationshipException;
	

	/**
	 * Returns a collection of the products to display in the SupplierBrower
	 * @param iwc
	 * @param iwrb
	 * @return
	 */
	public Collection getProducts(Supplier supplier, IWContext iwc) throws IDOLookupException, FinderException;
	
}
