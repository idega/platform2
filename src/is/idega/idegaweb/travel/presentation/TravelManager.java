package is.idega.idegaweb.travel.presentation;

import java.util.Locale;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import javax.servlet.jsp.JspPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.presentation.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import java.sql.SQLException;
import com.idega.block.login.presentation.Login;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.data.*;
import com.idega.block.login.business.*;
import com.idega.core.user.data.User;
import java.util.List;

public class TravelManager extends Block {

    public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
    private IWBundle bundle;
    private IWResourceBundle iwrb;
    Table table = new Table(2,2);

    private Supplier supplier;
    private Reseller reseller;
    public User user;
    public int userId = -1;

    private boolean oldLogin = false;

    protected Text theText = new Text();
    protected Text theBoldText = new Text();
    protected Text theBigBoldText = new Text();
    protected Text smallText = new Text();
    protected Text theSmallBoldText = new Text();

    public static String backgroundColor = "#235BA8" ;
   // public static String backgroundColor = "#1A4B8E";
    public static String textColor = "#FFFFFF";
//    public static String textColor = "#666699";

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

    protected int _localeId = -1;
    protected Locale _locale;

    public TravelManager(){
        super();
    }


    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }

    public IWBundle getBundle() {
      return bundle;
    }

    public IWResourceBundle getResourceBundle() {
      return iwrb;
    }


    public Reseller getReseller() {
      return reseller;
    }

    public Supplier getSupplier() {
        return supplier;
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

    protected boolean hasLoginExpired(IWContext iwc) {
      return (!iwc.hasEditPermission(this) && supplier == null && reseller == null);
    }

    protected boolean isLoggedOn(IWContext iwc) {
      return !hasLoginExpired(iwc);
    }

    protected Table getLogin(IWContext iwc) {
      LoginPage lp = new LoginPage();
      return lp.getLoginTable(iwc, bundle, iwrb);
    }

    protected Table getLoggedOffTable(IWContext iwc) {
      LoginPage lp = new LoginPage();
      return lp.getLoginTable(iwc, bundle, iwrb);
    }

    public void draw(IWContext iwc) {

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

        Image iDesign = iwrb.getImage("buttons/design_products.gif");
        Image iMyTrip = iwrb.getImage("buttons/my_products.gif");
        //Image iDesign = iwrb.getImage("buttons/design_trip.gif");
        //Image iMyTrip = iwrb.getImage("buttons/my_trips.gif");
        Image iOverview = iwrb.getImage("buttons/booking_overview.gif");
        Image iBooking = iwrb.getImage("buttons/booking.gif");
        Image iStatistics = iwrb.getImage("buttons/statistics.gif");
        Image iDailyReport = iwrb.getImage("buttons/daily_report.gif");
        Image iContracts = iwrb.getImage("buttons/contracts.gif");
        Image iInitialData = iwrb.getImage("buttons/initial_data.gif");
        Image iUpdatePassword = iwrb.getImage("buttons/update_password.gif");
        Image iHome = iwrb.getImage("buttons/home.gif");

        if (action.equals(this.parameterServiceDesigner)) {
          iDesign = iwrb.getImage("buttons/design_products_on.gif");
          //iDesign = iwrb.getImage("buttons/design_trip_on.gif");
        }else if (action.equals(this.parameterServiceOverview)) {
          iMyTrip = iwrb.getImage("buttons/my_products_on.gif");
          //iMyTrip = iwrb.getImage("buttons/my_trips_on.gif");
        }else if (action.equals(this.parameterBookingOverview)) {
          iOverview = iwrb.getImage("buttons/booking_overview_on.gif");
        }else if (action.equals(this.parameterBooking)) {
          iBooking = iwrb.getImage("buttons/booking_on.gif");
        }else if (action.equals(this.parameterStatistics)) {
          iStatistics = iwrb.getImage("buttons/statistics_on.gif");
        }else if (action.equals(this.parameterDailyReport)) {
          iDailyReport = iwrb.getImage("buttons/daily_report_on.gif");
        }else if (action.equals(this.parameterContracts)) {
          iContracts = iwrb.getImage("buttons/contracts_on.gif");
        }else if (action.equals(this.parameterInitialData)) {
          iInitialData = iwrb.getImage("buttons/initial_data_on.gif");
        }else if (action.equals(this.parameterUpdatePassword)) {
          iUpdatePassword = iwrb.getImage("buttons/update_password_on.gif");
        }else if (action.equals(this.parameterHome)) {
          iHome = iwrb.getImage("buttons/home_on.gif");
          showLogo = false;
        }else {
          iHome = iwrb.getImage("buttons/home_on.gif");
        }

        if (iwc.hasEditPermission(this)){
            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,this.parameterInitialData);
            table.add(lInitialData,1,1);

            Link lUpdatePassword = new Link(iUpdatePassword);
              lUpdatePassword.setWindowToOpen(LoginChanger.class);
            table.add(lUpdatePassword,1,1);

        }else if (supplier != null) {
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
            Link lDailyReport = new Link(iDailyReport,DailyReport.class);
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
        }else if (reseller!= null) {
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
          this.add(iwrb.getLocalizedString("travel.no_permission","No permission"));
        }

        Table logoTable = new Table(1,1);
          logoTable.setCellpadding(0);
          logoTable.setCellspacing(0);
          logoTable.setAlignment("center");
          logoTable.setWidth(tableWidth);
          logoTable.add(iwrb.getImage("images/admin_logo.gif"));
          logoTable.setAlignment(1,1,"left");

        if (showLogo)
        super.add(logoTable);
        super.add(table);
    }


    public void initializer(IWContext iwc) {
        bundle = getBundle(iwc);
        iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());
        user = LoginBusiness.getUser(iwc);
        _localeId = iwc.getCurrentLocaleId();
        _locale = iwc.getCurrentLocale();

        if (user != null) {
          userId = user.getID();
          isSuperAdmin = iwc.isSuperAdmin();
        }



        try {
            int supplierId = TravelStockroomBusiness.getUserSupplierId(iwc);
            supplier = new Supplier(supplierId);
            if (!supplier.getIsValid()) {
              supplier = null;
              oldLogin = true;
            }
        }
        catch (Exception e) {
        }

        try {
            int resellerId = TravelStockroomBusiness.getUserResellerId(iwc);
            reseller = new Reseller(resellerId);
            if (!reseller.getIsValid()) {
              reseller = null;
              oldLogin = true;
            }
        }
        catch (Exception e) {
        }

        //theText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        //theText.setFontFace(Text.FONT_FACE_VERDANA+", Helvetiva, sans-serif");
        theText.setFontColor(this.textColor);
        //theBoldText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        //theBoldText.setFontFace(Text.FONT_FACE_VERDANA+", Helvetiva, sans-serif");
        //theBoldText.setBold();
        theBigBoldText.setFontColor(this.textColor);
        theBigBoldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_12_STYLE_TAG+"; font-weight: bold;");
        theBoldText.setFontColor(this.textColor);
        theText.setFontStyle(theTextStyle);
        theBoldText.setFontStyle(theBoldTextStyle);
        smallText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_7_STYLE_TAG+";");
        theSmallBoldText.setFontStyle("font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_7_STYLE_TAG+"; font-weight: bold;");

        //smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        //smallText.setFontFace(Text.FONT_FACE_VERDANA+", Helvetiva, sans-serif");
        smallText.setFontColor(this.textColor);
        //theSmallBoldText.setFontFace(Text.FONT_FACE_VERDANA+", Helvetiva, sans-serif");
        //theSmallBoldText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        //theSmallBoldText.setBold();
        theSmallBoldText.setFontColor(this.textColor);

        this.isInPermissionGroup = this.isInPermissionGroup(iwc);
    }

    public void add(PresentationObject mo) {
        table.add(mo,1,2);
    }

    public void add(String string) {
      Text text = (Text) theText.clone();
        text.setText(string);
      this.add(text);
    }

    protected String getNextZebraColor(String color1, String color2, String currentColor) {
      if (currentColor.equals(color1)) {
        return color2;
      }else{
        return color1;
      }
    }

    protected Link getBackLink(int backUpHowManyPages) {
        Link backLink = new Link(iwrb.getImage("buttons/back.gif"),"#");
            backLink.setAttribute("onClick","history.go(-"+backUpHowManyPages+")");

        return backLink;
    }

    protected Link getBackLink() {
        return getBackLink(1);
    }

    public static Image getDefaultImage(IWResourceBundle _iwrb) {
      return _iwrb.getImage("images/picture.gif");
    }


    protected boolean isInPermissionGroup(IWContext iwc) {
      return isInPermissionGroup(iwc, user);
    }

    protected boolean isInPermissionGroup(IWContext iwc, User user) {
      if (user != null) {
        PermissionGroup pGroup = null;
        try {
          if (reseller != null) {
            pGroup = ResellerManager.getPermissionGroup(reseller);
          }
          else if (supplier != null) {
            pGroup = SupplierManager.getPermissionGroup(supplier);
          }

          if (pGroup != null) {
            com.idega.core.business.UserGroupBusiness ugb = new com.idega.core.business.UserGroupBusiness();
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
}
