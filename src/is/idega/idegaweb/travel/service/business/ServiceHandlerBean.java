package is.idega.idegaweb.travel.service.business;

import is.idega.idegaweb.travel.business.TravelStockroomBusiness;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.ServiceViewer;
import is.idega.idegaweb.travel.presentation.Voucher;
import is.idega.idegaweb.travel.service.carrental.business.CarRentalBusiness;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalBookingForm;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalBookingOverview;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalDesigner;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalOverview;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalSetup;
import is.idega.idegaweb.travel.service.carrental.presentation.CarRentalVoucher;
import is.idega.idegaweb.travel.service.fishing.business.FishingBusiness;
import is.idega.idegaweb.travel.service.fishing.presentation.FishingBookingForm;
import is.idega.idegaweb.travel.service.fishing.presentation.FishingBookingOverview;
import is.idega.idegaweb.travel.service.fishing.presentation.FishingDesigner;
import is.idega.idegaweb.travel.service.fishing.presentation.FishingOverview;
import is.idega.idegaweb.travel.service.fishing.presentation.FishingVoucher;
import is.idega.idegaweb.travel.service.hotel.business.HotelBusiness;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelBookingForm;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelBookingOverview;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelDesigner;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelOverview;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelSetup;
import is.idega.idegaweb.travel.service.hotel.presentation.HotelVoucher;
import is.idega.idegaweb.travel.service.presentation.AbstractBookingOverview;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.presentation.BookingOverview;
import is.idega.idegaweb.travel.service.presentation.DefaultBookingForm;
import is.idega.idegaweb.travel.service.presentation.DesignerForm;
import is.idega.idegaweb.travel.service.presentation.ServiceOverview;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;
import is.idega.idegaweb.travel.service.tour.presentation.TourBookingForm;
import is.idega.idegaweb.travel.service.tour.presentation.TourBookingOverview;
import is.idega.idegaweb.travel.service.tour.presentation.TourDesigner;
import is.idega.idegaweb.travel.service.tour.presentation.TourOverview;
import is.idega.idegaweb.travel.service.tour.presentation.TourSetup;
import is.idega.idegaweb.travel.service.tour.presentation.TourVoucher;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.text.Link;
import com.idega.util.IWTimestamp;

