/*
 * $Id: SupplierBrowserDetailsWindow.java,v 1.1 2005/10/03 11:32:37 gimmi Exp $
 * Created on Sep 23, 2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.ui.Window;


public class SupplierBrowserDetailsWindow extends Window {
	
	public static final String PARAMETER_PRODUCT_ID = "sbdw_pid";
	public static final String PARAMETER_SUPPLIER_ID = "sbdw_sid";
	
	public void main (IWContext iwc ) throws Exception {
		super.addStyleSheetURL("/template/css/custom.css");
		String sPid = iwc.getParameter(PARAMETER_PRODUCT_ID);
		String sSid = iwc.getParameter(PARAMETER_SUPPLIER_ID);
		SupplierBrowser b = new SupplierBrowser();
		b.initialize(iwc);
		b.setWidth("100%");
		if (sPid != null) {
			Product product = getProductBusiness(iwc).getProduct(new Integer(sPid));
			Table table = b.getProductInfo(iwc, product, false);
			add(table);
		} else if (sSid != null) {
			SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
			Supplier supplier = sHome.findByPrimaryKey(new Integer(sSid));
			Table table = b.getSupplierInfo(iwc, supplier);
//			table.setBorder(1);
			add(table);
		} else {
			add("no product man");
		}
	}
	
	private ProductBusiness getProductBusiness(IWContext iwc) {
		try {
			return (ProductBusiness) IBOLookup.getServiceInstance(iwc, ProductBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
}
