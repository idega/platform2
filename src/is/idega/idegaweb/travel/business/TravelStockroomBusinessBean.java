package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.ResellerDay;
import is.idega.idegaweb.travel.data.ResellerDayHome;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayBMPBean;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.data.ServiceDayPK;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.block.trade.business.CurrencyHolder;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.business.ProductBusinessBean;
import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.business.StockroomBusinessBean;
import com.idega.block.trade.stockroom.data.PriceCategory;
import com.idega.block.trade.stockroom.data.PriceCategoryBMPBean;
import com.idega.block.trade.stockroom.data.PriceCategoryHome;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplies;
import com.idega.block.trade.stockroom.data.SupplyPool;
import com.idega.block.trade.stockroom.data.SupplyPoolDay;
import com.idega.block.trade.stockroom.data.SupplyPoolDayHome;
import com.idega.block.trade.stockroom.data.SupplyPoolDayPK;
import com.idega.block.trade.stockroom.data.SupplyPoolHome;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TimeframeHome;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.data.TravelAddressBMPBean;
import com.idega.block.trade.stockroom.data.TravelAddressHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOLookupException;
import com.idega.business.IBORuntimeException;
import com.idega.core.location.data.Address;
import com.idega.data.EntityFinder;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOException;
import com.idega.data.IDOFinderException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDORelationshipException;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.presentation.IWContext;
import com.idega.util.IWTimestamp;
import com.idega.util.datastructures.HashtableDoubleKeyed;

