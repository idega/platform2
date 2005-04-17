package is.idega.idegaweb.golf.business;

import com.idega.presentation.IWContext;


public interface UserSynchronizationBusiness extends com.idega.business.IBOService
{
 //public boolean synchronize() throws java.rmi.RemoteException;
 public boolean sync() throws java.rmi.RemoteException;
	public void syncUserFromRequest(IWContext iwc);
}
