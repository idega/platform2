package is.idega.idegaweb.travel.service.hotel.business;

import javax.ejb.FinderException;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;
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

public class HotelBusinessBean extends TravelStockroomBusinessBean implements HotelBusiness {

  public HotelBusinessBean() {
  }

  public int createHotel(int supplierId, Integer fileId, String name, String number, String description, int numberOfUnits, boolean isValid, int discountTypeId) throws Exception{
    return updateHotel(-1, supplierId, fileId, name, number, description, numberOfUnits, isValid, discountTypeId);
  }

  public int updateHotel(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int numberOfUnits, boolean isValid, int discountTypeId) throws Exception{
    int productId = -1;

    if (serviceId == -1) {
      productId = createService(supplierId, fileId, name, number, description, isValid, new int[]{}, null, null, discountTypeId);
    }else {
      productId = updateService(serviceId, supplierId, fileId, name, number, description, isValid, new int[]{}, null, null, discountTypeId);
    }

    Hotel hotel;
    HotelHome hHome = (HotelHome) IDOLookup.getHome(Hotel.class);
    try {
      /** update hotel */
      hotel = hHome.findByPrimaryKey(new Integer(productId));
      hotel.setNumberOfUnits(numberOfUnits);
      hotel.store();
    }catch (FinderException fe) {
      /** create hotel */
      hotel = hHome.create();
      hotel.setPrimaryKey(new Integer(productId));
      hotel.setNumberOfUnits(numberOfUnits);
      hotel.store();
    }


    setActiveDaysAll(productId);

    try {
      ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
      ProductCategory pCat = pCatHome.getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      Product product = pHome.findByPrimaryKey(new Integer(productId));
      product.removeAllFrom(ProductCategory.class);
      pCat.addTo(Product.class, productId);
    }catch (SQLException sql) {}

    Product product = (( ProductHome ) IDOLookup.getHome(Product.class)).findByPrimaryKey(new Integer(productId));
    product.removeAllFrom(TravelAddress.class);
    product.removeAllFrom(Timeframe.class);

    this.removeExtraPrices(product);


    return productId;
  }


}
