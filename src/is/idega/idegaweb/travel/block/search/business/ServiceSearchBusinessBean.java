package is.idega.idegaweb.travel.block.search.business;

import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngine;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineHome;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroup;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupBMPBean;
import is.idega.idegaweb.travel.block.search.data.ServiceSearchEngineStaffGroupHome;
import is.idega.idegaweb.travel.block.search.presentation.AbstractSearchForm;
import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.TravelSessionManager;
import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.service.business.BookingBusiness;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import javax.transaction.UserTransaction;
import com.idega.block.basket.business.BasketBusiness;
import com.idega.block.basket.data.BasketEntry;
import com.idega.block.creditcard.business.CreditCardAuthorizationException;
import com.idega.block.creditcard.business.CreditCardBusiness;
import com.idega.block.creditcard.business.CreditCardClient;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductBusinessBean;
import com.idega.block.trade.stockroom.business.ProductComparator;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginDBHandler;
import com.idega.data.IDOEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.event.IWPageEventListener;
import com.idega.idegaweb.IWException;
import com.idega.idegaweb.IWUserContext;
import com.idega.presentation.IWContext;
import com.idega.user.business.GroupBusiness;
import com.idega.user.business.UserBusiness;
import com.idega.user.data.Group;
import com.idega.user.data.GroupHome;
import com.idega.user.data.User;
import com.idega.util.IWTimestamp;

