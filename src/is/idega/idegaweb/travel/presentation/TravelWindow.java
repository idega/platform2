package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.*;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.business.IBOLookup;
import java.rmi.RemoteException;
import com.idega.idegaweb.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import is.idega.idegaweb.travel.presentation.TravelManager;

import com.idega.core.user.data.User;
import com.idega.block.login.business.LoginBusiness;

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

  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";


  public TravelWindow() {
  }

  public void add(PresentationObject mo) {
    table.add(mo,2,2);
  }

  public void add(String s) {
    table.add(s,2,2);
  }


  public void main(IWContext iwc) throws Exception{
    setTemplate(iwc);
    initialize(iwc);
    super.add(table);
  }

  protected void close(boolean reloadParent) {
    if (reloadParent)
      jPage.setParentToReload();
    jPage.close();

  }

  private void initialize(IWContext iwc) throws RemoteException{
    user = LoginBusiness.getUser(iwc);
    if (user != null) {
      userId = user.getID();
      isSuperAdmin = iwc.isSuperAdmin();
    }
  }
  private void setTemplate(IWContext iwc) {
    //iwrb = super.getResourceBundle(iwc);
    iwb = getBundle(iwc);
    iwrb = iwb.getResourceBundle(iwc);

    table.setWidth("100%");
    table.setBorder(0);
    table.setCellpadding(0);
    table.setCellspacing(0);

    jPage = super.getPage(iwc);
      jPage.setAllMargins(0);

    table.setColor(1,1,TravelManager.backgroundColor);
    table.setColor(2,1,TravelManager.backgroundColor);
    table.setColor(3,1,TravelManager.backgroundColor);

    Image logo = iwb.getImage("buttons/iWTravel.gif");

    table.mergeCells(1,1,2,1);
    table.add(logo,1,1);

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
}
