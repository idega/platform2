package is.idega.idegaweb.travel.service.hotel.business;

import javax.ejb.FinderException;
import is.idega.idegaweb.travel.service.hotel.data.HotelHome;
import is.idega.idegaweb.travel.service.hotel.data.Hotel;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;
import java.util.Vector;

import com.idega.business.IBOLookup; 
import com.idega.block.trade.stockroom.data.*;
import com.idega.data.*;
import com.idega.presentation.IWContext;
import com.idega.util.*;
import com.idega.util.datastructures.HashtableDoubleKeyed;

import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
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

  public int createHotel(int supplierId, Integer fileId, String name, String number, String description, int numberOfUnits, int maxPerUnit, boolean isValid, int discountTypeId) throws Exception{
    return updateHotel(-1, supplierId, fileId, name, number, description, numberOfUnits, maxPerUnit, isValid, discountTypeId);
  }

  public int updateHotel(int serviceId, int supplierId, Integer fileId, String name, String number, String description, int numberOfUnits, int maxPerUnit, boolean isValid, int discountTypeId) throws Exception{
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
      hotel.setMaxPerUnit( maxPerUnit );
      hotel.store();
    }catch (FinderException fe) {
      /** create hotel */
      hotel = hHome.create();
      hotel.setPrimaryKey(new Integer(productId));
      hotel.setNumberOfUnits(numberOfUnits);
      hotel.setMaxPerUnit( maxPerUnit );
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



    return productId;
  }
  
  public void finalizeHotelCreation(Product product) throws FinderException, RemoteException{
    this.removeExtraPrices(product);
  }


	public boolean getIfDay(IWContext p0, int p1, int p2)throws RemoteException, RemoteException {
		return true;
	}

  public boolean getIfDay(IWContext iwc, Product product, Timeframe[] timeframes, IWTimestamp stamp, boolean includePast, boolean fixTimeframe) throws ServiceNotFoundException, TimeframeNotFoundException, RemoteException {
      boolean isDay = false;
      String key1 = Integer.toString(product.getID());
      String key2 = stamp.toSQLDateString();

      HashtableDoubleKeyed serviceDayHash = getServiceDayHashtable(iwc);
      Object obj = null;
      if (obj == null) {
      	boolean validDate = false;
	      if (!includePast) {
	        IWTimestamp now = IWTimestamp.RightNow();
	        IWTimestamp tNow = new IWTimestamp(now.getDay(), now.getMonth(), now.getYear());
	        if (!tNow.isLaterThan(stamp)) {
	        	validDate = true;
	      	}
	      }else {
	          validDate = true;
	      }
	      
	      if (validDate) {
	      	Hotel hotel;
					try {
						hotel =	((HotelHome) IDOLookup.getHome(Hotel.class)).findByPrimaryKey(product.getPrimaryKey());
		      	int totalRooms = hotel.getNumberOfUnits();
		      	if (totalRooms > 0) {
		      		HotelBooker hBook = (HotelBooker) IBOLookup.getServiceInstance( iwc, HotelBooker.class);
		      		int manyBookings = hBook.getNumberOfReservedRooms( product.getID(), stamp, null);
		      		if (totalRooms > manyBookings) {
		      			isDay = true;
		      		}
		        }else {
		        	isDay = true;	
		      	}
			
					} catch (FinderException e) {
						e.printStackTrace(System.err);
					}
				} 
      }
      else {
        isDay = ((Boolean) obj).booleanValue();
      }
      return isDay;
  }

	public List getDepartureDays(IWContext iwc,	Product product,	IWTimestamp fromStamp,	IWTimestamp toStamp,	boolean showPast)	throws FinderException, RemoteException, RemoteException {
    List returner = new Vector();
		IWTimestamp stamp = new IWTimestamp(fromStamp);	
		IWTimestamp now = new IWTimestamp(IWTimestamp.RightNow());
		now.setMinute(0);
		now.setHour(0);
		now.setSecond(0);
		now.setMilliSecond(0);
		while (toStamp.isLaterThan( stamp)) {
			if (stamp.isLaterThanOrEquals(now)) {
				returner.add(new IWTimestamp(stamp));
			}
			stamp.addDays(1);
		}
		if (toStamp.isLaterThanOrEquals(now)) {
			returner.add(toStamp);
		}


    return returner;
	}

	public int getMaxBookings(Product product, IWTimestamp stamp) throws RemoteException, FinderException {
		Hotel hotel = ((HotelHome) IDOLookup.getHome(Hotel.class)).findByPrimaryKey(product.getPrimaryKey());
		int returner = hotel.getNumberOfUnits();
		if (returner > 0) {
			return returner;
		}else {
			return super.getMaxBookings(product, stamp);
		}
	}

}
