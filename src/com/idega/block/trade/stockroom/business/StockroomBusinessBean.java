package com.idega.block.trade.stockroom.business;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.FinderException;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.ResellerBMPBean;
import com.idega.block.trade.stockroom.data.ResellerStaffGroup;
import com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.SupplierBMPBean;
import com.idega.block.trade.stockroom.data.SupplierHome;
import com.idega.block.trade.stockroom.data.SupplierStaffGroupBMPBean;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.core.accesscontrol.business.LoginBusinessBean;
import com.idega.core.accesscontrol.business.NotLoggedOnException;
import com.idega.core.data.GenericGroup;
import com.idega.core.data.GenericGroupHome;
import com.idega.core.user.data.User;
import com.idega.data.EntityControl;
import com.idega.data.EntityFinder;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.presentation.IWContext;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class StockroomBusinessBean extends IBOServiceBean implements StockroomBusiness {

  public StockroomBusinessBean() {
  }
  public void addSupplies(int product_id, float amount) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }
  public void depleteSupplies(int product_id, float amount) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method depleteSupplies() not yet implemented.");
  }
  public void setSupplyStatus(int product_id, float status) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }
  public float getSupplyStatus(int product_id)  throws SQLException {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method getSupplyStatus() not yet implemented.");
  }
  public float getSupplyStatus(int product_id, Timestamp time) {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    throw new java.lang.UnsupportedOperationException("Method getSupplyStatus() not yet implemented.");
  }

  public ProductPrice setPrice(int productPriceIdToReplace, int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId) throws SQLException {
    return setPrice(productPriceIdToReplace, productId, priceCategoryId, currencyId, time, price, priceType, timeframeId, addressId, -1);
  }

  public ProductPrice setPrice(int productPriceIdToReplace, int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId, int maxUsage) throws SQLException {
    if (productPriceIdToReplace != -1) {
        ProductPrice pPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).findByPrimaryKeyLegacy(productPriceIdToReplace);
          pPrice.invalidate();
          pPrice.update();
    }

    return setPrice(productId, priceCategoryId, currencyId, time, price, priceType, timeframeId, addressId, maxUsage);
  }

  public ProductPrice setPrice(int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId) throws SQLException {
    return setPrice(productId, priceCategoryId, currencyId, time, price, priceType, timeframeId, addressId, -1);
  }

  public ProductPrice setPrice(int productId, int priceCategoryId, int currencyId, Timestamp time, float price, int priceType, int timeframeId, int addressId, int maxUsage) throws SQLException {
       ProductPrice prPrice = ((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy();
         prPrice.setProductId(productId);
         prPrice.setCurrencyId(currencyId);
         prPrice.setPriceCategoryID(priceCategoryId);
         prPrice.setPriceDate(time);
         prPrice.setPrice(price);
         prPrice.setPriceType(priceType);
         prPrice.setMaxUsage(maxUsage);
       prPrice.insert();
       if (timeframeId != -1) {
         prPrice.addTo(Timeframe.class, timeframeId);
       }
       if (addressId != -1) {
         prPrice.addTo(TravelAddress.class, addressId);
       }
       return prPrice;
  }

  public  float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time) throws SQLException  {
    return getPrice(productPriceId, productId, priceCategoryId, currencyId, time, -1, -1);
  }

  public  ProductPrice getPrice(Product product) throws RemoteException {
    StringBuffer buffer = new StringBuffer();
      buffer.append("SELECT * FROM "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPriceTableName());
      buffer.append(" WHERE ");
      buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId() +" = "+product.getID());
      buffer.append(" AND ");
      buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId() +" is null");
      buffer.append(" ORDER BY "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" DESC");

    try {
//      EntityFinder.debug = true;
      List prices = EntityFinder.getInstance().findAll(ProductPrice.class, buffer.toString());
//      List prices = EntityFinder.findAll(((com.idega.block.trade.stockroom.data.ProductPriceHome)com.idega.data.IDOLookup.getHomeLegacy(ProductPrice.class)).createLegacy(), buffer.toString());
//      EntityFinder.debug = false;
      if (prices != null)
      if (prices.size() > 0) {
        return ((ProductPrice)prices.get(0));
      }
    }catch (IDOFinderException ido) {
      ido.printStackTrace(System.err);
    }

    return null;
  }

  public float getPrice(int productPriceId, int productId, int priceCategoryId, Timestamp time, int timeframeId, int addressId) throws SQLException  {
    return getPrice(productPriceId, productId, priceCategoryId, -1, time, timeframeId, addressId);
  }

  public float getPrice(int productPriceId, int productId, int priceCategoryId, int currencyId, Timestamp time, int timeframeId, int addressId) throws SQLException  {
    /**@todo: Implement this com.idega.block.trade.stockroom.business.SupplyManager method*/
    /*skila verði ef PRICETYPE_PRICE annars verði með tilliti til afsláttar*/

  	try {
        PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);
        ProductPrice ppr = ((ProductPrice)com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class));
        TravelAddress taddr = ((TravelAddress) com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class));
        Timeframe tfr = ((Timeframe) com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance(Timeframe.class));
        String addrTable = EntityControl.getManyToManyRelationShipTableName(TravelAddress.class, ProductPrice.class);
        String tfrTable = EntityControl.getManyToManyRelationShipTableName(Timeframe.class, ProductPrice.class);

        if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+taddr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            if (productPriceId != -1) {
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = "+productPriceId);
            }
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
//            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameCurrencyId()+" = "+currencyId);
//            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" <= '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+" = "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_PRICE);
            //buffer.append(" and ");
            //buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+ " desc");
          List result = EntityFinder.findAll(ppr,buffer.toString());

          if(result != null && result.size() > 0){
          	CurrencyHolder iceCurr = CurrencyBusiness.getCurrencyHolder(CurrencyHolder.ICELANDIC_KRONA);
          	if ( iceCurr != null && (((ProductPrice) result.get(0)).getCurrencyId() == iceCurr.getCurrencyID())) {
          		return new Float(Math.round( ((ProductPrice)result.get(0)).getPrice()) ).floatValue();
          	} else {
          		return ((ProductPrice)result.get(0)).getPrice();
          	}
          }else{
            //System.err.println(buffer.toString());
            throw new ProductPriceException("No Price Was Found");
          }
        }else if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
          StringBuffer buffer = new StringBuffer();
            buffer.append("select p.* from "+ppr.getEntityName()+" p");
            if (timeframeId != -1) {
              buffer.append(",  "+tfrTable+" tm");
            }
            if (addressId != -1) {
              buffer.append(", "+addrTable+" am");
            }
            buffer.append(" where ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);

            if (timeframeId != -1) {
              buffer.append(" and ");
              buffer.append("tm."+tfr.getIDColumnName()+" = "+timeframeId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = tm."+ppr.getIDColumnName());
            }
            if (addressId != -1) {
              buffer.append(" and ");
              buffer.append("am."+taddr.getIDColumnName()+" = "+addressId);
              buffer.append(" and ");
              buffer.append("p."+ppr.getIDColumnName()+" = am."+ppr.getIDColumnName());
            }

            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" < '"+time.toString()+"'");
            buffer.append(" and ");
            buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+" = "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.PRICETYPE_DISCOUNT);
            //buffer.append(" and ");
            //buffer.append("p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
            buffer.append(" order by p."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+ " desc");
          List result = EntityFinder.findAll(ppr,buffer.toString());
          float disc = 0;
          if(result != null && result.size() > 0){
            disc = ((ProductPrice)result.get(0)).getPrice();
          }
          
          float pr = getPrice(-1, productId,cat.getParentId(),currencyId,time, timeframeId, addressId);
          return pr*((100-disc) /100);
        }else{
          throw new ProductPriceException("No Price Was Found");
        }
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
      return 0;
    }

  }


  /**
   * returns 0.0 if pricecategory is not of type com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT
   */
  public float getDiscount(int productId, int priceCategoryId, Timestamp time) throws SQLException{
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKeyLegacy(priceCategoryId);
    if(cat.getType().equals(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT)){
      ProductPrice ppr = ((ProductPrice)com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class));
      StringBuffer buffer = new StringBuffer();
        buffer.append("select * from "+ppr.getEntityName());
        buffer.append(" where ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId()+" = "+productId);
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId()+" = "+priceCategoryId);
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate()+" < '"+time.toString()+"'");
        buffer.append(" and ");
        buffer.append(com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid()+" = 'Y'");
        buffer.append(" order by "+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceDate());
      List result = EntityFinder.findAll(ppr,buffer.toString());
      if(result != null && result.size() > 0){
        return ((ProductPrice)result.get(0)).getPrice();
      }else{
        return 0;
      }
    }else{
      throw new ProductPriceException();
    }
  }

  public int createPriceCategory(int supplierId, String name, String description, String extraInfo) throws SQLException {
		return createPriceCategory(supplierId, name, description, extraInfo, null);
  }
	
  public int createPriceCategory(int supplierId, String name, String description, String extraInfo, String key)throws SQLException {
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).createLegacy();
    cat.setType(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_PRICE);

    cat.setName(name);

    if(description != null){
      cat.setDescription(description);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }
    
    if (key != null) {
    	cat.setKey(key);
    }

    cat.insert();

    return cat.getID();
  }


  public void createPriceDiscountCategory(int parentId, int supplierId, String name, String description, String extraInfo) throws SQLException{
    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).createLegacy();
    cat.setParentId(parentId);
    cat.setType(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.PRICETYPE_DISCOUNT);

    cat.setName(name);

    if(description != null){
      cat.setDescription(description);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

    cat.insert();

  }


  public  int getUserSupplierId(User user) throws RuntimeException, SQLException{
  	try {
	  	GenericGroup gGroup = ((GenericGroupHome) IDOLookup.getHome(GenericGroup.class)).createLegacy();
	  	List gr = gGroup.getAllGroupsContainingUser(user);
	    if(gr != null){
    		System.out.println("StockroomBusiness : gr.size() = "+ gr.size());
    		SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);
	      Iterator iter = gr.iterator();
	      while (iter.hasNext()) {
	        GenericGroup item = (GenericGroup)iter.next();
	        System.out.println("StockroomBusiness : itemPK = "+ item.getPrimaryKey());
	    			System.out.println("StockroomBusiness : itemType = "+ item.getGroupType());
	    			System.out.println("StockroomBusiness : itemTypeValue = "+ item.getGroupTypeValue());
	        if(item.getGroupType().equals(SupplierStaffGroupBMPBean.GROUP_TYPE_VALUE)){
	        		System.out.println("StockroomBusiness : itemType == "+SupplierStaffGroupBMPBean.GROUP_TYPE_VALUE);
	        		try {
		        		System.out.println("StockroomBusiness : in try");
	        			Collection coll = sHome.findAllByGroupID(item.getID());
		        		System.out.println("StockroomBusiness : int try 2");
	        			if (coll != null && !coll.isEmpty()) {
	  	          		System.out.println("StockroomBusiness : coll != null && !coll.isEmpty()");
	        				return ((Supplier) coll.iterator().next()).getID();
	        			} else {
	  	          		System.out.println("StockroomBusiness : coll i ruglinu");
	        			}
	        		} catch (FinderException fe) {
	        			fe.printStackTrace();
	        		}
	        		
	          IDOLegacyEntity[] supp = ((Supplier) SupplierBMPBean.getStaticInstance(Supplier.class)).findAllByColumn(SupplierBMPBean.getColumnNameGroupID(),item.getID());
	          if(supp != null && supp.length > 0){
	          	System.out.println("StockroomBusiness : suppID (in here) == "+supp[0].getID());
	            return supp[0].getID();
	          } else {
	          		System.out.println("StockroomBusiness : supp = "+supp);
	          }
	        } else {
	        		System.out.println("StockroomBusiness : itemType != "+SupplierStaffGroupBMPBean.GROUP_TYPE_VALUE);
	        }
	      }
	    } else {
	    		System.out.println("StockroomBusiness : gr = null");
	    }
	    throw new RuntimeException("Does not belong to any supplier");
  	} catch (IDOLookupException e) {
  		e.printStackTrace();
  		throw new RuntimeException("Does not belong to any supplier");
  	}
  }

  public  int getUserSupplierId(IWContext iwc) throws RuntimeException, SQLException {
    String supplierLoginAttributeString = "sr_supplier_id";

    Object obj = LoginBusinessBean.getLoginAttribute(supplierLoginAttributeString,iwc);
    System.out.println("StockroomBusiness checking for supplier");
    if(obj != null){
      System.out.println("StockroomBusiness : obj = "+obj.toString());
      return ((Integer)obj).intValue();
    }else{
      System.out.println("StockroomBusiness : obj = NULL");
      User us = LoginBusinessBean.getUser(iwc);
      if(us != null){
      		System.out.println("StockroomBusiness : user = "+us.toString());
        int suppId = getUserSupplierId(us);
    			System.out.println("StockroomBusiness : suppID = "+suppId);
        LoginBusinessBean.setLoginAttribute(supplierLoginAttributeString,new Integer(suppId), iwc);
        return suppId;
      } else{
        System.out.println("StockroomBusiness : us = NULL");
        throw new NotLoggedOnException();
      }
    }
  }

  public  int getUserResellerId(IWContext iwc) throws RuntimeException, SQLException {
    String resellerLoginAttributeString = "sr_reseller_id";

    Object obj = LoginBusinessBean.getLoginAttribute(resellerLoginAttributeString,iwc);

    if(obj != null){
      return ((Integer)obj).intValue();
    }else{
      User us = LoginBusinessBean.getUser(iwc);
      if(us != null){
        int resellerId = getUserResellerId(us);
        LoginBusinessBean.setLoginAttribute(resellerLoginAttributeString,new Integer(resellerId), iwc);
        return resellerId;
      } else{
        throw new NotLoggedOnException();
      }
    }
  }


  public  int getUserResellerId(User user) throws RuntimeException, SQLException{
	try {
		GenericGroup gGroup = ((GenericGroupHome) IDOLookup.getHome(GenericGroup.class)).createLegacy();
		List gr = gGroup.getAllGroupsContainingUser(user);
		if(gr != null){
		  Iterator iter = gr.iterator();
		  while (iter.hasNext()) {
				GenericGroup item = (GenericGroup)iter.next();
				if(item.getGroupType().equals(((ResellerStaffGroup) ResellerStaffGroupBMPBean.getStaticInstance(ResellerStaffGroup.class)).getGroupTypeValue())){
				  IDOLegacyEntity[] reseller = ((Reseller) ResellerBMPBean.getStaticInstance(Reseller.class)).findAllByColumn(ResellerBMPBean.getColumnNameGroupID(),item.getID());
				  if(reseller != null && reseller.length > 0){
					return reseller[0].getID();
				  }
				}
		  }
		}
		throw new RuntimeException("Does not belong to any supplier");
	} catch (IDOLookupException e) {
		throw new RuntimeException("Does not belong to any supplier");
	}
	
	/*com.idega.core.data.GenericGroup gGroup = ((com.idega.core.data.GenericGroupHome)com.idega.data.IDOLookup.getHomeLegacy(GenericGroup.class)).createLegacy();
    List gr = gGroup.getAllGroupsContainingUser(user);
    if(gr != null){
      Iterator iter = gr.iterator();
      while (iter.hasNext()) {
        GenericGroup item = (GenericGroup)iter.next();
        if(item.getGroupType().equals(((ResellerStaffGroup)com.idega.block.trade.stockroom.data.ResellerStaffGroupBMPBean.getStaticInstance(ResellerStaffGroup.class)).getGroupTypeValue())){
          IDOLegacyEntity[] reseller = ((Reseller)com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class)).findAllByColumn(com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameGroupID(),item.getID());
          if(reseller != null && reseller.length > 0){
            return reseller[0].getID();
          }
        }
      }
    }
    throw new RuntimeException("Does not belong to any reseller");
    */
  }

  public  int updateProduct(int productId, int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return getProductBusiness().createProduct(productId,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }

  public  int createProduct(int supplierId, Integer fileId, String productName, String number, String productDescription, boolean isValid, int[] addressIds, int discountTypeId) throws Exception{
    return getProductBusiness().createProduct(-1,supplierId, fileId, productName, number, productDescription, isValid, addressIds, discountTypeId);
  }

  private ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }

  public DropdownMenu getCurrencyDropdownMenu(String menuName) {
    DropdownMenu menu = new DropdownMenu(menuName);
    List currencyList = CurrencyBusiness.getCurrencyList();
    Iterator iter = currencyList.iterator();
    while (iter.hasNext()) {
      CurrencyHolder holder = (CurrencyHolder) iter.next();
      menu.addMenuElement(holder.getCurrencyID(), holder.getCurrencyName());
    }

    return menu;
  }

	public boolean isInTimeframe(IWTimestamp from, IWTimestamp to, IWTimestamp stampToCheck, boolean yearly) {
		return isBetween(from, to, stampToCheck, yearly, true);
	}

	public boolean isBetween(IWTimestamp from, IWTimestamp to, IWTimestamp stampToCheck, boolean yearly, boolean bordersCount) {
		from.setAsDate();
		to.setAsDate();
		if (yearly) {
			IWTimestamp temp = new IWTimestamp(stampToCheck);
			temp.setAsDate();
			if (from.getYear() == to.getYear()) {
				temp.setYear(from.getYear());
				if (bordersCount) {
					return (temp.isLaterThanOrEquals(from) && to.isLaterThanOrEquals(temp));
				}
				else {
					return (temp.isLaterThan(from) && to.isLaterThan(temp));
				}
			}
			else {
				if (temp.getYear() >= to.getYear()) {
					if (temp.getMonth() > to.getMonth()) {
						temp.setYear(from.getYear());
					}
					else {
						temp.setYear(to.getYear());
					}
				}
				return isBetween(from, to, temp, false, bordersCount);
			}
		}
		else {
			if (bordersCount) {
				return (stampToCheck.isLaterThanOrEquals(from) && to.isLaterThanOrEquals(stampToCheck));
			}
			else {
				return (stampToCheck.isLaterThan(from) && to.isLaterThan(stampToCheck));
			}
		}
	}

}
