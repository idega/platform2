package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.block.search.business.ServiceSearchBusiness;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.business.Assigner;
import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.ContractBusiness;
import is.idega.idegaweb.travel.business.Inquirer;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.service.business.BookingBusiness;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactory;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import javax.ejb.FinderException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ResellerManager;
import com.idega.block.trade.stockroom.business.SupplierManagerBusiness;
import com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.Block;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.User;

/**
 * @author gimmi
 */
public class TravelBlock extends Block {

  protected TravelSessionManager tsm;
  protected boolean isInPermissionGroup = false;
  protected boolean isSuperAdmin = false;
  protected boolean expiredLogin = false;
  
  public static final String IW_BUNDLE_IDENTIFIER = "is.idega.travel";
  
  private static final String TEST_MODE_PARAMETER_NAME = "test_mode";

	public TravelBlock() {
		super();
	}
	
  public String getBundleIdentifier(){
    return IW_BUNDLE_IDENTIFIER;
  }

  public void main(IWContext iwc) throws Exception{
  		initializer(iwc);
	}

	protected void initializer(IWContext iwc) throws RemoteException {
    tsm = getTravelSessionManager(iwc);
    if (tsm.isSet()) {
//    	System.out.println("SessionManager already set");
    } else {
//    	System.out.println("Setting SessionManager");
	    if (!isTravelAdministrator(iwc)) {
	      try {
		      int supplierId = getTravelStockroomBusiness(iwc).getUserSupplierId(iwc);
		      SupplierHome suppHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
		      Supplier supplier = suppHome.findByPrimaryKey(supplierId);
		      if (!supplier.getIsValid()) {
		        //supplier = null;
		      		expiredLogin = true;
		      }else {
		        tsm.setSupplier(supplier);
		      }
	      }
	      catch (Exception e) {
	        debug(e.getMessage());
	      }
	
	      try {
		      int resellerId = getTravelStockroomBusiness(iwc).getUserResellerId(iwc);
		      Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
		      if (!reseller.getIsValid()) {
		        //reseller = null;
		      		expiredLogin = true;
		      } else {
		        tsm.setReseller(reseller);
		      }
	      }
	      catch (Exception e) {
	      	//e.printStackTrace(System.err);
	        debug(e.getMessage());
	      }
	      
	      try {
		      	ServiceSearchBusiness ssb = (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwc, ServiceSearchBusiness.class);
		      	User user = tsm.getUser();
		      	if (user != null) {
		      		ServiceSearchEngine engine = ssb.getUserSearchEngine(user);
		      		if (!engine.getIsValid()) {
		      			expiredLogin = true;
		      		} else {
		      			tsm.setSearchEngine(engine);
		      		}
		      	}
	      } catch (Exception e) {
	      	debug(e.getMessage());
	      }
	      
	      if (this.isSupplierManager(iwc)) {
	      	tsm.setIsSupplierManager(true);
	      	tsm.setSupplierManager(getSupplierManager(iwc));
	      }
	    }
    }
    this.isInPermissionGroup = this.isInPermissionGroup(iwc);
    this.isSuperAdmin = isTravelAdministrator(iwc);

	}

  protected boolean hasPermission() {
		return this.isInPermissionGroup || this.isSuperAdmin;
	}
	
	public boolean isInPermissionGroup(IWContext iwc) throws RemoteException {
	  return isInPermissionGroup(iwc, tsm.getUser());
	}

	protected boolean isInPermissionGroup(IWContext iwc, User user) throws RemoteException {
	  if (user != null) {
	    Group pGroup = null;
	    try {
	      if (tsm.getReseller() != null) {
	        pGroup = getResellerManager(iwc).getPermissionGroup(tsm.getReseller());
	      }
	      else if (tsm.getSupplier() != null) {
	        pGroup = getSupplierManagerBusiness(iwc).getPermissionGroup(tsm.getSupplier());
	      } else if (tsm.getSearchEngine() != null) {
	      		ServiceSearchBusiness ssb = (ServiceSearchBusiness) IBOLookup.getServiceInstance(iwc, ServiceSearchBusiness.class);
	      		pGroup = ssb.getPermissionGroup(tsm.getSearchEngine()); 
	      }
	
	      if (pGroup != null) {
		      	UserBusiness uBus = (UserBusiness) IBOLookup.getServiceInstance(iwc, UserBusiness.class);
		      	GroupBusiness gBus = (GroupBusiness) IBOLookup.getServiceInstance(iwc, GroupBusiness.class);
		      	List allGroups = user.getParentGroups();
		      	//List allUsers = pGroup.getAllGroupsContainingUser(user);
		      	Iterator iter = allGroups.iterator();
	        if (allGroups != null) {
	          return allGroups.contains(pGroup);
	        }
	      }
	    }catch (Exception sql) {
	      sql.printStackTrace(System.err);
	    }
	
	  }
	  return false;
	}
		
  protected boolean hasLoginExpired(IWContext iwc) throws RemoteException {
    return (!iwc.hasEditPermission(this) && tsm.getSupplier() == null && tsm.getReseller() == null && tsm.getSearchEngine() == null && !isSupplierManager(iwc));
  }

