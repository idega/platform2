package is.idega.idegaweb.travel.service.business;

import is.idega.idegaweb.travel.service.carrental.business.CarRentalBusiness;
import is.idega.idegaweb.travel.service.carrental.presentation.*;
import is.idega.idegaweb.travel.service.fishing.business.FishingBusiness;
import is.idega.idegaweb.travel.service.fishing.presentation.*;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.presentation.*;
import com.idega.presentation.text.Link;
import com.idega.util.*;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.*;
import is.idega.idegaweb.travel.service.hotel.business.HotelBusiness;
import is.idega.idegaweb.travel.service.hotel.presentation.*;
import is.idega.idegaweb.travel.service.presentation.*;
import is.idega.idegaweb.travel.service.presentation.ServiceOverview;
import is.idega.idegaweb.travel.service.presentation.BookingOverview;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;
import is.idega.idegaweb.travel.service.tour.presentation.*;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceHandlerBean extends IBOServiceBean implements ServiceHandler {

  public ServiceHandlerBean() {
  }

  public BookingForm getBookingForm(IWContext iwc, Product product) throws Exception{
    Collection coll = getProductCategoryFactory().getProductCategory(product);
    Iterator iter = coll.iterator();
    /**
     * Only supports Products with ONE ProductCategory
     */
    if (iter.hasNext()) {
      ProductCategory pCat = (ProductCategory) iter.next();
      String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
      if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
//        System.out.println("Returning bookingform for TOUR");
        return new TourBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//        System.out.println("Returning bookingform for HOTEL");
        return new HotelBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//        System.out.println("Returning bookingform for FISHING");
        return new FishingBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//        System.out.println("Cannot find BookingForm for ProductCategory PRODUCT, returning form for TOUR");
        return new TourBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//        System.out.println("Returning bookingform for CAR RENTAL");
        return new CarRentalBookingForm(iwc, product);
      }
    }else {
//      System.out.println("[ServiceHandlerBean] iter.hasNext() = false");
    }
    return new TourBookingForm(iwc, product);
  }

  public DesignerForm getDesignerForm(IWContext iwc, ProductCategory productCategory) throws Exception{
    String categoryType = getProductCategoryFactory().getProductCategoryType(productCategory);

    if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
//      System.out.println("Returning form for TOUR");
      return new TourDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//      System.out.println("Returning form for HOTEL");
      return new HotelDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//      System.out.println("Returning form for FISHING");
      return new FishingDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//      System.out.println("Cannot find DesignerForm for ProductCategory PRODUCT, returning form for TOUR");
      return new TourDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//      System.out.println("Returning form for CAR RENTAL");
      return new CarRentalDesigner(iwc);
    }
    return new TourDesigner(iwc);
  }

  public ServiceOverview getServiceOverview(IWContext iwc, Product product) throws RemoteException, FinderException{
    Collection coll = getProductCategoryFactory().getProductCategory(product);
    Iterator iter = coll.iterator();
    /**
     * Only supports Products with ONE ProductCategory
     */
    if (iter.hasNext()) {
      ProductCategory pCat = (ProductCategory) iter.next();
      String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
      if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
//        System.out.println("Returning ServiceOverview for TOUR");
        return new TourOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//        System.out.println("Returning ServiceOverview for HOTEL");
        return new HotelOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//        System.out.println("Returning ServiceOverview for FISHING");
        return new FishingOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//        System.out.println("Cannot find ServiceOverview for ProductCategory PRODUCT, returning form for TOUR");
        return new TourOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//        System.out.println("Returning ServiceOverview for CAR RENTAL");
        return new CarRentalOverview(iwc);
      }
    }

    TourOverview tOverview = new TourOverview(iwc);
    return tOverview;
  }

  public BookingOverview getBookingOverview(IWContext iwc, Product product) throws RemoteException, FinderException{
    if (product != null) {

      Collection coll = getProductCategoryFactory().getProductCategory(product);
      Iterator iter = coll.iterator();
      /**
       * Only supports Products with ONE ProductCategory
       */
      if (iter.hasNext()) {
        ProductCategory pCat = (ProductCategory) iter.next();
        String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
        if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
  //        System.out.println("Returning BookingOverview for TOUR");
          return new TourBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//          System.out.println("Returning BookingOverview for HOTEL");
          return new HotelBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//          System.out.println("Returning BookingOverview for FISHING");
          return new FishingBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//          System.out.println("Cannot find BookingOverview for ProductCategory PRODUCT, returning form for TOUR");
          return new TourBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//          System.out.println("Returning BookingOverview for CAR RENTAL");
          return new CarRentalBookingOverview(iwc);
        }
      }
      TourBookingOverview tbOverview = new TourBookingOverview(iwc);
      return tbOverview;
    }else {
      return new AbstractBookingOverview(iwc);
    }

  }

  public Voucher getVoucher(Booking booking) throws Exception {
    Product product = ((ProductHome) IDOLookup.getHome(Product.class)).findByPrimaryKey(new Integer(booking.getServiceID()));
    Collection coll = getProductCategoryFactory().getProductCategory(product);
    Iterator iter = coll.iterator();


    if (iter.hasNext()) {
      ProductCategory pCat = (ProductCategory) iter.next();
      String type = getProductCategoryFactory().getProductCategoryType(pCat);
      if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
//        System.out.println("Returning voucher for TOUR");
        return new TourVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//        System.out.println("Returning voucher for HOTEL");
        return new HotelVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//        System.out.println("Returning voucher for FISHING");
        return new FishingVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//        System.out.println("Cannot find Voucher for ProductCategory PRODUCT, returning voucher for TOUR");
        return new TourVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//        System.out.println("Returning voucher for CAR RENTAL");
        return new CarRentalVoucher(booking);
      }
    }else {
//      System.out.println("Returning voucher for TOUR, because iter.hasNext() = false");
    }

    return new TourVoucher(booking);
  }

	public TravelStockroomBusiness getServiceBusiness(Product product) throws RemoteException, FinderException {
    if (product != null) {

      Collection coll = getProductCategoryFactory().getProductCategory(product);
      Iterator iter = coll.iterator();
      /**
       * Only supports Products with ONE ProductCategory
       */
      if (iter.hasNext()) {
        ProductCategory pCat = (ProductCategory) iter.next();
        String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
        if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
//          System.out.println("Returning Business for TOUR");
          return (TourBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TourBusiness.class);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
//          System.out.println("Returning Business for HOTEL");
          return (HotelBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), HotelBusiness.class);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
//          System.out.println("Returning Business for FISHING");
          return (FishingBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), FishingBusiness.class);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
//          System.out.println("Returning Business for PRODUCT");
          return (TravelStockroomBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TravelStockroomBusiness.class);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
//          System.out.println("Returning Business for CAR RENTAL");
          return (CarRentalBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), CarRentalBusiness.class);
        }
      }
    }
	  return (TravelStockroomBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TravelStockroomBusiness.class);
	}

  private ProductCategoryFactory getProductCategoryFactory() throws RemoteException{
    return (ProductCategoryFactory) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductCategoryFactory.class);
  }


  public IWTimestamp getDepartureTime(Product product) throws SQLException, RemoteException {
    return getDepartureTime(product.getID());
 }

  public IWTimestamp getDepartureTime(int productId) throws SQLException {
    /** @todo FIXA STRAX !!! */
    try {
      Service service = ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(productId));
      IWTimestamp tempStamp = new IWTimestamp(service.getDepartureTime());
      return tempStamp;
    }catch (Exception e) {
      throw new SQLException(e.getMessage());
    }
  }

  public void removeProductApplication(IWContext iwc, int supplierId) throws RemoteException{
    getProductBusiness().clearProductCache(supplierId);
    iwc.getIWMainApplication().getIWCacheManager().invalidateCache(ServiceViewer.CACHE_KEY+""+supplierId);
  }

	public List getServiceLinks(IWResourceBundle iwrb) {
		List list = new Vector();
		list.add(new Link(iwrb.getLocalizedImageButton("travel.hotel_setup","Hotel setup"), HotelSetup.class));
		list.add(new Link(iwrb.getLocalizedImageButton("travel.tour_setup","Tour setup"), TourSetup.class));
		list.add(new Link(iwrb.getLocalizedImageButton("travel.car_rental_setup","CarRental setup"), CarRentalSetup.class));
		return list;	
	}

  public ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }


}
