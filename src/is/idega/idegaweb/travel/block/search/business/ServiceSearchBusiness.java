package is.idega.idegaweb.travel.block.search.business;

import java.rmi.RemoteException;

import javax.ejb.FinderException;

import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;


public interface ServiceSearchBusiness extends com.idega.business.IBOService
{
 public java.util.HashMap checkResults(IWContext iwc, java.util.Collection p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelStockroomBusiness getBusiness(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getErrorFormFields(IWContext iwc, String categoryKey) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.business.ServiceHandler getServiceHandler()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelSessionManager getTravelSessionManager(com.idega.idegaweb.IWUserContext p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public DropdownMenu getPostalCodeDropdown(IWResourceBundle iwrb) throws RemoteException, FinderException;
 public Object[] getPostalCodeIds(IWContext iwc) throws IDOLookupException, FinderException;
}
