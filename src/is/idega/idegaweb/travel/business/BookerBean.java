package is.idega.idegaweb.travel.business;

import is.idega.idegaweb.travel.data.BookingEntry;
import is.idega.idegaweb.travel.data.BookingEntryHome;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.GeneralBookingHome;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactory;
import is.idega.idegaweb.travel.service.business.ProductCategoryFactoryBean;
import is.idega.idegaweb.travel.service.business.ServiceHandler;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.trade.data.Currency;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductCategory;
import com.idega.block.trade.stockroom.data.ProductPrice;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Timeframe;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.block.trade.stockroom.data.TravelAddressBMPBean;
import com.idega.block.trade.stockroom.data.TravelAddressHome;
import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.ui.DropdownMenu;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class BookerBean extends IBOServiceBean implements Booker{

  private  String bookingPriceApplication = "bookingPriceApplication_";
  private  String bookingEntryPriceApplication = "bookingEntryPriceApplication_";

  private GeneralBookingHome gbHome;

  public BookerBean() {
  }

  public  int BookBySupplier(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment, String code) throws RemoteException, CreateException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, Booking.BOOKING_TYPE_ID_SUPPLIER_BOOKING, postalCode, paymentType, userId, ownerId, addressId, comment, code);
  }

  public  int Book(int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, int bookingType, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment, String code) throws RemoteException, CreateException {
    return Book(-1, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, bookingType, postalCode, paymentType, userId, ownerId, addressId, comment, code);
  }

  public  int updateBooking(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, String postalCode, int paymentType, int userId, int ownerId, int addressId, String comment, String code) throws RemoteException, CreateException {
    return Book(bookingId, serviceId, country, name, address, city, telephoneNumber, email, date, totalCount, -1, postalCode, paymentType, userId, ownerId, addressId, comment, code);
  }

  private int Book(int bookingId, int serviceId, String country, String name, String address, String city, String telephoneNumber, String email, IWTimestamp date, int totalCount, int bookingType, String postalCode, int paymentTypeId, int userId, int ownerId, int addressId, String comment, String code) throws RemoteException, CreateException {
    GeneralBooking booking = null;
    int returner = bookingId;
    address = TextSoap.findAndReplace(address, "'", "´");
    name = TextSoap.findAndReplace(name, "'", "´");
    country = TextSoap.findAndReplace(country, "'", "´");
    city = TextSoap.findAndReplace(city, "'", "´");
    if (comment != null) {
      comment = TextSoap.findAndReplace(comment, "'", "´");
    }

    try {
      if (bookingId == -1) {
        booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).create();
          booking.setServiceID(serviceId);
          booking.setAddress(address);
          booking.setBookingDate(date.getTimestamp());
          booking.setBookingTypeID(bookingType);
          booking.setCity(city);
          booking.setCountry(country);
          booking.setDateOfBooking(IWTimestamp.getTimestampRightNow());
          booking.setEmail(email);
          booking.setName(name);
          booking.setPostalCode(postalCode);
          booking.setPaymentTypeId(paymentTypeId);
          booking.setTelephoneNumber(telephoneNumber);
          booking.setTotalCount(totalCount);
          booking.setUserId(userId);
          booking.setOwnerId(ownerId);
          if (comment == null) {
            booking.setComment("");
          }else {
            booking.setComment(comment);
          }
          if (code != null) {
          	booking.setCode(code);
          }
        booking.store();

        returner =  booking.getID();
      }else {
        booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
          booking.setServiceID(serviceId);
          booking.setAddress(address);
          booking.setBookingDate(date.getTimestamp());
          if (bookingType != -1)
          booking.setBookingTypeID(bookingType);
          booking.setCity(city);
          booking.setCountry(country);
          booking.setDateOfBooking(IWTimestamp.getTimestampRightNow());
          booking.setEmail(email);
          booking.setName(name);
          booking.setPostalCode(postalCode);
          booking.setPaymentTypeId(paymentTypeId);
          booking.setTelephoneNumber(telephoneNumber);
          booking.setTotalCount(totalCount);
          booking.setUserId(userId);
          booking.setOwnerId(ownerId);
          if (comment == null) {
            booking.setComment("");
          }else {
            booking.setComment(comment);
          }
					if (code != null) {
					  booking.setCode(code);
					}
        booking.store();

        /** @todo fixa þetta getStaticInstance() crap */
        removeBookingPriceApplication(booking);
      }

      GeneralBooking temp = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(booking.getID()));
  //    temp.removeFrom(TravelAddress.class);
      temp.removeAllTravelAddresses();

      if (addressId > 0) {
        TravelAddressHome taHome = (TravelAddressHome) IDOLookup.getHome(TravelAddress.class);
        TravelAddress tAddress = taHome.findByPrimaryKey(addressId);
        temp.addTravelAddress(tAddress);
      }
  //    temp.addTo(TravelAddress.class, addressId);
    }catch (FinderException fe) {
      throw new CreateException(fe.getMessage());
    }catch (IDORemoveRelationshipException rre) {
      throw new CreateException(rre.getMessage());
    }catch (IDOAddRelationshipException are) {
      throw new CreateException(are.getMessage());
    }

    return returner;
  }

	public boolean setPickup(int bookingId, int pickupPlaceId, String pickupInfo) throws RemoteException{
		try {
			GeneralBooking booking = ((GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
			booking.setPickupPlaceId(pickupPlaceId);
			if (pickupInfo != null) {
				booking.setPickupExtraInfo(pickupInfo);
			}
			booking.store();
			return true;
		} catch (FinderException e) {
			e.printStackTrace(System.err);
		}
		return false;
	}

  public  int getBookingsTotalCountByResellers(int serviceId, IWTimestamp stamp) throws RemoteException {
    return getBookingsTotalCount(-1, serviceId, stamp, null);
  }

  public  int getBookingsTotalCountByResellers(int[] resellerIds, int serviceId, IWTimestamp stamp) throws RemoteException {
    int returner = 0;
    for (int i = 0; i < resellerIds.length; i++) {
      returner += getBookingsTotalCount(resellerIds[i], serviceId, stamp, null);
    }
    return returner;
  }

  public  int getBookingsTotalCountByReseller(int resellerId, int serviceId, IWTimestamp stamp) throws RemoteException{
    return getBookingsTotalCount(resellerId, serviceId, stamp, null);
  }

  public  int getBookingsTotalCountByReseller(int resellerId, int serviceId, IWTimestamp stamp, TravelAddress travelAddress) throws RemoteException{
    return getBookingsTotalCount(resellerId, serviceId, stamp, travelAddress);
  }

  private  int getBookingsTotalCount(int resellerId, int serviceId, IWTimestamp stamp, TravelAddress travelAddress) throws RemoteException{
    if (resellerId != -1) {
      try {
				Reseller reseller = ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(resellerId);
        Iterator iter = reseller.getChildren();
        Product product = getProductBusiness().getProduct(serviceId);
        List items = new Vector();
        Collection coll;
        if (travelAddress != null) {
          coll = ((TravelStockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), TravelStockroomBusiness.class)).getTravelAddressIdsFromRefill(product,travelAddress);
        }
        if (iter == null) {
            int[] tempInts = {reseller.getID()};
            return getGeneralBookingHome().getBookingsTotalCount( tempInts , serviceId, stamp);
        }else {
          if (iter.hasNext()) {
            /**
             * @todo gera recusive fall sem skilar int[] af resellerId-um
             */
            while (iter.hasNext()) {
              items.add(iter.next());
            }
            int[] tempInts = new int[items.size()];
            for (int i = 0; i < tempInts.length; i++) {
              tempInts[i] = ((Reseller) items.get(i)).getID();
            }
						System.out.println("[BookerBean] here...1");
            return getGeneralBookingHome().getBookingsTotalCount( tempInts , serviceId, stamp);
          }else {
            int[] tempInts = {reseller.getID()};
            System.out.println("[BookerBean] here...2");
            return getGeneralBookingHome().getBookingsTotalCount( tempInts , serviceId, stamp);
          }
        }
      }catch (SQLException sql) {
        return 0;
      }catch (FinderException idoFE) {
        idoFE.printStackTrace(System.err);
        return 0;
      }
    }else {
      return getGeneralBookingHome().getBookingsTotalCount(null, serviceId, stamp);
    }
  }

/*
  private int[] getResellerIds(Reseller reseller) {
    List ids = new Vector();
    return getResellerIds(reseller, ids);
  }

  private int[] getResellerIds(Reseller reseller, List ids) {
    int[] returner = {};
    ids.add(reseller);

    Reseller tempReseller;
    Iterator iter = reseller.getChildren();
    while (iter.hasNext()) {
      tempReseller = (Reseller) iter.next();
      getResellerIds(tempReseller, ids);
    }

    return returner;
  }
*/

  public int getBookingsTotalCount(List products, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, Collection travelAddresses) throws RemoteException {
    int counter = 0;
    Iterator iter = products.iterator();
    while (iter.hasNext()) {
      counter += getBookingsTotalCount( ( (Product) iter.next() ).getID(), fromStamp, toStamp, bookingType, travelAddresses);
    }

    return counter;
  }


  public  int getBookingsTotalCount(int serviceId, IWTimestamp stamp, int travelAddressID)throws RemoteException{
  		if (travelAddressID > 0) {
  			try {
	  			//TravelAddress travelAddress = ((TravelAddressHome) IDOLookup.getHome(TravelAddress.class)).findByPrimaryKey(travelAddressID);
	  			Vector vector = new Vector();
	  			vector.add(new Integer(travelAddressID));
		    return getBookingsTotalCount(serviceId, stamp, null, vector);
  			} catch (Exception e) {
  				e.printStackTrace(System.err);
  			}
  		}
	    return getBookingsTotalCount(serviceId, stamp, null, null);
  }

  public  int getBookingsTotalCount(int serviceId, IWTimestamp stamp, int bookingType, Collection travelAddresses) throws RemoteException{
      return getBookingsTotalCount(serviceId, stamp, null, bookingType, travelAddresses);
  }

  public  int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, Collection travelAddresses)throws RemoteException{
      return getBookingsTotalCount(serviceId, fromStamp, toStamp, -1, travelAddresses);
  }

  public  int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, Collection travelAddresses) throws RemoteException{
    return getBookingsTotalCount(serviceId, fromStamp, toStamp, bookingType, travelAddresses, false);//, new int[] {});
  }

  public  int getBookingsTotalCount(int serviceId, IWTimestamp fromStamp, IWTimestamp toStamp, int bookingType, Collection travelAddresses, boolean orderByDateOfBooking) throws RemoteException{
    if (!orderByDateOfBooking) {
      return getGeneralBookingHome().getBookingsTotalCount(serviceId, fromStamp, toStamp, bookingType, new int[] {}, travelAddresses);
    } else {
      return getGeneralBookingHome().getBookingsTotalCountByDateOfBooking(serviceId, fromStamp, toStamp, bookingType, new int[] {}, travelAddresses);
    }
  }

  public  Booking[] getBookings(List products, IWTimestamp stamp) throws RemoteException, FinderException{
    return getBookings(products, stamp, null);
  }
  public  Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp stamp) throws RemoteException, FinderException{
    return getBookings(products, bookingTypeIds, stamp, null, null, null);
  }
  public  Booking[] getBookings(List products, IWTimestamp fromStamp, IWTimestamp toStamp) throws RemoteException, FinderException{
    return getBookings(products, new int[]{}, fromStamp, toStamp, null, null);
  }
  public  Booking[] getBookings(List products, IWTimestamp fromStamp, IWTimestamp toStamp, String columnName, String columnValue) throws RemoteException, FinderException{
    return getBookings(products, new int[]{}, fromStamp, toStamp, columnName, columnValue);
  }

  public  Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp fromStamp, IWTimestamp toStamp, String columnName, String columnValue) throws RemoteException, FinderException {
    return getBookings(products, bookingTypeIds, fromStamp, toStamp, columnName, columnValue, false, true);
  }

  public  Booking[] getBookings(List products, int[] bookingTypeIds, IWTimestamp fromStamp, IWTimestamp toStamp, String columnName, String columnValue, boolean searchByDateOfBooking, boolean validOnly) throws RemoteException, FinderException {
    if (products != null) {
      int[] ids = new int[products.size()];
      Product prod;
      for (int i = 0; i < ids.length; i++) {
        prod = (Product) products.get(i);
        ids[i] = prod.getID();
      }
      if (searchByDateOfBooking) {
        return this.collectionToBookingsArray(getGeneralBookingHome().findBookingsByDateOfBooking(ids, fromStamp, toStamp,bookingTypeIds, columnName, columnValue, null, null, validOnly));
      }else {
        return this.collectionToBookingsArray(getGeneralBookingHome().findBookings(ids, fromStamp, toStamp,bookingTypeIds, columnName, columnValue, null, null, validOnly));
      }
    }
    return new Booking[]{};
  }


  public  Booking[] getBookings(int serviceId, IWTimestamp stamp) throws RemoteException, FinderException{
    return getBookings(serviceId, stamp, new int[]{});
  }

  public  Booking[] getBookings(int serviceId, IWTimestamp stamp, TravelAddress address) throws RemoteException, FinderException{
    return getBookings(new int[]{serviceId},stamp, null, new int[]{}, address);
  }

  public  Booking[] getBookings(int serviceId, IWTimestamp stamp, int bookingTypeId) throws RemoteException, FinderException{
    return getBookings(serviceId,stamp,new int[]{bookingTypeId});
  }

  public  Booking[] getBookings(int serviceId, IWTimestamp stamp, int[] bookingTypeIds) throws RemoteException, FinderException{
    return getBookings(new int[]{serviceId}, stamp, bookingTypeIds);
  }

  public  Booking[] getBookings(int[] serviceIds, IWTimestamp stamp, int[] bookingTypeIds) throws RemoteException, FinderException {
    return getBookings(serviceIds, stamp, null, bookingTypeIds);
  }

  public  Booking[] getBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,int[] bookingTypeIds) throws RemoteException, FinderException{
    return getBookings(serviceIds, fromStamp, toStamp, bookingTypeIds, null);
  }
  public  Booking[] getBookings(int[] serviceIds, IWTimestamp fromStamp, IWTimestamp toStamp,int[] bookingTypeIds, TravelAddress address) throws RemoteException, FinderException{
    return this.collectionToBookingsArray(getGeneralBookingHome().findBookings(serviceIds, fromStamp, toStamp, bookingTypeIds, null, null, address, null, true));
  }


  public  float getBookingEntryPrice(BookingEntry entry, Booking booking) throws RemoteException{
    String applName = bookingEntryPriceApplication+((Integer)entry.getPrimaryKey()).intValue();;
      float total = 0;

      try {
        ProductPrice pPrice;
        Float temp = (Float) getIWApplicationContext().getApplicationAttribute(applName);
        if (temp == null) {
          pPrice = entry.getProductPrice();
          int timeframeId = -1;
          int addressId = -1;
          try {
						Collection tFrames = pPrice.getTimeframes();
						Collection tAddr = pPrice.getTravelAddresses();
						
						if (tFrames != null) {
							Iterator iter = tFrames.iterator();
							if (iter.hasNext()) {
								timeframeId = ((Timeframe) iter.next()).getID();	
							}	
						}
						
						if (tAddr != null) {
							Iterator iter = tAddr.iterator();
							if (iter.hasNext()) {
								addressId = ((TravelAddress) iter.next()).getID();	
							}
						}
					} catch (IDORelationshipException e) {
						e.printStackTrace();
					}
          
          total = entry.getCount() * getTravelStockroomBusiness().getPrice(pPrice.getID(), booking.getServiceID(), pPrice.getPriceCategoryID(), pPrice.getCurrencyId(), booking.getDateOfBooking(), timeframeId, addressId);
          getIWApplicationContext().setApplicationAttribute(applName, new Float(total));
        }else {
          total = temp.floatValue();
        }

      }catch (FinderException fe) {
        fe.printStackTrace(System.err);
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }catch (com.idega.block.trade.stockroom.business.ProductPriceException ppe) {
        ppe.printStackTrace(System.err);
      }
      return total;
  }

  public  float getBookingPrice(List bookings) throws RemoteException, FinderException{
    float price = 0;
    Booking gBook;
    if (bookings != null) {
      for (int i = 0; i < bookings.size(); i++) {
        gBook = (Booking) bookings.get(i);
        price += getBookingPrice(gBook);
      }
    }
    return price;
  }

  public  float getBookingPrice(GeneralBooking[] bookings) throws RemoteException, FinderException{
    float price = 0;
    for (int i = 0; i < bookings.length; i++) {
      price += getBookingPrice((Booking) bookings[i]);
    }
    return price;
  }

  public  float getBookingPrice(Booking[] bookings) throws RemoteException, FinderException{
    float price = 0;
    for (int i = 0; i < bookings.length; i++) {
    		//if (bookings[i].getIsValid()) {
    			price += getBookingPrice(bookings[i]);
    		//} else {
    		//	price -= getBookingPrice(bookings[i]);
    		//}
    }
    return price;
  }

  public  float getBookingPrice(Booking booking) throws RemoteException , FinderException{
      float total = 0;
      String applName = bookingPriceApplication+booking.getID();

      Float temp = (Float) getIWApplicationContext().getApplicationAttribute(applName);
      if (temp == null) {
      	BookingEntryHome beHome = (BookingEntryHome) IDOLookup.getHome(BookingEntry.class);
      	Collection entries = beHome.getEntries(booking);
        //BookingEntry[] entries = booking.getBookingEntries();

        float price;
        ProductPrice pPrice;

				Iterator iter = entries.iterator();
				BookingEntry bEntry;
				while (iter.hasNext()) {
        //for (int i = 0; i < entries.length; i++) {
        	bEntry = beHome.findByPrimaryKey(iter.next()); 
          total += getBookingEntryPrice(bEntry, booking);
        }
        getIWApplicationContext().setApplicationAttribute(applName, new Float(total));
      }else {
        total = temp.floatValue();
      }


      return total;
  }

  public  void removeBookingPriceApplication(Booking booking) throws RemoteException{
    String applName = bookingPriceApplication+booking.getID();
    String applNameEnt = bookingEntryPriceApplication+booking.getID();
    getIWApplicationContext().removeApplicationAttribute(applName);
    getIWApplicationContext().removeApplicationAttribute(applNameEnt);
  }

  public  BookingEntry[] getBookingEntries(Booking booking) throws RemoteException, FinderException{
    return booking.getBookingEntries();
  }

  public  boolean deleteBooking(int bookingId) throws RemoteException, FinderException{
    Booking booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(bookingId));
    return deleteBooking(booking);
  }
  
  public boolean deleteBooking(Booking booking, boolean deleteRelated) throws RemoteException, FinderException {
  		if (deleteRelated) {
  			List list = getMultibleBookings((GeneralBooking) booking);
  			if (list != null) {
  				Iterator iter = list.iterator();
  				while (iter.hasNext()) {
  					GeneralBooking gBook = (GeneralBooking) iter.next();
  					deleteBooking(gBook);
  				}
  			}
			return true;
  		} else {
  			return deleteBooking(booking);
  		}
  }

  public  boolean deleteBooking(Booking booking) throws RemoteException {
      booking.setIsValid(false);
      booking.store();
      return true;
  }

  /**
   * @deprecated
   */
  public  Object getServiceType(int serviceId) {
    Object object;
    try {
      object = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).findByPrimaryKey(new Integer(serviceId));
      return object;
    }catch (Exception e) {
      return null;
    }
  }

  public  DropdownMenu getPaymentTypes(IWResourceBundle iwrb) {
    DropdownMenu menu = getPaymentTypeDropdown(iwrb, "payment_type");
      menu.setMarkupAttribute("style","font-family: Verdana; font-size: 8pt; border: 1 solid #000000");
    return menu;
  }

  public  DropdownMenu getPaymentTypeDropdown(IWResourceBundle iwrb, String name) {
    DropdownMenu menu = new DropdownMenu(name);
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_NO_PAYMENT, getPaymentType(iwrb, Booking.PAYMENT_TYPE_ID_NO_PAYMENT));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CASH, getPaymentType(iwrb, Booking.PAYMENT_TYPE_ID_CASH));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_CREDIT_CARD ,getPaymentType(iwrb, Booking.PAYMENT_TYPE_ID_CREDIT_CARD));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_VOUCHER ,getPaymentType(iwrb, Booking.PAYMENT_TYPE_ID_VOUCHER));
      menu.addMenuElement(Booking.PAYMENT_TYPE_ID_ACCOUNT, getPaymentType(iwrb, Booking.PAYMENT_TYPE_ID_ACCOUNT));
    return menu;
  }
  
  public String getPaymentType(IWResourceBundle iwrb, int paymentType) {
  	switch (paymentType) {
  		case Booking.PAYMENT_TYPE_ID_NO_PAYMENT : return iwrb.getLocalizedString("travel.unpaid","Unpaid");
			case Booking.PAYMENT_TYPE_ID_CASH : return iwrb.getLocalizedString("travel.cash","Cash");
			case Booking.PAYMENT_TYPE_ID_CREDIT_CARD : return iwrb.getLocalizedString("travel.credit_card","Credit card");
			case Booking.PAYMENT_TYPE_ID_VOUCHER : return iwrb.getLocalizedString("travel.voucher","Voucher");
			case Booking.PAYMENT_TYPE_ID_ACCOUNT : return iwrb.getLocalizedString("travel.account","Account");
  		default : return iwrb.getLocalizedString("travel.unspecified","Unspecified");	
  	}
  }

  public  Currency getCurrency(Booking booking) throws SQLException, FinderException, RemoteException {
      Currency currency = null;
      BookingEntry[] entries = booking.getBookingEntries();

      if (entries.length > 0)
        currency = ((com.idega.block.trade.data.CurrencyHome)com.idega.data.IDOLookup.getHomeLegacy(Currency.class)).findByPrimaryKeyLegacy(entries[0].getProductPrice().getCurrencyId());

      return currency;
  }
  
  public TravelAddress getDepartureAddress(Booking _booking) throws RemoteException, FinderException {
  	
	  if (_booking != null) {
			Collection coll;
			try {
				coll = _booking.getTravelAddresses();
				if (coll != null) {
					boolean cont = true;
					TravelAddress ta;
					Iterator iter = coll.iterator();
					while (iter.hasNext() && cont) {
						ta = (TravelAddress) iter.next();
							if (ta.getAddressType() == TravelAddressBMPBean.ADDRESS_TYPE_DEPARTURE) {
								return ta;
							}
					}
				}
			} catch (IDORelationshipException e) {
				e.printStackTrace();
			} 
	  }
  	return null;	  	
  
	}
