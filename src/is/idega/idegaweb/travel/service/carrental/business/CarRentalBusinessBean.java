package is.idega.idegaweb.travel.service.carrental.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusinessBean;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.data.PickupPlaceHome;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactoryBean;
import is.idega.idegaweb.travel.service.carrental.data.CarRental;
import is.idega.idegaweb.travel.service.carrental.data.CarRentalHome;

import java.sql.SQLException;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductCategoryHome;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;


/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class CarRentalBusinessBean extends TravelStockroomBusinessBean implements CarRentalBusiness {

  public CarRentalBusinessBean() {
  }

  public int createCar(int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String[] pickupPlaceIds, String[] dropoffPlaceIds, boolean isValid, int discountTypeId) throws Exception{
    return updateCar(-1, supplierId, fileId, name, number, description, activeDays, pickupPlaceIds, dropoffPlaceIds, isValid, discountTypeId);
  }

  public int updateCar(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int[] activeDays, String[] pickupPlaceIds, String[] dropoffPlaceIds, boolean isValid, int discountTypeId) throws Exception{
    int productId = -1;

    if (serviceId == -1) {
      productId = createService(supplierId, fileId, name, number, description, isValid, null, null, null, discountTypeId);
      
    }else {
      productId = updateService(serviceId, supplierId, fileId, name, number, description, isValid, null, null, null, discountTypeId);
    }

		if ( productId > 0) {
			CarRentalHome cHome = (CarRentalHome) IDOLookup.getHome(CarRental.class);
			CarRental car;
			try {
			  /** update car */
			  car = cHome.findByPrimaryKey(new Integer(productId));
			  car.store();
			}catch (FinderException fe) {
			  /** create car */
			  car = cHome.create();
			  car.setPrimaryKey(new Integer(productId));
			  car.store();
			}
			
			PickupPlaceHome pHome = (PickupPlaceHome) IDOLookup.getHome(PickupPlace.class);
			PickupPlace p;
			car.removeAllDropoffPlaces();
			car.removeAllPickupPlaces();
			if (pickupPlaceIds != null) {
				for (int i = 0 ; i < pickupPlaceIds.length; i++) {
					p = pHome.findByPrimaryKey(new Integer(pickupPlaceIds[i]));
					try {
						car.addPickupPlace(p);
					}catch (IDOAddRelationshipException e) {
						System.err.println("[CarRentalBuisnessBean] trying to add pickupPlace failed");	
					}
				}	
			}
			if (dropoffPlaceIds != null) {
				for (int i = 0 ; i < dropoffPlaceIds.length; i++) {
					p = pHome.findByPrimaryKey(new Integer(dropoffPlaceIds[i]));
					try {
						car.addDropoffPlace(p);
					}catch (IDOAddRelationshipException e) {
						System.err.println("[CarRentalBuisnessBean] trying to add dropoffPlace failed");	
					}
				}	
			}
		}

    setActiveDays(productId, activeDays);

    try {
      ProductCategoryHome pCatHome = (ProductCategoryHome) IDOLookup.getHomeLegacy(ProductCategory.class);
      ProductCategory pCat = pCatHome.getProductCategory(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      Product product = pHome.findByPrimaryKey(new Integer(productId));
      product.removeAllFrom(ProductCategory.class);
      pCat.addTo(Product.class, productId);
    }catch (SQLException sql) {
    	sql.printStackTrace(System.err);
    }

    return productId;
  }


}
