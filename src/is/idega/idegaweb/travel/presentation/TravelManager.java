package is.idega.travel.presentation;

import com.idega.jmodule.object.*;
import com.idega.jmodule.object.interfaceobject.*;
import com.idega.jmodule.object.textObject.*;
import javax.servlet.jsp.JspPage;
import com.idega.idegaweb.IWBundle;
import com.idega.projects.nat.business.NatBusiness;
import com.idega.idegaweb.IWResourceBundle;
import is.idega.travel.presentation.*;

public class TravelManager extends JModuleObject {

    public static String IW_BUNDLE_IDENTIFIER="is.idega.travel";
    private IWBundle bundle;
    private IWResourceBundle iwrb;
    Table table = new Table(1,2);

    public TravelManager(){
        super();
    }


    public String getBundleIdentifier(){
      return IW_BUNDLE_IDENTIFIER;
    }


    public void main(ModuleInfo modinfo) {

        bundle = getBundle(modinfo);
        iwrb = bundle.getResourceBundle(modinfo.getCurrentLocale());

          table.setBorder(0);
          table.setHeight("100%");
          table.setCellpadding(0);
          table.setCellspacing(0);
          table.setColor(1,2,NatBusiness.backgroundColor);
          table.setVerticalAlignment(1,1,"top");
          table.setVerticalAlignment(1,2,"top");
          table.setAlignment(1,1,"left");
          table.setAlignment(1,2,"center");
          table.setAlignment("center");
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


        Link lDesign = new Link(iDesign,ServiceDesigner.class);
          lDesign.addParameter("manager_action","lDesign");
        Link lMyTrip = new Link(iMyTrip,ServiceOverview.class);
          lMyTrip.addParameter("manager_action","lMyTrip");
        Link lOverview = new Link(iOverview,BookingOverview.class);
          lOverview.addParameter("manager_action","lOverview");
        Link lBooking = new Link(iBooking,Booking.class);
          lBooking.addParameter("manager_action","lBooking");
        Link lStatistics = new Link(iStatistics,Statistics.class);
          lStatistics.addParameter("manager_action","lStatistics");
        Link lDailyReport = new Link(iDailyReport,DailyReport.class);
          lDailyReport.addParameter("manager_action","lDailyReport");
        Link lContracts = new Link(iContracts,"/contracts.jsp");
          lContracts.addParameter("manager_action","lContracts");
        Link lInitialData = new Link(iInitialData,InitialData.class);
          lInitialData.addParameter("manager_action","lInitialData");
//        Link lInitialData = new Link(iInitialData,"initial_data.jsp");
//          lInitialData.addParameter("manager_action","lInitialData");

        table.add(lDesign,1,1);
        table.add(lMyTrip,1,1);
        table.add(lOverview,1,1);
        table.add(lBooking,1,1);
        table.add(lStatistics,1,1);
        table.add(lDailyReport,1,1);
        table.add(lContracts,1,1);
        table.add(lInitialData,1,1);


        String action = modinfo.getParameter("action");
        if (action != null) {
          if (action.equals("lDesign")) {
          }
        }

        super.add(table);
    }


    public void add(ModuleObject mo) {
        table.add(mo,1,2);
    }

    public void add(String string) {
        table.add(string,1,2);
    }


}