/**
 * Title:        IW Travel
 * Description:  Stockroom Business
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TravelStockroomBusinessBean extends StockroomBusinessBean implements TravelStockroomBusiness, ActionListener {

  private String resellerDayHashtableSessionName = "resellerDayHashtable";
  private String resellerDayOfWeekHashtableSessionName = "resellerDayOfWeekHashtable";
  private String serviceDayHashtableSessionName = "serviceDayHashtable";

  protected Timeframe timeframe;
	private HashMap maxDaysMap = new HashMap();

  public TravelStockroomBusinessBean() {
  }

  public TravelStockroomBusinessBean getNewInstance(IWApplicationContext iwac) throws RemoteException{
    return (TravelStockroomBusinessBean) IBOLookup.getServiceInstance(iwac, TravelStockroomBusiness.class);
  }

  public void addSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }

  public void depleteSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method depleteSupplies() not yet implemented.");
  }

  public void setSupplyStatus(int productId, float status, int period) throws SQLException {
    Supplies supplies = ((com.idega.block.trade.stockroom.data.SuppliesHome)com.idega.data.IDOLookup.getHomeLegacy(Supplies.class)).createLegacy();

    supplies.setProductId(productId);
    supplies.setCurrentSupplies(status);
    supplies.setRecordTime(IWTimestamp.RightNow().getTimestamp());

    if(period > -1){
      supplies.setPeriod(period);
    }

    supplies.insert();
  }

  public float getSupplyStatus(int productId) throws SQLException {
    Supplies supplies = (Supplies)com.idega.block.trade.stockroom.data.SuppliesBMPBean.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAllByColumnOrdered(supplies,com.idega.block.trade.stockroom.data.SuppliesBMPBean.getColumnNameProductId(),Integer.toString(productId),com.idega.block.trade.stockroom.data.SuppliesBMPBean.getColumnNameRecordTime());
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public float getSupplyStatus(int productId, Date date) throws SQLException {
    Supplies supplies = (Supplies)com.idega.block.trade.stockroom.data.SuppliesBMPBean.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAll(supplies,"SELECT * FROM  "+supplies.getEntityName()+" WHERE "+com.idega.block.trade.stockroom.data.SuppliesBMPBean.getColumnNameProductId()+" = "+Integer.toString(productId)+" AND "+com.idega.block.trade.stockroom.data.SuppliesBMPBean.getColumnNameRecordTime()+" <= '"+date.toString()+"' ORDER BY "+ com.idega.block.trade.stockroom.data.SuppliesBMPBean.getColumnNameRecordTime(),1);
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo, int visibility) throws Exception {
      return this.createPriceCategory(supplierId, name, description,type,extraInfo, visibility, -1);
  }

  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo, int visibility, int parentId) throws Exception {
		int catId = this.createPriceCategory(supplierId, name, description, extraInfo);

    PriceCategory cat = ((com.idega.block.trade.stockroom.data.PriceCategoryHome)com.idega.data.IDOLookup.getHomeLegacy(PriceCategory.class)).findByPrimaryKey(catId);

    if(parentId != -1) {
      cat.setParentId(parentId);
    }

    if(type != null){
      cat.setType(type);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

		cat.setVisibility(visibility);

//		/** backwards compatability */
//    cat.isNetbookingCategory(false);
    cat.setSupplierId(supplierId);
    cat.setCountAsPerson(true);

    cat.update();


    return catId;
  }


  public int createService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception{
    return createService(-1,supplierId, fileId, serviceName, number, serviceDescription, isValid, addressIds, departure,arrival, discountTypeId);
  }

  public int updateService(int serviceId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception {
    return createService(serviceId,supplierId, fileId, serviceName, number, serviceDescription, isValid, addressIds, departure,arrival, discountTypeId);
  }

  private int createService(int serviceId,int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception{
//    TransactionManager transaction = IdegaTransactionManager.getInstance();
    try{
      //transaction.begin();

      int id = -1;
      if (serviceId == -1) {
        id = createProduct(supplierId,fileId,serviceName,number,serviceDescription,isValid, addressIds, discountTypeId);
      }else {
        id = updateProduct(serviceId, supplierId,fileId,serviceName,number,serviceDescription,isValid, addressIds, discountTypeId);
      }

      Service service = null;
      try {
        service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(id));
      } catch (FinderException fe) {
        service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).create();
        service.setPrimaryKey(new Integer(id));
//        service.setID(id);
      }

        service.setDepartureTime(departure);
        service.setAttivalTime(arrival);

      service.store();
/*      if (serviceId == -1) {
        service.insert();
      }else {
        service.update();
      }*/


        if (timeframe != null) {
          try {
            Product product = getProductBusiness().getProduct(id);
            product.addTimeframe(timeframe);
//            product.addTo(timeframe);
          }catch (IDOException sql) {
            //sql.printStackTrace(System.err);
          }
        }else {
          System.err.println("Timeframe is null");
        }
        //transaction.commit();

      return id;
    }catch(SQLException e){
      //transaction.rollback();
      e.printStackTrace(System.err);
      throw new RuntimeException("IWE226TB89");
    }
  }


  public void setTimeframe(IWTimestamp from, IWTimestamp to, boolean yearly) throws SQLException {
    setTimeframe(-1,from,to,yearly);
  }

  public void setTimeframe(int timeframeId, IWTimestamp from, IWTimestamp to, boolean yearly) throws SQLException {
    if (timeframeId != -1) {
      if ((from != null) && (to != null)) {
        timeframe = ((com.idega.block.trade.stockroom.data.TimeframeHome)com.idega.data.IDOLookup.getHomeLegacy(Timeframe.class)).findByPrimaryKeyLegacy(timeframeId);
          timeframe.setTo(to.getTimestamp());
          timeframe.setFrom(from.getTimestamp());
          timeframe.setYearly(yearly);
          timeframe.update();
      }
    }else {
      if ((from != null) && (to != null)) {
        timeframe = ((com.idega.block.trade.stockroom.data.TimeframeHome)com.idega.data.IDOLookup.getHomeLegacy(Timeframe.class)).createLegacy();
          timeframe.setTo(to.getTimestamp());
          timeframe.setFrom(from.getTimestamp());
          timeframe.setYearly(yearly);
          timeframe.insert();
      }
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(Reseller reseller) {
    Product[] returner = {};
    try {
      Reseller parent = (Reseller) reseller.getParentEntity();
      if (parent != null) {

      }
    }catch (Exception sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId) throws RemoteException{
    List list = getProductBusiness().getProducts(supplierId);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId, IWTimestamp stamp) throws RemoteException, FinderException{
    List list = getProductBusiness().getProducts(supplierId, stamp);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }

  /**
   * @deprecated
   */
  public Product[] getProducts(int supplierId, IWTimestamp from, IWTimestamp to) throws FinderException, RemoteException{
    List list = getProductBusiness().getProducts(supplierId, from, to);
    if (list == null) {
      return new Product[]{};
    }else {
      return (Product[]) list.toArray(new Product[]{});
    }
  }


  public Service getService(Product product) throws ServiceNotFoundException, RemoteException {
    Service service = null;
    try {
      service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(product.getPrimaryKey());
    }
    catch (FinderException sql) {
      throw new ServiceNotFoundException(sql);
    }
    return service;
  }

  public Timeframe getTimeframe(Product product) throws RemoteException, ServiceNotFoundException, TimeframeNotFoundException {
    Timeframe timeFrame = null;
    try {
//      Service service = TravelStockroomBusiness.getService(product);
      timeFrame = product.getTimeframe();
    }
    catch (SQLException sql) {
      throw new TimeframeNotFoundException();
    }/*
    catch (ServiceNotFoundException snf) {
      throw new ServiceNotFoundException();
    }*/
    return timeFrame;
  }


  public PriceCategory[] getPriceCategories(int supplierId) {
    PriceCategory[] returner = {};
    try {
      StringBuffer sql = new StringBuffer();
        sql.append("Select * from ")
           .append(PriceCategoryBMPBean.getPriceCategoryTableName())
           .append(" where ")
           .append(PriceCategoryBMPBean.getColumnNameSupplierId())
           .append(" = ")
           .append(Integer.toString(supplierId))
           .append(" and ")
           .append(PriceCategoryBMPBean.getColumnNameIsValid())
           .append(" = 'Y' and (")
           .append(PriceCategoryBMPBean.getColumnNameCountAsPerson())
           .append(" = 'Y' or ")
           .append(PriceCategoryBMPBean.getColumnNameCountAsPerson())
           .append(" is null )");

      returner = (PriceCategory[]) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class).findAll(sql.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return returner;
  }

	public PriceCategory[] getPriceCategories(String key) {
		PriceCategory[] returner = {};
		try {
		  StringBuffer sql = new StringBuffer();
			sql.append("Select * from ")
			   .append(PriceCategoryBMPBean.getPriceCategoryTableName())
			   .append(" where ")
			   .append(PriceCategoryBMPBean.getColumnNameKey())
			   .append(" = '")
			   .append(key)
			   .append("' and ")
			   .append(PriceCategoryBMPBean.getColumnNameIsValid())
			   .append(" = 'Y'");

		  returner = (PriceCategory[]) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class).findAll(sql.toString());

		}catch (SQLException sql) {
		  sql.printStackTrace(System.err);
		}
		return returner;
		
	}

  public PriceCategory[] getMiscellaneousServices(int supplierId) {
    PriceCategory[] returner = {};
    try {
    		PriceCategoryHome pcHome = (PriceCategoryHome) IDOLookup.getHome(PriceCategory.class);
    		Collection coll = pcHome.findBySupplierAndCountAsPerson(supplierId, false);
    		if (coll != null && !coll.isEmpty()) {
    			returner = (PriceCategory[]) coll.toArray(new PriceCategory[]{});
    		}
      //returner = (PriceCategory[]) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class).findAllByColumn(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameSupplierId(),Integer.toString(supplierId), com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameIsValid(), "Y",com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameCountAsPerson(), "N" );
//      returner = (PriceCategory[]) com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getStaticInstance(PriceCategory.class).findAllByColumn(com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameSupplierId(),Integer.toString(supplierId), com.idega.block.trade.stockroom.data.PriceCategoryBMPBean.getColumnNameIsValid(), "Y");
    }catch (FinderException sql) {
      sql.printStackTrace(System.err);
    }
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
    return returner;
  }

  protected HashtableDoubleKeyed getServiceDayHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(serviceDayHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(serviceDayHashtableSessionName, hash);
      }

      return hash;
  }

  public boolean getIfDay(IWContext iwc,int productId, int dayOfWeek) throws RemoteException{
      boolean returner = false;

      HashtableDoubleKeyed hash = getServiceDayHashtable(iwc);
      Object obj = hash.get(productId+"_"+dayOfWeek,"");
      if (obj == null) {
      	SupplyPoolHome spHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
      	try {
					SupplyPool pool = spHome.findByProduct(new Integer(productId));
					SupplyPoolDayHome spdHome = (SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class);
					try {
						spdHome.findByPrimaryKey(new SupplyPoolDayPK(pool.getPrimaryKey(), new Integer(dayOfWeek)));
						hash.put(productId+"_"+dayOfWeek, "", new Boolean(true));
						return true;
					} catch (FinderException f) {
						hash.put(productId+"_"+dayOfWeek, "", new Boolean(false));
						return false;
					}
				}
				catch (IDORelationshipException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
//					e.printStackTrace();
				}
      	
        returner = getServiceDayHome().getIfDay(productId, dayOfWeek);
        hash.put(productId+"_"+dayOfWeek,"",new Boolean(returner));
      }else {
        returner = ((Boolean)obj).booleanValue();
      }

      return returner;
  }

  public boolean getIfDay(IWContext iwc, Product product, IWTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException, SQLException, RemoteException {
    return getIfDay(iwc, product, product.getTimeframes(), stamp, true, true);
  }

  public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException, RemoteException {
    return getIfDay(iwc, product, timeframes, stamp, true, false);
  }

  public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp, boolean includePast, boolean fixTimeframe) throws ServiceNotFoundException, TimeframeNotFoundException, RemoteException {
      boolean isDay = false;
      String key1 = Integer.toString(product.getID());
      String key2 = stamp.toSQLDateString();

      HashtableDoubleKeyed serviceDayHash = getServiceDayHashtable(iwc);
      Object obj = serviceDayHash.get(key1, key2);
      if (obj == null) {

          int dayOfWeek = stamp.getDayOfWeek();

          boolean tooEarly = false;
          if (!includePast) {
            IWTimestamp now = IWTimestamp.RightNow();
            IWTimestamp tNow = new IWTimestamp(now.getDay(), now.getMonth(), now.getYear());
            if (tNow.isLaterThan(stamp)) {
              tooEarly = true;
            }
          }

          if (!tooEarly) {
            boolean isValidWeekDay = getIfDay(iwc, product.getID(), dayOfWeek);
            if (isValidWeekDay) {
              if (timeframes == null || timeframes.length == 0) {
                serviceDayHash.put(key1, key2, new Boolean(true) );
                isDay = true;
              }else {
                if (isDayValid(timeframes, stamp, fixTimeframe)) {
                  isDay = true;
                  serviceDayHash.put(key1, key2, new Boolean(true) );
                }
                else {
                  serviceDayHash.put(key1, key2, new Boolean(false) );
                }
              }
            }else {
              serviceDayHash.put(key1, key2, new Boolean(false) );
            }
          }
      }
      else {
        isDay = ((Boolean) obj).booleanValue();
      }
      return isDay;
  }

  private boolean isDateValid(Contract contract, IWTimestamp stamp) throws RemoteException {
    IWTimestamp theStamp= IWTimestamp.RightNow();
      theStamp.addDays(contract.getExpireDays()-1);

    return getStockroomBusiness().isInTimeframe(new IWTimestamp(contract.getFrom()), new IWTimestamp(contract.getTo()), stamp, false);
    /*
    if (stamp.isLaterThan(theStamp)) {
      return new IWTimestamp(contract.getTo()).isLaterThan(stamp);
    }else {
      return false;
    }
    */

  }

  private boolean isDayValid(Timeframe[] frames, IWTimestamp stamp, boolean fixTimeframe) {
    return isDayValid(frames, null, stamp, fixTimeframe);
  }

  private boolean isDayValid(Timeframe[] frames, Contract contract, IWTimestamp stamp) {
    return isDayValid(frames, contract, stamp, false);
  }

  private boolean isDayValid(Timeframe[] frames, Contract contract, IWTimestamp stamp, boolean fixTimeframe) {

    boolean returner = false;

    try {
      boolean goOn = false;
      if (contract == null) {
        goOn = true;
      }else {
        goOn = isDateValid(contract, stamp);
      }


      if (goOn) {
        boolean isYearly = false;

        for (int i = 0; i < frames.length; i++) {
          if (fixTimeframe) {
//            System.err.println("---------------------------------------------------------------------------------------------------------------");
//            System.err.println("isDayValid.... : from : "+new IWTimestamp(frames[i].getFrom()).toSQLDateString());
//            System.err.println(".............. : to   : "+new IWTimestamp(frames[i].getTo()).toSQLDateString());
            fixTimeframe(frames[i], stamp);
//            System.err.println(":::::::FIXING:::::: "+stamp.toSQLDateString());
//            System.err.println("isDayValid.... : from : "+new IWTimestamp(frames[i].getFrom()).toSQLDateString());
//            System.err.println(".............. : to   : "+new IWTimestamp(frames[i].getTo()).toSQLDateString());
          }
//          System.err.println(".............. : year : "+frames[i].getYearly());
          isYearly = frames[i].getIfYearly();
          returner = getStockroomBusiness().isInTimeframe(new IWTimestamp(frames[i].getFrom()), new IWTimestamp(frames[i].getTo() ), stamp, isYearly);
          if (returner) break;
        }


      }
    }catch (Exception e) {
        e.printStackTrace(System.err);
    }

    return returner;
  }

  public Timeframe fixTimeframe(Timeframe frame, IWTimestamp stamp) {
    return fixTimeframe(frame, stamp, null);
  }

  public Timeframe fixTimeframe(Timeframe frame, IWTimestamp from, IWTimestamp to) {
    IWTimestamp tFrom = new IWTimestamp(frame.getFrom());
    IWTimestamp tTo = new IWTimestamp(frame.getTo());

    if (frame.getYearly()) {
      int fromYear = tFrom.getYear();
      int fromY = from.getYear();
      int fromMonth = tFrom.getMonth();
      int fromM = from.getMonth();

      int toYear   = tTo.getYear();
      int toMonth = tTo.getMonth();
      int yearsBetween = 0;

      int toY = 0;
      int toM = 0;
      if (to != null) {
        toY = to.getYear();
        toM = to.getMonth();
      }else {
        toY = fromY;
        toM = fromM;
      }

      if (fromYear == toYear) { // If timeframe is in the same year...
        tFrom.setYear(fromY);
        tTo.setYear(toY);
        //from.setYear(fromYear);
      }else {
          if (fromY <= fromYear) {
            if (fromM < toMonth) {
              tFrom.addYears(toY - toYear);
              tTo.addYears(toY - toYear);
              //System.err.println("Tepps : fromM ("+fromM+") < toMonth ("+toMonth+")");
            }else if (fromM == toMonth) {
              if (from.getDay() < tTo.getDay()) {
                tFrom.addYears(toY - toYear);
                tTo.addYears(toY - toYear);
              }
              //System.err.println("fromM ("+fromM+") >= toMonth ("+toMonth+")");
            }
          }else {
            if (toM < fromMonth) {
//              System.err.println("Sepps : toM ("+toM+") < fromMonth ("+fromMonth+")");
              tFrom.addYears(toY - toYear);
              tTo.addYears(toY - toYear);
            }else {
              if (to != null) {
//                System.err.println("toDay : "+to.getDay());
//                System.err.println("tFrom : "+tFrom.getDay());
                if (to.getDay() > tFrom.getDay()) {
                  tFrom.addYears(toY - toYear);
                  tTo.addYears(toY - toYear);
//                  System.err.println("craniton");
                }
              }
//              System.err.println("Ranus : toM ("+toM+") >= fromMonth ("+fromMonth+")");
            }
//                  System.err.println("sraneson");
          }
      }
      //System.err.println("yearsBetween : "+yearsBetween);
    }

    frame.setFrom(tFrom.getTimestamp());
    frame.setTo(tTo.getTimestamp());

    return frame;
  }

  public HashtableDoubleKeyed getResellerDayHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(resellerDayHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(resellerDayHashtableSessionName, hash);
      }
      return hash;
  }

  public void removeResellerHashtables(IWContext iwc) {
      iwc.removeSessionAttribute(resellerDayHashtableSessionName);
      iwc.removeSessionAttribute(resellerDayOfWeekHashtableSessionName);
  }

  public void removeServiceDayHashtable(IWContext iwc) {
    iwc.removeSessionAttribute(serviceDayHashtableSessionName);
  }

  private HashtableDoubleKeyed getResellerDayOfWeekHashtable(IWContext iwc) {
      HashtableDoubleKeyed hash = (HashtableDoubleKeyed) iwc.getSessionAttribute(resellerDayOfWeekHashtableSessionName);
      if (hash == null) {
        hash =  new HashtableDoubleKeyed();
        iwc.setSessionAttribute(resellerDayOfWeekHashtableSessionName, hash);
      }
      return hash;
  }

  public boolean getIfExpired(Contract contract, IWTimestamp stamp) throws RemoteException {
    boolean returner = false;
      IWTimestamp temp = new IWTimestamp(IWTimestamp.RightNow().getDay() , IWTimestamp.RightNow().getMonth(), IWTimestamp.RightNow().getYear());
      int daysBetween = stamp.getDaysBetween(temp, stamp);
      if (daysBetween < contract.getExpireDays()) {
        returner = true;
      }
    return returner;
  }

  public boolean getIfDay(IWContext iwc, Contract contract, Product product, IWTimestamp stamp) throws ServiceNotFoundException, TimeframeNotFoundException, SQLException, RemoteException {
      boolean isDay = false;
      if (contract != null) {
        String key1 = ( (Integer) contract.getPrimaryKey()).toString();
        String key2 = stamp.toSQLDateString();

        int dayOfWeek = stamp.getDayOfWeek();
        boolean isValidWeekDay = false;
        boolean isValidServiceDay = false;


        isValidServiceDay = getIfDay(iwc,product,product.getTimeframes(), stamp);

        if (isValidServiceDay) {
          HashtableDoubleKeyed resellerDayOfWeekHash = getResellerDayHashtable(iwc);
          Object object = resellerDayOfWeekHash.get(key1, key2);
          if (object == null) {
              isValidWeekDay = getResellerDayHome().getIfDay(contract.getResellerId(),contract.getServiceId() , dayOfWeek);
              resellerDayOfWeekHash.put(key1, key2, new Boolean(isValidWeekDay));
          }else {
            isValidWeekDay = ((Boolean) object).booleanValue();
          }
        }


        HashtableDoubleKeyed resellerDayHash = getResellerDayHashtable(iwc);
        Object obj = resellerDayHash.get(key1, key2);
        if (obj == null) {
          if (isValidWeekDay) {
            IWTimestamp from = new IWTimestamp(contract.getFrom());
            IWTimestamp to = new IWTimestamp(contract.getTo());
            if (stamp.isLaterThan(from) && to.isLaterThan(stamp)  ) {
              isDay = true;
              resellerDayHash.put(key1, key2, new Boolean(true));
            }else if (stamp.toSQLDateString().equals(from.toSQLDateString()) || stamp.toSQLDateString().equals(to.toSQLDateString())) {
              isDay = true;
              resellerDayHash.put(key1, key2, new Boolean(true));
            }else {
              resellerDayHash.put(key1, key2, new Boolean(false));
            }
          }else {
            resellerDayHash.put(key1, key2, new Boolean(false));
          }
        }else {
          isDay = ((Boolean) obj).booleanValue();
        }
      }else {
        System.err.println("TravelStockroomBusinessBean : getIfDay(iwc, contract, product, stamp) - Contract er null");
      }

      return isDay;
  }


  /**
   * @deprecated
   */
  public int getCurrencyIdForIceland(){
      int returner = -1;
      try {
        CurrencyHolder holder = CurrencyBusiness.getCurrencyHolder("ISK");
        returner = holder.getCurrencyID();
/*        String iceKr = "Íslenskar Krónur";
        String[] id = com.idega.data.SimpleQuerier.executeStringQuery("Select "+curr.getIDColumnName()+" from "+curr.getEntityName()+" where "+com.idega.block.trade.data.CurrencyBMPBean.getColumnNameCurrencyName() +" = '"+iceKr+"'");
        if (id == null || id.length == 0) {
            curr = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).createLegacy();
            curr.setCurrencyName(iceKr);
            curr.setCurrencyAbbreviation("ISK");
            curr.insert();
            returner = curr.getID();
        } else if (id.length > 0) {
          returner = Integer.parseInt(id[id.length -1]);
        }*/

      }
      catch (Exception e) {
        e.printStackTrace(System.err);
      }

      return returner;
  }

  public List getDepartureDays(IWContext iwc, Product product)throws RemoteException , FinderException{
    return getDepartureDays(iwc, product, null, null, true);
  }

  public List getDepartureDays(IWContext iwc, Product product, boolean showPast) throws RemoteException, FinderException{
    return getDepartureDays(iwc, product, null, null, showPast);
  }

  public List getDepartureDays(IWContext iwc, Product product, IWTimestamp from, IWTimestamp to) throws RemoteException, FinderException{
    return getDepartureDays(iwc, product, from, to, true);
  }

  public List getDepartureDays(IWContext iwc, Product product, IWTimestamp fromStamp, IWTimestamp toStamp, boolean showPast) throws RemoteException, FinderException{
    List returner = new Vector();
    try {
//      Service service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHomeLegacy(Service.class)).findByPrimaryKeyLegacy(product.getID());
      Timeframe[] frames = product.getTimeframes();

      for (int j = 0; j < frames.length; j++) {
        boolean yearly = frames[j].getIfYearly();


        IWTimestamp tFrom = new IWTimestamp(frames[j].getFrom());
        IWTimestamp tTo = new IWTimestamp(frames[j].getTo());


        IWTimestamp from = null;
        if (fromStamp != null) from = new IWTimestamp(fromStamp);
        IWTimestamp to = null;
        if (toStamp != null) to = new IWTimestamp(toStamp);

        if (from == null) {
          from = new IWTimestamp(tFrom);
        }
        if (to == null) {
          to   = new IWTimestamp(tTo);
        }

        int toMonth = tTo.getMonth();
        int toM = to.getMonth();
        int fromM = from.getMonth();
        int yearsBetween = 0;

        to.addDays(1);

        if (yearly) {
          int fromYear = tFrom.getYear();
          int toYear   = tTo.getYear();

          int fromY = from.getYear();
          int toY = to.getYear();

          int daysBetween = IWTimestamp.getDaysBetween(from, to);

          if (fromYear == toYear) {
            from.setYear(fromYear);
          }else {
              if (fromY >= toYear) {
                if (fromM > toMonth) {
                  from.setYear(fromYear);
                }else {
                  from.setYear(toYear);
                }
              }
          }

          to = new IWTimestamp(from);
            to.addDays(daysBetween);

          yearsBetween = to.getYear() - toY;
        }

        IWTimestamp stamp = new IWTimestamp(from);
        IWTimestamp temp;

        if (!showPast) {
          IWTimestamp now = IWTimestamp.RightNow();
          if (now.isLaterThan(from) && to.isLaterThan(now)) {
            stamp = new IWTimestamp(now);
          }else if (now.isLaterThan(from) && now.isLaterThan(to)) {
            stamp = new IWTimestamp(to);
          }
        }

        int[] weekDays = getWeekDays(product);


        //weekDays = is.idega.idegaweb.travel.data.ServiceDayBMPBean.getDaysOfWeek(product.getID());

        while (to.isLaterThan(stamp)) {
          for (int i = 0; i < weekDays.length; i++) {
            if (stamp.getDayOfWeek() == weekDays[i]) {
              if (yearly) stamp.addYears(-yearsBetween);
              returner.add(stamp);
              stamp = new IWTimestamp(stamp);
              if (yearly) stamp.addYears(yearsBetween);
            }
          }
          stamp.addDays(1);
        }
      }

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return returner;
  }
  
  public int[] getWeekDays(Product product) {
  	int[] weekDays = new int[]{};
  	try {
	  	SupplyPoolHome poolHome = (SupplyPoolHome) IDOLookup.getHome(SupplyPool.class);
	  	SupplyPoolDayHome poolDayHome = (SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class);
	  	SupplyPool pool = poolHome.findByProduct(product.getPrimaryKey());
	  	Collection poolDays = poolDayHome.findBySupplyPool(pool);
	  	if (poolDays != null && !poolDays.isEmpty()) {
	  		weekDays = new int[poolDays.size()];
	  		int counter = 0;
	  		Iterator iter = poolDays.iterator();
	  		SupplyPoolDayPK pk;
	  		while (iter.hasNext()) {
	  			pk = (SupplyPoolDayPK) ((SupplyPoolDay) iter.next()).getPrimaryKey();
	  			weekDays[counter++] = ((Integer) pk.getDayOfWeek()).intValue();
	  		}
	  	}
	  	return weekDays;
	  	
  	} catch (FinderException f) {
  		f.printStackTrace();
  	}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
    try {
      ServiceDayHome sdayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
      weekDays = sdayHome.getDaysOfWeek(product.getID());
    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return weekDays;
  }

  public boolean isWithinTimeframe(Timeframe timeframe, IWTimestamp stamp) throws RemoteException {
    boolean yearly = timeframe.getIfYearly();
    IWTimestamp from = new IWTimestamp(timeframe.getFrom());
    IWTimestamp to   = new IWTimestamp(timeframe.getTo());
    return getStockroomBusiness().isInTimeframe(from, to, stamp,yearly);
  }


  public ServiceDayHome getServiceDayHome() throws RemoteException{
    ServiceDayHome sdHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
    return sdHome;
//    return (ServiceDay) IBOLookup.getServiceInstance(this.getIWApplicationContext(), ServiceDay.class);
  }

  public ResellerDayHome getResellerDayHome() throws RemoteException{
    ResellerDayHome rdHome = (ResellerDayHome) IDOLookup.getHome(ResellerDay.class);
    return rdHome;
//    return (ResellerDay) IBOLookup.getServiceInstance(this.getIWApplicationContext(), ResellerDay.class);
  }

  public Collection getTravelAddressIdsFromRefill(Product product, int tAddressId) throws RemoteException, IDOFinderException, FinderException {
    if (tAddressId > 0) {
      TravelAddress ta = ((TravelAddressHome) IDOLookup.getHomeLegacy(TravelAddress.class)).findByPrimaryKey(tAddressId);
      return getTravelAddressIdsFromRefill(product, ta);
    }else {
      return new Vector();
    }
  }

  public Collection getTravelAddressIdsFromRefill(Product product, TravelAddress tAddress) throws RemoteException, IDOFinderException {
    List list = null;
	  	if (tAddress == null) {
	  		list = getProductBusiness().getDepartureAddresses(product, true);
	  	} else {
	    list = getTravelAddressesFromRefill(product, tAddress);
	  	}
    Collection coll = new Vector();
    Iterator iter = list.iterator();
    while (iter.hasNext()) {
      TravelAddress item = (TravelAddress) iter.next();
//      System.err.println("adding : "+item.getID());
//      coll.add(new Integer(item.getID()));
      coll.add(item.getPrimaryKey());
    }
    return coll;
  }

  private List getTravelAddressesFromRefill(Product product, TravelAddress tAddress) throws RemoteException, IDOFinderException {
    List addresses = getProductBusiness().getDepartureAddresses(product, true);
    int indexOf = addresses.indexOf(tAddress);

    TravelAddress tAdd;
    int startIndex = 0;
    for (int i = indexOf; i >= 0; i--) {
      tAdd = (TravelAddress) addresses.get(i);
      if (tAdd.getRefillStock()) {
        startIndex = i;
        break;
      }
    }

    int size = addresses.size();
    for (int i = (indexOf+1); i < size ; i++) {
      indexOf = i;
      tAdd = (TravelAddress) addresses.get(i);
      if (tAdd.getRefillStock()) {
        --indexOf;
        break;
      }
    }

    Collection coll = new Vector();
    List list = new Vector(addresses.subList(startIndex, indexOf+1));
    if (startIndex == (indexOf+1)) {
      list = new Vector();
        list.add(tAddress);
    }

    return list;
  }

  protected boolean setActiveDaysAll(int serviceId) throws RemoteException {
    return setActiveDays(serviceId, new int[]{GregorianCalendar.SUNDAY, GregorianCalendar.MONDAY,GregorianCalendar.TUESDAY, GregorianCalendar.WEDNESDAY, GregorianCalendar.THURSDAY, GregorianCalendar.FRIDAY, GregorianCalendar.SATURDAY}, false);
  }

  protected boolean setActiveDays(int serviceId, int[] activeDays) throws RemoteException {
  	return setActiveDays(serviceId, activeDays, true);
  }	
  protected boolean setActiveDays(int serviceId, int[] activeDays, boolean removeUnactive) throws RemoteException {
    boolean returner = true;
    try {
      List list = new Vector();
      for (int i = 0; i < activeDays.length; i++) {
        list.add(new Integer(activeDays[i]));
      }

      if (serviceId != -1) {
        if (activeDays.length > 0) {
          ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          ServiceDay sDay = null;
          ServiceDayPK sdPK = null;

          for (int i = ServiceDayBMPBean.SUNDAY; i <= ServiceDayBMPBean.SATURDAY; i++) {
          	sdPK = new ServiceDayPK(new Integer(serviceId), new Integer(i));
          	try {
          		sDay = sDayHome.findByPrimaryKey(sdPK);
          	} catch (FinderException f) {
          		sDay = null;
          	}
          	//sDay = sDayHome.findByServiceAndDay(serviceId, i);
          	
            if (list.contains(new Integer(i))) {
              if (sDay == null) {
                sDay = sDayHome.create(new ServiceDayPK(new Integer(serviceId), new Integer(i)));
                sDay.setServiceId(serviceId);
                sDay.setDayOfWeek(i);
                sDay.store();
              }
            }else {
              if (sDay != null && removeUnactive) {
                sDay.remove();
              }

            }
          }

        }else {
          ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          sDayHome.deleteService(serviceId);
        }
      }else {
        /*
        if (activeDays.length > 0) {
          ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          ServiceDay sDayTemp = sDayHome.create();
          ServiceDay sDay;
          for (int i = 0; i < activeDays.length; i++) {
            sDay = sDayHome.create();
            sDay.setServiceId(serviceId);
            sDay.setDayOfWeek(activeDays[i]);
            sDay.store();
          }
        }
        */
      }

      returner = true;
    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
      returner = false;
    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
      returner = false;
    }catch (RemoveException re) {
      re.printStackTrace(System.err);
      returner = false;
    }


    return returner;
  }

  protected void removeExtraPrices(Product product) throws RemoteException, FinderException{
    try {
      System.out.println("[TSB] removing extra prices ");
      ProductBusiness pBus = this.getProductBusiness();
      List addresses = pBus.getDepartureAddresses(product, false);
      Timeframe[] timeframes = product.getTimeframes();
      TravelAddress tAddress = ((TravelAddressHome) IDOLookup.getHome(TravelAddress.class)).create();
      Timeframe timeframe = ((TimeframeHome) IDOLookup.getHome(Timeframe.class)).create();


      ProductPrice[] allPrices = com.idega.block.trade.stockroom.data.ProductPriceBMPBean.getProductPrices(product.getID(), -1, -1, false);

      ProductPrice price;
      Collection cFrames;
      Collection cAddresses;
      boolean remove = false;
      for (int i = 0; i < allPrices.length; i++) {
        price = allPrices[i];
        System.out.println("============================================================");
        System.out.println("[TSB] working with price : "+price.getPrice());

        cFrames = price.getTimeframes();
        cAddresses = price.getTravelAddresses();

        Iterator iter = cFrames.iterator();
        System.out.print("[TSB] cFrames contains : ");
        while (iter.hasNext()) {
          Object item = iter.next();
          System.out.print(" "+item.toString() +",");
        }
        System.out.println(";");

        iter = cAddresses.iterator();
        System.out.print("[TSB] cAddresses contains : ");
        while (iter.hasNext()) {
          Object item = iter.next();
          System.out.print(" "+item.toString() +",");
        }
        System.out.println(";");

        if (timeframes.length == 0 && cFrames.size() > 0) {
          remove = true;
        }
        if (addresses.size() == 0 && cAddresses.size() > 0) {
          remove = true;
        }


        if (!remove) {
          for (int j = 0; j < timeframes.length; j++) {
            System.out.println("... timeframeId = "+timeframes[j].getID());
            if ( ! cFrames.contains(timeframes[j].getPrimaryKey()) ) {
              remove = true;
              break;
            }
          }
        }

        if (remove) {
          System.out.println("[TSB] remove == true, (timeframe) removing price with price = "+price.getPrice());
          price.delete();
          price.update();
        }else {
          Iterator iAddresses = addresses.iterator();
          while (iAddresses.hasNext()) {
            TravelAddress item = (TravelAddress) iAddresses.next();
            System.out.println("... addressId = "+item.getPrimaryKey());
            if (cAddresses.contains(item.getPrimaryKey()) ) {
              remove = true;
              break;
            }

          }
        }

        if (remove) {
          System.out.println("[TSB] remove == true, (address) removing price with price = "+price.getPrice());
          price.delete();
          price.update();
        }


      }

    }catch (SQLException ex) {
      throw new FinderException(ex.getMessage());
    }catch (CreateException ex) {
      throw new FinderException(ex.getMessage());
    }catch (IDORelationshipException ex) {
      throw new FinderException(ex.getMessage());
    }

  }

  protected int[] setDepartureAddress(int serviceId, String departureFrom, IWTimestamp departureTime) throws RemoteException, FinderException, SQLException, IDOFinderException{
    int departureAddressTypeId = com.idega.core.location.data.AddressTypeBMPBean.getId(ProductBusinessBean.uniqueDepartureAddressType);
    TravelAddress departureAddress = null;
    Address address = null;

    if (serviceId == -1) {

      address = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
      address.setAddressTypeID(departureAddressTypeId);
      address.setStreetName(departureFrom);
      address.insert();

      departureAddress = ((com.idega.block.trade.stockroom.data.TravelAddressHome)com.idega.data.IDOLookup.getHomeLegacy(TravelAddress.class)).createLegacy();
      departureAddress.setAddressId(address.getID());
      departureAddress.setAddressTypeId(com.idega.block.trade.stockroom.data.TravelAddressBMPBean.ADDRESS_TYPE_DEPARTURE);
      departureAddress.setTime(departureTime.getTimestamp());
      departureAddress.insert();

    }else {
      Product product = getProductBusiness().getProduct(serviceId);//((com.idega.block.trade.stockroom.data.ProductHome)com.idega.data.IDOLookup.getHomeLegacy(Product.class)).findByPrimaryKeyLegacy(tourId);
      List tAddresses = getProductBusiness().getDepartureAddresses(product, false); ///Address[]) (product.findRelated( (Address) com.idega.core.data.AddressBMPBean.getStaticInstance(Address.class), com.idega.core.data.AddressBMPBean.getColumnNameAddressTypeId(), Integer.toString(departureAddressTypeId)));
      TravelAddress tAddress;
      if (tAddresses.size() > 0) {
        tAddress = (TravelAddress) tAddresses.get(0);
        departureAddress = ((com.idega.block.trade.stockroom.data.TravelAddressHome)com.idega.data.IDOLookup.getHomeLegacy(TravelAddress.class)).findByPrimaryKeyLegacy(tAddress.getID());
        departureAddress.setTime(departureTime);
        departureAddress.update();

        address = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).findByPrimaryKeyLegacy(departureAddress.getAddressId());
        address.setStreetName(departureFrom);
        address.update();
      }else {
        address = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
        address.setAddressTypeID(departureAddressTypeId);
        address.setStreetName(departureFrom);
        address.insert();

        departureAddress = ((com.idega.block.trade.stockroom.data.TravelAddressHome)com.idega.data.IDOLookup.getHomeLegacy(TravelAddress.class)).createLegacy();
        departureAddress.setAddressTypeId(TravelAddressBMPBean.ADDRESS_TYPE_DEPARTURE);
        departureAddress.setTime(departureTime);
        departureAddress.setAddressId(address.getID());
        departureAddress.insert();
      }

    }
    return new int[]{departureAddress.getID()};

  }

  protected int[] setArrivalAddress(int serviceId, String arrivalAt) throws RemoteException, FinderException, SQLException, IDOFinderException{
    int arrivalAddressTypeId = com.idega.core.location.data.AddressTypeBMPBean.getId(ProductBusinessBean.uniqueArrivalAddressType);
    Address arrivalAddress = null;
    Address address = null;

    if (serviceId == -1) {
      arrivalAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
      arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
      arrivalAddress.setStreetName(arrivalAt);
      arrivalAddress.insert();
    }else {
      Product product = getProductBusiness().getProduct(serviceId);//((com.idega.block.trade.stockroom.data.ProductHome)com.idega.data.IDOLookup.getHomeLegacy(Product.class)).findByPrimaryKeyLegacy(tourId);

      Address[] tempAddresses = getProductBusiness().getArrivalAddresses(product);// (Address[]) (product.findRelated( (Address) com.idega.core.data.AddressBMPBean.getStaticInstance(Address.class), com.idega.core.data.AddressBMPBean.getColumnNameAddressTypeId(), Integer.toString(arrivalAddressTypeId)));
      if (tempAddresses.length > 0) {
        arrivalAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).findByPrimaryKeyLegacy(tempAddresses[0].getID());
        arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
        arrivalAddress.setStreetName(arrivalAt);
        arrivalAddress.update();
      }else {
        arrivalAddress = ((com.idega.core.location.data.AddressHome)com.idega.data.IDOLookup.getHomeLegacy(Address.class)).createLegacy();
        arrivalAddress.setAddressTypeID(arrivalAddressTypeId);
        arrivalAddress.setStreetName(arrivalAt);
        arrivalAddress.insert();
      }
    }
    return new int[]{arrivalAddress.getID()};
  }
  
  public void removeDepartureDaysApplication(IWApplicationContext iwac, Product product) throws RemoteException{
    Enumeration enumer = iwac.getIWMainApplication().getAttributeNames();
    List names = new Vector();
    String index2check = "prodDepDays";
    if (product != null) {
    	index2check = "prodDepDays"+product.getPrimaryKey().toString()+"_";
    }
    String name;
    while (enumer.hasMoreElements()) {
      name = (String) enumer.nextElement();
      if (name.indexOf(index2check) != -1) {
      	names.add(name);
      }
    }
    
    Iterator iter = names.iterator();
    while (iter.hasNext()) {
      iwac.removeApplicationAttribute((String) iter.next());
    }
  }

  /**
   * returns the other products, Null if this is the only one, or if this one is not using a pool
   * @param product
   * @return
 * @throws RemoteException
   */
  public Collection getProductsSharingPool(Product product) throws RemoteException {
		try {
			if (supportsSupplyPool()) {
				SupplyPool pool = ((SupplyPoolHome) IDOLookup.getHome(SupplyPool.class)).findByProduct(product);
				Collection products = getProductBusiness().getProductHome().findBySupplyPool(pool);
				products.remove(product);
				return products;
			} else {
				return null;
			}
		}
		catch (IDOLookupException e) {
			e.printStackTrace();
		}
		catch (IDORelationshipException e) {
			e.printStackTrace();
		}
		catch (FinderException e) {
			//e.printStackTrace();
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}
		return null;
  }
  
	public int getMaxBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException{
		try {
			HashMap subMap = (HashMap) maxDaysMap.get((Integer) product.getPrimaryKey());
//			Cacheing disabled for a while // erm re-enabled			
//			HashMap subMap = null;
			if (subMap == null) {
				subMap = new HashMap();
			}
			Integer returner = (Integer) subMap.get(stamp.toSQLDateString()); 
			if ( returner == null ) {
				if (stamp != null) {
					if (supportsSupplyPool()) {
						try {
							SupplyPool pool = ((SupplyPoolHome) IDOLookup.getHome(SupplyPool.class)).findByProduct(product);
							SupplyPoolDay pDay = 	((SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class)).findByPrimaryKey(new SupplyPoolDayPK(pool.getPrimaryKey(), new Integer(stamp.getDayOfWeek())));
							int max = pDay.getMax();
	
							int iBookingExtra = getBooker().getBookingsTotalCountByOthersInPool(product, stamp);
							max -= iBookingExtra;
							returner = new Integer(max);
						} catch (FinderException fe) {
							//fe.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if (returner == null) {
					  ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
					  ServiceDay sDay;
					  sDay = sDayHome.findByServiceAndDay(product.getID() , stamp.getDayOfWeek());
					  if (sDay != null) {
					    returner = new Integer(sDay.getMax());
					  }
				  }
					subMap.put(stamp.toSQLDateString(), returner);
					maxDaysMap.put((Integer) product.getPrimaryKey(), subMap);
			  }
				//System.out.println("Getting max for product = "+product.getPrimaryKey()+" stamp = "+stamp.toSQLString()+" = MAX = "+returner.intValue());
			} else {
				//System.out.println("Found max for product = "+product.getPrimaryKey()+" stamp = "+stamp.toSQLString()+" = MAX = "+returner.intValue());
			}
			return returner.intValue();

		} catch (Exception e) {
			return 0;
		}
	
	
	}
	public int getMinBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException{
		try {
			if (stamp != null) {
				
				if (supportsSupplyPool()) {
					try {
						SupplyPool pool = ((SupplyPoolHome) IDOLookup.getHome(SupplyPool.class)).findByProduct(product);
						SupplyPoolDay pDay = 	((SupplyPoolDayHome) IDOLookup.getHome(SupplyPoolDay.class)).findByPrimaryKey(new SupplyPoolDayPK(pool.getPrimaryKey(), new Integer(stamp.getDayOfWeek())));
						return pDay.getMin();
					} catch (FinderException fe) {
						//fe.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			
				ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
			  ServiceDay sDay;
			  sDay = sDayHome.findByServiceAndDay(product.getID() , stamp.getDayOfWeek());
			  if (sDay != null) {
			    return sDay.getMin();
			  }
		  }
			return 0;

		} catch (Exception e) {
			return 0;
		}
	}

	public void initializeBean() {
		getBooker().addActionListener(this);
		getInquirer().addActionListener(this);
	}


	public void actionPerformed(ActionEvent event) {
		if (event != null) {
			if (event.getActionCommand().equals(BookerBean.COMMAND_BOOKING)) {
				try {
					GeneralBooking booking = getBooker().getGeneralBookingHome().findByPrimaryKey(new Integer(event.getID()));
					Product product = booking.getService().getProduct();
					Collection products = this.getProductsSharingPool(product);
					maxDaysMap.remove( (Integer) product.getPrimaryKey());
					System.out.println("[ServiceSearchBusinessBean] Invalidating max days cache for product = "+product.getPrimaryKey());
					if (products != null) {
						Iterator iter = products.iterator();
						while (iter.hasNext()) {
							product = (Product) iter.next();
							maxDaysMap.remove( (Integer) product.getPrimaryKey());
							System.out.println("[ServiceSearchBusinessBean] Invalidating max days cache for product = "+product.getPrimaryKey());
						}
					}
				}
				catch (RemoteException e) {
					e.printStackTrace();
				}
				catch (FinderException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/*
	 * override me please
	 */
	public boolean supportsSupplyPool() {
		return false;
	}
	
  private ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }

  private Booker getBooker() {
    try {
			return (Booker) IBOLookup.getServiceInstance(getIWApplicationContext(), Booker.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }

  private Inquirer getInquirer() {
    try {
			return (Inquirer) IBOLookup.getServiceInstance(getIWApplicationContext(), Inquirer.class);
		}
		catch (IBOLookupException e) {
			throw new IBORuntimeException(e);
		}
  }  
  
  private StockroomBusiness getStockroomBusiness() throws RemoteException {
    return (StockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), StockroomBusiness.class);
  }
}