/*
  public  List getMultibleBookings(Booking booking) {
    List list = new Vector();
    try {
      getMultibleBookings(((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHomeLegacy(GeneralBooking.class)).findByPrimaryKeyLegacy(booking.getID()));
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return list;
  }*/

  public  List getMultibleBookings(GeneralBooking booking) throws RemoteException, FinderException{
    Collection bookingColl = getGeneralBookingHome().getMultibleBookings(booking);
    List list = new Vector();
    int numberOfDays = 1;
    Collection coll = getProductCategoryFactory().getProductCategory(booking.getService().getProduct());
    Iterator iter = coll.iterator();
    if (iter.hasNext()) {
      ProductCategory pCat = (ProductCategory) iter.next();
      /**
       * @todo Kannski bara laga thetta crap her...
       */
      if (ProductCategoryFactoryBean.CATEGORY_TYPE_TOUR.equals(pCat.getCategoryType()) ) {
        numberOfDays = ((is.idega.idegaweb.travel.service.tour.data.TourHome)com.idega.data.IDOLookup.getHome(Tour.class)).findByPrimaryKey(new Integer(booking.getServiceID())).getNumberOfDays();
        if (numberOfDays < 1){
          numberOfDays = 1;
        }
      }

    }
    
		if (bookingColl == null) {
			bookingColl = new Vector();
		}
		Iterator bookingIter = bookingColl.iterator();
		while ( bookingIter.hasNext()) {
			list.add( getGeneralBookingHome().findByPrimaryKey(bookingIter.next()) );
		}

    if (bookingColl.size() < 2) {
      return list;
    }else {
    	List temp = new Vector(bookingColl);
      int myIndex = temp.indexOf(booking.getPrimaryKey());
      list = cleanList(list, booking, myIndex, numberOfDays);
      if (list.size() == 0) {
        list.add(booking);
      }
    }

    return list;
  }

  private  List cleanList(List list, Booking booking, int mainIndex, int numberOfDays) throws RemoteException {
    Booking book;
    int betw = 1;
    int index = mainIndex;
    boolean cont = true;

    IWTimestamp tempStamp = new IWTimestamp(booking.getBookingDate());

    if (mainIndex == 0) {
      while (cont) {
        ++index;
        book = (Booking) list.get(index);
        betw = IWTimestamp.getDaysBetween(tempStamp, new IWTimestamp(book.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(mainIndex, index-1);
          cont = false;
        }
        if (index == list.size()-1) cont = false;
        tempStamp = new IWTimestamp(book.getBookingDate());
      }
    }else if (mainIndex == list.size() -1) {
      while (cont) {
        --index;
        book = (Booking) list.get(index);
        betw = IWTimestamp.getDaysBetween(new IWTimestamp(book.getBookingDate()), tempStamp);
        if (betw != numberOfDays) {
          list = list.subList(index+1, mainIndex);
          cont = false;
        }
        if (index == 0) cont = false;
        tempStamp = new IWTimestamp(book.getBookingDate());
      }
    }else {
      while (cont) {
        --index;
        book = (Booking) list.get(index);
        betw = IWTimestamp.getDaysBetween(new IWTimestamp(book.getBookingDate()), tempStamp);
        if (betw != numberOfDays) {
          list = list.subList(index+1, list.size()-1);
          cont = false;
        }

        if (index == 0) cont = false;
        tempStamp = new IWTimestamp(book.getBookingDate());
      }

      index = 0;
      if (list.size() == 1 ) {
        cont=false;
      }else {
        tempStamp = new IWTimestamp(((Booking) list.get(index)).getBookingDate());
        cont=true;
      }

      while (cont) {
        ++index;
        book = (Booking) list.get(index);
        betw = IWTimestamp.getDaysBetween(tempStamp, new IWTimestamp(book.getBookingDate()));
        if (betw != numberOfDays) {
          list = list.subList(0, index-1);
          cont = false;
        }
        if (index == list.size()-1) cont = false;
        tempStamp = new IWTimestamp(book.getBookingDate());
      }
    }

    return list;
  }



  public  int getAvailableItems(ProductPrice pPrice, IWTimestamp stamp) {
    return -1;
  }


  /**
   * returns int[], int[0] is number of current booking, int[1] is total bookings number, int[2] is the id of the first one
   */
  public  int[] getMultipleBookingNumber(GeneralBooking booking) throws RemoteException, FinderException{
    List list = getMultibleBookings(booking);
    Booking firstBooking;
    int firstBookingId = -1;
    int[] returner = new  int[3];
    if (list == null || list.size() < 2 ) {
      returner[0] = 0;
      returner[1] = 0;
      returner[2] = 0;
    }else {
      returner[0] = list.indexOf(booking) + 1;
      returner[1] = list.size();
      returner[2] = ((Booking)list.get(0)).getID();
    }
    return returner;
  }



  public  int getBookingsTotalCount(ProductPrice pPrice) throws RemoteException {
    try {

      BookingEntryHome beHome = (BookingEntryHome) IDOLookup.getHome(BookingEntry.class);
      BookingEntry entry = beHome.create();
      Collection entries = entry.getEntries(pPrice);
      Iterator iter = entries.iterator();
      int returner = 0;

      while (iter.hasNext()) {
        entry = (BookingEntry) iter.next();
        returner += entry.getCount();
      }

      return returner;

    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }
    return -1;
  }

  private TravelStockroomBusiness getTravelStockroomBusiness() throws RemoteException{
    return (TravelStockroomBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), TravelStockroomBusiness.class);
  }

  public GeneralBookingHome getGeneralBookingHome() throws RemoteException {
    if (this.gbHome == null) {
      gbHome = (GeneralBookingHome) IDOLookup.getHome(GeneralBooking.class);
    }
    return gbHome;
  }

  public Booking[] collectionToBookingsArray(Collection coll) {
    return (Booking[]) coll.toArray(new Booking[]{});
  }

  public ServiceHandler getServiceHandler() throws RemoteException{
    return (ServiceHandler) IBOLookup.getServiceInstance(getIWApplicationContext(), ServiceHandler.class);
  }

  public ProductCategoryFactory getProductCategoryFactory() throws RemoteException {
    return (ProductCategoryFactory) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductCategoryFactory.class);
  }

  public ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }

}
