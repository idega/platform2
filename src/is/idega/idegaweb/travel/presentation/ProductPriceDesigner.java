package is.idega.idegaweb.travel.presentation;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.presentation.BookingForm;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.ejb.FinderException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceBMPBean;
import com.idega.block.trade.stockroom.data.Settings;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.data.TravelAddressHome;
import com.idega.business.IBOLookup;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.presentation.ui.Form;
import com.idega.presentation.ui.HiddenInput;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.ui.TextInput;
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

public class ProductPriceDesigner extends TravelWindow {

  private static final String PARAMETER_PRODUCT_ID = "ppd_pid";
  private String PARAMETER_CURRENCY_ID = "ppd_cid";
  private String FORM_NAME = "ppd_frm";
  private String FORM_ACTION = "ppd_frm_act";
  private String FORM_ACTION_SAVE = "ppd_act_sv";
  private String PARAMETER_TIMEFRAME_ID = "ppd_tfid";
  private String PARAMETER_ADDRESS_ID = "ppd_taid";
  private String PARAMETER_PRODUCT_PRICE_ID ="ppd_ppid";
  private String PARAMETER_PRODUCT_CATEGORY_ID = "ppd_pcid";
  private String PARAMETER_PRICE_DISCOUNT = "ppd_pr_di";

  private String TABLE_WIDTH = "90%";

  private Product _product;
  private Settings _settings;
  private Supplier _supplier;
  private int _currencyId = -1;
  private int _visibility = 3;
  
  private BookingForm bf;

  public ProductPriceDesigner() {
    super.setWidth(800);
    super.setHeight(600);
    super.setResizable(true);
    super.setScrollbar(true);
  }

  public ProductPriceDesigner(IWContext iwc) throws RemoteException{
    super.setWidth(800);
    super.setHeight(600);
    super.setResizable(true);
    super.setScrollbar(true);
    super.initialize(iwc);
    init(iwc);
  }

  public void main(IWContext iwc) throws Exception{
    super.main(iwc);
    init(iwc);
    if (_supplier == null) {
      notLoggedOn();
    }else {
      String action = iwc.getParameter(FORM_ACTION);
      if (action != null && action.equals(FORM_ACTION_SAVE)) {
        this.priceCategorySave(iwc);
      }
      displayForm(iwc);
    }
  }

  private void init(IWContext iwc) throws RemoteException{
    String productId = iwc.getParameter(PARAMETER_PRODUCT_ID);
    if (productId != null) {
      try {
        ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
        _product = pHome.findByPrimaryKey(new Integer(productId) );
      }catch (FinderException fe) {
        fe.printStackTrace(System.err);
      }
    }

    try {
      _supplier = super.getTravelSessionManager(iwc).getSupplier();
      _settings = _supplier.getSettings();
      if (iwc.isParameterSet(PARAMETER_CURRENCY_ID)) {
        _currencyId = Integer.parseInt(iwc.getParameter(PARAMETER_CURRENCY_ID));
      }else{
        _currencyId = _settings.getCurrencyId();
      }
    }catch (Exception e) {}

		ServiceHandler sh = (ServiceHandler) IBOLookup.getServiceInstance(iwc, ServiceHandler.class);
		try {
			if (_product != null) {
				bf = sh.getBookingForm(iwc, _product);
			} 
		} catch (Exception e1) {
			e1.printStackTrace();
		}

  }

