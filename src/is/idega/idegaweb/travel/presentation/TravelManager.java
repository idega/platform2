package is.idega.idegaweb.travel.presentation;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.core.accesscontrol.data.*;
import com.idega.core.user.data.*;
import com.idega.data.*;
import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.service.business.*;

public class TravelManager extends Block {

    Table table = new Table(2,2);
    TravelSessionManager tsm;

    private boolean oldLogin = false;

    protected Text theText = new Text();
    protected Text theBoldText = new Text();
    protected Text theBigBoldText = new Text();
    protected Text smallText = new Text();
    protected Text theSmallBoldText = new Text();

    public static String backgroundColor = "#235BA8" ;
    public static String textColor = "#FFFFFF";

    public static String YELLOW = "#FFFFCC";
    public static String GREEN = "#99FF99";
    public static String ORANGE = "#FCCB66";
    public static String RED = "#F19393";
    public static String BLUE = "#99CCFF";
    public static String LIGHTBLUE = "#D5D7EA";
    public static String LIGHTGREEN = "#CCFFCC";
    public static String LIGHTORANGE = "#FFCC99";
    public static String DARKBLUE = "#85839D";
    public static String WHITE = "#FFFFFF";
    public static String GRAY = "#CCCCCC";
    public static String BLACK = "#000000";

    protected static String sAction = "travelManagerAction";
    protected static String parameterServiceDesigner = "lServiceDesigner";
    protected static String parameterServiceOverview = "lServiceOverview";
    protected static String parameterBookingOverview = "lBookingOverview";
    protected static String parameterBooking = "lBooking";
    protected static String parameterStatistics = "lStatistics";
    protected static String parameterDailyReport = "lDailyReport";
    protected static String parameterContracts = "lContracts";
    protected static String parameterInitialData = "lInitialData";
    protected static String parameterUpdatePassword = "lUpdatePassword";
    protected static String parameterHome = "lHome";

    protected boolean isInPermissionGroup = false;
    protected boolean isSuperAdmin = false;

    public static String theTextStyle = "font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+";";
    public static String theBoldTextStyle = "font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+"; font-weight: bold;";

    private int tableWidth = 849;
    private boolean showLogo = true;