/**
 * @author root
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServiceSearchBusinessBean extends IBOServiceBean implements ServiceSearchBusiness, IWPageEventListener {

	private HashMap resultMap = new HashMap();

	public static String SEARCH_FORM_CACHE_KEY = "abstract_search_form";

	
	private String SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION = "Search Engine administator group";
	private String permissionGroupNameExtention = " - admins";

	public ServiceSearchBusinessBean() {
		super();
	}
	
	public void initializeBean() {
		getProductBusiness().addActionListener(this);
	}


	public List getErrorFormFields(IWContext iwc, String categoryKey, boolean useCVC, boolean useBasket) throws IDOLookupException, FinderException {
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

		if (!useBasket) {
			String productId = iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_ID);
			
			ProductPriceHome ppHome = (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
			Collection pPrices = ppHome.findProductPrices(Integer.parseInt(productId), -1, -1, true, categoryKey);
			Iterator iter = pPrices.iterator();
			ProductPrice price;
			int iMany = 0;
			while (iter.hasNext()) {
				price = (ProductPrice) iter.next();
			  try {
				iMany += Integer.parseInt(iwc.getParameter("priceCategory"+price.getPrimaryKey()));
			  }catch (NumberFormatException n) {
			  	//n.printStackTrace();
			  }
			}		
			
			if (iMany < 1) {
				list.add(AbstractSearchForm.ERROR_NO_BOOKING_COUNT);
			}
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
//			System.out.println("ServiceSearchBusiness : checking reults : " +results.size());
			results = getProductInstanceCollection(results);
//			System.out.println("ServiceSearchBusiness : results converted to products");
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
			if (from == null || to == null || to.isEarlierThan(from)) {
				return new Vector();
			}
			while (iter.hasNext() && from != null && to != null) {
				try {
					product = (Product) iter.next();
//					System.out.println("ServiceSearchBusiness : checking product : " +product.getProductName(iwc.getCurrentLocaleId()));
					productIsValid = getBookingBusiness().getIsProductValid(iwc, product, from, to);
					if (productIsValid) {
//						System.out.println("ServiceSearchBusiness : valid");
						map.put(product, new Boolean(productIsValid));
					}	else {
//						System.out.println("ServiceSearchBusiness : invalid");
						toRemove.add(product);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			results.removeAll(toRemove);
			return results;
		} else {
//			System.out.println("ServiceSearchBusiness : No results");
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

	public Collection getServiceSearchEngines(Group supplierManager) {
		Collection coll = new Vector();
		try {
			coll = getSearchEngineHome().findAll(supplierManager);
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
	
	/**
	 * @deprecated
	 * @return
	 */
	public ServiceSearchEngine storeEngine(Object pk, String name, String code, String url, Group supplierManager) {
		return storeEngine(pk, name, code, url, supplierManager, false);
	}	
	public ServiceSearchEngine storeEngine(Object pk, String name, String code, String url, Group supplierManager, boolean useBasket) {
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
			if (supplierManager != null) {
				engine.setSupplierManager(supplierManager);
			}
			engine.setURL(url);
			engine.setUseBasket(useBasket);
			engine.store();
			
			if (getServiceSearchEngineStaffGroup(engine) == null) {
				String sName = name+"_"+engine.getPrimaryKey().toString();
				Object object = IDOLookup.getHome(ServiceSearchEngineStaffGroup.class);
				ServiceSearchEngineStaffGroupHome ssesgh = (ServiceSearchEngineStaffGroupHome) IDOLookup.getHomeLegacy(ServiceSearchEngineStaffGroup.class);	
				ServiceSearchEngineStaffGroup sGroup = ssesgh.create();
				sGroup.setName(sName);
				sGroup.store();
				engine.setStaffGroupID(((Integer)sGroup.getPrimaryKey()).intValue());
				engine.store();
			} else if (engine.getStaffGroupID() < 1){
				System.out.println("[ServiceSearchBusinessBean] Fixing engine, setting groupID");
				engine.setStaffGroupID(((Integer)getServiceSearchEngineStaffGroup(engine).getPrimaryKey()).intValue());
				engine.store();
			}
			
			if (getPermissionGroup(engine) == null) {
				String sName = name+"_"+engine.getPrimaryKey().toString();
	      Group pGroup = ((GroupHome) IDOLookup.getHome(Group.class)).create();
	      pGroup.setName(sName+permissionGroupNameExtention);
	      pGroup.setDescription(SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION);
	      pGroup.setGroupType("permission");
	      pGroup.store();
	      
//	      pGroup.addGroup(user);
//	      AccessControl ac = new AccessControl();
//				ac.createPermissionGroup(sName+permissionGroupNameExtention, SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION, "", null ,null);
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
		User user;
		try {
			user = getUserBusiness().insertUser(name,"","- admin",name+" - admin","Search Engine administrator",null,IWTimestamp.RightNow(),null);
			LoginDBHandler.createLogin(user.getID(), userName, password);
		
			addUser(engine, user, addToPermissionGroup);		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean isUserInPermissionGroup(ServiceSearchEngine engine, User user) throws RemoteException, FinderException {
		Group pGroup = getPermissionGroup(engine);
		Collection allUsers = getUserBusiness().getUsersInGroup(pGroup);
		return allUsers.contains(user);
	}
	
	public Group getPermissionGroup(ServiceSearchEngine engine) throws RemoteException, FinderException {
	  String name = engine.getName()+"_"+engine.getPrimaryKey().toString() + permissionGroupNameExtention;
	  String description = SEARCH_ENGINE_ADMINISTRATOR_GROUP_DESCRIPTION ;

	  Group pGroup = null;
	  Collection coll = getGroupBusiness().getGroupHome().findGroupsByNameAndDescription(name, description);
	  if (coll != null && !coll.isEmpty()) {
	  	Iterator iter = coll.iterator();
	  	pGroup = (Group) iter.next();
	  }

	  if (coll != null && !coll.isEmpty()) {
		  coll = getGroupBusiness().getGroupHome().findGroupsByNameAndDescription(engine.getName()+permissionGroupNameExtention, description);
		  if (coll != null && !coll.isEmpty()) {
		  	Iterator iter = coll.iterator();
		  	pGroup = (Group) iter.next();
		  }
	  }
//	  List listi = null;
//	  try {
//	  	listi = EntityFinder.findAllByColumn((PermissionGroup) GroupBMPBean.getStaticInstance(Group.class), GroupBMPBean.getNameColumnName(), name, GroupBMPBean.getGroupDescriptionColumnName(), description);
//	  } catch (SQLException e) {
//	  	e.printStackTrace();
//	  }
//	  if (listi != null) {
//	  	if (listi.size() > 0) {
//	  		pGroup = (Group) listi.get(listi.size()-1);
//	  	}
//	  } else {
//	  	try {
//	  		listi = EntityFinder.findAllByColumn((PermissionGroup) GroupBMPBean.getStaticInstance(Group.class), GroupBMPBean.getNameColumnName(), engine.getName()+permissionGroupNameExtention, GroupBMPBean.getGroupDescriptionColumnName(), description);
//	  	} catch (SQLException e1) {
//	  		e1.printStackTrace();
//	  	}
//	  	
//	  	if (listi != null) {
//	  		if (listi.size() > 0) {
//	  			pGroup = (Group) listi.get(listi.size()-1);
//	  		}
//	  	}
//	  }

	  return pGroup;
	}

	public ServiceSearchEngineStaffGroup getServiceSearchEngineStaffGroup(ServiceSearchEngine engine) throws RemoteException, FinderException {
	  String name = engine.getName()+"_"+engine.getPrimaryKey().toString();
	  ServiceSearchEngineStaffGroup sGroup = null;
	  ServiceSearchEngineStaffGroupHome ssesgh = (ServiceSearchEngineStaffGroupHome) IDOLookup.getHome(ServiceSearchEngineStaffGroup.class);

	  Collection coll = ssesgh.findGroupsByName(name);
	  if (coll != null && !coll.isEmpty()) {
	  	Iterator iter = coll.iterator();
	  	sGroup = (ServiceSearchEngineStaffGroup) iter.next();
	  }

	  if (coll != null && !coll.isEmpty()) {
		  coll = ssesgh.findGroupsByName(engine.getName());
		  if (coll != null && !coll.isEmpty()) {
		  	Iterator iter = coll.iterator();
		  	sGroup = (ServiceSearchEngineStaffGroup) iter.next();
		  }
	  }	  
//	  List listi = EntityFinder.findAllByColumn((GenericGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class), ServiceSearchEngineStaffGroupBMPBean.getNameColumnName(), name);
//	  if (listi != null) {
//			if (listi.size() > 0) {
//			  sGroup = (ServiceSearchEngineStaffGroup) listi.get(listi.size()-1);
//			}
//	  }
//	  if (listi == null) {
//		  	listi = EntityFinder.findAllByColumn((GenericGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class), ServiceSearchEngineStaffGroupBMPBean.getNameColumnName(), engine.getName());
//			if (listi != null && listi.size() > 0) {
//			  sGroup = (ServiceSearchEngineStaffGroup) listi.get(listi.size()-1);
//			}
//	  }
	  if (sGroup == null) {
	  		System.err.println("searchEngineStaffGroup == null");
	  }
	  return sGroup;
	}

	public void addUser(ServiceSearchEngine engine, User user, boolean addToPermissionGroup) throws RemoteException, FinderException {
	  Group pGroup = getPermissionGroup(engine);
	  ServiceSearchEngineStaffGroup sGroup = getServiceSearchEngineStaffGroup(engine);
	  if (addToPermissionGroup) {
	  	pGroup.addGroup(user);
	  }
	  sGroup.addGroup(user);
	}

	public ServiceSearchEngine getUserSearchEngine(User user) throws RuntimeException, RemoteException{
	  try {
//		  Group gGroup = ((GroupHome) IDOLookup.getHome(Group.class)).createLegacy();
//		  List gr = gGroup.getAllGroupsContainingUser(user);
		  Collection gr = getUserBusiness().getUserGroups(user);
		  if(gr != null){
				Iterator iter = gr.iterator();
				while (iter.hasNext()) {
					try {
						Group item = (Group)iter.next();
						String flepps = item.getGroupTypeValue();
						if(item.getGroupType().equals(((ServiceSearchEngineStaffGroup) ServiceSearchEngineStaffGroupBMPBean.getStaticInstance(ServiceSearchEngineStaffGroup.class)).getGroupTypeValue())){
							return getSearchEngineHome().findByGroupID( ((Integer) item.getPrimaryKey()).intValue());
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

	public boolean deleteServiceSearchEngine(ServiceSearchEngine engine, User performer) {
		try {
			ServiceSearchEngineStaffGroup sGroup = this.getServiceSearchEngineStaffGroup(engine);
			Collection users = getUserBusiness().getUsersInGroup(sGroup);
//			List users = UserGroupBusiness.getUsersContained(sGroup.getID());
			if (users != null) {
				Iterator iter = users.iterator();
				User user;
				while (iter.hasNext()) {
					try {
						user = (User) iter.next();
						sGroup.removeGroup(user, performer);
						//sGroup.removeFrom(user);
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
	public TravelStockroomBusiness getTravelStockroomBusiness() throws RemoteException {
		return (TravelStockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), TravelStockroomBusiness.class);
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
				clearAllEngineCache();
			}
		}
	}

  protected BookingBusiness getBookingBusiness() {
		try {
			return (BookingBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), BookingBusiness.class);
		} catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }

  protected GroupBusiness getGroupBusiness() {
  	try {
			return (GroupBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), GroupBusiness.class);
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
  
  public void clearAllEngineCache() {
	resultMap = new HashMap();
	getIWApplicationContext().getIWMainApplication().getIWCacheManager().invalidateCache(SEARCH_FORM_CACHE_KEY);
	System.out.println("[ServiceSearchBusinessBean] Invalidating stored search results");
  }

  public boolean actionPerformed(IWContext iwc) throws IWException {
		String action = iwc.getParameter(AbstractSearchForm.ACTION);
		if (action == null) {
			action = "";
		}
		if (action != null && action.equals(AbstractSearchForm.ACTION_ADD_TO_BASKET)) {
			boolean success = addToBasket(iwc);
			try {
				getSearchSession(iwc).setAddToBasketSuccess(success);
			}
			catch (RemoteException e) {
				e.printStackTrace();
			}
			return success;
		} else if (action.equals(AbstractSearchForm.ACTION_CONFIRM)){
			try {
				Collection bookings = doBasketBooking(iwc);
				getSearchSession(iwc).setBookingsSavedFromBasket(bookings);
				return true;
			}
			catch (Exception e) {
				try {
					getSearchSession(iwc).setException(e);
				}
				catch (RemoteException e1) {
					e1.printStackTrace();
				}
				return false;
			}
		}
		return false;

	  }

  /**
   * Creates a booking and adds it to the basket
   * @param iwc
   * @return
   */
	public boolean addToBasket(IWContext iwc) {
		BasketBusiness travelbasket = getBasketBusiness(iwc);
		
		try {
			GeneralBooking booking = doBooking(iwc, false);
//			booking.setIsValid(false);
			booking.store();
			
			Collection coll = getBooker().getMultibleBookings(booking);
			if (coll != null) {
				Iterator iter = coll.iterator();
				while (iter.hasNext()) {
					GeneralBooking b = (GeneralBooking) iter.next();
					b.setIsValid(false);
					b.store();
				}
			}
			
			getBooker().invalidateCache(booking.getID());
			
			travelbasket.addItem(booking);
			return true;
		}
		catch (Exception e1) {
			String message = e1.getMessage();
			try {
				int error = Integer.parseInt(message);
				switch (error) {
					case BookingForm.errorTooMany :
						getSearchSession(iwc).setAddToBasketErrorLocalizedKey("travel.error_too_many", "Too many");
						break;
					case BookingForm.errorTooFew :
						getSearchSession(iwc).setAddToBasketErrorLocalizedKey("travel.error_too_few", "Too few");
						break;
					case BookingForm.errorFieldsEmpty :
						getSearchSession(iwc).setAddToBasketErrorLocalizedKey("travel.error_field_empty", "Some fields are empty");
						break;
				}
			} catch (NumberFormatException ignore) {
				
				e1.printStackTrace();
			} catch (RemoteException r) {
				
			}
		}
		return false;
	}
  
	protected ServiceSearchSession getSearchSession(IWContext iwc) {
		try {
			return (ServiceSearchSession) IBOLookup.getSessionInstance(iwc, ServiceSearchSession.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	protected BasketBusiness getBasketBusiness(IWContext iwc) {
		try {
			return (BasketBusiness) IBOLookup.getSessionInstance(iwc, BasketBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	protected CreditCardBusiness getCreditCardBusiness() {
		try {
			return (CreditCardBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), CreditCardBusiness.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	protected Booker getBooker() {
		try {
			return (Booker) IBOLookup.getServiceInstance(getIWApplicationContext(), Booker.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
	}
	public Collection doBasketBooking(IWContext iwc) throws Exception {
		String sEngine = iwc.getParameter(BookingForm.PARAMETER_CODE);
		if (sEngine != null) {
			ServiceSearchEngine engine = getSearchEngineHome().findByCode(sEngine);
			return doBasketBooking(iwc, engine);
		} else {
			throw new NullPointerException("Engine Code is null");
		}
		
	}
	
	
	public Collection doBasketBooking(IWContext iwc, ServiceSearchEngine engine) throws Exception {
		CreditCardClient client = getCreditCardBusiness().getCreditCardClient(engine.getSupplierManager(), IWTimestamp.RightNow());

		// Going through bookings, getting prices and currencies
		HashMap currMaps = new HashMap(); // A map with Currency as key and float[] as value
		Collection values = getBasketBusiness(iwc).getBasket().values();
		Collection bookings = new Vector();
		BasketEntry entry;
		GeneralBooking booking;
		Iterator iter = values.iterator();
		String ccCurr = null;
		while (iter.hasNext()) {
			entry = (BasketEntry) iter.next();
			booking = (GeneralBooking) entry.getItem();
			ccCurr = getBooker().getCurrency(booking).getCurrencyAbbreviation();
			if (currMaps.get(ccCurr) == null) {
				currMaps.put(ccCurr, new float[]{0});
			}
			float price = getBooker().getBookingPrice(booking);
			((float[])currMaps.get(ccCurr))[0] += price;
			bookings.add(booking);
		}

		if (currMaps.keySet().size() != 1) {
			CreditCardAuthorizationException e = new CreditCardAuthorizationException();
			e.setDisplayError("Invalid number of currencies ("+currMaps.keySet().size()+")");
			e.setErrorMessage("Invalid number of currencies ("+currMaps.keySet().size()+")");
			throw e;
		}
		
		// Getting parameters needed for the cc authorization
		String ccName = iwc.getParameter(BookingForm.PARAMETER_NAME_ON_CARD);
		String ccNumber = iwc.getParameter(BookingForm.parameterCCNumber);
		String ccYear = iwc.getParameter(BookingForm.parameterCCYear);
		String ccMonth = iwc.getParameter(BookingForm.parameterCCMonth);
		String ccCVC = iwc.getParameter(BookingForm.parameterCCCVC);
		String refNum = iwc.getParameter(AbstractSearchForm.PARAMETER_REFERENCE_NUMBER);

		// Checking for Authorization for every currency
		Collection responseStrings = new Vector();
		Iterator currIterator = currMaps.keySet().iterator();
		String authNr = null;
		while (currIterator.hasNext()) {
			String curr = (String) currIterator.next();
			float cPrice = ((float[])currMaps.get(curr))[0];
			System.out.println("[SearchBus] checking for price = "+cPrice+" "+curr+" in merchant = "+client.getCreditCardMerchant().getMerchantID());
			try {
				authNr = client.doSale(ccName, ccNumber, ccMonth, ccYear, ccCVC, cPrice, curr, refNum);
				responseStrings.add(authNr);
			} catch (CreditCardAuthorizationException e) {
				throw e;
			}
		}
		
		// If no authorization error have been thrown, the bookings must now be validated
		String surname = iwc.getParameter(BookingForm.PARAMETER_FIRST_NAME);
		String lastname = iwc.getParameter(BookingForm.PARAMETER_LAST_NAME);
		String address = iwc.getParameter(BookingForm.PARAMETER_ADDRESS);
		String areaCode = iwc.getParameter(BookingForm.PARAMETER_AREA_CODE);
		String email = iwc.getParameter(BookingForm.PARAMETER_EMAIL);
		String phone = iwc.getParameter(BookingForm.PARAMETER_PHONE);
		
		String city = iwc.getParameter(BookingForm.PARAMETER_CITY);
		String country = iwc.getParameter(BookingForm.PARAMETER_COUNTRY);
//		String pickupId = iwc.getParameter(BookingForm.parameterPickupId);
//		String pickupInfo = iwc.getParameter(BookingForm.parameterPickupInf);
//		String sPaymentType = iwc.getParameter("payment_type");
		String comment = iwc.getParameter(BookingForm.PARAMETER_COMMENT);
		String code = iwc.getParameter(BookingForm.PARAMETER_CODE);
//		String key = iwc.getParameter(BookingForm.parameterPriceCategoryKey);
		
		Iterator biter = bookings.iterator();
		while (biter.hasNext()) {
			booking = (GeneralBooking) biter.next();
			
			Collection coll = getBooker().getMultibleBookings(booking);
			if (coll != null) {
				Iterator miter = coll.iterator();
				while (miter.hasNext()) {
					GeneralBooking b = (GeneralBooking) miter.next();
					b.setIsValid(true);
					b.setName(surname+" "+lastname);
					b.setAddress(address);
					b.setPostalCode(areaCode);
					b.setEmail(email);
					b.setTelephoneNumber(phone);
					b.setCity(city);
					b.setCountry(country);
					if (comment == null) {
						comment = "";
					}
					b.setComment(comment);
					if (code != null) {
						b.setCode(code);
					}
					b.setCreditcardAuthorizationNumber(authNr);
					b.setIsValid(true);
					b.store();
				}
			}
			getBooker().invalidateCache(booking.getID());
			
		}
		
		
//		Iterator citer = responseStrings.iterator();
//		while (citer.hasNext()) {
//			String reps = (String) citer.next();
//			client.finishTransaction(reps);
//		}
		getBasketBusiness(iwc).emptyBasket();
		return bookings;
//		getBasketBusiness(iwc).repps();
	}

	public GeneralBooking doBooking(IWContext iwc, boolean doCreditCardCheck) throws Exception {
		if (!iwc.isParameterSet(BookingForm.PARAMETER_CODE)) {
//			throw new RuntimeException("EngineCode not found");
		}
		Product product = getProduct(iwc);
		int bookingId = -1;
		if (!doCreditCardCheck) {
			bookingId = getBookingForm(iwc).checkBooking(iwc, true, false, false, doCreditCardCheck);
		} else {
			bookingId = getBookingForm(iwc).handleInsert(iwc, doCreditCardCheck);
		}
		
//		int bookingId = getBookingForm().checkBooking(iwc, true);
		GeneralBookingHome gBookingHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
		GeneralBooking gBooking = null;
		
		if (bookingId > 0) {
			gBooking = gBookingHome.findByPrimaryKey(new Integer(bookingId));
			gBooking.setCode(iwc.getParameter(BookingForm.PARAMETER_CODE));
//			gBooking.setCode(this.engine.getCode());
		} else if (bookingId == BookingForm.errorTooFew) {
			throw new Exception(Integer.toString(bookingId));
		} else if (bookingId == BookingForm.errorTooMany) {
			throw new Exception(Integer.toString(bookingId));
		} else if (bookingId == BookingForm.errorFieldsEmpty) {
			throw new Exception(Integer.toString(bookingId));
		}
		
		return gBooking;
	}
	
	protected BookingForm getBookingForm(IWContext iwc) throws Exception {
		return getServiceHandler().getBookingForm(iwc, getProduct(iwc));
	}

	protected Product getProduct(IWContext iwc) {
		try {
			ProductHome home = (ProductHome) IDOLookup.getHome(Product.class);
			return home.findByPrimaryKey(new Integer(iwc.getParameter(AbstractSearchForm.PARAMETER_PRODUCT_ID)));
		}catch (Exception e) {
			return null;
		}
	}
	
}
