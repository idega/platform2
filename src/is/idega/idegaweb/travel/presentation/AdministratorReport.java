package is.idega.idegaweb.travel.presentation;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.idega.presentation.PresentationObject;
import com.idega.util.IWTimestamp;
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

public interface AdministratorReport {

  public boolean useTwoDates();
  public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp stamp)throws RemoteException, FinderException;
  public PresentationObject getAdministratorReport(List suppliers, IWContext iwc, IWTimestamp fromStamp, IWTimestamp toStamp)throws RemoteException, FinderException;
  public String getReportName();
  public String getReportDescription();
}