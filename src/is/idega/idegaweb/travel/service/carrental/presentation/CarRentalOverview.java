package is.idega.idegaweb.travel.service.carrental.presentation;
import is.idega.idegaweb.travel.presentation.TravelCurrencyCalculatorWindow;
import com.idega.presentation.text.Link;
import java.util.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.idegaweb.IWBundle;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.IBOLookup;
import is.idega.idegaweb.travel.business.*;
import javax.ejb.FinderException;
import is.idega.idegaweb.travel.presentation.TravelManager;
import is.idega.idegaweb.travel.service.presentation.AbstractServiceOverview;
import java.rmi.RemoteException;
import java.sql.SQLException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductPriceException;
import com.idega.core.location.data.Address;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CarRentalOverview extends AbstractServiceOverview {
	
	Product _product;

  public CarRentalOverview(IWContext iwc) throws RemoteException{
    super.main(iwc);
  }

  public void main(IWContext iwc) throws RemoteException {
    super.main(iwc);
  }

  public Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException{
		_product = product;
		Table contentTable;
		int contRow = 0;
		contentTable = new Table();
	
		int[] dayOfWeek = new int[] {};
		IWCalendar iwCal;
	
		Text nameText = (Text) theText.clone();
		nameText.setText(_iwrb.getLocalizedString("travel.name_of_product","Name of product"));
		nameText.addToText(":");
		nameText.setFontColor(super.BLACK);
		Image imageToClone = _iwrb.getImage("images/picture.gif");
		Image image;
	
		Service service;
	
		Text prodName;
	
		Text nameOfCategory;
		Text priceText;
		ProductPrice[] prices;
		Currency currency;
	
		service = getTravelStockroomBusiness(iwc).getService(product);
		Timeframe[] tFrames = product.getTimeframes();
	
		if (product.getFileId() != -1) {
		  image = new Image(product.getFileId());
		  image.setMaxImageWidth(138);
		}else{
		  image = (Image) imageToClone.clone();
		}
		prodName = (Text) theBoldText.clone();
		prodName.setText(getProductBusiness(iwc).getProductNameWithNumber(product));
		prodName.setFontColor(super.BLACK);
	
	
	
		++contRow;
		contentTable.mergeCells(1,contRow,1,contRow+3);
		contentTable.add(image,1,contRow);
		contentTable.setVerticalAlignment(1,contRow,"top");
		contentTable.add(nameText,2,contRow);
		contentTable.mergeCells(3, contRow, 5, contRow);
		contentTable.setVerticalAlignment(2,contRow,"top");
		contentTable.setVerticalAlignment(3,contRow,"top");
		contentTable.setVerticalAlignment(4,contRow,"top");
		contentTable.setVerticalAlignment(5,contRow,"top");
		contentTable.setAlignment(2,contRow,"right");
		contentTable.setAlignment(3,contRow,"left");
		contentTable.setAlignment(4,contRow,"right");
		contentTable.setAlignment(5,contRow,"left");
		contentTable.add(prodName,3,contRow);
		contentTable.setRowColor(contRow, super.GRAY);
	
		++contRow;
	    
		if (tFrames.length == 0) {
	//		prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, -1, false);
				contRow = listPrices(iwc, contentTable, contRow, service, -1);
		}else {
			Text timeframeText;
			--contRow;
			for (int i = 0; i < tFrames.length; i++ ) {
	//			timeframeText = (Text) theBoldText.clone();
					++contRow;
				timeframeText = super.getTimeframeText(tFrames[i], iwc);
				timeframeText.setFontStyle(super.theBoldTextStyle);
				contentTable.add(timeframeText, 2, contRow);
				contentTable.mergeCells(2, contRow, 3, contRow);
				contentTable.setRowColor(contRow, super.GRAY);
				++contRow;
					contRow = listPrices(iwc, contentTable, contRow, service, tFrames[i].getID());				
			}
		}
	    
	
	
		contentTable.setVerticalAlignment(2,contRow,"top");
		contentTable.setVerticalAlignment(3,contRow,"top");
		contentTable.setVerticalAlignment(4,contRow,"top");
		contentTable.setVerticalAlignment(5,contRow,"top");
		contentTable.setAlignment(2,contRow,"right");
		contentTable.setAlignment(3,contRow,"left");
		contentTable.setAlignment(4,contRow,"right");
		contentTable.setAlignment(5,contRow,"left");
		contentTable.setRowColor(contRow, super.GRAY);
	
	
	
		contentTable.setWidth("100%");
		contentTable.setBorder(0);
		contentTable.setAlignment("center");
		contentTable.setWidth(1,"138");
		contentTable.setWidth(2,"130");
		contentTable.setWidth(4,"130");
	//		  contentTable.setWidth(5,"110");
		contentTable.setCellspacing(1);
		contentTable.setColor(super.WHITE);
	
		return contentTable;
  }


	private int listPrices(IWContext iwc, Table contentTable,	int contRow,	Service service,	int timeframeId)	throws SQLException, RemoteException {
		Text nameOfCategory;
		Text priceText;
		Currency currency;
		ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(_product.getID(), timeframeId, -1, new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC});
		if (prices.length > 0) {
		  contentTable.setVerticalAlignment(2,contRow,"top");
		  contentTable.setVerticalAlignment(3,contRow,"top");
		  contentTable.setVerticalAlignment(4,contRow,"top");
		  contentTable.setVerticalAlignment(5,contRow,"top");
		  contentTable.setAlignment(2,contRow,"right");
		  contentTable.setAlignment(3,contRow,"left");
		  contentTable.setRowColor(contRow, super.GRAY);
		}
		
		int col1 = 2;
		int col2 = 3;
		for (int j = 0; j < prices.length; j++) {
			try {
			currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[j].getCurrencyId());
			}catch (Exception e) {
				currency = null;
			}
		  nameOfCategory = (Text) theText.clone();
		  nameOfCategory.setFontColor(super.BLACK);
		  nameOfCategory.setText(prices[j].getPriceCategory().getName());
		  nameOfCategory.addToText(":");
		  priceText = (Text) theBoldText.clone();
		  priceText.setFontColor(super.BLACK);
		  try {
			if (service == null) {debug("SERVICE");}
			if (prices[j] == null) {debug("PRICES");}
			priceText.setText(Integer.toString( (int) getTravelStockroomBusiness(iwc).getPrice(prices[j].getID(),((Integer) service.getPrimaryKey()).intValue(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1 ) ));
			priceText.addToText(Text.NON_BREAKING_SPACE);
			priceText.addToText(currency.getCurrencyAbbreviation());
		  }catch (Exception p) {
			priceText.setText("Rangt upp sett");
		  }
		
		  if (prices[j].getPriceType() == com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT) {
			priceText.addToText(Text.NON_BREAKING_SPACE+"("+prices[j].getPrice()+"%)");
		  }
		
		  contentTable.setVerticalAlignment(col1,contRow,"top");
		  contentTable.setVerticalAlignment(col2,contRow,"top");
		  contentTable.setAlignment(col1,contRow,"right");
		  contentTable.setAlignment(col2,contRow,"left");
		
		  contentTable.add(nameOfCategory,col1,contRow);
		  contentTable.add(priceText,col2,contRow);
		  contentTable.setRowColor(contRow, super.GRAY);
		
		  if (j % 2 != 0) {
			++contRow;
			col1 = 2;
			col2 = 3;
		  }else {
			col1 = 4;
			col2 = 5;
		  }
		}
		return contRow;
	}


  public Table getPublicServiceInfoTable(IWContext iwc, Product product) throws RemoteException, FinderException {
		TravelSessionManager tsm = (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
		Supplier supplier =( (SupplierHome) IDOLookup.getHome(Supplier.class)).findByPrimaryKey(product.getSupplierId());
		IWBundle bundle = tsm.getIWBundle();
		Image background = bundle.getImage("images/sb_background.gif");
	
	
		Table aroundTable = new Table(2,2);
		  aroundTable.setWidth("100%");
		  aroundTable.setHeight("100%");
		  aroundTable.setCellpadding(0);
		  aroundTable.setCellspacing(0);
		  aroundTable.setBackgroundImage(1,1,background);
		  aroundTable.setBackgroundImage(2,1,background);
		  aroundTable.setBackgroundImage(1,2,background);
		  aroundTable.setWidth(1,"1");
		  aroundTable.setHeight(1,"1");
		  aroundTable.setBorder(0);
	
	
		Table table = new Table();
		  aroundTable.add(table,2,2);
	
	
	
		try {
		//      ServiceOverview so = new ServiceOverview(iwc);
		//        form.add(so.getProductInfoTable(iwc, iwrb, product));
	
		  table.setWidth("100%");
		  table.setHeight("100%");
		  table.setAlignment("center");
		  table.setBorder(0);
	
		  Currency currency;
	
		  Text nameText = getText(_iwrb.getLocalizedString("travel.name","Name"));
		  Text timeframeText = getText(_iwrb.getLocalizedString("travel.timeframe","Timeframe"));
		  Text supplierText = getText(_iwrb.getLocalizedString("travel.supplier","Supplier"));
		  Text pricesText = getText(_iwrb.getLocalizedString("travel.prices","Prices"));
		  Image image = TravelManager.getDefaultImage(_iwrb);
		  if (product.getFileId() != -1) {
			image = new Image(product.getFileId());
		  }
		  image.setMaxImageWidth(138);
	
		  Image arrow = bundle.getImage("images/black_arrow.gif");
			arrow.setAlignment("center");
	
		  Text space = getText(" : ");
	
	
		  Text nameTextBold = getBoldText("");
		  Text supplierTextBold = getBoldText("");
		  Text pricesTextBold = getBoldText("");
		  Text nameOfCategory = getBoldText("");
		  Text priceText = getBoldText("");
		  Text currencyText = getBoldText("");
	
		  nameTextBold.setText(getProductBusiness(iwc).getProductNameWithNumber(product, true, iwc.getCurrentLocaleId()));
		  supplierTextBold.setText(supplier.getName());
		  Address a = supplier.getAddress();
		  if (a != null) {
		  		supplierTextBold.addToText(", "+a.getStreetName());
		  		if (a.getStreetNumber() != null) {
		  			supplierTextBold.addToText(" "+a.getStreetNumber());
		  		}
		  }
	
		  IWCalendar cal = new IWCalendar();
		  Locale locale = tsm.getLocale();
	//		Locale locale = iwc.getCurrentLocale();
	
		  table.add(nameText,1,1);
		  table.add(space,1,1);
		  table.add(nameTextBold,1,1);
	
		  table.add(supplierText,1,2);
		  table.add(space,1,2);
		  table.add(supplierTextBold,1,2);
	
		  table.add(image,1,3);
		  table.setAlignment(1,3,"left");
	
	
	
		  String stampTxt1 = _iwrb.getLocalizedString("travel.not_configured","Not configured");
		  String stampTxt2 = _iwrb.getLocalizedString("travel.not_configured","Not configured");
		  ProductPrice[] prices;
		  Text timeframeTextBold;
			  Timeframe[] tFrames = product.getTimeframes();
	
		  Table pTable = new Table();
			pTable.setCellspacing(0);
	
		  int pRow = 1;
		  if (tFrames.length == 0) {
					pRow = listPublicPrices(iwc, product, priceText, -1, pTable, pRow);
		  }else {
			for (int i = 0; i < tFrames.length; i++) {
				Text text = super.getTimeframeText(tFrames[i], iwc); 
				text.setFontStyle(super.theBoldTextStyle);
				pTable.add(text, 1, pRow);
						pRow = listPublicPrices(iwc, product, priceText, tFrames[i].getID(), pTable, pRow);
			}	
		  }
	
		  pTable.setColumnAlignment(1,"left");
		  pTable.setColumnAlignment(2,"left");
		  pTable.setWidth(2, "20");
		  pTable.setColumnAlignment(3,"left");
		  pTable.setColumnAlignment(4,"right");
		  pTable.setColumnAlignment(5,"left");
		  pTable.setHorizontalZebraColored("#FFFFFF","#F1F1F1");
	
		  table.add(pTable,2,3);
	
		  Link currCalc = new Link(_iwrb.getLocalizedImageButton("travel.currency_calculator","Currency calculator"));
			currCalc.setWindowToOpen(TravelCurrencyCalculatorWindow.class);
		//      table.add(currCalc, 2, 3);
	
		  table.setAlignment(2,1,"right");
		  table.setAlignment(2,2,"right");
		  table.setAlignment(2,3,"right");
		  table.setAlignment(2,4,"right");
		  table.setVerticalAlignment(1,3,"top");
		  table.setVerticalAlignment(1,4,"top");
		  table.mergeCells(1,1,2,1);
		//      table.mergeCells(1,2,2,2);
		  table.mergeCells(1,3,1,5);
		  table.mergeCells(2,3,2,5);
		//      table.setWidth(1,"138");
		//      table.setWidth(3,"350");
		//      table.setWidth(2,"350");
		//      table.setBorder(1);
	
	
		}catch (Exception e) {
		  e.printStackTrace(System.err);
		}
		return aroundTable;
  }

	private int listPublicPrices(IWContext iwc,	Product product,	Text priceText,	int timeframeId,	Table pTable,	int pRow)	throws SQLException, RemoteException {
		Currency currency;
		Text nameOfCategory;
		Text currencyText;
		ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), timeframeId, -1, true);
		  if (prices.length > 0) {
			for (int j = 0; j < prices.length; j++) {
			  currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(prices[j].getCurrencyId());
			  nameOfCategory = getText(prices[j].getPriceCategory().getName());
				nameOfCategory.addToText(Text.NON_BREAKING_SPACE+":"+Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE);
			  try {
				priceText = getBoldText(Integer.toString( (int) getTravelStockroomBusiness(iwc).getPrice(prices[j].getID(),((Integer) product.getPrimaryKey()).intValue(),prices[j].getPriceCategoryID() , prices[j].getCurrencyId(), IWTimestamp.getTimestampRightNow(), timeframeId, -1) ) );
				currencyText = getBoldText(currency.getCurrencyAbbreviation());
				pTable.add(currencyText,5,pRow);
			  }catch (ProductPriceException p) {
				priceText.setText(_iwrb.getLocalizedString("travel.not_configured","Not configured"));
			  }
		
			  pTable.add(nameOfCategory,3,pRow);
			  pTable.add(priceText,4,pRow);
			  ++pRow;
			}
		  }
		 return pRow;
	}

  private Text getBoldText(String content) {
		Text text = new Text();
		text.setStyle(TravelManager.theBoldTextStyle);
		text.setBold(true);
		text.setText(content);
		return text;
  }
}