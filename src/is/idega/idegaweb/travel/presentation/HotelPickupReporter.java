package is.idega.idegaweb.travel.presentation;

import is.idega.idegaweb.travel.business.BookingComparator;
import is.idega.idegaweb.travel.data.PickupPlace;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.service.tour.business.TourBooker;
import is.idega.idegaweb.travel.service.tour.data.TourBooking;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Product;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.IWApplicationContext;
import com.idega.idegaweb.IWBundle;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import com.idega.presentation.PresentationObject;
import com.idega.presentation.Table;
import com.idega.presentation.text.Text;
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

public class HotelPickupReporter extends TravelManager implements Report {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  public HotelPickupReporter(IWContext iwc) throws Exception {
    initialize(iwc);
  }

  public boolean useTwoDates() {
    return false;
  }

  public String getReportName() {
    return iwrb.getLocalizedString("travel.report_name.pickup","Pickup");
  }

  public String getReportDescription() {
    return iwrb.getLocalizedString("travel.report_description.pikcup","Displays booking with pickup.");
  }

  private void initialize(IWContext iwc) throws Exception {
    super.main(iwc);
    bundle = super.getBundle();
    iwrb = super.getResourceBundle();
  }
/*
  public Table getHotelPickupReport(IWContext iwc, Supplier supplier, IWTimestamp stamp) {
    List products = ProductBusiness.getProducts(supplier.getID(), stamp);
    return getHotelPickupReport(iwc, products, stamp);
  }

  public Table getHotelPickupReport(IWContext iwc, Product product, IWTimestamp stamp) {
    List list = new Vector();
    list.add(product);
    return getHotelPickupReport(iwc, list, stamp);
  }
*/

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp, IWTimestamp toStamp) {
    /**
     * unsupported
     */
    return new Table();
  }

  public PresentationObject getReport(IWContext iwc, List products, IWTimestamp stamp) throws RemoteException, FinderException{
    Table table = new Table();
      table.setColor(super.WHITE);
      table.setCellspacing(1);
      table.setCellpadding(3);
      table.setWidth("100%");
//      table.setWidth("50%");
      table.setBorder(0);
    int row = 0;
    int bookingCounter = 0;
    int count = 0;
    int totalCount = 0;
    int productCount = 0;
    int hotelCount = 0;
    boolean expand = false;


    Booking[] bookings = {};// = Booker.getBookings(products, stamp);
    List bookingsList = getBookingList(bookings);;
    Booking booking;
    TourBooking tBooking;
    Product prod;
    PickupPlace hpp;
    int oldHppNumber = -100;
    Text hotelCountText = (Text) super.theBoldText.clone();
    Text hotelNameText = (Text) super.theBoldText.clone();
    Text headerCountTxt = new Text();
      headerCountTxt.setFontStyle(super.theBoldTextStyle+";text-decoration: underline");
      headerCountTxt.setText(iwrb.getLocalizedString("travel.count","Count"));
      headerCountTxt.setFontColor(super.WHITE);
    Text headerRoomTxt = new Text();
      headerRoomTxt.setFontStyle(super.theBoldTextStyle+";text-decoration: underline");
      headerRoomTxt.setText(iwrb.getLocalizedString("travel.room","Room"));
      headerRoomTxt.setFontColor(super.WHITE);

    IWTimestamp tempStamp;
    Text productNameTxt;
    Text productTimeTxt;
    Text bookingNameTxt;
    Text bookingCountTxt;
    Text bookingRoomNumberTxt;



    for (int j = 0; j < products.size(); j++) {
      hotelCountText = (Text) super.theBoldText.clone();
        hotelCountText.setText("0");
      oldHppNumber = -100;
      prod = (Product) products.get(j);
      bookings = getTourBooker(iwc).getBookings(prod.getID(), stamp, true);
      bookingsList = getBookingList(bookings);
      Collections.sort(bookingsList, new BookingComparator(iwc, BookingComparator.HOTELPICKUP_NAME));


      try {
        if (bookings.length > 0) {
          ++row;
          tempStamp = getServiceHandler(iwc).getDepartureTime(prod);
          table.mergeCells(1,row,3,row);
          productNameTxt = new Text();
            productNameTxt.setFontStyle(super.theBoldTextStyle+";text-decoration: underline");
            productNameTxt.setText(getProductBusiness(iwc).getProductNameWithNumber(prod, true) );
            productNameTxt.setFontColor(super.WHITE);
          productTimeTxt = new Text();
            productTimeTxt.setFontStyle(super.theBoldTextStyle+";text-decoration: underline");
            productTimeTxt.setText(TextSoap.addZero(tempStamp.getHour())+":"+TextSoap.addZero(tempStamp.getMinute()));
            productTimeTxt.setFontColor(super.WHITE);
          table.add(productNameTxt, 1, row);
          table.add(headerCountTxt, 4, row);
          table.add(headerRoomTxt, 5, row);
          table.add(productTimeTxt, 6, row);
          table.setAlignment(6, row, "right");
          table.setRowColor(row, super.backgroundColor);

            for (int i = 0; i < bookingsList.size(); i++) {
              try {
                booking = (Booking) bookingsList.get(i);

                ++row;
                table.setRowColor(row, super.GRAY);
                tBooking = ((is.idega.idegaweb.travel.service.tour.data.TourBookingHome)com.idega.data.IDOLookup.getHome(TourBooking.class)).findByPrimaryKey(booking.getPrimaryKey());
                ++bookingCounter;
                hpp = tBooking.getPickupPlace();
                if (((Integer) hpp.getPrimaryKey()).intValue() != oldHppNumber) {
                  hotelNameText = (Text) super.theBoldText.clone();
                    hotelNameText.setText(hpp.getName());
                    hotelNameText.setFontColor(super.BLACK);
                  table.add(hotelNameText, 2, row);
                  table.mergeCells(2,row,3,row);

                  hotelCountText = (Text) super.theBoldText.clone();
                    hotelCountText.setText("0");
                    hotelCountText.setFontColor(super.BLACK);
                  table.add(hotelCountText, 4, row);
                  table.setAlignment(5, row, "right");

                  oldHppNumber = ((Integer) hpp.getPrimaryKey()).intValue();
                  bookingCounter = 1;
                  hotelCount = 0;
                  ++row;
                }

                if (tBooking.getPickupExtraInfo() != null) {
                  bookingRoomNumberTxt = (Text) super.smallText.clone();
                    bookingRoomNumberTxt.setFontColor(super.BLACK);
                    bookingRoomNumberTxt.setText(tBooking.getPickupExtraInfo());
                  table.add(bookingRoomNumberTxt, 5, row);
                }else {
                  table.add(Text.NON_BREAKING_SPACE, 5, row);
                }

                table.setRowColor(row, super.GRAY);
                count = booking.getTotalCount();

                totalCount += count;
                hotelCount += count;
                productCount += count;

  //              table.mergeCells(1,row,2,row);
                bookingNameTxt = (Text) super.smallText.clone();
                  bookingNameTxt.setText(booking.getName());
                  bookingNameTxt.setFontColor(super.BLACK);
                bookingCountTxt = (Text) super.smallText.clone();
                  bookingCountTxt.setText(Integer.toString(count));
                  bookingCountTxt.setFontColor(super.BLACK);

                table.add(bookingNameTxt, 3, row);
                table.add(bookingCountTxt, 4, row);

                hotelCountText.setText(""+hotelCount);
              }catch (FinderException fe) {
                debug(fe.getMessage());
              }
            }
          if (expand) {
            table.add("EXPAND");
          }
        }
      }catch (SQLException sql) {
        sql.printStackTrace(System.err);
      }
    }

    table.setColumnAlignment(4, "right");
    table.setColumnAlignment(5, "right");

    return table;
  }

  private List getBookingList(Booking[] bookings) {
    List list = new Vector(bookings.length);
    for (int i = 0; i < bookings.length; i++) {
      list.add(bookings[i]);
    }
    return list;
  }

  private TourBooker getTourBooker(IWApplicationContext iwac) throws RemoteException {
    return (TourBooker) IBOLookup.getServiceInstance(iwac, TourBooker.class);
  }

}
