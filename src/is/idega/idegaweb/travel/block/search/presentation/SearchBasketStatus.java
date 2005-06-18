/*
 * $Id: SearchBasketStatus.java,v 1.2 2005/06/18 17:59:08 gimmi Exp $
 * Created on 24.5.2005
 *
 * Copyright (C) 2005 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package is.idega.idegaweb.travel.block.search.presentation;

import is.idega.idegaweb.travel.presentation.TravelBlock;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.builder.data.ICPage;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.util.IWTimestamp;


public class SearchBasketStatus extends TravelBlock {
	
	private ICPage viewerPage = null;
	private String textStyleClass = null;
	private String linkStyleClass = null;
	
	private String urlToCheckout = null;
	
	public void main(IWContext iwc) throws Exception {
		super.main(iwc);
		
		
		
		int quantity = getBasketBusiness(iwc).getQuantity();
		
		
		Table table = new Table();
		table.setCellpaddingAndCellspacing(0);
		
		if (quantity == 0) {
			table.add(getText(getResourceBundle().getLocalizedString("travel.basket_is_empty", "Basket is empty.")), 1, 1);
		} else {
			Link book = getLink(super.getResourceBundle().getLocalizedString("travel.book", "Book"));
			if (urlToCheckout != null) {
				book.setURL(urlToCheckout);
			}
			if (viewerPage != null) {
				book.setPage(viewerPage);
			}

			book.addParameter(AbstractSearchForm.ACTION, AbstractSearchForm.ACTION_BOOKING_FORM);
			book.addParameter(AbstractSearchForm.PARAMETER_REFERENCE_NUMBER, IWTimestamp.RightNow().toString());

			table.add(getText(quantity + " "+getResourceBundle().getLocalizedString("travel.item.s_in_basket", "item(s) in basket.")), 1, 1);
			table.add(book, 2, 1);
		}
		
		add(table);
	}
	
	public Link getLink(String content) {
		return new Link(getText(content, linkStyleClass));
	}
	
	public Text getText(String content) {
		return getText(content, textStyleClass);
	}
	
	public Text getText(String content, String styleClass) {
		Text text = new Text(content);
		if (styleClass != null) {
			text.setStyleClass(styleClass);
		}
		return text;
	}
	
	protected BasketBusiness getBasketBusiness(IWUserContext iwuc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwuc, BasketBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public void setViewerPage(ICPage page) {
		this.viewerPage = page;
	}
	
	public void setTextStyleClass(String styleClass) {
		this.textStyleClass = styleClass;
	}
	
	public void setLinkStyleClass(String styleClass) {
		this.linkStyleClass = styleClass;
	}
	
	public void setURLToCheckout(String url) {
		this.urlToCheckout = url;
	}
	
}