  protected boolean isLoggedOn(IWContext iwc) throws RemoteException {
    return !hasLoginExpired(iwc);
  }

  public IWBundle getBundle() throws RemoteException{
    return tsm.getIWBundle();
  }

  public IWResourceBundle getResourceBundle()  throws RemoteException{
    return tsm.getIWResourceBundle();
  }

  public Locale getLocale() throws RemoteException {
    return tsm.getLocale();
  }

  public int getLocaleId() throws RemoteException {
    return tsm.getLocaleId();
  }

  public Reseller getReseller() throws RemoteException{
    return tsm.getReseller();
  }

  public Supplier getSupplier() throws RemoteException {
      return tsm.getSupplier();
  }

  public User getUser() throws RemoteException {
    return tsm.getUser();
  }

  public int getUserId() throws RemoteException {
    return tsm.getUserId();
  }
  protected Booker getBooker(IWApplicationContext iwac) throws RemoteException{
    return (Booker) IBOLookup.getServiceInstance(iwac, Booker.class);
  }

  protected Assigner getAssigner(IWApplicationContext iwac) throws RemoteException {
    return (Assigner) IBOLookup.getServiceInstance(iwac, Assigner.class);
  }

  protected Inquirer getInquirer(IWApplicationContext iwac) throws RemoteException {
    return (Inquirer) IBOLookup.getServiceInstance(iwac, Inquirer.class);
  }

  protected TravelStockroomBusiness getTravelStockroomBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }

  protected ProductCategoryFactory getProductCategoryFactory(IWApplicationContext iwac) throws RemoteException {
    return (ProductCategoryFactory) IBOLookup.getServiceInstance(iwac, ProductCategoryFactory.class);
  }

  protected ServiceHandler getServiceHandler(IWApplicationContext iwac) throws RemoteException {
    return (ServiceHandler) IBOLookup.getServiceInstance(iwac, ServiceHandler.class);
  }

  protected ContractBusiness getContractBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ContractBusiness) IBOLookup.getServiceInstance(iwac, ContractBusiness.class);
  }

  protected ProductBusiness getProductBusiness(IWApplicationContext iwac) throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(iwac, ProductBusiness.class);
  }
  
  protected TravelStockroomBusiness getServiceBusiness(IWApplicationContext iwac, Product product) throws RemoteException, FinderException {
  	return this.getServiceHandler(iwac).getServiceBusiness(product);
  }

  protected boolean isTravelAdministrator(IWContext iwc) {
  	return iwc.isSuperAdmin();
  }
  
  protected boolean isSupplierManager() throws RemoteException {
  	return tsm.isSupplierManager();
  }
  
  private boolean isSupplierManager(IWContext iwc) {
  	try {
  		return iwc.getAccessController().hasRole(SupplierManagerBusinessBean.SUPPLIER_MANAGER_ROLE_KEY, iwc);
  	} catch (NotLoggedOnException n) {
  		return false;
  	}
  }
  
  protected Group getSupplierManager() throws RemoteException {
  	return tsm.getSupplierManager();
  }
  
  private Group getSupplierManager(IWContext iwc) throws RemoteException {
		Group tmp = getSupplierManagerBusiness(iwc).getSupplierManager(iwc.getCurrentUser());
		if (tmp != null) {
			System.out.println("TravelBlock : supplierManager = "+tmp.getName());
		} else {
			System.out.println("TravelBlock : supplierManager = null");
		}
		return tmp;
  }

  protected TravelSessionManager getTravelSessionManager(IWContext iwc) throws RemoteException{
  	if (tsm == null) {
  		tsm = getTravelSessionManagerStatic(iwc);
  	}
  	return tsm;
  }
  
  protected boolean isTestMode() throws RemoteException {
  	String par = getBundle().getProperty(TEST_MODE_PARAMETER_NAME);
  	return (par != null && par.equalsIgnoreCase("yes"));
  }

  public static TravelSessionManager getTravelSessionManagerStatic(IWContext iwc) throws RemoteException{
    return (TravelSessionManager) IBOLookup.getSessionInstance(iwc, TravelSessionManager.class);
  }
  
  protected CreditCardBusiness getCreditCardBusiness(IWApplicationContext iwac) {
  		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(iwac, CreditCardBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
  
  protected BookingBusiness getBookingBusiness(IWApplicationContext iwac) {
		try {
			return (BookingBusiness) IBOLookup.getServiceInstance(iwac, BookingBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
  
  protected GroupBusiness getGroupBusiness(IWApplicationContext iwac) {
  	try {
			return (GroupBusiness) IBOLookup.getServiceInstance(iwac, GroupBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
  
	protected SupplierManagerBusiness getSupplierManagerBusiness(IWApplicationContext iwc) {
		try {
			return (SupplierManagerBusiness) IBOLookup.getServiceInstance(iwc, SupplierManagerBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	protected ResellerManager getResellerManager(IWApplicationContext iwc) {
		try {
			return (ResellerManager) IBOLookup.getServiceInstance(iwc, ResellerManager.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
  protected UserBusiness getUserBusiness() {
  	try {
			return (UserBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), UserBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }
}