    public TravelManager(){
        super();
    }


    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }

    public IWBundle getBundle() throws RemoteException{
      return tsm.getIWBundle();
    }

    /**
     * @deprecated
     * @param iwc IWContext
     * @return IWResourceBundle
     * @throws RemoteException
     */
    public IWResourceBundle getResourceBundle(IWContext iwc)  throws RemoteException{
      return tsm.getIWResourceBundle();
    }

    public IWResourceBundle getResourceBundle()  throws RemoteException{
      return tsm.getIWResourceBundle();
    }

    public Locale getLocale() throws RemoteException {
      return tsm.getLocale();
    }

    public int getLocaleId() throws RemoteException {
      return tsm.getLocaleId();
    }

    public Reseller getReseller() throws RemoteException{
      return tsm.getReseller();
    }

    public Supplier getSupplier() throws RemoteException {
        return tsm.getSupplier();
    }

    public User getUser() throws RemoteException {
      return tsm.getUser();
    }

    public int getUserId() throws RemoteException {
      return tsm.getUserId();
    }

    public void main(IWContext iwc) throws Exception{
      initializer(iwc);
      showLogo = isLoggedOn(iwc);
      draw(iwc);
    }

    private boolean isLoginPage(IWContext iwc) {
        String action = iwc.getParameter(this.sAction);
        if (action != null)
        if (action.equals(this.parameterHome)) {
          return true;
        }
        return false;
    }

    protected boolean hasLoginExpired(IWContext iwc) throws RemoteException {
      return (!iwc.hasEditPermission(this) && tsm.getSupplier() == null && tsm.getReseller() == null);
    }

    protected boolean isLoggedOn(IWContext iwc) throws RemoteException {
      return !hasLoginExpired(iwc);
    }

    protected Table getLogin(IWContext iwc)  throws RemoteException{
      LoginPage lp = new LoginPage();
      return lp.getLoginTable(iwc, tsm.getIWBundle(), tsm.getIWResourceBundle());
    }

    protected Table getLoggedOffTable(IWContext iwc)  throws RemoteException{
      LoginPage lp = new LoginPage();
      return lp.getLoginTable(iwc, tsm.getIWBundle(), tsm.getIWResourceBundle());
    }

    public void draw(IWContext iwc)  throws RemoteException{

        table.setBorder(0);
        table.setHeight("100%");
        table.setCellpadding(0);
        table.setCellspacing(0);
        table.setColor(1,2,this.backgroundColor);
        table.setVerticalAlignment(1,1,"top");
        table.setVerticalAlignment(1,2,"top");
        table.setAlignment(1,1,"left");
        table.setAlignment(1,2,"center");
        table.setAlignment("center");
        table.mergeCells(1,2,2,2);
        table.setAlignment(2,1,"right");
        table.setHeight(1,2,"100%");
        table.setWidth(tableWidth);

        String action = iwc.getParameter(this.sAction);
        if (action == null) {
          action = (String) iwc.getSessionAttribute(this.sAction);
          if (action == null) {
            action ="";
          }
        }else {
          iwc.setSessionAttribute(sAction, action);
        }

        Image iDesign = tsm.getIWResourceBundle().getImage("buttons/design_products.gif");
        Image iMyTrip = tsm.getIWResourceBundle().getImage("buttons/my_products.gif");
        Image iOverview = tsm.getIWResourceBundle().getImage("buttons/booking_overview.gif");
        Image iBooking = tsm.getIWResourceBundle().getImage("buttons/booking.gif");
        Image iStatistics = tsm.getIWResourceBundle().getImage("buttons/statistics.gif");
        Image iDailyReport = tsm.getIWResourceBundle().getImage("buttons/daily_report.gif");
        Image iContracts = tsm.getIWResourceBundle().getImage("buttons/contracts.gif");
        Image iInitialData = tsm.getIWResourceBundle().getImage("buttons/initial_data.gif");
        Image iUpdatePassword = tsm.getIWResourceBundle().getImage("buttons/update_password.gif");
        Image iHome = tsm.getIWResourceBundle().getImage("buttons/home.gif");

        if (action.equals(this.parameterServiceDesigner)) {
          iDesign = tsm.getIWResourceBundle().getImage("buttons/design_products_on.gif");
        }else if (action.equals(this.parameterServiceOverview)) {
          iMyTrip = tsm.getIWResourceBundle().getImage("buttons/my_products_on.gif");
        }else if (action.equals(this.parameterBookingOverview)) {
          iOverview = tsm.getIWResourceBundle().getImage("buttons/booking_overview_on.gif");
        }else if (action.equals(this.parameterBooking)) {
          iBooking = tsm.getIWResourceBundle().getImage("buttons/booking_on.gif");
        }else if (action.equals(this.parameterStatistics)) {
          iStatistics = tsm.getIWResourceBundle().getImage("buttons/statistics_on.gif");
        }else if (action.equals(this.parameterDailyReport)) {
          iDailyReport = tsm.getIWResourceBundle().getImage("buttons/daily_report_on.gif");
        }else if (action.equals(this.parameterContracts)) {
          iContracts = tsm.getIWResourceBundle().getImage("buttons/contracts_on.gif");
        }else if (action.equals(this.parameterInitialData)) {
          iInitialData = tsm.getIWResourceBundle().getImage("buttons/initial_data_on.gif");
        }else if (action.equals(this.parameterUpdatePassword)) {
          iUpdatePassword = tsm.getIWResourceBundle().getImage("buttons/update_password_on.gif");
        }else if (action.equals(this.parameterHome)) {
          iHome = tsm.getIWResourceBundle().getImage("buttons/home_on.gif");
          showLogo = false;
        }else {
          iHome = tsm.getIWResourceBundle().getImage("buttons/home_on.gif");
        }

        if (isTravelAdministrator(iwc)){
            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,this.parameterInitialData);
            table.add(lInitialData,1,1);

            Link lResellers = new Link(iContracts, ResellerCreator.class);
              lResellers.addParameter(this.sAction, this.parameterContracts);
            table.add(lResellers, 1, 1);

            Link lReports = new Link(iDailyReport, AdministratorReports.class);
              lReports.addParameter(this.sAction, this.parameterDailyReport);
            table.add(lReports, 1, 1);

						List links = getServiceHandler(iwc).getServiceLinks(tsm.getIWResourceBundle());
						for (int i = 0; i < links.size(); i++) {
							table.add( (Link) links.get(i), 1, 1);
						}
						
            Link lUpdatePassword = new Link(iUpdatePassword);
              lUpdatePassword.setWindowToOpen(LoginChanger.class);
            table.add(lUpdatePassword,1,1);
            
        }else if (tsm.getSupplier() != null) {
            Link lDesign = new Link(iDesign,ServiceDesigner.class);
              lDesign.addParameter(this.sAction,this.parameterServiceDesigner);
            Link lMyTrip = new Link(iMyTrip,ServiceOverview.class);
              lMyTrip.addParameter(this.sAction,this.parameterServiceOverview);
            Link lOverview = new Link(iOverview,BookingOverview.class);
              lOverview.addParameter(this.sAction,this.parameterBookingOverview);
            Link lBooking = new Link(iBooking,Booking.class);
              lBooking.addParameter(this.sAction,this.parameterBooking);
            Link lStatistics = new Link(iStatistics,Statistics.class);
              lStatistics.addParameter(this.sAction,this.parameterStatistics);
            Link lDailyReport = new Link(iDailyReport,Reports.class);
              lDailyReport.addParameter(this.sAction,this.parameterDailyReport);
            Link lContracts = new Link(iContracts,Contracts.class);
              lContracts.addParameter(this.sAction,this.parameterContracts);
            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,this.parameterInitialData);

            if (this.isInPermissionGroup)
            table.add(lDesign,1,1);
            table.add(lMyTrip,1,1);
            table.add(lOverview,1,1);
            table.add(lBooking,1,1);
            table.add(lStatistics,1,1);
            table.add(lDailyReport,1,1);
            table.add(lContracts,1,1);
            table.add(lInitialData,1,1);

            Link lUpdatePassword = new Link(iUpdatePassword);
              lUpdatePassword.setWindowToOpen(LoginChanger.class);
            table.add(lUpdatePassword,1,1);
        }else if (tsm.getReseller()!= null) {
            Link lMyTrip = new Link(iMyTrip,ServiceOverview.class);
              lMyTrip.addParameter(this.sAction,this.parameterServiceOverview);
            table.add(lMyTrip,1,1);
            Link lBooking = new Link(iBooking,Booking.class);
              lBooking.addParameter(this.sAction,this.parameterBooking);
            table.add(lBooking,1,1);
            Link lOverview = new Link(iOverview,BookingOverview.class);
              lOverview.addParameter(this.sAction,this.parameterBookingOverview);
            table.add(lOverview,1,1);
            Link lContracts = new Link(iContracts,Contracts.class);
              lContracts.addParameter(this.sAction,this.parameterContracts);
            table.add(lContracts,1,1);
            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,this.parameterInitialData);
            table.add(lInitialData,1,1);

            Link lUpdatePassword = new Link(iUpdatePassword);
              lUpdatePassword.setWindowToOpen(LoginChanger.class);
            table.add(lUpdatePassword,1,1);
        }

        Link lHome = new Link(iHome,LoginPage.class);
          lHome.addParameter(this.sAction, this.parameterHome);
        table.add(lHome,2,1);

        if (oldLogin) {
          this.add(tsm.getIWResourceBundle().getLocalizedString("travel.no_permission","No permission"));
        }

        Table logoTable = new Table(1,1);
          logoTable.setCellpadding(0);
          logoTable.setCellspacing(0);
          logoTable.setAlignment("center");
          logoTable.setWidth(tableWidth);
          logoTable.add(tsm.getIWResourceBundle().getImage("images/admin_logo.gif"));
          logoTable.setAlignment(1,1,"left");

        if (showLogo)
        super.add(logoTable);
        super.add(table);
    }


    public void initializer(IWContext iwc) throws RemoteException {
      tsm = getTravelSessionManager(iwc);

      if (!isTravelAdministrator(iwc)) {
        try {
            int supplierId = getTravelStockroomBusiness(iwc).getUserSupplierId(iwc);
            SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
            Supplier supplier = suppHome.findByPrimaryKey(supplierId);
            if (!supplier.getIsValid()) {
              //supplier = null;
              oldLogin = true;
            }else {
              tsm.setSupplier(supplier);
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
              oldLogin = true;
            } else {
              tsm.setReseller(reseller);
            }
        }
        catch (Exception e) {
          debug(e.getMessage());
        }
      }

        theText.setFontColor(this.textColor);
        theBigBoldText.setFontColor(this.textColor);
        theBigBoldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_12_STYLE_TAG+"; font-weight: bold;");
        theBoldText.setFontColor(this.textColor);
        theText.setFontStyle(theTextStyle);
        theBoldText.setFontStyle(theBoldTextStyle);
        smallText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_7_STYLE_TAG+";");
        theSmallBoldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_7_STYLE_TAG+"; font-weight: bold;");

        smallText.setFontColor(this.textColor);
        theSmallBoldText.setFontColor(this.textColor);

        this.isInPermissionGroup = this.isInPermissionGroup(iwc);
    }

    public void add(PresentationObject mo) {
        table.add(mo,1,2);
    }

    public void addToBlock(PresentationObject po) {
      super.add(po);
    }

    public void add(String string) {
      Text text = (Text) theText.clone();
        text.setText(string);
      this.add(text);
    }

    public void addToBlock(String string) {
      Text text = (Text) theText.clone();
        text.setText(string);
      super.add(text);
    }
    protected String getNextZebraColor(String color1, String color2, String currentColor) {
      if (currentColor.equals(color1)) {
        return color2;
      }else{
        return color1;
      }
    }

    protected Link getBackLink(int backUpHowManyPages) throws RemoteException {
        Link backLink = new Link(tsm.getIWResourceBundle().getImage("buttons/back.gif"),"#");
            backLink.setAttribute("onClick","history.go(-"+backUpHowManyPages+")");

        return backLink;
    }

    protected Link getBackLink() throws RemoteException {
        return getBackLink(1);
    }

    /**
     * @deprecated
     * @param _iwrb IWResourceBundle
     * @return default bundle image
     */
    public static Image getDefaultImage(IWResourceBundle _iwrb) {
      return _iwrb.getImage("images/picture.gif");
    }


    protected boolean isInPermissionGroup(IWContext iwc) throws RemoteException {
      return isInPermissionGroup(iwc, tsm.getUser());
    }

    protected boolean isInPermissionGroup(IWContext iwc, User user) throws RemoteException {
      if (user != null) {
        PermissionGroup pGroup = null;
        try {
          if (tsm.getReseller() != null) {
            pGroup = ResellerManager.getPermissionGroup(tsm.getReseller());
          }
          else if (tsm.getSupplier() != null) {
            pGroup = SupplierManager.getPermissionGroup(tsm.getSupplier());
          }

          if (pGroup != null) {
            com.idega.core.user.business.UserGroupBusiness ugb = new com.idega.core.user.business.UserGroupBusiness();
            List allUsers = ugb.getUsersContained(pGroup);
            if (allUsers != null) {
              return allUsers.contains(user);
            }
          }
        }catch (SQLException sql) {
          sql.printStackTrace(System.err);
        }

      }
      return false;

    }

    public static Table getTable() {
      Table table = new Table();
        table.setCellpaddingAndCellspacing(1);
        table.setColor(WHITE);
      return table;
    }

    protected Text getText(String content) {
      Text text = (Text) this.theText.clone();
        text.setText(content);
        text.setFontColor(BLACK);
      return text;
    }

    protected Text getHeaderText(String content) {
      Text text = getText(content);
        text.setFontColor(WHITE);
        text.setBold(true);
      return text;
    }

    protected Booker getBooker(IWApplicationContext iwac) throws RemoteException{
      return (Booker) IBOLookup.getServiceInstance(iwac, Booker.class);
    }

    protected Assigner getAssigner(IWApplicationContext iwac) throws RemoteException {
      return (Assigner) IBOLookup.getServiceInstance(iwac, Assigner.class);
    }

    protected Inquirer getInquirer(IWApplicationContext iwac) throws RemoteException {
      return (Inquirer) IBOLookup.getServiceInstance(iwac, Inquirer.class);
    }

    protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
      return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
    }

    protected ProductCategoryFactory getProductCategoryFactory(IWApplicationContext iwac) throws RemoteException {
      return (ProductCategoryFactory) IBOLookup.getServiceInstance(iwac, ProductCategoryFactory.class);
    }

    protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
      return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
    }

    protected ContractBusiness getContractBusiness(IWApplicationContext iwac) throws RemoteException {
      return (ContractBusiness) IBOLookup.getServiceInstance(iwac, ContractBusiness.class);
    }

    protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
      return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
    }

    protected boolean isTravelAdministrator(IWContext iwc) {
      return iwc.hasEditPermission(this);
    }

    protected TravelSessionManager getTravelSessionManager(IWContext iwc) throws RemoteException{
      return TravelManager.getTravelSessionManagerStatic(iwc);
    }

    public static TravelSessionManager getTravelSessionManagerStatic(IWContext iwc) throws RemoteException{
      return (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
    }
}
