package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import java.rmi.RemoteException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.user.data.User;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.Page;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.Window;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TravelWindow extends Window {

  private Table table = new Table(3,2);
  protected Page jPage;


  protected Text text = new Text();
  protected IWResourceBundle iwrb;
  protected IWBundle iwb;
  protected User user = null;
  protected int userId = -1;
  protected boolean isSuperAdmin = false;
  
  public static String LOCALIZATION_KEY = "locK";
  public static String LOCALIZATION_KEY_FOR_HEADER = "locK_h";
  
  protected Image headerImage;

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";


  public TravelWindow() {
  	super.setTitle("idegaWEB travel");
  	super.setScrollbar(true);
  	super.setResizable(true);
  }

  public void add(PresentationObject mo) {
    table.add(mo,2,2);
  }

  public void add(String s) {
    table.add(s,2,2);
  }

  public void main(IWContext iwc) throws Exception{
  		initialize(iwc);
    setTemplate(iwc);
    super.add(table);
    
    String locKeyH = iwc.getParameter(LOCALIZATION_KEY_FOR_HEADER);
    String locKey = iwc.getParameter(LOCALIZATION_KEY);
    if(locKeyH != null || locKey != null) {
    	this.add(Text.BREAK);
    	Table table = new Table();
    	table.setWidth("75%");
    	table.setAlignment(Table.HORIZONTAL_ALIGN_CENTER);
    	if (locKeyH != null && iwrb.getLocalizedString(locKeyH) != null) {
	    	Text text = getTextHeader(iwrb.getLocalizedString(locKeyH, locKeyH));
	    	text.setFontColor("BLACK");
	    	table.add(text, 1, 1);
	    }
	    if (locKey != null && iwrb.getLocalizedString(locKey) != null) {
	    	Text text = getText(iwrb.getLocalizedString(locKey, locKey));
	    	table.add(text, 1, 2);
	    }
	    this.add(table);
    }
  }

  public void close(boolean reloadParent) {
    if (reloadParent)
      jPage.setParentToReload();
    jPage.close();

  }

  protected void initialize(IWContext iwc) throws RemoteException{
    user = LoginBusinessBean.getUser(iwc);
    iwb = getBundle(iwc);
    iwrb = iwb.getResourceBundle(iwc);
    if (user != null) {
      userId = user.getID();
      isSuperAdmin = iwc.isSuperAdmin();
    }

    try {
        int supplierId = getTravelStockroomBusiness(iwc).getUserSupplierId(iwc);
        SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
        Supplier supplier = suppHome.findByPrimaryKey(supplierId);
        if (!supplier.getIsValid()) {
//          oldLogin = true;
        }else {
          getTravelSessionManager(iwc).setSupplier(supplier);
        }
    }
    catch (Exception e) {
      //e.printStackTrace(System.err);
      debug(e.getMessage());
    }

    try {
        int resellerId = getTravelStockroomBusiness(iwc).getUserResellerId(iwc);
        Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
        if (!reseller.getIsValid()) {
          //reseller = null;
//          oldLogin = true;
        } else {
          getTravelSessionManager(iwc).setReseller(reseller);
        }
    }
    catch (Exception e) {
      debug(e.getMessage());
    }


  }
  private void setTemplate(IWContext iwc) {
    //iwrb = super.getResourceBundle(iwc);


    table.setWidth("100%");
    table.setBorder(0);
    table.setCellpadding(0);
    table.setCellspacing(0);

    jPage = super.getPage(iwc);
      jPage.setAllMargins(0);


		if (headerImage == null) {
    	headerImage = iwb.getImage("buttons/iWTravel.gif");
			table.setColor(1,1,TravelManager.backgroundColor);
			table.setColor(2,1,TravelManager.backgroundColor);
			table.setColor(3,1,TravelManager.backgroundColor);
		}

    table.mergeCells(1,1,3,1);
    table.add(headerImage,1,1);

    text.setFontColor(TravelManager.BLACK);
    text.setFontSize(Text.FONT_SIZE_10_HTML_2);
  }

  protected Text getText(String content) {
    Text text = new Text(content);
      text.setFontStyle(TravelManager.theTextStyle);
      text.setFontColor(TravelManager.BLACK);
    return text;
  }

  protected Text getTextHeader(String content) {
    Text text = getText(content);
      text.setBold(true);
      text.setFontColor(TravelManager.WHITE);
    return text;
  }

  protected Text getTextBold(String content) {
    Text text = new Text(content);
    text.setStyle(TravelManager.theBoldTextStyle);
    return text;
  }

	public void setHeaderImage(Image image) {
		this.headerImage = image;
	}

	protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
		return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
	}
	
	protected TravelStockroomBusiness getBusiness(IWApplicationContext iwac, Product product) {
		try {
			return getServiceHandler(iwac).getServiceBusiness(product);
		}
		catch (Exception e) {
			throw new IBORuntimeException(e);
		}
	}
	
  protected Booker getBooker(IWApplicationContext iwac) throws RemoteException{
    return (Booker) IBOLookup.getServiceInstance(iwac, Booker.class);
  }

  protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }
  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }

  protected TravelSessionManager getTravelSessionManager(IWContext iwc) throws RemoteException {
    return TravelManager.getTravelSessionManagerStatic(iwc);
//    return (TravelSessionManager) IBOLookup.getServiceInstance(iwc, TravelSessionManager.class);
  }
  
  protected CreditCardBusiness getCreditCardBusiness(IWContext iwc) {
  	try {
  		return (CreditCardBusiness) IBOLookup.getServiceInstance(iwc, CreditCardBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
}
