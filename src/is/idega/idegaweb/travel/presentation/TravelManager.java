package is.idega.travel.presentation;

import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import javax.servlet.jsp.JspPage;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.travel.business.TravelStockroomBusiness;
import is.idega.travel.presentation.*;
import com.idega.block.trade.stockroom.data.*;
import java.sql.SQLException;
import com.idega.core.accesscontrol.business.AccessControl;

public class TravelManager extends Block {

    public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
    private IWBundle bundle;
    private IWResourceBundle iwrb;
    Table table = new Table(2,2);

    private Supplier supplier;
    private Reseller reseller;

    protected Text theText = new Text();
    protected Text theBoldText = new Text();
    protected Text smallText = new Text();
    protected Text theSmallBoldText = new Text();

    public static String backgroundColor = "#D5D7EA";
    public static String textColor = "#666699";

    public static String YELLOW = "#FFFFCC";
    public static String GREEN = "#99FF99";
    public static String ORANGE = "#FCCB66";
    public static String RED = "#F19393";
    public static String BLUE = "#99CCFF";
    public static String LIGHTGREEN = "#CCFFCC";
    public static String LIGHTORANGE = "#FFCC99";
    public static String DARKBLUE = "#85839D";

    private static String sAction = "travelManagerAction";


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

    public void main(IWContext iwc) throws SQLException{
        initializer(iwc);

        String action = iwc.getParameter(this.sAction);
        if (action == null) {
          action = (String) iwc.getSessionAttribute(this.sAction);
          if (action == null) {
            action ="";
          }
        }else {
          iwc.setSessionAttribute(sAction, action);
        }


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
          table.setWidth("850");

        Image iDesign = iwrb.getImage("buttons/design_trip.gif");
        Image iMyTrip = iwrb.getImage("buttons/my_trips.gif");
        Image iOverview = iwrb.getImage("buttons/booking_overview.gif");
        Image iBooking = iwrb.getImage("buttons/booking.gif");
        Image iStatistics = iwrb.getImage("buttons/statistics.gif");
        Image iDailyReport = iwrb.getImage("buttons/daily_report.gif");
        Image iContracts = iwrb.getImage("buttons/contracts.gif");
        Image iInitialData = iwrb.getImage("buttons/initial_data.gif");
        if (action.equals("lDesign")) {
          iDesign = iwrb.getImage("buttons/design_trip_on.gif");
        }else if (action.equals("lMyTrip")) {
          iMyTrip = iwrb.getImage("buttons/my_trips_on.gif");
        }else if (action.equals("lOverview")) {
          iOverview = iwrb.getImage("buttons/booking_overview_on.gif");
        }else if (action.equals("lBooking")) {
          iBooking = iwrb.getImage("buttons/booking_on.gif");
        }else if (action.equals("lStatistics")) {
          iStatistics = iwrb.getImage("buttons/statistics_on.gif");
        }else if (action.equals("lDailyReport")) {
          iDailyReport = iwrb.getImage("buttons/daily_report_on.gif");
        }

        if ( AccessControl.isAdmin(iwc)){

            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,"lInitialData");
            table.add(lInitialData,1,1);

            Link lHome = new Link("heim","/index.jsp");
            table.add(lHome,2,1);
        }else if (supplier != null) {

            Link lDesign = new Link(iDesign,ServiceDesigner.class);
              lDesign.addParameter(this.sAction,"lDesign");
            Link lMyTrip = new Link(iMyTrip,ServiceOverview.class);
              lMyTrip.addParameter(this.sAction,"lMyTrip");
            Link lOverview = new Link(iOverview,BookingOverview.class);
              lOverview.addParameter(this.sAction,"lOverview");
            Link lBooking = new Link(iBooking,Booking.class);
              lBooking.addParameter(this.sAction,"lBooking");
            Link lStatistics = new Link(iStatistics,Statistics.class);
              lStatistics.addParameter(this.sAction,"lStatistics");
            Link lDailyReport = new Link(iDailyReport,DailyReport.class);
              lDailyReport.addParameter(this.sAction,"lDailyReport");
            Link lContracts = new Link(iContracts,Contracts.class);
              lContracts.addParameter(this.sAction,"lContracts");
            Link lInitialData = new Link(iInitialData,InitialData.class);
              lInitialData.addParameter(this.sAction,"lInitialData");

            table.add(lDesign,1,1);
            table.add(lMyTrip,1,1);
            table.add(lOverview,1,1);
            table.add(lBooking,1,1);
            table.add(lStatistics,1,1);
            table.add(lDailyReport,1,1);
            table.add(lContracts,1,1);
            table.add(lInitialData,1,1);

            Link lHome = new Link("heim","/index.jsp");
            table.add(lHome,2,1);
        }
        else if (reseller!= null) {

            Link lBooking = new Link(iBooking,Booking.class);
              lBooking.addParameter(this.sAction,"lBooking");
            table.add(lBooking,1,1);
            Link lOverview = new Link(iOverview,BookingOverview.class);
              lOverview.addParameter(this.sAction,"lOverview");
            table.add(lOverview,1,1);

            Link lHome = new Link("heim","/index.jsp");
            table.add(lHome,2,1);
        }
        /*else {
            Link lBooking = new Link(iBooking,Booking.class);
              lBooking.addParameter(this.sAction,"lBooking");
            table.add(lBooking,1,1);
        }*/

        super.add(table);
    }


    public void initializer(IWContext iwc) {
        bundle = getBundle(iwc);
        iwrb = bundle.getResourceBundle(iwc.getCurrentLocale());

        try {
            int supplierId = TravelStockroomBusiness.getUserSupplierId(iwc);
            supplier = new Supplier(supplierId);
        }
        catch (Exception e) {
        }

        try {
            int resellerId = TravelStockroomBusiness.getUserResellerId(iwc);
            reseller = new Reseller(resellerId);
        }
        catch (Exception e) {
        }


        theText.setFontSize(Text.FONT_SIZE_10_HTML_2);
        theText.setFontFace(Text.FONT_FACE_VERDANA);
        theText.setFontColor(this.textColor);
        theBoldText.setFontSize(Text.FONT_SIZE_10_HTML_2);
        theBoldText.setFontFace(Text.FONT_FACE_VERDANA);
        theBoldText.setBold();
        theBoldText.setFontColor(this.textColor);
        smallText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        smallText.setFontFace(Text.FONT_FACE_VERDANA);
        smallText.setFontColor(this.textColor);
        theSmallBoldText.setFontFace(Text.FONT_FACE_VERDANA);
        theSmallBoldText.setFontSize(Text.FONT_SIZE_7_HTML_1);
        theSmallBoldText.setBold();
        theSmallBoldText.setFontColor(this.textColor);

    }

    public void add(PresentationObject mo) {
        table.add(mo,1,2);
    }

    public void add(String string) {
      Text text = (Text) theText.clone();
        text.setText(string);
      this.add(text);
    }


}

