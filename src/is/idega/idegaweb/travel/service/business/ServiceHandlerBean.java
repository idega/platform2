package is.idega.idegaweb.travel.service.business;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.business.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.presentation.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.*;
import is.idega.idegaweb.travel.service.hotel.presentation.*;
import is.idega.idegaweb.travel.service.presentation.*;
import is.idega.idegaweb.travel.service.presentation.ServiceOverview;
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
        System.out.println("Returning form for TOUR");
        return new TourBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
        System.out.println("Returning form for HOTEL");
        return new HotelBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
        System.out.println("Cannot find BookingForm for ProductCategory FISHING, returning form for TOUR");
        return new TourBookingForm(iwc, product);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
        System.out.println("Cannot find BookingForm for ProductCategory PRODUCT, returning form for TOUR");
        return new TourBookingForm(iwc, product);
      }
    }else {
      System.out.println("[ServiceHandlerBean] iter.hasNext() = false");
    }
    return new TourBookingForm(iwc, product);
  }

  public DesignerForm getDesignerForm(IWContext iwc, ProductCategory productCategory) throws Exception{
    String categoryType = getProductCategoryFactory().getProductCategoryType(productCategory);

    if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
      System.out.println("Returning form for TOUR");
      return new TourDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
      System.out.println("Returning form for HOTEL");
      return new HotelDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
      System.out.println("Cannot find DesignerForm for ProductCategory FISHING, returning form for TOUR");
      return new TourDesigner(iwc);
    }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
      System.out.println("Cannot find DesignerForm for ProductCategory PRODUCT, returning form for TOUR");
      return new TourDesigner(iwc);
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
        System.out.println("Returning ServiceOverview for TOUR");
        return new TourOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
        System.out.println("Cannot find ServiceOverview for ProductCategory HOTEL, returning form for TOUR");
        return new TourOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
        System.out.println("Cannot find ServiceOverview for ProductCategory FISHING, returning form for TOUR");
        return new TourOverview(iwc);
      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
        System.out.println("Cannot find ServiceOverview for ProductCategory PRODUCT, returning form for TOUR");
        return new TourOverview(iwc);
      }
    }

    TourOverview tOverview = new TourOverview(iwc);

    return tOverview;
  }

  public Voucher getVoucher(Booking booking) throws Exception {
    int productId = booking.getServiceID();
    ProductCategoryFactory pcFact = (ProductCategoryFactory) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductCategoryFactory.class);
    Collection coll = pcFact.getProductCategory(getProductBusiness().getProduct(productId));
    if (coll != null) {
      Iterator iter = coll.iterator();
      if (iter.hasNext()) {
        ProductCategory pCat = (ProductCategory) iter.next();
        String type = pCat.getCategoryType();
        if (type == null) {
          type = ProductCategoryFactoryBean.CATEGORY_TYPE_DEFAULT;
        }

        if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
          System.out.println("Returning voucher for TOUR");
          return new TourVoucher(booking);
        }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
          System.out.println("Returning voucher for HOTEL");
          return new HotelVoucher(booking);
        }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
          System.out.println("Cannot find Voucher for ProductCategory FISHING, returning voucher for TOUR");
          return new TourVoucher(booking);
        }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
          System.out.println("Cannot find Voucher for ProductCategory PRODUCT, returning voucher for TOUR");
          return new TourVoucher(booking);
        }
      }else {
        System.out.println("Returning voucher for TOUR, because iter.hasNext() = false");
      }
    }else {
      System.out.println("Returning voucher for TOUR, because coll == null");
    }
    return new TourVoucher(booking);
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
    iwc.getApplication().getIWCacheManager().invalidateCache(ServiceViewer.CACHE_KEY+""+supplierId);
  }

  public ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }


}
