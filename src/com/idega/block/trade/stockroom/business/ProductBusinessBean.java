package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.category.data.ICCategory;
import com.idega.block.category.data.ICCategoryBMPBean;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryHome;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.ProductPriceHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.presentation.ProductCatalog;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.business.IBOServiceBean;
import com.idega.core.localisation.business.ICLocaleBusiness;
import com.idega.core.localisation.presentation.ICLocalePresentation;
import com.idega.core.location.data.Address;
import com.idega.data.EntityFinder;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWMainApplication;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
import com.idega.util.Timer;
/**
 * @todo losa vi� service;
 */
//import is.idega.idegaweb.travel.data.Service;
//import is.idega.idegaweb.travel.presentation.ServiceViewer;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ProductBusinessBean extends IBOServiceBean implements ProductBusiness {
  public static final String PRODUCT_ID = "pr_bus_prod_id";

  public static String uniqueDepartureAddressType = "TB_TRIP_DEPARTURE_ADDRESS";
  public static String uniqueArrivalAddressType = "TB_TRIP_ARRIVAL_ADDRESS";
  public static String uniqueHotelPickupAddressType = "TB_HOTEL_PICKUP_ADDRESS";

  public static String PARAMETER_LOCALE_DROP = "product_locale_drop";
  public static int defaultLocaleId = 1;

  private static String productsApplication = "productsApplication_";
  
  public static final String COMMAND_CLEAR_CACHE = "clearProductsCache";
  public HashMap products = new HashMap();

  public ProductBusinessBean() {
  }

  public int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(productId,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  public int updateProduct(int productId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int localeId) throws Exception{
    return createProduct(productId,-1, fileId, productName, number, productDescription, isValid, null, -1, localeId);
  }

  public int createProduct(Integer fileId, String productName, String number, String productDescription, boolean isValid, int localeId) throws Exception{
    return createProduct(-1,-1, fileId, productName, number, productDescription, isValid, null, -1, localeId);
  }

  public int createProduct(Integer fileId, String productName, String number, String productDescription, boolean isValid) throws Exception{
    return createProduct(-1,-1, fileId, productName, number, productDescription, isValid, null, -1, -1);
  }

  public int createProduct(int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(-1,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  public int createProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return createProduct(productId, supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId, -1);
  }

  public int createProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId, int localeId) throws Exception{
    Product product= null;
    if (productId == -1) {
      product = getProductHome().create();
//      product = ((com.idega.block.trade.stockroom.data.ProductHome)com.idega.data.IDOLookup.getHomeLegacy(Product.class)).createLegacy();
    }else {
      product = getProduct(productId);// Product(productId);
    }

    if (supplierId != -1) {
		product.setSupplierId(supplierId);
	}
    if(fileId != null){
      product.setFileId(fileId);
    }
    product.setIsValid(isValid);
    if (discountTypeId != -1) {
      product.setDiscountTypeId(discountTypeId);
    }
    if (number == null) {
		number = "";
	}
    product.setNumber(number);


    if (productId == -1) {
      product.store();
    }else {
      updateProduct(product);
      //product.update();
    }

    if (localeId == -1) {
      product.setProductName(1, productName);
      product.setProductDescription(1, productDescription);
    }else {
      product.setProductName(localeId, productName);
      product.setProductDescription(localeId, productDescription);
    }

    product.addTravelAddresses(addressIds);
    clearAddressMaps(product.getPrimaryKey().toString());
	  invalidateProductCache(product.getPrimaryKey().toString());
    clearProductCache(Integer.toString(supplierId));
    return ((Integer) product.getPrimaryKey()).intValue();
  }
  
  public boolean clearAddressMaps(String productID, String remoteDomainToExclude) {
    this.productDepartureAddresses.put(productID+false, null); 
    this.productDepartureAddresses.put(productID+true, null); 
    this.productDepartureAddresses2 = new HashMap();
	getStockroomBusiness().executeRemoteService(remoteDomainToExclude, "clearAddressMaps&productID="+productID);
	return true;
  }
  
  private boolean clearAddressMaps(String productID) {
	  return clearAddressMaps(productID, null);
  }

  public void addTravelAddress(Product product, TravelAddress travelAddress) {
	  product.addTravelAddress(travelAddress);
	  clearAddressMaps(product.getPrimaryKey().toString());
  }
  
  public void addArrivalAddress(Product product, Address address) {
	  product.addArrivalAddress(address);
  }
  
  public void removeTravelAddress(Product product, TravelAddress travelAddress) throws IDORemoveRelationshipException {
	  product.removeTravelAddress(travelAddress);
	  clearAddressMaps(product.getPrimaryKey().toString());
  }
  
  public void removeAllTravelAddresses(Product product) throws IDORemoveRelationshipException {
	  product.removeAllFrom(TravelAddress.class);
	  clearAddressMaps(product.getPrimaryKey().toString());
  }
  
  public String getProductIdParameter() {
    return PRODUCT_ID;
  }

  public String getParameterLocaleDrop() {
    return PARAMETER_LOCALE_DROP;
  }


  public Product getProduct(Integer productId) throws RemoteException, FinderException{
    return getProduct(productId.intValue());
  }

  public Product getProduct(int productId) throws RemoteException, FinderException{
    Object obj = this.products.get(Integer.toString(productId));
    if (obj == null) {
      Product prod = getProductHome().findByPrimaryKey(new Integer(productId));
//      Product prod = ((com.idega.block.trade.stockroom.data.ProductHome)com.idega.data.IDOLookup.getHomeLegacy(Product.class)).findByPrimaryKeyLegacy(productId);
      this.products.put(Integer.toString(productId), prod);
      //System.err.println("ProductBusiness : creating product : "+productId);
      return prod;
    }else {
      //System.err.println("ProductBusiness : found product : "+productId);
      return (Product) obj;
    }
  }
  
  public boolean invalidateProductCache(String productID) {
	  return invalidateProductCache(productID, null);
  }
  public boolean invalidateProductCache(String productID, String remoteDomainToExclude) {
	  this.products.remove(productID);
	  this.timeframeMap.remove(productID);
	  getStockroomBusiness().executeRemoteService(remoteDomainToExclude, "invalidateProductCache&productID="+productID);
	  return true;
  }

  public void deleteProduct(Product product) throws RemoteException , IDOException {
	  invalidateProductCache(product.getPrimaryKey().toString());
    product.invalidate();
  }

  public Product updateProduct(Product product) throws RemoteException, FinderException, IDOException {
	  invalidateProductCache(product.getPrimaryKey().toString());
    product.store();
    return getProduct( (Integer) product.getPrimaryKey() );
  }
  
  public ProductCategory getProductCategory(int categoryID) {
    try {
      return ((com.idega.block.trade.stockroom.data.ProductCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(ProductCategory.class)).findByPrimaryKeyLegacy(categoryID);
    }
    catch (SQLException e) {
      return null;
    }
  }

  public String getProductNameWithNumber(Product product) throws RemoteException{
    return getProductNameWithNumber(product, true);
  }

  public String getProductNameWithNumber(Product product, int localeID) throws RemoteException{
    return getProductNameWithNumber(product, true, localeID);
  }

  public String getProductNameWithNumber(Product product, boolean numberInFront) throws RemoteException{
    return getProductNameWithNumber(product, numberInFront, -1);
  }

  public String getProductNameWithNumber(Product product, boolean numberInFront, int localeID) throws RemoteException{
    String returnString = "";

    String number = product.getNumber();
    String name = "";
    if (localeID == -1) {
      name = product.getProductName(defaultLocaleId);
    }else {
//      name = getProductName(product, localeID);
      name = product.getProductName(localeID);
    }

    if (numberInFront) {
      if (number != null && !number.equals("")) {
	returnString = number + " " + name;
      }else {
	returnString = name;
      }
    }else {
      if (!number.equals("")) {
	returnString = name + " " + number;
      }else {
	returnString = name;
      }
    }

    return returnString;
  }



  public int getSelectedLocaleId(IWContext iwc) {
    String sLocaleId = iwc.getParameter(PARAMETER_LOCALE_DROP);
    Locale currentLocale = iwc.getCurrentLocale();
    Locale chosenLocale;

    int iLocaleId = -1;
    if(sLocaleId!= null){
      iLocaleId = Integer.parseInt(sLocaleId);
      chosenLocale = ICLocaleBusiness.getLocaleReturnIcelandicLocaleIfNotFound(iLocaleId);
    }
    else{
      chosenLocale = currentLocale;
      iLocaleId = ICLocaleBusiness.getLocaleId(chosenLocale);
    }

    return iLocaleId;
  }

  public DropdownMenu getLocaleDropDown(IWContext iwc) {
    int iLocaleId = getSelectedLocaleId(iwc);
    DropdownMenu localeDrop = ICLocalePresentation.getLocaleDropdownIdKeyed(PARAMETER_LOCALE_DROP);
      localeDrop.setToSubmit();
      localeDrop.setSelectedElement(Integer.toString(iLocaleId));
    return localeDrop;
  }

  public boolean clearProductCache(String supplierId) {
	  return clearProductCache(supplierId, null);
  }
	  
  public boolean clearProductCache(String supplierId, String remoteDomainToExclude) {
    getIWApplicationContext().removeApplicationAttribute(productsApplication+supplierId);
    IWMainApplication.getIWCacheManager().invalidateCache(ProductCatalog.CACHE_KEY);
    this.triggerActionEvent(COMMAND_CLEAR_CACHE);
	getStockroomBusiness().executeRemoteService(remoteDomainToExclude, "clearProductCache&supplierID="+supplierId);
    return true;
  }

  public List getProducts(IWContext iwc, int supplierId) throws RemoteException{
    List temp = (List) iwc.getApplicationAttribute(productsApplication+supplierId);
    if (temp == null) {
      temp = getProducts(supplierId);
      iwc.setApplicationAttribute(productsApplication+supplierId, temp);
      return temp;
    }else {
      return temp;
    }
  }

  /**
   * @deprecated
   */
  public List getProducts(int supplierId) throws RemoteException{
    //return getProducts(supplierId, null);
    List list = new Vector();
    try {
      Collection coll = getProductHome().findProducts(supplierId);
      if (coll != null) {
      		list = new Vector(coll);
      }
    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }
    return list;
  }
  
  public Collection getProduct(int supplierId, int firstEntity, int lastEntity) throws FinderException, RemoteException {
	  return getProductHome().findProducts(supplierId, firstEntity, lastEntity);
  }


  public List getProducts() throws RemoteException, FinderException{
    return getProducts(-1, null);
  }

  public List getProducts(List productCategories) throws RemoteException, FinderException {
    List returner = new Vector();
    List temp;
    Product product;
    for (int i = 0; i < productCategories.size(); i++) {
      temp = getProducts((ICCategory) productCategories.get(i));
      for (int j = 0; j < temp.size(); j++) {
	product = (Product) temp.get(j);
	if (!returner.contains(product)) {
	  returner.add(product);
	}
      }
    }

    return returner;
  }

  public List getProducts(ICCategory category) throws RemoteException, FinderException{
    return getProducts(-1, category.getID(), null,null);
  }

  public List getProducts(ProductCategory productCategory) throws RemoteException, FinderException {
    return getProducts((ICCategory) productCategory);
  }

  public List getProducts(IWTimestamp stamp) throws RemoteException, FinderException{
    return getProducts(-1, stamp);
  }

  public List getProducts(IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException {
    return getProducts(-1, fromStamp, toStamp);
  }

  public List getProducts(int supplierId, IWTimestamp stamp) throws RemoteException, FinderException {
    if (stamp != null) {
		return getProducts(supplierId, stamp, new IWTimestamp(stamp));
	}
	else {
		return getProducts(supplierId, null, null);
	}
  }

  public List getProducts(int supplierId, IWTimestamp from, IWTimestamp to) throws RemoteException, FinderException{
    return getProducts(supplierId, -1, from, to);
  }

  /**
   * @deprecated
   */
  public List getProducts(int supplierId, int productCategoryId ,IWTimestamp from, IWTimestamp to) throws RemoteException, FinderException{
    Object obj = getIWApplicationContext().getApplicationAttribute(productsApplication+supplierId+productCategoryId+from+to);
    List products = null;
    if (obj != null) {
      products = (List) obj;
    }

    if (products == null) {
      products = new Vector();
      Collection coll = getProductHome().findProducts(supplierId, productCategoryId, from, to);
      if (coll != null) {
      		products = new Vector(coll);
      }
    }

    return products;
  }

  /**
   * @throws FinderException 
 * @throws EJBException 
 * @deprecated
   */
  public Timeframe getTimeframe(Product product, IWTimestamp stamp) throws RemoteException, EJBException, FinderException {
    return getTimeframe(product, stamp, -1);
  }
  
  private HashMap timeframeMap = new HashMap();
  public Timeframe[] getTimeframes(Product product) throws SQLException {
	  Timeframe[] returner = (Timeframe[]) this.timeframeMap.get(product.getPrimaryKey().toString());
	  if (returner == null) {
		  returner = product.getTimeframes();
		  this.timeframeMap.put(product.getPrimaryKey().toString(), returner);
	  }
	  return returner;
  }

  public Timeframe getTimeframe(Product product) throws SQLException {
	  Timeframe[] ts = getTimeframes(product);
	  if (ts != null && ts.length>0) {
		  return ts[0];
	  }
	  return null;
  }
  
  public Timeframe getTimeframe(Product product, IWTimestamp stamp, int travelAddressId) throws RemoteException, EJBException, FinderException {
  	try {
  		return getTimeframe(product, getTimeframes(product), stamp, travelAddressId);
  	} catch (SQLException e) {
  		e.printStackTrace(System.err);
  		return null;
  	}
  }
  
  public Timeframe getTimeframe(Product product, Timeframe[] timeframes, IWTimestamp stamp, int travelAddressId) throws RemoteException, EJBException, FinderException {
		Timeframe returner = null;
	  Collection pPrices;
	  for (int i = 0; i < timeframes.length; i++) {
	  	returner = timeframes[i];
	    if (travelAddressId != -1) {
	    	Timer t = new Timer();
	    	t.start();
	    	pPrices = getProductPriceBusiness().getProductPrices(((Integer) product.getPrimaryKey()).intValue() , timeframes[i].getID(), travelAddressId, false, null);
	    	t.stop();
	    	System.out.println("      [ProductBusiness] got prices : "+t.getTimeString());
//	      pPrices = getProductPriceHome().findProductPrices(((Integer) product.getPrimaryKey()).intValue() , timeframes[i].getID(), travelAddressId, false);
	//          System.err.println("getting prices : length = "+pPrices.length);
	      if (pPrices == null || pPrices.isEmpty()) {
	        continue;
	      }
	    }

		if (getStockroomBusiness().isInTimeframe( new IWTimestamp(returner.getFrom()) , new IWTimestamp(returner.getTo()), stamp, returner.getIfYearly() )) {
		  return returner;
		}
	  }
    return returner;
  }
  
  public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered) throws RemoteException, FinderException  {
  		return getDepartureAddresses(product, stamp, ordered, null);
  }
  
  public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key) throws RemoteException, FinderException  {
  	return getDepartureAddresses(product, stamp, ordered, key, null);
  } 
  
  private HashMap productDepartureAddresses = new HashMap();
  private HashMap productDepartureAddresses2 = new HashMap();

  public List getDepartureAddresses(Product product, IWTimestamp stamp, boolean ordered, String key, Timeframe[] timeframes) throws RemoteException, FinderException  {

		String tFramesString = null;
		if (timeframes != null) {
			for (int i = 0; i < timeframes.length; i++) {
				tFramesString += timeframes[i].getPrimaryKey().toString();
			}
		}
		
		String mapKey = product.getPrimaryKey().toString()+stamp.toSQLDateString()+ordered+key+tFramesString;
		List returner = (List) this.productDepartureAddresses2.get(mapKey);
		
//		Collection pPrices;
		if (returner == null) {
			returner = new Vector();
			List list = getDepartureAddresses(product, ordered);
			TravelAddress ta;
			boolean add = false;
			if (list != null && !list.isEmpty()) {
				if (timeframes == null) {
					try {
						timeframes = getTimeframes(product);
					}
					catch (SQLException e) {
						throw new FinderException(e.getMessage());
					}
				}
				Iterator iter = list.iterator();
				while (iter.hasNext()) {
					ta = (TravelAddress) iter.next();
					add = false;
					for (int i = 0; i < timeframes.length; i++) {
//						Timer t2 = new Timer();
						if (getStockroomBusiness().isInTimeframe(new IWTimestamp(timeframes[i].getFrom()), new IWTimestamp(timeframes[i].getTo()), stamp, timeframes[i].getYearly())) {
							try {
//								t2.start();
								boolean b = getProductPriceHome().hasProductPrices(product.getID(), timeframes[i].getID(), ta.getID(), false, key);
//								t2.stop();
//								System.out.println("[ProductBusinessBean] check B : " +t2.getTimeString());
								if (b) {
//									if (key == null) {
//									pPrices = getProductPriceHome().findProductPrices(product.getID(), timeframes[i].getID(), ta.getID(), false);
//									} else {
//									pPrices = getProductPriceHome().findProductPrices(product.getID(), timeframes[i].getID(), ta.getID(), false, key);
//									}
//									
//									if (pPrices != null && !pPrices.isEmpty()) {
									add = true;
									break;
								}
							}
							catch (IDOException e) {
								e.printStackTrace();
							}
						}
					}
					if (add) {
						returner.add(ta);	
					}
				}
			}
			this.productDepartureAddresses2.put(mapKey, returner);
		}
		return returner;
  }

  public List getDepartureAddresses(Product product, boolean ordered) throws RemoteException, IDOFinderException  {
	  List list  = (List) this.productDepartureAddresses.get(product.getPrimaryKey().toString()+ordered);
	  if (list == null) {
		  list = product.getDepartureAddresses(ordered);
		  if (ordered) {
			  Collections.sort(list, new TravelAddressComparator(TravelAddressComparator.TIME));
		  }
		  this.productDepartureAddresses.put(product.getPrimaryKey().toString()+ordered, list);
	  }
	  return list;
  }

  public TravelAddress getDepartureAddress(Product product) throws RemoteException, IDOFinderException, SQLException{
      List tempAddresses = getDepartureAddresses(product, false);
//      List tempAddresses = product.getDepartureAddresses(false);
      if (tempAddresses.size() > 0) {
	return ((com.idega.block.trade.stockroom.data.TravelAddressHome)com.idega.data.IDOLookup.getHomeLegacy(TravelAddress.class)).findByPrimaryKeyLegacy(((TravelAddress)tempAddresses.get(0)).getID() );
      }else {
	return null;
      }
  }
  public Address[] getArrivalAddresses(Product product) throws RemoteException, IDOFinderException {
    List addresses = product.getArrivalAddresses();
    return ( (Address[]) addresses.toArray(new Address[]{}) );
//    return (Address[]) (product.findRelated( (Address) com.idega.core.data.AddressBMPBean.getStaticInstance(Address.class), com.idega.core.data.AddressBMPBean.getColumnNameAddressTypeId(), Integer.toString(com.idega.core.data.AddressTypeBMPBean.getId(uniqueArrivalAddressType))));
  }

  public Address getArrivalAddress(Product product) throws RemoteException, IDOFinderException, SQLException{
    Address[] tempAddresses = getArrivalAddresses(product);
    if (tempAddresses.length > 0) {
      return ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).findByPrimaryKeyLegacy(tempAddresses[0].getID());
    }else {
      return null;
    }
  }

  public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int supplierId) throws RemoteException {
    return getDropdownMenuWithProducts(iwc, supplierId, com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName());
  }

  public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, int supplierId, String parameterName) throws RemoteException{
  	List list = getProducts(iwc, supplierId);
  	return getDropdownMenuWithProducts(iwc, list, parameterName);
  }	
  public DropdownMenu getDropdownMenuWithProducts(IWContext iwc, List products, String parameterName) throws RemoteException{
    DropdownMenu menu = new DropdownMenu(parameterName);
    Product product;
    if (products != null && products.size() > 0) {
      for (int i = 0; i < products.size(); i++) {
	product = (Product) products.get(i);
	menu.addMenuElement(product.getPrimaryKey().toString() , getProductNameWithNumber(product));
      }
    }
    return menu;
  }

  /**
   * @deprecated
   */
  public List getProductCategories() throws IDOFinderException{
  		try {
			ProductCategoryHome pcHome = (ProductCategoryHome) IDOLookup.getHome(ProductCategory.class);
			return new Vector(pcHome.findAll());
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			e.printStackTrace();
		}
    return EntityFinder.getInstance().findAllOrdered(ProductCategory.class, ICCategoryBMPBean.getColumnName());
  }

  public List getProductCategories(Product product) throws RemoteException, IDORelationshipException{
    Collection coll = product.getProductCategories();
    List list = new Vector();
    if (coll != null) {
      Iterator iter = coll.iterator();
      ProductCategory pCat;
      while (iter.hasNext()) {
        pCat = (ProductCategory) iter.next();
        list.add(pCat);
      }
    }

    return new Vector(coll);
//    return EntityFinder.getInstance().findRelated(product, ProductCategory.class);
  }

  public ProductHome getProductHome() throws RemoteException {
    return (ProductHome) IDOLookup.getHome(Product.class);
  }

  public ProductPriceHome getProductPriceHome() throws RemoteException {
	    return (ProductPriceHome) IDOLookup.getHome(ProductPrice.class);
  }

  protected StockroomBusiness getStockroomBusiness() {
    try {
		return (StockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), StockroomBusiness.class);
	}
	catch (IBOLookupException e) {
		throw new IBORuntimeException(e);
	}
  }
  
  public ProductPriceBusiness getProductPriceBusiness() {
	  try {
		return (ProductPriceBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductPriceBusiness.class);
	}
	catch (IBOLookupException e) {
		throw new IBORuntimeException(e);
	}
  }
}
