package is.idega.travel.business;

import com.idega.block.trade.stockroom.business.StockroomBusiness;
import com.idega.block.trade.stockroom.data.*;
//import com.idega.block.trade.stockroom.data.Product;
//import com.idega.block.trade.stockroom.data.Supplies;
import is.idega.travel.data.Service;
import java.sql.Timestamp;
import com.idega.core.data.Address;
import is.idega.travel.data.HotelPickupPlace;
import java.sql.SQLException;
import com.idega.util.idegaTimestamp;
import java.sql.SQLException;
import com.idega.data.EntityFinder;
import java.util.List;
import com.idega.transaction.IdegaTransactionManager;
import javax.transaction.TransactionManager;
import java.sql.Date;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TravelStockroomBusiness extends StockroomBusiness {

  public TravelStockroomBusiness() {
  }

  public void addSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method addSupplies() not yet implemented.");
  }

  public void depleteSupplies(int product_id, float amount) {
    throw new java.lang.UnsupportedOperationException("Method depleteSupplies() not yet implemented.");
  }



  public void setSupplyStatus(int productId, float status, int period) throws SQLException {
    Supplies supplies = new Supplies();

    supplies.setProductId(productId);
    supplies.setCurrentSupplies(status);
    supplies.setRecordTime(idegaTimestamp.RightNow().getTimestamp());

    if(period > -1){
      supplies.setPeriod(period);
    }

    supplies.insert();

  }

  public float getSupplyStatus(int productId) throws SQLException {
    Supplies supplies = (Supplies)Supplies.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAllByColumnOrdered(supplies,supplies.getColumnNameProductId(),Integer.toString(productId),Supplies.getColumnNameRecordTime());
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public float getSupplyStatus(int productId, Date date) throws SQLException {
    Supplies supplies = (Supplies)Supplies.getStaticInstance(Supplies.class);
    List lSupplies = EntityFinder.findAll(supplies,"SELECT * FROM  "+supplies.getEntityName()+" WHERE "+supplies.getColumnNameProductId()+" = "+Integer.toString(productId)+" AND "+Supplies.getColumnNameRecordTime()+" <= '"+date.toString()+"' ORDER BY "+ Supplies.getColumnNameRecordTime(),1);
    if(lSupplies != null && lSupplies.size() > 0){
      return ((Supplies)lSupplies.get(0)).getCurrentSupplies();
    }else{
      return 0;
    }
  }

  public void setPrice(int productId, int priceCategoryId, Integer currnecyId, float price, int priceType, Timestamp time) throws Exception {
    ProductPrice prPrice = new ProductPrice();

    prPrice.setProductId(productId);
    prPrice.setPriceCategoryID(priceCategoryId);


    if(currnecyId != null){
      prPrice.setCurrencyId(currnecyId);
    }

    prPrice.setPrice(price);
    prPrice.setPriceType(priceType);
    prPrice.setPriceDate(time);

    prPrice.insert();

  }

  public float getPrice(int productId, int priceCategoryId, Integer currencyId, Date date) throws Exception {
    ProductPrice pr = (ProductPrice)ProductPrice.getStaticInstance(ProductPrice.class);
    List result = null;
    if(currencyId != null){
      result = EntityFinder.findAll(pr,"SELECT * FROM "+pr.getEntityName()+" WHERE "+ProductPrice.getColumnNameProductId()+" LIKE '"+productId+"' AND "+ProductPrice.getColumnNamePriceCategoryId()+" LIKE '"+priceCategoryId+"' AND "+ProductPrice.getColumnNameCurrencyId()+" LIKE '"+currencyId+"' AND "+ProductPrice.getColumnNamePriceDate()+" <= '"+date.toString()+"' ORDER BY "+ProductPrice.getColumnNamePriceDate() , 1);
    }else{
      result = EntityFinder.findAll(pr,"SELECT * FROM "+pr.getEntityName()+" WHERE "+ProductPrice.getColumnNameProductId()+" LIKE '"+productId+"' AND "+ProductPrice.getColumnNamePriceCategoryId()+" LIKE '"+priceCategoryId+"' AND "+ProductPrice.getColumnNamePriceDate()+" <= '"+date.toString()+"' ORDER BY "+ProductPrice.getColumnNamePriceDate() , 1);
    }

    if(result != null && result.size() > 0){
      return ((ProductPrice)result.get(0)).getPrice();
    }else{
      throw  new RuntimeException("Price not set");
    }
  }



  public int createPriceCategory(int supplierId, String name, String description, String type, String extraInfo) throws Exception {

    PriceCategory cat = new PriceCategory();

    cat.setName(name);

    if(description != null){
      cat.setDescription(description);
    }

    if(type != null){
      cat.setType(type);
    }

    if(extraInfo != null){
      cat.setExtraInfo(extraInfo);
    }

    cat.insert();

    cat.addTo(Supplier.class,supplierId);

    return cat.getID();
  }



  public static int createService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int[] pickupPlaceIds) throws Exception{


    TransactionManager transaction = IdegaTransactionManager.getInstance();
    try{
      transaction.begin();

      int id = StockroomBusiness.createProduct(supplierId,fileId,serviceName,serviceDescription,isValid);
      Service service = new Service();
      service.setID(id);

      service.setDepartureTime(departure);
      service.setAttivalTime(arrival);

      if(pickupPlaceIds != null && pickupPlaceIds.length > 0){
        for (int i = 0; i < pickupPlaceIds.length; i++) {
          service.addTo(HotelPickupPlace.class, pickupPlaceIds[i]);
        }
        service.setHotelPickup(true);
      }else{
        service.setHotelPickup(false);
      }

      if(addressIds != null){
        for (int i = 0; i < addressIds.length; i++) {
          service.addTo(Address.class, addressIds[i]);
        }
      }
      service.insert();
      transaction.commit();

      return id;
    }catch(SQLException e){
      transaction.rollback();
      throw new RuntimeException("IWE226TB89");
    }
  }


  /**
   * @todo createTripService
   */
  public static int createTripService(int supplierId, Integer fileId, String serviceName, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int[] pickupPlaceIds) throws Exception {
      return TravelStockroomBusiness.createService(supplierId, fileId, serviceName, serviceDescription, isValid, addressIds, departure,arrival, pickupPlaceIds);
  }




}