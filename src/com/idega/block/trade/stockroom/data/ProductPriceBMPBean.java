package com.idega.block.trade.stockroom.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.block.trade.data.Currency;
import com.idega.block.trade.data.CurrencyHome;
import com.idega.core.location.data.Address;
import com.idega.data.EntityControl;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.SimpleQuerier;
import com.idega.util.text.TextSoap;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class ProductPriceBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.ProductPrice {

  public static final int PRICETYPE_PRICE = 0;
  public static final int PRICETYPE_DISCOUNT = 1;

  public ProductPriceBMPBean(){
          super();
  }
  public ProductPriceBMPBean(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameProductId(), "Vara" ,true, true, Integer.class, "many_to_one", Product.class);
    addAttribute(getColumnNamePriceCategoryId(), "Verðflokkur" ,true, true, Integer.class, "many_to_one", PriceCategory.class);
    addAttribute(getColumnNameCurrencyId(),"Gjaldmiðill",true,true,Integer.class,"many_to_one", Currency.class);
    addAttribute(getColumnNamePrice(), "Verð", true, true, Float.class);
    addAttribute(getColumnNamePriceDate(), "Dagsetning verðs", true, true, Timestamp.class);
    addAttribute(getColumnNamePriceType(),"Gerð",true,true,Integer.class);
    addAttribute(getColumnNameIsValid(), "virkt", true, true, Boolean.class);
    /** added 22.04.2002 by gimmi */
    addAttribute(getColumnNameMaxUsage(), "hámarks fjoldi", true, true, Integer.class);

    this.addManyToManyRelationShip(Timeframe.class,getProductPriceTableName()+"_TIMEFRAME");
    this.addManyToManyRelationShip(Address.class,getProductPriceTableName()+"_ADDRESS");
    this.addManyToManyRelationShip(TravelAddress.class);
    
    addIndex("IDX_PRO_PRI_1", new String[]{getColumnNameProductId(), getColumnNamePriceCategoryId(), getColumnNameCurrencyId(), getColumnNameIsValid()});
    addIndex("IDX_PRO_PRI_2", new String[]{getColumnNameProductId(), getColumnNamePriceCategoryId(), getColumnNameIsValid()});
    addIndex("IDX_PRO_PRI_3", new String[]{getColumnNameProductId(), getColumnNameCurrencyId(), getColumnNameIsValid()});
    addIndex("IDX_PRO_PRI_4", getColumnNameProductId());
  }


  public void delete() {
    this.invalidate();
  }

  public void invalidate() {
    this.setIsValid(false);
  }

  public void validate() {
    this.setIsValid(true);
  }

  public String getEntityName(){
    return getProductPriceTableName();
  }

  public void setDefaultValues() {
    this.setIsValid(true);
    this.setMaxUsage(-1);
  }

  public int getProductId() {
    return getIntColumnValue(getColumnNameProductId());
  }

  public void setProductId(int id){
    setColumn(getColumnNameProductId(),id);
  }


  public PriceCategory getPriceCategory() {
    return (PriceCategory) getColumnValue(getColumnNamePriceCategoryId());
  }

  public int getPriceCategoryID() {
    return getIntColumnValue(getColumnNamePriceCategoryId());
  }

  public Integer getPriceCategoryIDInteger() {
    return getIntegerColumnValue(getColumnNamePriceCategoryId());
  }

  public void setPriceCategoryID(int id) {
    setColumn(getColumnNamePriceCategoryId(), id);
  }

  public int getCurrencyId(){
    int currId = getIntColumnValue(getColumnNameCurrencyId());
//    if (currId == 1) {
      try {
        CurrencyHome cHome = (CurrencyHome) IDOLookup.getHome(Currency.class);
        Currency currency = cHome.findByPrimaryKey(currId);
        CurrencyHolder holder = CurrencyBusiness.getCurrencyHolder(currency.getCurrencyName());
        if (holder != null) {
        	if (currId != holder.getCurrencyID()) {
	          System.out.println("[ProductPriceBMPBean] Backwards compatability : changing currencyId from 1 to "+holder.getCurrencyID());
	          this.setCurrencyId(holder.getCurrencyID());
	          this.store();
        	}
        }else {
          System.out.println("[ProductPriceBMPBean] Cannot execute Backwards compatability : currencyHolder == null");
        }
      }catch (Exception e) {
        e.printStackTrace(System.err);
      }
//    }
    return currId;
  }

  public void setCurrencyId(int id){
    setColumn(getColumnNameCurrencyId(), id);
  }

  public void setCurrencyId(Integer id){
    setColumn(getColumnNameCurrencyId(), id);
  }

  public float getPrice() {
    return getFloatColumnValue(getColumnNamePrice());
  }

  public int getDiscount() {
    int returner = 0;
    if (this.getPriceType() == PRICETYPE_DISCOUNT) {
      returner = (int) getFloatColumnValue(getColumnNamePrice());
    }
    return returner;
  }

  public void setPrice(float price) {
    setColumn(getColumnNamePrice(), price);
  }

  public Timestamp getPriceDate() {
    return (Timestamp) getColumnValue(getColumnNamePriceDate());
  }

  public void setPriceDate(Timestamp timestamp) {
    setColumn(getColumnNamePriceDate(), timestamp);
  }

  public int getPriceType(){
    return getIntColumnValue(getColumnNamePriceType());
  }

  public void setPriceType(int type){
    setColumn(getColumnNamePriceType(), type);
  }

  public void setIsValid(boolean isValid) {
    setColumn(getColumnNameIsValid(), isValid);
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getColumnNameIsValid());
  }

  public static void clearPrices(int productId, int currencyId) throws SQLException {
  	clearPrices(productId, currencyId, null);
  }
  
  public static void clearPrices(int productId, int currencyId, String key) throws SQLException{
		ProductPrice[] prices = getProductPrices(productId, -1, -1, -1, currencyId, PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, key);
		for (int i = 0; i < prices.length; i++) {
	  	prices[i].invalidate();
	  	prices[i].update();
		}
  }

  public static ProductPrice[] getProductPrices(int productId, boolean netBookingOnly) {
    return getProductPrices(productId, -1, netBookingOnly);
  }

  public static ProductPrice[] getProductPrices(int productId, int timeframeId, boolean netBookingOnly) {
    return getProductPrices(productId, timeframeId, -1, netBookingOnly);
  }

  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, String key) {
		return getProductPrices(productId, timeframeId, addressId, netBookingOnly, 0, -1, key);
  }

  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly) {
    return getProductPrices(productId, timeframeId, addressId, netBookingOnly, 0, -1);
  }

  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, int[] visibility) {
	return getProductPrices(productId, timeframeId, addressId, 0, -1, visibility, null);
  }

  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, int[] visibility, String key) {
    return getProductPrices(productId, timeframeId, addressId, 0, -1, visibility, key);
  }
  
  public static ProductPrice[] getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly) {
    return getMiscellaneousPrices(productId, timeframeId, addressId, netBookingOnly, -1);
  }

  public static ProductPrice[] getMiscellaneousPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, int currencyId) {
    return getProductPrices(productId, timeframeId, addressId, netBookingOnly, 1, currencyId);
  }

  /**
   * @param productId Product id
   * @param timeframeId Timeframe id
   * @param addressId TravelAddress id
   * @param netBookingOnly View netBookings only
   * @param countAsPersonStatus 0 = selects when COUNT_AS_PERSON = 'Y' or NULL, 1 = selects where COUNT_AS_PERSON = 'N', -1 both 0 and 1
   * @param currencyId Currency id
   * @return ProductPrice array
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, int countAsPersonStatus, int currencyId) {
		return getProductPrices(productId, timeframeId, addressId,  netBookingOnly, countAsPersonStatus, currencyId, null);
  }

  /**
   * @param productId Product id
   * @param timeframeId Timeframe id
   * @param addressId TravelAddress id
   * @param netBookingOnly View netBookings only
   * @param countAsPersonStatus 0 = selects when COUNT_AS_PERSON = 'Y' or NULL, 1 = selects where COUNT_AS_PERSON = 'N', -1 both 0 and 1
   * @param currencyId Currency id
   * @return ProductPrice array
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, boolean netBookingOnly, int countAsPersonStatus, int currencyId, String key) {
  	int[] visibility = new int[]{};
  	if (netBookingOnly) {
  		visibility = new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_PUBLIC};	
  	}else {
  		visibility = new int[] {PriceCategoryBMPBean.PRICE_VISIBILITY_BOTH_PRIVATE_AND_PUBLIC, PriceCategoryBMPBean.PRICE_VISIBILITY_PRIVATE};	
  	}
		return getProductPrices(productId, timeframeId, addressId,  countAsPersonStatus, currencyId, visibility, key);	
  }

  /**
   * @param productId Product id
   * @param timeframeId Timeframe id
   * @param addressId TravelAddress id
   * @param netBookingOnly View netBookings only
   * @param countAsPersonStatus 0 = selects when COUNT_AS_PERSON = 'Y' or NULL, 1 = selects where COUNT_AS_PERSON = 'N', -1 both 0 and 1
   * @param currencyId Currency id
   * @param visibility 1 = Private Prices, 2 = Public Prices, 3 Both Types
   * @param key used for special prices, default is null
   * @return ProductPrice array
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus, int currencyId, int visibility, String key) {
		return getProductPrices(productId, timeframeId, addressId, countAsPersonStatus, currencyId, new int[]{visibility}, key);
  }

  /**
   * @param productId Product id
   * @param timeframeId Timeframe id
   * @param addressId TravelAddress id
   * @param netBookingOnly View netBookings only
   * @param countAsPersonStatus 0 = selects when COUNT_AS_PERSON = 'Y' or NULL, 1 = selects where COUNT_AS_PERSON = 'N', -1 both 0 and 1
   * @param currencyId Currency id
   * @param visibility 1 = Private Prices, 2 = Public Prices, 3 Both Types
   * @return ProductPrice array
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus, int currencyId, int visibility) {
		return getProductPrices(productId, timeframeId, addressId, countAsPersonStatus, currencyId, new int[]{visibility}, null);
  }

  /**
   * @param productId Product id
   * @param timeframeId Timeframe id
   * @param addressId TravelAddress id
   * @param netBookingOnly View netBookings only
   * @param countAsPersonStatus 0 = selects when COUNT_AS_PERSON = 'Y' or NULL, 1 = selects where COUNT_AS_PERSON = 'N', -1 both 0 and 1
   * @param currencyId Currency id
   * @param visibility[] 1 = Private Prices, 2 = Public Prices, 3 Both Types
   * @return ProductPrice array
   */
  public static ProductPrice[] getProductPrices(int productId, int timeframeId, int addressId, int countAsPersonStatus, int currencyId, int[] visibility, String key) {
      ProductPrice[] prices = {};
      try {
        String sql = getSQLQuery(productId, timeframeId, addressId,  countAsPersonStatus, currencyId, visibility, key);
        //if (key != null) {
        	//System.out.println("[ProductPriceBMPBean]\n"+sql);
        //}
        prices = (ProductPrice[]) (com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class)).findAll(sql);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
      return prices;
  }

  public static int[] getCurrenciesInUse(int productId) {
		return getCurrenciesInUse(productId, -1);
  }
  public static int[] getCurrenciesInUse(int productId, int visibility) {
		return getCurrenciesInUse(productId, new int[]{visibility});
  }
  
  public static int[] getCurrenciesInUse(int productId, int[] visibility) {
    String sql = getSQLQuery(productId, -1, -1, -1, -1, visibility);
    sql = TextSoap.findAndReplace(sql, getProductPriceTableName()+".*", "distinct "+getColumnNameCurrencyId());
    try {
      String[] sIds = SimpleQuerier.executeStringQuery(sql);
      if (sIds != null && sIds.length > 0) {
        int[] ids = new int[sIds.length];
        for (int i = 0; i < sIds.length; i++) {
          ids[i] = Integer.parseInt(sIds[i]);
        }
        return ids;
      }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return new int[]{};
  }

  private static String getSQLQuery(int productId, int timeframeId, int addressId, int countAsPersonStatus, int currencyId, int[] visibility) {
		return getSQLQuery(productId, timeframeId, addressId, countAsPersonStatus, currencyId, visibility, null);
  }
  
  private static String getSQLQuery(int productId, int timeframeId, int addressId, int countAsPersonStatus, int currencyId, int[] visibility, String categoryKey) {
    ProductPrice price = (ProductPrice) com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getStaticInstance(ProductPrice.class);
    PriceCategory category = (PriceCategory) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class);
    Timeframe timeframe = (Timeframe) com.idega.block.trade.stockroom.data.TimeframeBMPBean.getStaticInstance(Timeframe.class);
    TravelAddress tAddress = (TravelAddress) com.idega.block.trade.stockroom.data.TravelAddressBMPBean.getStaticInstance(TravelAddress.class);

    String ptmTable = EntityControl.getManyToManyRelationShipTableName(ProductPrice.class, Timeframe.class);
    String pamTable = EntityControl.getManyToManyRelationShipTableName(ProductPrice.class, TravelAddress.class);
    String pTable = getProductPriceTableName();
    String cTable = category.getEntityName();

    StringBuffer SQLQuery = new StringBuffer();
    SQLQuery.append("SELECT "+pTable+".* FROM "+pTable+", "+cTable);
    if (timeframeId != -1) {
      SQLQuery.append(" , "+ptmTable);
    }
    if (addressId != -1) {
      SQLQuery.append(" , "+pamTable);
    }
    SQLQuery.append(" WHERE ");
    if (timeframeId != -1) {
      SQLQuery.append(ptmTable+"."+timeframe.getIDColumnName()+" = "+timeframeId);
      SQLQuery.append(" AND ");
      SQLQuery.append(ptmTable+"."+price.getIDColumnName()+" = "+pTable+"."+price.getIDColumnName());
      SQLQuery.append(" AND ");
    }
    if (currencyId > 0) {
      SQLQuery.append(getColumnNameCurrencyId())
          .append(" = ")
          .append(currencyId)
          .append(" AND ");
    }
    if (countAsPersonStatus == 0) {
      SQLQuery.append("(");
      SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameCountAsPerson()+" = 'Y'");
      SQLQuery.append(" OR ");
      SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameCountAsPerson()+" is null");
      SQLQuery.append(")");
      SQLQuery.append(" AND ");
    }else if (countAsPersonStatus == 1) {
      SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameCountAsPerson()+" = 'N'");
      SQLQuery.append(" AND ");
    }
    if (addressId != -1) {
      SQLQuery.append(pamTable+"."+tAddress.getIDColumnName()+" = "+addressId);
      SQLQuery.append(" AND ");
      SQLQuery.append(pamTable+"."+price.getIDColumnName()+" = "+pTable+"."+price.getIDColumnName());
      SQLQuery.append(" AND ");
    }
    SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceCategoryId() + " = "+cTable+"."+category.getIDColumnName());
    SQLQuery.append(" AND ");
    SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameProductId() +" = " + productId);
    SQLQuery.append(" AND ");
    SQLQuery.append(pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNameIsValid() +"='Y'");
    SQLQuery.append(" AND ");
    SQLQuery.append(cTable+"."+com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameIsValid() +"='Y'");
    if (visibility != null && visibility.length > 0 && !(visibility.length == 1 && visibility[0] == 3) ) {
      SQLQuery.append(" AND (");
      SQLQuery.append(cTable+"."+com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameNetbookingCategory()+" = 'Y'");
      for (int i = 0 ;  i < visibility.length ; i++ ) {
		    	SQLQuery.append(" OR ");
		    	SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameVisibility()+"="+visibility[i]);
      }
    	SQLQuery.append(")");
    }
		SQLQuery.append(" AND ").append(cTable).append(".").append(PriceCategoryBMPBean.getColumnNameKey());
    if (categoryKey == null || categoryKey.equals("")) {
    	SQLQuery.append(" is null");
    } else {
			SQLQuery.append(" = '").append(categoryKey).append("'");
    }
