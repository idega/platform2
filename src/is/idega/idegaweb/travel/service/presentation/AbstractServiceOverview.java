package is.idega.idegaweb.travel.service.presentation;

import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import java.sql.SQLException;
import com.idega.data.IDOFinderException;
import com.idega.block.trade.stockroom.data.Product;
import java.rmi.RemoteException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.SubmitButton;
import com.idega.presentation.text.Text;
import com.idega.presentation.ui.DateInput;
import com.idega.presentation.Table;
import com.idega.util.IWTimestamp;
import com.idega.util.IWCalendar;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.travel.presentation.TravelManager;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public abstract class AbstractServiceOverview extends TravelManager implements ServiceOverview{

  protected String[] dayOfWeekName = new String[8];
  protected IWCalendar iwCal;
  protected IWResourceBundle _iwrb;

  public String PARAMETER_FROM = "prmFrm";
  public String PARAMETER_TO = "prmTo";

  public AbstractServiceOverview() {
  }

  public void main(IWContext iwc) throws RemoteException{
//    super.main(iwc);
    super.initializer(iwc);
    this.initialize(iwc);
  }

  public abstract Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException;

  protected void initialize(IWContext iwc) throws RemoteException{
    _iwrb = super.getResourceBundle();
    iwCal = new IWCalendar();
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY ,iwc).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY] = iwCal.getNameOfDay(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY ,iwc).substring(0,3);
  }


}


/**
 * @todo Implementa fyrir timabil

  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(4,2);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      DateInput active_from = new DateInput("active_from");
          IWTimestamp fromStamp = getFromIdegaTimestamp(iwc);
          active_from.setDate(fromStamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
          IWTimestamp toStamp = getToIdegaTimestamp(iwc);
          active_to.setDate(toStamp.getSQLDate());

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(_iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(_iwrb.getLocalizedString("travel.to","to"));


      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(_iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(timeframeText,1,1);
      topTable.add(tfFromText,1,1);
      topTable.add(active_from,2,1);
      topTable.add(tfToText,2,1);
      topTable.add(active_to,2,1);
      topTable.mergeCells(2,1,4,1);
//    topTable.mergeCells(2,2,4,2);



      topTable.setAlignment(4,2,"right");
      topTable.add(new SubmitButton("TEMP-Sækja"),4,2);

      return topTable;
  }

  public IWTimestamp getFromIdegaTimestamp(IWContext iwc) {
      IWTimestamp stamp = null;
      String from_time = iwc.getParameter(PARAMETER_FROM);
      if (from_time!= null) {
          stamp = new IWTimestamp(from_time);
      }
      else {
          stamp = IWTimestamp.RightNow();
      }
      return stamp;
  }

  public IWTimestamp getToIdegaTimestamp(IWContext iwc) {
      IWTimestamp stamp = null;
      String from_time = iwc.getParameter(PARAMETER_TO);
      if (from_time!= null) {
          stamp = new IWTimestamp(from_time);
      }
      else {
          stamp = IWTimestamp.RightNow();
          stamp.addDays(15);
      }
      return stamp;
  }
*/

