package is.idega.idegaweb.travel.block.search.business;

import java.util.Collection;
import com.idega.presentation.IWContext;


public interface ServiceSearchBusiness extends com.idega.business.IBOService
{
	public static String SEARCH_FORM_CACHE_KEY = "abstract_search_form";
 public void addSearchEngineUser(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0,java.lang.String p1,java.lang.String p2,java.lang.String p3,boolean p4) throws java.rmi.RemoteException;
 public void addUser(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0,com.idega.core.user.data.User p1,boolean p2)throws java.sql.SQLException, java.rmi.RemoteException;
 public java.util.Collection checkResults(com.idega.presentation.IWContext p0,java.util.Collection p1)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public boolean deleteServiceSearchEngine(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine findByCode(java.lang.String p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine findByName(java.lang.String p0) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelStockroomBusiness getBusiness(com.idega.block.trade.stockroom.data.Product p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
 public java.util.List getErrorFormFields(com.idega.presentation.IWContext p0,java.lang.String p1, boolean useCVC) throws java.rmi.RemoteException;
 public com.idega.core.accesscontrol.data.PermissionGroup getPermissionGroup(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0) throws java.rmi.RemoteException;
// public com.idega.presentation.ui.DropdownMenu getPostalCodeDropdown(com.idega.idegaweb.IWResourceBundle p0)throws java.rmi.RemoteException,javax.ejb.FinderException, java.rmi.RemoteException;
// public java.lang.Object[] getPostalCodeIds(com.idega.presentation.IWContext p0)throws com.idega.data.IDOLookupException,javax.ejb.FinderException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome getSearchEngineHome()throws com.idega.data.IDOLookupException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.service.business.ServiceHandler getServiceHandler()throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroup getServiceSearchEngineStaffGroup(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0)throws java.sql.SQLException, java.rmi.RemoteException;
 public java.util.Collection getServiceSearchEngines() throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.business.TravelSessionManager getTravelSessionManager(com.idega.idegaweb.IWUserContext p0)throws java.rmi.RemoteException, java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine getUserSearchEngine(com.idega.core.user.data.User p0)throws java.lang.RuntimeException,java.sql.SQLException, java.rmi.RemoteException;
 public boolean isUserInPermissionGroup(is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine p0,com.idega.core.user.data.User p1) throws java.rmi.RemoteException;
 public java.util.Collection sortProducts(IWContext iwc, java.util.Collection p0,com.idega.block.trade.stockroom.data.PriceCategory p1,com.idega.util.IWTimestamp p2, int searchMethod) throws java.rmi.RemoteException;
 public is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine storeEngine(java.lang.Object p0,java.lang.String p1,java.lang.String p2) throws java.rmi.RemoteException;
// public boolean getIsProductValid(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to) throws Exception;
 public Collection getSearchResults(String key);
 public void addSearchResults(String key, Collection results);

}