  private void displayForm(IWContext iwc) throws RemoteException{
    if (_product == null) {
      productIsNull();
    }else if (_settings == null) {
      settingsAreNull();
    }else {
      Form form = new Form();
      form.maintainParameter(PARAMETER_PRODUCT_ID);
      Table table = new Table();
      form.add(table);
      table.setColor(TravelManager.WHITE);
      table.setWidth(TABLE_WIDTH);
      table.setAlignment("center");
      table.setCellpadding(2);
      table.setCellspacing(0);
      int row = 1;

      table.add(super.getTextHeader(getProductBusiness(iwc).getProductNameWithNumber(_product, true)), 1, row);

      table.add(super.getTextHeader(iwrb.getLocalizedString("travel.currency","Currency")), 2, row);
      table.add(super.getTextHeader(" : "), 2, row);

      DropdownMenu menu = super.getTravelStockroomBusiness(iwc).getCurrencyDropdownMenu(PARAMETER_CURRENCY_ID);
      menu.setMarkupAttribute("style","font-size: 8pt");
      menu.setToSubmit();
      if (iwc.isParameterSet(PARAMETER_CURRENCY_ID)) {
        menu.setSelectedElement(iwc.getParameter(PARAMETER_CURRENCY_ID));
      }else if (_currencyId > 0){
        menu.setSelectedElement(Integer.toString(_currencyId));
      }

      table.add(menu, 2, row);
      table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
      table.setRowColor(row, TravelManager.backgroundColor);

      ++row;
      table.add(super.getTextHeader(iwrb.getLocalizedString("travel.active_currencies","Active currencies")), 2, row);
      table.add(super.getTextHeader(" : "), 2, row);
      int[] activeCurrencies = ProductPriceBMPBean.getCurrenciesInUse(_product.getID());
      Link link;
      Currency currency;
      CurrencyHome cHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
      for (int i = 0; i < activeCurrencies.length; i++) {
        try {
          currency = cHome.findByPrimaryKey(activeCurrencies[i]);
          if (i != 0) {
            table.add(super.getTextHeader(", "), 2, row);
          }
          link = new Link(super.getTextHeader(currency.getCurrencyAbbreviation()));
          link.addParameter(PARAMETER_CURRENCY_ID, activeCurrencies[i]);
          link.addParameter(PARAMETER_PRODUCT_ID, _product.getID());
          table.add(link, 2, row);
        }catch (FinderException fe) {
          fe.printStackTrace(System.err);
        }
      }
      table.setAlignment(2, row, Table.HORIZONTAL_ALIGN_RIGHT);
      table.setRowColor(row, TravelManager.backgroundColor);


      Table content = getTable(iwc);
      form.add(content);

      add(form);
    }
  }

  public Form getPriceCategoryForm(IWContext iwc, Product product, String submitParameterName, String submitParameterValue) throws RemoteException{
		return getPriceCategoryForm(iwc, product, submitParameterName, submitParameterValue, new Form());
  }
	
