package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroup;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupBMPBean;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupHome;
import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.service.business.BookingBusiness;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductBusinessBean;
import com.idega.block.trade.stockroom.business.ProductComparator;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.AccessControl;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.data.GenericGroup;
import com.idega.core.data.GenericGroupHome;
import com.idega.core.user.business.UserBusiness;
import com.idega.core.user.business.UserGroupBusiness;
import com.idega.core.user.data.User;
import com.idega.data.EntityFinder;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServiceSearchBusinessBean extends IBOServiceBean implements ServiceSearchBusiness, ActionListener {

	private HashMap resultMap = new HashMap();
	
	
	private String SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION = "Search Engine administator group";
	private String permissionGroupNameExtention = " - admins";

	public ServiceSearchBusinessBean() {
		super();
	}
	
	public void initializeBean() {
		getProductBusiness().addActionListener(this);
	}


	public List getErrorFormFields(IWContext iwc, String categoryKey, boolean useCVC) {
		List list = new Vector();
		String firstName = iwc.getParameter(AbstractSearchForm.PARAMETER_FIRST_NAME);
		String lastName = iwc.getParameter(AbstractSearchForm.PARAMETER_LAST_NAME);
		String street = iwc.getParameter(AbstractSearchForm.PARAMETER_STREET);
		String pc = iwc.getParameter(AbstractSearchForm.PARAMETER_POSTAL_CODE);
		String city = iwc.getParameter(AbstractSearchForm.PARAMETER_CITY);
		String country = iwc.getParameter(AbstractSearchForm.PARAMETER_COUNTRY);
		String email = iwc.getParameter(AbstractSearchForm.PARAMETER_EMAIL);
		String ccNum = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_NUMBER);
		String ccMon = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_MONTH);
		String ccYear = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_YEAR);
		String ccCVC = iwc.getParameter(AbstractSearchForm.PARAMETER_CC_CVC);
		String ccName = iwc.getParameter(AbstractSearchForm.PARAMETER_NAME_ON_CARD);
		
		if (firstName == null || firstName.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_FIRST_NAME);
		}
		if (lastName == null || lastName.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_LAST_NAME);
		}
		if (street == null || street.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_STREET);
		}
		if (pc == null || pc.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_POSTAL_CODE);
		}
		if (city == null || city.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CITY);
		}
		if (country == null || country.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_COUNTRY);
		}
		if (email == null || email.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_EMAIL);
		}
		if (ccNum == null || ccNum.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_NUMBER);
		}
		if (ccMon == null || ccMon.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_MONTH);
		}
		if (ccYear == null || ccYear.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_CC_YEAR);
		}
		if (useCVC && (ccCVC == null || ccCVC.equals(""))) {
			list.add(AbstractSearchForm.PARAMETER_CC_CVC);
		}
		if (ccName == null || ccName.equals("")) {
			list.add(AbstractSearchForm.PARAMETER_NAME_ON_CARD);
		}
		String productId = iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_ID);
		
		ProductPrice[] pPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(Integer.parseInt(productId), -1, -1, true, categoryKey);
		int iMany = 0;
		for (int i = 0; i < pPrices.length; i++) {
		  try {
			iMany += Integer.parseInt(iwc.getParameter("priceCategory"+pPrices[i].getID()));
		  }catch (NumberFormatException n) {
		  	//n.printStackTrace();
		  }
		}		
		
		if (iMany < 1) {
			list.add(AbstractSearchForm.ERROR_NO_BOOKING_COUNT);
		}
		
		return list;
	}

	public Collection sortProducts(IWContext iwc, Collection productsToSort, PriceCategory priceCat, IWTimestamp bookingDate, int sortMethod) {
		try {
			//if (productComparator == null) {
			ProductComparator	productComparator = new ProductComparator(sortMethod, iwc.getCurrentLocale());
				productComparator.setPriceCategoryValues(priceCat, -1, bookingDate);
			//}
			/** Gera betra */
			//Collection tmp = getProductInstanceCollection(productsToSort);
			Collections.sort( (Vector) productsToSort, productComparator);
			
			return productsToSort;
			//return getPKCollectionFromInstances(tmp);
		}catch (Exception e) {
			e.printStackTrace(System.err);
		}
		return productsToSort;//getProductInstanceCollection(productsToSort);
		//return getPKCollectionFromInstances(productsToSort);
//		return productsToSort;		 
	}

	public Collection checkResults(IWContext iwc, Collection results) throws RemoteException {
		if (results != null && !results.isEmpty()) {
			System.out.println("ServiceSearchBusiness : checking reults : " +results.size());
			results = getProductInstanceCollection(results);
			System.out.println("ServiceSearchBusiness : results converted to products");
			HashMap map = new HashMap();
			Collection coll = new Vector();
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Product product;
			IWTimestamp from = null;
			IWTimestamp to = null;
			IWTimestamp tmp;
			Collection addresses = null;
			try {
				from = new IWTimestamp(iwc.getParameter(AbstractSearchForm.PARAMETER_FROM_DATE));
				int betw = 1;
				try {
					betw = Integer.parseInt(iwc.getParameter(AbstractSearchForm.PARAMETER_MANY_DAYS));
					to = new IWTimestamp(from);
					to.addDays(betw);
				} catch (NumberFormatException n) {
					try {
						to = new IWTimestamp(iwc.getParameter(AbstractSearchForm.PARAMETER_TO_DATE));
					} catch (Exception e) {
						//e.printStackTrace();
						to = new IWTimestamp(from);
						to.addDays(betw);
					}
				}
			}catch (Exception e) {
				System.out.println("error getting stamps : "+e.getMessage());
				e.printStackTrace();
			}
			int addressId = -1;
			int timeframeId = -1;
			Timeframe timeframe;
			BookingForm bf;
			ProductPrice[] prices;
			Iterator iter = results.iterator();
			Collection toRemove = new Vector(); 
			boolean productIsValid = true;
			while (iter.hasNext() && from != null && to != null) {
				try {
					product = (Product) iter.next();
					System.out.println("ServiceSearchBusiness : checking product : " +product.getProductName(iwc.getCurrentLocaleId()));
					productIsValid = getBookingBusiness(iwc).getIsProductValid(iwc, product, from, to);
					if (productIsValid) {
						System.out.println("ServiceSearchBusiness : valid");
						map.put(product, new Boolean(productIsValid));
					}	else {
						System.out.println("ServiceSearchBusiness : invalid");
						toRemove.add(product);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			results.removeAll(toRemove);
			return results;
		} else {
			System.out.println("ServiceSearchBusiness : No results");
		}
		return new Vector();
	}



	
	private Collection getProductInstanceCollection(Collection pks) {
		Collection coll = new Vector();
		try {
			ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
			Iterator iter = pks.iterator();
			while (iter.hasNext()) {
				coll.add( pHome.findByPrimaryKey(((IDOEntity)iter.next()).getPrimaryKey()));
			}
		} catch (Exception e) {
			
		}
		return coll;
	}
	
	private Collection getPKCollectionFromInstances(Collection insts) {
		Collection coll = new Vector();
		
		Iterator iter = insts.iterator();
		while (iter.hasNext()) {
			coll.add( ((Product)iter.next()).getPrimaryKey());
		}
		
		return coll;
	}

	public Collection getServiceSearchEngines() {
		Collection coll = new Vector();
		try {
			coll = getSearchEngineHome().findAll();
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return coll;
	}
	
	public ServiceSearchEngine findByName(String name) {
		ServiceSearchEngine engine = null;
		try {
			return getSearchEngineHome().findByName(name);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return engine;
	}

	public ServiceSearchEngine findByCode(String code) {
		ServiceSearchEngine engine = null;
		try {
			return getSearchEngineHome().findByCode(code);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return engine;
	}
	
	public ServiceSearchEngine storeEngine(Object pk, String name, String code) {
		ServiceSearchEngine engine = null;
		UserTransaction t = getSessionContext().getUserTransaction();
		try {
			t.begin();
			String oldCode = null;
			boolean isUpdate = pk != null;
			if (pk == null) {
				engine = getSearchEngineHome().create();
			} else {
				engine = getSearchEngineHome().findByPrimaryKey(pk);
				oldCode = engine.getCode();
				if (code != null && code.equals(oldCode)) {
					oldCode = null;
				}
			}
			
			engine.setName(name);
			engine.setCode(code);
			engine.store();
			
			if (getServiceSearchEngineStaffGroup(engine) == null) {
				String sName = name+"_"+engine.getPrimaryKey().toString();
				Object object = IDOLookup.getHome(ServiceSearchEngineStaffGroup.class);
				System.out.println("object = "+ object.getClass().getName());
				ServiceSearchEngineStaffGroupHome ssesgh = (ServiceSearchEngineStaffGroupHome) IDOLookup.getHomeLegacy(ServiceSearchEngineStaffGroup.class);	
				ServiceSearchEngineStaffGroup sGroup = ssesgh.create();
				sGroup.setName(sName);
				sGroup.insert();
				engine.setStaffGroupID(sGroup.getID());
				engine.store();
			} else if (engine.getStaffGroupID() < 1){
				System.out.println("[ServiceSearchBusinessBean] Fixing engine, setting groupID");
				engine.setStaffGroupID(getServiceSearchEngineStaffGroup(engine).getID());
				engine.store();
			}
			
			if (getPermissionGroup(engine) == null) {
				String sName = name+"_"+engine.getPrimaryKey().toString();

				AccessControl ac = new AccessControl();
				ac.createPermissionGroup(sName+permissionGroupNameExtention, SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION, "", null ,null);
			}
			
			
			if (oldCode != null) {
				GeneralBookingHome gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
				Collection bookings = gbHome.findAllByCode(oldCode);
				if (bookings != null && !bookings.isEmpty()) {
					GeneralBooking booking;
					Iterator iter = bookings.iterator();
					while (iter.hasNext() ){
						booking = (GeneralBooking) iter.next();
						booking.setCode(code);
						booking.store();
					}
				}
			}
			
			t.commit();
			return engine;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return engine;
	}
	
	public void addSearchEngineUser(ServiceSearchEngine engine, String name, String userName, String password, boolean addToPermissionGroup) {
		UserBusiness uBus = new UserBusiness();
		User user;
		try {
			user = uBus.insertUser(name,"","- admin",name+" - admin","Search Engine administrator",null,IWTimestamp.RightNow(),null);
			LoginDBHandler.createLogin(user.getID(), userName, password);
		
			addUser(engine, user, addToPermissionGroup);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isUserInPermissionGroup(ServiceSearchEngine engine, User user) {
		PermissionGroup pGroup = getPermissionGroup(engine);
		UserGroupBusiness ugb = new UserGroupBusiness();
		try {
			List allUsers = ugb.getUsersContained(pGroup);
			return allUsers.contains(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public PermissionGroup getPermissionGroup(ServiceSearchEngine engine) {
	  String name = engine.getName()+"_"+engine.getPrimaryKey().toString() + permissionGroupNameExtention;
	  String description = SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION ;

	  PermissionGroup pGroup = null;
	  List listi = null;
	  try {
	  	listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), name, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
	  } catch (SQLException e) {
	  	e.printStackTrace();
	  }
	  if (listi != null) {
	  	if (listi.size() > 0) {
	  		pGroup = (PermissionGroup) listi.get(listi.size()-1);
	  	}
	  } else {
	  	try {
	  		listi = EntityFinder.findAllByColumn((PermissionGroup) com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getStaticInstance(PermissionGroup.class), com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getNameColumnName(), engine.getName()+permissionGroupNameExtention, com.idega.core.accesscontrol.data.PermissionGroupBMPBean.getGroupDescriptionColumnName(), description);
	  	} catch (SQLException e1) {
	  		e1.printStackTrace();
	  	}
	  	
	  	if (listi != null) {
	  		if (listi.size() > 0) {
	  			pGroup = (PermissionGroup) listi.get(listi.size()-1);
	  		}
	  	}
	  }

	  return pGroup;
	}

	public ServiceSearchEngineStaffGroup getServiceSearchEngineStaffGroup(ServiceSearchEngine engine) throws SQLException {
	  String name = engine.getName()+"_"+engine.getPrimaryKey().toString();
	  ServiceSearchEngineStaffGroup sGroup = null;
	  List listi = EntityFinder.findAllByColumn((GenericGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class), ServiceSearchEngineStaffGroupBMPBean.getNameColumnName(), name);
	  if (listi != null) {
			if (listi.size() > 0) {
			  sGroup = (ServiceSearchEngineStaffGroup) listi.get(listi.size()-1);
			}
	  }
	  if (listi == null) {
		  	listi = EntityFinder.findAllByColumn((GenericGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class), ServiceSearchEngineStaffGroupBMPBean.getNameColumnName(), engine.getName());
			if (listi != null && listi.size() > 0) {
			  sGroup = (ServiceSearchEngineStaffGroup) listi.get(listi.size()-1);
			}
	  }
	  if (sGroup == null) {
	  		System.err.println("searchEngineStaffGroup == null");
	  }
	  return sGroup;
	}

	public void addUser(ServiceSearchEngine engine, User user, boolean addToPermissionGroup) throws SQLException{
	  PermissionGroup pGroup = getPermissionGroup(engine);
	  ServiceSearchEngineStaffGroup sGroup = getServiceSearchEngineStaffGroup(engine);
	  if (addToPermissionGroup) {
	  	pGroup.addUser(user);
	  }
	  sGroup.addUser(user);
	}

	public ServiceSearchEngine getUserSearchEngine(User user) throws RuntimeException, SQLException{
	  try {
		  GenericGroup gGroup = ((GenericGroupHome) IDOLookup.getHome(GenericGroup.class)).createLegacy();
		  List gr = gGroup.getAllGroupsContainingUser(user);
		  if(gr != null){
				Iterator iter = gr.iterator();
				while (iter.hasNext()) {
					try {
						GenericGroup item = (GenericGroup)iter.next();
						String flepps = item.getGroupTypeValue();
						if(item.getGroupType().equals(((ServiceSearchEngineStaffGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class)).getGroupTypeValue())){
							return getSearchEngineHome().findByGroupID(item.getID());
						}
					} catch (ClassCastException cce) {
						System.out.println("[ServiceSearchBusinessBean] classcastexception");
					}
				}
		  }
		  throw new RuntimeException("Does not belong to any searchengine");
	  } catch (IDOLookupException e) {
		  throw new RuntimeException("Does not belong to any searchengine");
	  } catch (FinderException e) {
	  	throw new RuntimeException("Does not belong to any searchengine");
	  }
	}

	public boolean deleteServiceSearchEngine(ServiceSearchEngine engine) {
		try {
			ServiceSearchEngineStaffGroup sGroup = this.getServiceSearchEngineStaffGroup(engine);
			List users = UserGroupBusiness.getUsersContained(sGroup.getID());
			if (users != null) {
				Iterator iter = users.iterator();
				User user;
				while (iter.hasNext()) {
					try {
						user = (User) iter.next();
						sGroup.removeFrom(user);
						LoginDBHandler.deleteUserLogin( user.getID() );
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			engine.remove();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Collection getSearchResults(String key) {
		return (Collection) resultMap.get(key);
	}
	
	public void addSearchResults(String key, Collection results) {
		resultMap.put(key, results);
	}
	
	
	public ServiceSearchEngineHome getSearchEngineHome() throws IDOLookupException {
		return (ServiceSearchEngineHome) IDOLookup.getHome(ServiceSearchEngine.class);
	}
	
	public TravelSessionManager getTravelSessionManager(IWUserContext iwuc) throws RemoteException {
		return (TravelSessionManager) IBOLookup.getSessionInstance(iwuc, TravelSessionManager.class);
	}
	
	public TravelStockroomBusiness getBusiness(Product product) throws RemoteException, FinderException {
		return getServiceHandler().getServiceBusiness(product);
	}
	
	public ProductBusiness getProductBusiness() {
		try {
			return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	
	public ServiceHandler getServiceHandler() throws RemoteException {
		return (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
	}

	public void actionPerformed(ActionEvent event) {
		if (event != null) {
			if (event.getActionCommand().equals(ProductBusinessBean.COMMAND_CLEAR_CACHE)) {
				resultMap = new HashMap();
				getIWApplicationContext().getIWMainApplication().getIWCacheManager().invalidateCache(SEARCH_FORM_CACHE_KEY);
				System.out.println("[ServiceSearchBusinessBean] Invalidating stored search results");
			}
		}
	}

  protected BookingBusiness getBookingBusiness(IWApplicationContext iwac) {
		try {
			return (BookingBusiness) IBOLookup.getServiceInstance(iwac, BookingBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
}
	
}
