package is.idega.idegaweb.travel.presentation;
import java.rmi.RemoteException;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.util.IWTimestamp;

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