  public Form getPriceCategoryForm(IWContext iwc, Product product, String submitParameterName, String submitParameterValue, Form form) throws RemoteException{
    _product = product;
		ServiceHandler sh = (ServiceHandler) IBOLookup.getServiceInstance(iwc, ServiceHandler.class);
		try {
			if (_product != null) {
				bf = sh.getBookingForm(iwc, _product);
			} 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    FORM_ACTION = submitParameterName;
    FORM_ACTION_SAVE = submitParameterValue;

    form.add(getTable(iwc));
    return form;
  }

  public boolean handleInsert(IWContext iwc, Product product) {
    _product = product;
		try {
			ServiceHandler sh = (ServiceHandler) IBOLookup.getServiceInstance(iwc, ServiceHandler.class);
			if (_product != null) {
				bf = sh.getBookingForm(iwc, _product);
			} 
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    return this.priceCategorySave(iwc);
  }


  private Table getTable(IWContext iwc) throws RemoteException{
    Table table = new Table();
    table.setAlignment("center");
    table.setWidth(TABLE_WIDTH);
    table.setColor(TravelManager.WHITE);
    table.setCellspacing(1);
    int row = 1;


    Text addrText;
    Text counter;
    IWTimestamp timestamp;

    TravelAddressHome taHome = (TravelAddressHome) IDOLookup.getHome(TravelAddress.class);
    TravelAddress address;

    Timeframe[] tFrames = {};
    try {
      tFrames = _product.getTimeframes();
    }
    catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    List addresses = com.idega.util.ListUtil.getEmptyList();
    try {
      addresses = _product.getDepartureAddresses(true);
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
    }
    int addressesSize = addresses.size();

    Iterator iter = addresses.iterator();

    PriceCategory[] cats = getTravelStockroomBusiness(iwc).getPriceCategories(this._supplier.getID());
    PriceCategory[] misc = getTravelStockroomBusiness(iwc).getMiscellaneousServices(this._supplier.getID());
		PriceCategory[] spec = getTravelStockroomBusiness(iwc).getPriceCategories(bf.getPriceCategorySearchKey());

    Text catName = getText(iwrb.getLocalizedString("travel.price","Price"));
    Text priceDiscountText = getText(iwrb.getLocalizedString("travel.price_discount","Price / Discount"));

    catName.setFontColor(TravelManager.WHITE);
    priceDiscountText.setFontColor(TravelManager.WHITE);


    table.add(catName,1,row);
    table.add(priceDiscountText,2,row);
    table.setAlignment(3, row, "right");
    table.setRowColor(row,TravelManager.backgroundColor);
//		table.mergeCells(1,row,3,row);
//		table.setRowColor(row, TravelManager.backgroundColor);

    DecimalFormat df = new DecimalFormat("0.00");
    if (!iter.hasNext()) {
    	if (tFrames.length == 0) {
	      ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(((Integer)_product.getPrimaryKey()).intValue(), -1, -1,  0, _currencyId, _visibility);
	      for (int i = 0; i < cats.length; i++) {
	        try {
	          table.add(new HiddenInput(PARAMETER_TIMEFRAME_ID, "-1"),1,row);
	          table.add(new HiddenInput(PARAMETER_ADDRESS_ID, "-1"),1,row);
	          insertCategoryIntoTable(table, row, cats[i], prices);
						++row;
	        }catch (SQLException sql) {
	          sql.printStackTrace(System.out);
	        }
	      }
	      
	      
			  row = insertMiscellaneousProductCategories(table, row, misc, -1, -1);
				row = insertSpecialPriceCategories(table, row, spec, -1, -1);
	      	      
    	}else {
				for (int i = 0; i < tFrames.length; i++ ) {
					++row;
					table.setRowColor(row, TravelManager.backgroundColor);
	        Text timeframeText = getTimeframeText(tFrames[i], iwc);
	        timeframeText.setFontColor(TravelManager.WHITE);
	        table.add(timeframeText,3,row);
	        table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);

		      ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(((Integer)_product.getPrimaryKey()).intValue(), tFrames[i].getID(), -1,  0, _currencyId, _visibility);
		      for (int p = 0; p < cats.length; p++) {
		        try {
		          table.add(new HiddenInput(PARAMETER_TIMEFRAME_ID, Integer.toString(tFrames[i].getID() ) ),1,row);
		          table.add(new HiddenInput(PARAMETER_ADDRESS_ID, "-1"),1,row);
		          insertCategoryIntoTable(table, row, cats[p], prices);
		          ++row;
		
		        }catch (SQLException sql) {
		          sql.printStackTrace(System.out);
		        }
		      }

				  row = insertMiscellaneousProductCategories(table, row, misc, tFrames[i].getID(),-1);
				  row = insertSpecialPriceCategories(table, row, spec, tFrames[i].getID(), -1);
      
				}
    	}
    	
    }


    while (iter.hasNext()) {
      address = (TravelAddress) iter.next();
      addrText = (Text) super.getTextHeader(address.getName());
//      ++row;
			table.add(addrText, 1, row+1);
			table.setRowColor(row+1, TravelManager.backgroundColor);


      for (int k = 0; k < tFrames.length; k++) {
				++row;
        Text timeframeText = getTimeframeText(tFrames[k], iwc);
        timeframeText.setFontColor(TravelManager.WHITE);
        table.add(timeframeText,3,row);
        table.setAlignment(3, row, Table.HORIZONTAL_ALIGN_RIGHT);
        table.setRowColor(row, TravelManager.backgroundColor);

        ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(((Integer)_product.getPrimaryKey()).intValue(), tFrames[k].getID(), address.getID(),  0, _currencyId, _visibility);
        for (int i = 0; i < cats.length; i++) {
          try {
            table.add(new HiddenInput(PARAMETER_TIMEFRAME_ID, Integer.toString(tFrames[k].getID())),1,row);
            table.add(new HiddenInput(PARAMETER_ADDRESS_ID, Integer.toString(address.getID())),1,row);

            insertCategoryIntoTable(table, row, cats[i], prices);
            ++row;

          }catch (SQLException sql) {
            sql.printStackTrace(System.out);
          }
        }

				row = insertMiscellaneousProductCategories(table, row, misc, tFrames[k].getID(),address.getID());
				row = insertSpecialPriceCategories(table, row, spec, tFrames[k].getID(), address.getID());
      }
    }

		++row;
    table.setRowColor(row,TravelManager.GRAY);
    table.setAlignment(3,row,"right");
    table.add(new SubmitButton(iwrb.getImage("/buttons/save.gif"),this.FORM_ACTION, this.FORM_ACTION_SAVE),3,row);

    add(Text.BREAK);
    return table;
  }

	private int insertMiscellaneousProductCategories(Table table, int row,	PriceCategory[] misc, int tFrameId, int addressId) throws RemoteException {
		Text catName;

		if (misc.length > 0) {
		  ++row;
		  catName = getText(iwrb.getLocalizedString("travel.miscellaneous_services","Miscellaneous services"));
		  catName.setFontColor(TravelManager.WHITE);
		  table.add(catName, 1, row);
		  table.setRowColor(row, TravelManager.backgroundColor);
		}
		
		ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getMiscellaneousPrices(((Integer)_product.getPrimaryKey()).intValue(), tFrameId, addressId, false, _currencyId);
		for (int i = 0; i < misc.length; i++) {
		  table.add(new HiddenInput(PARAMETER_TIMEFRAME_ID, Integer.toString(tFrameId)),1,row);
		  table.add(new HiddenInput(PARAMETER_ADDRESS_ID, Integer.toString(addressId)),1,row);
		
		  try {
		    insertCategoryIntoTable(table, row, misc[i], prices);
		  }
		  catch (SQLException ex) {
		    ex.printStackTrace(System.err);
		  }
		  ++row;
		}
		return row;
	}
	
	private int insertSpecialPriceCategories(Table table, int row, PriceCategory[] specials, int tFrameId, int addressId) throws RemoteException{
		Text catName;

		if (specials.length > 0) {
		  ++row;
		  catName = getText(iwrb.getLocalizedString("travel.search.special_prices","Special prices"));
		  catName.setFontColor(TravelManager.WHITE);
		  table.add(catName, 1, row);
		  table.setRowColor(row, TravelManager.backgroundColor);
		}
		//System.out.println("[ProductPriceDesigner] timeframId = "+tFrameId);
		//System.out.println("[ProductPriceDesigner] addressId  = "+addressId);
		
		ProductPrice[] prices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(((Integer)_product.getPrimaryKey()).intValue(), tFrameId, addressId, false, 0, _currencyId, bf.getPriceCategorySearchKey());
		for (int i = 0; i < specials.length; i++) {
		  table.add(new HiddenInput(PARAMETER_TIMEFRAME_ID, Integer.toString(tFrameId)),1,row);
		  table.add(new HiddenInput(PARAMETER_ADDRESS_ID, Integer.toString(addressId)),1,row);
		
		  try {
				insertCategoryIntoTable(table, row, specials[i], prices);
		  }
		  catch (SQLException ex) {
			ex.printStackTrace(System.err);
		  }
		  ++row;
		}
		return row;		
	}

  private boolean priceCategorySave(IWContext iwc) {
    String[] timeframeIds = (String[]) iwc.getParameterValues(PARAMETER_TIMEFRAME_ID);
    String[] addressIds = (String[]) iwc.getParameterValues(PARAMETER_ADDRESS_ID);
    String[] priceDiscount = (String[]) iwc.getParameterValues(PARAMETER_PRICE_DISCOUNT);
    String[] maxUsage = (String[]) iwc.getParameterValues("max_usage");
    String[] priceCategoryIds = (String[]) iwc.getParameterValues(this.PARAMETER_PRODUCT_CATEGORY_ID);

    String[] productPriceIds = (String[]) iwc.getParameterValues(this.PARAMETER_PRODUCT_PRICE_ID);

    try {
      if (priceDiscount != null) {
        int priceCategoryId = 0;
        int productPriceId = -1;

        com.idega.block.trade.stockroom.data.ProductPriceBMPBean.clearPrices(((Integer)_product.getPrimaryKey()).intValue(), this._currencyId);
				com.idega.block.trade.stockroom.data.ProductPriceBMPBean.clearPrices(((Integer)_product.getPrimaryKey()).intValue(), this._currencyId, bf.getPriceCategorySearchKey());

        float price;
        int iMaxUsage;
        int iAddressId;
        int iTimeframeId;
        PriceCategory pCategory;
        ProductPrice pPrice;
        for (int i = 0; i < priceDiscount.length; i++) {
          pPrice = null;
          if (!priceDiscount[i].equals("")) {
            productPriceId = Integer.parseInt(productPriceIds[i]);
            priceCategoryId = Integer.parseInt(priceCategoryIds[i]);

            try {
              iAddressId = Integer.parseInt(addressIds[i]);
            } catch (NullPointerException ex) {
              iAddressId = -1;
            }

            try {
              iTimeframeId = Integer.parseInt(timeframeIds[i]);
            } catch (NullPointerException ex) {
              iTimeframeId = -1;
            }

            pCategory = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);

						//System.out.println("[ProductPriceDesigner] pCat = "+pCategory.getName()+", timeframeId = "+iTimeframeId+", addressId = "+iAddressId);

            if (pCategory.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)) {
              priceDiscount[i] = TextSoap.findAndReplace(priceDiscount[i],',','.');
              pPrice = getTravelStockroomBusiness(iwc).setPrice(productPriceId,((Integer)_product.getPrimaryKey()).intValue() , priceCategoryId, _currencyId,IWTimestamp.getTimestampRightNow(), Float.parseFloat(priceDiscount[i]), com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT, iTimeframeId, iAddressId);
            }else if (pCategory.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
              priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],".");
              if (priceDiscount[i].indexOf(",") > 0) {
                priceDiscount[i] = TextSoap.findAndCut(priceDiscount[i],",");
                price = (float) Float.parseFloat(priceDiscount[i]);
                price = price / 100;
              }else {
                price = (float) Float.parseFloat(priceDiscount[i]);
              }
              pPrice = getTravelStockroomBusiness(iwc).setPrice(productPriceId,((Integer)_product.getPrimaryKey()).intValue() , priceCategoryId, _currencyId,IWTimestamp.getTimestampRightNow(), price, com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE, iTimeframeId, iAddressId);
//						System.out.println("[ProductPriceDesigner] pCategory save price (id) : "+pPrice.getPrice()+ "("+pPrice.getID()+")");
            }
          }
        }
      }
      return true;
    }catch (Exception e) {
      e.printStackTrace(System.err);
      return false;
    }

  }

  private void insertCategoryIntoTable(Table table, int row, PriceCategory pCat, ProductPrice[] prices) throws SQLException, RemoteException{
    Text categoryName = getText(pCat.getName());
    Text infoText = getText(pCat.getName());
    TextInput priceDiscount = new TextInput(PARAMETER_PRICE_DISCOUNT);
    priceDiscount.setMarkupAttribute("style","font-size: 8pt");

    if (pCat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)) {
      infoText.setText("");
    }else if (pCat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
      priceDiscount.setSize(6);
      infoText.setText("%");
      infoText.addToText(Text.NON_BREAKING_SPACE);
      infoText.addToText(iwrb.getLocalizedString("travel.discount_of","discount of"));
      infoText.addToText(Text.NON_BREAKING_SPACE);
      infoText.addToText(((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(pCat.getParentId()).getName());
    }

    HiddenInput hi = new HiddenInput(this.PARAMETER_PRODUCT_PRICE_ID, "-1");

    int iMaxUsage = 0;
    for (int j = 0; j < prices.length; j++) {
      iMaxUsage = prices[j].getMaxUsage();
      if (pCat.getID() == prices[j].getPriceCategoryID()) {
        if (prices[j].getPriceType() == com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE) {
          priceDiscount.setContent(Integer.toString((int)prices[j].getPrice()));
        }else {
          priceDiscount.setContent(Float.toString(prices[j].getPrice()));
        }
        hi.setValue(prices[j].getID());
        break;
      }

    }
    table.add(hi, 1, row);


    ++row;
    table.add(new HiddenInput(this.PARAMETER_PRODUCT_CATEGORY_ID,Integer.toString(pCat.getID())),1,row);
    table.add(categoryName,1,row);
    table.add(priceDiscount,2,row);
    table.setAlignment(2,row,"right");
    table.setWidth(2,"150");
    table.add(infoText,3,row);

    table.setAlignment(3, row, "left");
    table.setRowColor(row,TravelManager.GRAY);

  }

  private Text getTimeframeText(Timeframe timeframe, IWContext iwc) {
    IWTimestamp from = new IWTimestamp(timeframe.getFrom());
    IWTimestamp to = new IWTimestamp(timeframe.getTo());
    Text text = new Text();
    text.setText(from.getLocaleDate(iwc)+ " - " + to.getLocaleDate(iwc) );
    return text;
  }

  private void settingsAreNull() {
    add(iwrb.getLocalizedString("travel.settings_not_defined","Settings not defined"));
  }

  private void productIsNull() {
    add(iwrb.getLocalizedString("travel.no_product_selected","No product selected"));
  }

  private void notLoggedOn() {
    add(iwrb.getLocalizedString("travel.session_expired","Session expired"));
  }

  public static Link getLink(Product product) throws RemoteException{
    return getLink( ((Integer)product.getPrimaryKey()).intValue());
  }

  public static Link getLink(int productId) {
    Link link = new Link();
    link.addParameter(PARAMETER_PRODUCT_ID, productId);
    link.setWindowToOpen(ProductPriceDesigner.class);

    return link;
  }

}