/*
 * $Id: ProductImageSlideShowWindow.java,v 1.2 2005/09/05 11:14:55 gimmi Exp $
 * Created on 21.6.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.IWBundleStarter;
import com.idega.block.text.business.ContentFinder;
import com.idega.block.text.business.ContentHelper;
import com.idega.block.text.data.TxText;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ImageSlideShow;
import com.idega.presentation.ui.Window;


public class ProductImageSlideShowWindow extends Window {
	
	public static final String PARAMETER_PRODUCT_ID = "piss_pid";
	
	public ProductImageSlideShowWindow() {
		super();
		this.setOnLoad("window.resizeTo(300 +10, 300 +22)");
	}
	
	public void main(IWContext iwc) throws Exception {
		String sProdID = iwc.getParameter(PARAMETER_PRODUCT_ID);
		ImageSlideShow iss = new ImageSlideShow();
		iss.setWidth(300);
		iss.setHeight(300);
		
		IWBundle bundle = iwc.getIWMainApplication().getBundle(TravelBlock.IW_BUNDLE_IDENTIFIER);
		String datasource = bundle.getProperty(IWBundleStarter.DATASOURCE);
		if (datasource == null) {
			datasource = "default";
		}
		
		if (sProdID != null) {
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class, datasource);
			Product product = pHome.findByPrimaryKey(new Integer(sProdID));
			TxText descriptionText = product.getText();
			if (descriptionText != null) {
				ContentHelper ch = null;
				ch = ContentFinder.getContentHelper(descriptionText.getContentId(), iwc.getCurrentLocaleId(), product.getDatasource());
				if (ch.hasFiles()) {
					iss.setFiles(ch.getFiles());
				}
			}
		}
		
		add(iss);
	}

}
