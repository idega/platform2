package is.idega.idegaweb.travel.presentation;
import com.idega.presentation.PresentationObject;
import com.idega.util.idegaTimestamp;
import java.util.List;
import com.idega.presentation.IWContext;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public interface Report {

  public boolean useTwoDates();
  public PresentationObject getReport(IWContext iwc, List products, idegaTimestamp stamp);
  public PresentationObject getReport(IWContext iwc, List products, idegaTimestamp fromStamp, idegaTimestamp toStamp);
  public String getReportName();
  public String getReportDescription();

}