/**
 * <p>Title: idega</p>
 * <p>Description: software</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: idega software</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ServiceHandlerBean extends IBOServiceBean implements ServiceHandler {

	private HashMap inits = new HashMap();
	private HashMap unInits = new HashMap();
	private HashMap serviceOverviews = new HashMap();
	private HashMap businesses = new HashMap();
	
  public ServiceHandlerBean() {
  }

  public BookingForm getBookingForm(IWContext iwc, Product product) throws Exception{
	  return getBookingForm(iwc, product, true);
  }
  public BookingForm getBookingForm(IWContext iwc, Product product, boolean initializeBookingForm) throws Exception{
  	if (product == null) {
  		return new DefaultBookingForm(iwc, product);
  	}
  	
  	BookingForm bf = getBookingForm(product, initializeBookingForm);
  	if (bf == null) {
	    Collection coll = getProductCategoryFactory().getProductCategory(product);
	    Iterator iter = coll.iterator();
	    /**
	     * Only supports Products with ONE ProductCategory
	     */
	    if (iter.hasNext()) {
	      ProductCategory pCat = (ProductCategory) iter.next();
	      String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
	      if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
	        bf = setBookingForm(product, new TourBookingForm(iwc, product, initializeBookingForm), initializeBookingForm);
	      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
	    	  bf = setBookingForm(product, new HotelBookingForm(iwc, product, initializeBookingForm), initializeBookingForm);
	      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
	    	  bf = setBookingForm(product, new FishingBookingForm(iwc, product, initializeBookingForm), initializeBookingForm);
	      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
	    	  bf = setBookingForm(product, new TourBookingForm(iwc, product, initializeBookingForm), initializeBookingForm);
	      }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
	    	  bf = setBookingForm(product, new CarRentalBookingForm(iwc, product,initializeBookingForm), initializeBookingForm);
	      }
	    } else {
	    	return new DefaultBookingForm(iwc, product, initializeBookingForm);
	    }
  	}
	return bf;
  }
  
  private BookingForm setBookingForm(Product product, BookingForm bf, boolean init ) {
//	  System.out.println("[ServiceHandler] Setting BookingForm for product = "+product.getPrimaryKey().toString()+",  ... "+init);
	  
	  if (init) {
		  // Cant be done this way... figure something else out
//		  inits.put(product.getPrimaryKey(), bf);
	  } else {
		  unInits.put(product.getPrimaryKey(), bf);
	  }
	  return bf;
  }
  
  private BookingForm getBookingForm(Product product, boolean init) {
//	  System.out.println("[ServiceHandler] Getting BookingForm for product = "+product.getPrimaryKey().toString()+",  ... "+init);
	  if (init){
		  return (BookingForm) inits.get(product.getPrimaryKey());
	  } else {
		  return (BookingForm) unInits.get(product.getPrimaryKey());
	  }
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
	  ServiceOverview so = (ServiceOverview) serviceOverviews.get(product.getPrimaryKey());
	  
	  if (so == null) {
		  Collection coll = getProductCategoryFactory().getProductCategory(product);
		  Iterator iter = coll.iterator();
		  /**
		   * Only supports Products with ONE ProductCategory
		   */
		  if (iter.hasNext()) {
			  ProductCategory pCat = (ProductCategory) iter.next();
			  String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
			  if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
				  so = new TourOverview(iwc);
			  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
				  so =  new HotelOverview(iwc);
			  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
				  so =  new FishingOverview(iwc);
			  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
				  so =  new TourOverview(iwc);
			  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
				  so =  new CarRentalOverview(iwc);
			  }
		  }
		  serviceOverviews.put(product.getPrimaryKey(), so);
	  }
	  
	  return so;
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
          return new TourBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
          return new HotelBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
          return new FishingBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
          return new TourBookingOverview(iwc);
        }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
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
        return new TourVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
        return new HotelVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
        return new FishingVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
        return new TourVoucher(booking);
      }else if (type.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
        return new CarRentalVoucher(booking);
      }
    }else {
//      System.out.println("Returning voucher for TOUR, because iter.hasNext() = false");
    }

    return new TourVoucher(booking);
  }

  public TravelStockroomBusiness getServiceBusiness(Product product) throws RemoteException, FinderException {
	  if (product != null) {
		  TravelStockroomBusiness bus = (TravelStockroomBusiness) businesses.get(product.getPrimaryKey());
		  if (bus == null) {
			  Collection coll = getProductCategoryFactory().getProductCategory(product);
			  Iterator iter = coll.iterator();
			  /**
			   * Only supports Products with ONE ProductCategory
			   */
			  if (iter.hasNext()) {
				  ProductCategory pCat = (ProductCategory) iter.next();
				  String categoryType = getProductCategoryFactory().getProductCategoryType(pCat);
				  if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR)) {
					  bus = (TourBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TourBusiness.class);
				  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_HOTEL)) {
					  bus = (HotelBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), HotelBusiness.class);
				  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_FISHING)) {
					  bus = (FishingBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), FishingBusiness.class);
				  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_PRODUCT)) {
					  bus = (TravelStockroomBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TravelStockroomBusiness.class);
				  }else if (categoryType.equals(ProductCategoryFactoryBean.CATEGORY_TYPE_CAR_RENTAL)) {
					  bus = (CarRentalBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), CarRentalBusiness.class);
				  }
			  }
			  businesses.put(product.getPrimaryKey(), bus);
		  }
		  return bus;
	  } else {
		  TravelStockroomBusiness bus = (TravelStockroomBusiness) businesses.get(new Integer(0));
		  if (bus == null) {
			  bus = (TravelStockroomBusiness) IBOLookup.getServiceInstance( getIWApplicationContext(), TravelStockroomBusiness.class);
			  businesses.put(new Integer(0), bus);
		  }
		  return bus;
	  }
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
      if (service.getDepartureTime() != null) {
	      IWTimestamp tempStamp = new IWTimestamp(service.getDepartureTime());
	      return tempStamp;
      }
      
    }catch (Exception e) {
    	e.printStackTrace();
      throw new SQLException(e.getMessage());
    }
    return null;
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
