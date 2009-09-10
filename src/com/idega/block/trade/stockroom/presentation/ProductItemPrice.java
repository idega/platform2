package com.idega.block.trade.stockroom.presentation;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.NumberFormat;

import javax.ejb.FinderException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Text;

/**
 *  Title: idegaWeb TravelBooking Description: Copyright: Copyright (c) 2001
 *  Company: idega
 *
 *@author     <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 *@created    9. mars 2002
 *@version    1.0
 */

public class ProductItemPrice extends ProductItem {

	private String defaultText = "Product Price";
	private boolean showCurrency = false;
	private boolean showLocalized = false;

	public ProductItemPrice() {
	}
	public ProductItemPrice(int productId) throws RemoteException, FinderException {
		super(productId);
	}
	public ProductItemPrice(Product product) throws RemoteException {
		super(product);
	}

	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		drawObject(iwc);
	}

	private void drawObject(IWContext iwc) throws RemoteException {
		Text text = getText(this.defaultText);
		if (this._product != null) {
			ProductPrice pPrice = getStockroomBusiness(iwc).getPrice(this._product);
			if (pPrice != null && pPrice.getPrice() > 0) {
				NumberFormat format = NumberFormat.getInstance(iwc.getCurrentLocale());
				text.setText(format.format(pPrice.getPrice()));
				if (this.showCurrency) {
					try {
						text.addToText(Text.NON_BREAKING_SPACE);
						String abbreviation = ((com.idega.block.trade.data.CurrencyHome) com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(pPrice.getCurrencyId()).getCurrencyAbbreviation();
						if (this.showLocalized) {
							text.addToText(this._iwrb.getLocalizedString("currency."+abbreviation, abbreviation));
						}
						else {
							text.addToText(abbreviation);
						}
					}
					catch (SQLException sql) {
					}
				}
			}
			else {
				text.setText("");
			}
		}
		add(text);
	}

	public void setShowCurrency(boolean show) {
		this.showCurrency = show;
	}

	public void setShowLocalized(boolean showLocalized) {
		this.showLocalized = showLocalized;
	}
}
