package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import java.util.Locale;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.business.IBOSession;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.user.data.Group;
import com.idega.user.data.User;


/**
 * @author gimmi
 */
public interface TravelSessionManager extends IBOSession {

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#clearLocale
	 */
	public void clearLocale() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#clearAll
	 */
	public void clearAll() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#isSet
	 */
	public boolean isSet() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#isSupplierManager
	 */
	public boolean isSupplierManager() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setIsSupplierManager
	 */
	public void setIsSupplierManager(boolean isSupplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSupplierManager
	 */
	public void setSupplierManager(Group supplierManager) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSupplierManager
	 */
	public Group getSupplierManager() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getUser
	 */
	public User getUser() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getUserId
	 */
	public int getUserId() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getLocale
	 */
	public Locale getLocale() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getLocaleId
	 */
	public int getLocaleId() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getIWResourceBundle
	 */
	public IWResourceBundle getIWResourceBundle() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getIWBundle
	 */
	public IWBundle getIWBundle() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSupplier
	 */
	public Supplier getSupplier() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSupplier
	 */
	public void setSupplier(Supplier supplier) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getReseller
	 */
	public Reseller getReseller() throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setReseller
	 */
	public void setReseller(Reseller reseller) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#setSearchEngine
	 */
	public void setSearchEngine(ServiceSearchEngine engine) throws java.rmi.RemoteException;

	/**
	 * @see is.idega.idegaweb.travel.business.TravelSessionManagerBean#getSearchEngine
	 */
	public ServiceSearchEngine getSearchEngine() throws java.rmi.RemoteException;
}
