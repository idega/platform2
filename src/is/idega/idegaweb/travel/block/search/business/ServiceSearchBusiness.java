package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroup;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOService;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;


/**
 * @author gimmi
 */
public interface ServiceSearchBusiness extends IBOService, ActionListener {

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#initializeBean
	 */
	public void initializeBean();

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getErrorFormFields
	 */
	public List getErrorFormFields(IWContext iwc, String categoryKey, boolean useCVC) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#sortProducts
	 */
	public Collection sortProducts(IWContext iwc, Collection productsToSort, PriceCategory priceCat,
			IWTimestamp bookingDate, int sortMethod) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#checkResults
	 */
	public Collection checkResults(IWContext iwc, Collection results) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getServiceSearchEngines
	 */
	public Collection getServiceSearchEngines(Group supplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#findByName
	 */
	public ServiceSearchEngine findByName(String name) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#findByCode
	 */
	public ServiceSearchEngine findByCode(String code) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#storeEngine
	 */
	public ServiceSearchEngine storeEngine(Object pk, String name, String code, String url, Group supplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#addSearchEngineUser
	 */
	public void addSearchEngineUser(ServiceSearchEngine engine, String name, String userName, String password,
			boolean addToPermissionGroup) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#isUserInPermissionGroup
	 */
	public boolean isUserInPermissionGroup(ServiceSearchEngine engine, User user) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getPermissionGroup
	 */
	public Group getPermissionGroup(ServiceSearchEngine engine) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getServiceSearchEngineStaffGroup
	 */
	public ServiceSearchEngineStaffGroup getServiceSearchEngineStaffGroup(ServiceSearchEngine engine)
			throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#addUser
	 */
	public void addUser(ServiceSearchEngine engine, User user, boolean addToPermissionGroup) throws RemoteException,
			FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getUserSearchEngine
	 */
	public ServiceSearchEngine getUserSearchEngine(User user) throws RuntimeException, RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#deleteServiceSearchEngine
	 */
	public boolean deleteServiceSearchEngine(ServiceSearchEngine engine, User performer)
			throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getSearchResults
	 */
	public Collection getSearchResults(String key) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#addSearchResults
	 */
	public void addSearchResults(String key, Collection results) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getSearchEngineHome
	 */
	public ServiceSearchEngineHome getSearchEngineHome() throws IDOLookupException, java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getTravelSessionManager
	 */
	public TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getBusiness
	 */
	public TravelStockroomBusiness getBusiness(Product product) throws RemoteException, FinderException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getProductBusiness
	 */
	public ProductBusiness getProductBusiness() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#getServiceHandler
	 */
	public ServiceHandler getServiceHandler() throws RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.block.search.business.ServiceSearchBusinessBean#actionPerformed
	 */
	public void actionPerformed(ActionEvent event);
	
	public void clearAllEngineCache();

}
