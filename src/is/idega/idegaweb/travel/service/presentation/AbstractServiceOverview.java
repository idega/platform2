package is.idega.idegaweb.travel.service.presentation;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.presentation.TravelManager;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Locale;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.data.IDOFinderException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

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
    super.initializer(iwc);
    this.initialize(iwc);
  }

  public abstract Table getServiceInfoTable(IWContext iwc, Product product) throws IDOFinderException, SQLException, ServiceNotFoundException, TimeframeNotFoundException, RemoteException;

  protected void initialize(IWContext iwc) throws RemoteException{
    _iwrb = super.getResourceBundle();
    iwCal = new IWCalendar();
    Locale locale = iwc.getCurrentLocale();
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SUNDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.MONDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.TUESDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.WEDNESDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.THURSDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.FRIDAY ,locale,IWCalendar.LONG).substring(0,3);
    dayOfWeekName[is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY] = iwCal.getDayName(is.idega.idegaweb.travel.data.ServiceDayBMPBean.SATURDAY ,locale,IWCalendar.LONG).substring(0,3);
  }

  protected Text getTimeframeText(Timeframe timeframe, IWContext iwc) {
//    IWTimestamp from = new IWTimestamp(timeframe.getFrom());
//    IWTimestamp to = new IWTimestamp(timeframe.getTo());
    IWCalendar cal = new IWCalendar(new IWTimestamp(timeframe.getFrom()));
		String txt1 = cal.getLocaleDate();
    cal = new IWCalendar(new IWTimestamp(timeframe.getTo()));
		String txt2 = cal.getLocaleDate();
		
	  try {
	    if (timeframe.getIfYearly() ){
	      txt1 = txt1.substring(0, txt1.length() -4);
	      txt2 = txt2.substring(0, txt2.length() -4);
	    }
	  }catch (ArrayIndexOutOfBoundsException ai) {}

    Text text = new Text();
      text.setText(txt1+ " - " + txt2 );
    return text;
  }

}