//    if (visibility > 0 && visibility != 3) { 
//			/** visibility == 3, applies applies to both 1 and 2 */
//    }/*else if (visibility == 3) {
//    	SQLQuery.append(" OR ");
//    	SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameVisibility()+"=1");
//    	SQLQuery.append(" OR ");
//    	SQLQuery.append(cTable+"."+PriceCategoryBMPBean.getColumnNameVisibility()+"=2");
//  	}*/

    
    SQLQuery.append(" ORDER BY "+pTable+"."+com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getColumnNamePriceType()+","+cTable+"."+com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameName());
/*
		try {
			throw new Exception("Reppis");
		}catch (Exception e) {
			e.printStackTrace(System.out);	
		}
*/

		System.out.println("[ProductPriceBMPBean] sql : "+SQLQuery.toString());
    return SQLQuery.toString();
  }

  public int getMaxUsage() {
    return getIntColumnValue(getColumnNameMaxUsage());
  }

  public void setMaxUsage(int maxUsage) {
    setColumn(getColumnNameMaxUsage(), maxUsage);
  }

  public Collection getTravelAddresses() throws IDORelationshipException{
    return this.idoGetRelatedEntities(TravelAddress.class);
  }
  public Collection getTimeframes() throws IDORelationshipException {
    return this.idoGetRelatedEntities(Timeframe.class);
  }

  public static String getProductPriceTableName(){return "SR_PRODUCT_PRICE";}
  public static String getColumnNameProductId(){return "SR_PRODUCT_ID";}
  public static String getColumnNamePriceCategoryId() {return "SR_PRICE_CATEGORY_ID";}
  public static String getColumnNamePrice() {return "PRICE";}
  public static String getColumnNamePriceDate() {return "PRICE_DATE"; }
  public static String getColumnNameCurrencyId() {return "TR_CURRENCY_ID"; }
  public static String getColumnNamePriceType() {return "PRICE_TYPE"; }
  public static String getColumnNameIsValid() {return "IS_VALID";}
  public static String getColumnNameMaxUsage() {return "MAX_USAGE";}



}
