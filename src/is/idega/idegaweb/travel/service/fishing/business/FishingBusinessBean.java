package is.idega.idegaweb.travel.service.fishing.business;

import java.sql.*;


import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.service.business.*;


/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class FishingBusinessBean extends TravelStockroomBusinessBean implements FishingBusiness {

  public FishingBusinessBean() {
  }

  public int createFishing(int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime,  boolean isValid, int discountTypeId) throws Exception{
    return updateFishing(-1, supplierId, fileId, name, number, description, activeDays, departureFrom, departureTime, arrivalAt, arrivalTime, isValid, discountTypeId);
  }

  public int updateFishing(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String departureFrom, IWTimestamp departureTime, String arrivalAt, IWTimestamp arrivalTime,boolean isValid, int discountTypeId) throws Exception{
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
      ProductCategory pCat = pCatHome.getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      Product product = pHome.findByPrimaryKey(new Integer(productId));
      product.removeAllFrom(ProductCategory.class);
      pCat.addTo(Product.class, productId);
    }catch (SQLException sql) {}

    return productId;
  }


}
