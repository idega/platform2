package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.presentation.ServiceSearchAdmin;
import is.idega.idegaweb.travel.block.search.presentation.ServiceSearchEditor;

import java.rmi.RemoteException;
import java.util.List;

import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Image;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;

public class TravelManager extends TravelBlock {

    Table table = new Table(2,2);


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
    protected static String parameterEngines = "lEngines";


    public static String theTextStyle = "font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+";";
    public static String theBoldTextStyle = "font-face: Verdana, Helvetica, sans-serif; font-size: "+Text.FONT_SIZE_10_STYLE_TAG+"; font-weight: bold;";

    private int tableWidth = 849;
    private boolean showLogo = true;

    public TravelManager(){
        super();
    }

    public void main(IWContext iwc) throws Exception{
    		super.main(iwc);
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
						
						Link engines = new Link(tsm.getIWResourceBundle().getLocalizedImageButton("travel.search_engines", "Search Engines"), ServiceSearchEditor.class);
						engines.addParameter(this.sAction, this.parameterEngines);
						table.add(engines, 1, 1);
						
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
        }else if (tsm.getSearchEngine() != null) {
					Link engines = new Link(tsm.getIWResourceBundle().getLocalizedImageButton("travel.search_engines", "Search Engines"), ServiceSearchAdmin.class);
					engines.addParameter(this.sAction, this.parameterEngines);
					table.add(engines, 1, 1);
					if (isInPermissionGroup) {
						//table.add(" admin walking...");
					}
        }

        Link lHome = new Link(iHome,LoginPage.class);
          lHome.addParameter(this.sAction, this.parameterHome);
        table.add(lHome,2,1);

        if (expiredLogin) {
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
    		if (super.tsm == null) {
    			super.initializer(iwc);
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
            backLink.setMarkupAttribute("onClick","history.go(-"+backUpHowManyPages+")");

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


    
}
