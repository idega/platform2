package is.idega.idegaweb.travel.service.hotel.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusinessBean;
import is.idega.idegaweb.travel.service.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.util.IWTimestamp;
import com.idega.data.IDOLookup;
import com.idega.business.IBOLookup;
import java.sql.SQLException;
import java.util.*;
import javax.ejb.*;
import java.rmi.RemoteException;


/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class HotelBusinessBean extends TravelStockroomBusinessBean implements HotelBusiness {

  public HotelBusinessBean() {
  }

//  public int createService(int supplierId, Integer fileId, String serviceName, String number, String serviceDescription, boolean isValid, int[] addressIds, Timestamp departure, Timestamp arrival, int discountTypeId) throws Exception{
  public int createHotel(int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime,  boolean isValid, int discountTypeId) throws Exception{
/*
    int[] departureAddressIds = setDepartureAddress(-1, departureFrom, departureTime);
    int[] arrivalAddressIds = setArrivalAddress(-1, arrivalAt);

    int productId = createService(supplierId, fileId, name, number, description, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp(), discountTypeId);

    setActiveDays(productId, activeDays);

    try {
      ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
      ProductCategory pCat = pCatHome.getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL);
      pCat.addTo(Product.class, productId);
    }catch (SQLException sql) {}

    return productId;
*/
    return updateHotel(-1, supplierId, fileId, name, number, description, activeDays, departureFrom, departureTime, arrivalAt, arrivalTime, isValid, discountTypeId);

  }

  public int updateHotel(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime,boolean isValid, int discountTypeId) throws Exception{
    int productId = -1;
    int[] departureAddressIds = setDepartureAddress(serviceId, departureFrom, departureTime);
    int[] arrivalAddressesIds = setArrivalAddress(serviceId, arrivalAt);

    if (serviceId == -1) {
      productId = createService(supplierId, fileId, name, number, description, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp(), discountTypeId);
    }else {
      productId = updateService(serviceId, supplierId, fileId, name, number, description, isValid, departureAddressIds, departureTime.getTimestamp(), arrivalTime.getTimestamp(), discountTypeId);
    }

    setActiveDays(productId, activeDays);

    try {
      ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
      ProductCategory pCat = pCatHome.getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL);
      Product product = ProductBusiness.getProduct(productId);
      product.removeFrom(ProductCategory.class);
      pCat.addTo(Product.class, productId);
    }catch (SQLException sql) {}

    return productId;
  }


}
