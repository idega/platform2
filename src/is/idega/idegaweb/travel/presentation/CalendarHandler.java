package is.idega.idegaweb.travel.presentation;


import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.calendar.presentation.SmallCalendar;
import com.idega.util.*;
import java.util.*;
import java.sql.SQLException;

import com.idega.block.trade.stockroom.business.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;

/**
 * Title:        idegaWeb Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href mailto:"gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class CalendarHandler extends TravelManager {
  private IWResourceBundle iwrb;
  private Supplier _supplier;

  private String colorForAvailableDay = TravelManager.ORANGE;
  private String colorForAvailableDayText = TravelManager.backgroundColor;
  private String colorForInquery = TravelManager.YELLOW;
  private String colorForFullyBooked = TravelManager.RED;
  private String colorForFullyBookedText = TravelManager.WHITE;
  private String colorForToday = "#71CBFB";

  private idegaCalendar cal = new idegaCalendar();
  private SmallCalendar sm = new SmallCalendar();

  private Product _product;
  private Service _service;
  private Tour _tour;
  private Timeframe _timeframe;
  private Contract _contract;
  private Reseller _reseller;
  private idegaTimestamp _fromStamp;
  private idegaTimestamp _toStamp;

  private int _productId;
  private int _resellerId;

  public CalendarHandler(IWContext iwc) throws Exception{
    super.main(iwc);
    iwrb = super.getResourceBundle();
    _supplier = super.getSupplier();
    //handleInsert(iwc, supplier);
    buildCalendar();
    setTimestampsAuto();
  }

  private void buildCalendar() {
    sm = new SmallCalendar();
      sm.T.setBorder(1);
      sm.T.setCellpadding(2);
      sm.T.setBorderColor(super.backgroundColor);
      sm.useNextAndPreviousLinks(true);
      sm.setBackgroundColor(super.backgroundColor);
      sm.setTextColor("WHITE");
      sm.setDaysAsLink(true);
      sm.showNameOfDays(true);
      sm.setHeaderTextColor(super.textColor);
      sm.setDayTextColor(super.textColor);
      sm.setHeaderColor(super.backgroundColor);
      sm.setDayCellColor(super.backgroundColor);
      sm.setBodyColor("#8484D6");
      sm.setInActiveCellColor("#666666");
      sm.setColorToday(colorForToday);
      sm.setSelectedHighlighted(false);

      sm.setDayFontColor(idegaTimestamp.RightNow(),super.backgroundColor);
  }

  private void timeframeCheck() {
    if (_service != null) {
      try {
        if (_timeframe == null) {
          _timeframe = _service.getTimeframe();
        }
        if (new idegaTimestamp(_timeframe.getFrom()).isLaterThan(_fromStamp)) {
          _fromStamp = new idegaTimestamp(_timeframe.getFrom());
        }
        if (_toStamp.isLaterThan(new idegaTimestamp(_timeframe.getTo()))) {
          _toStamp = new idegaTimestamp(_timeframe.getTo());
        }
      }catch (SQLException s) {
        s.printStackTrace(System.err);
      }
    }
  }

  public Table getCalendarTable(IWContext iwc) {
    /**
     * @todo Perform check of some sort
     */
    return getCalendarTablePrivate(iwc);
  }

  private Table getCalendarTablePrivate(IWContext iwc) {
      sm.setTimestamp(_fromStamp);
      this.timeframeCheck();


      Table table = new Table(4,6);
          table.setBorder(0);
          table.setColor(TravelManager.backgroundColor);
          table.setAlignment("center");


      Text jan = (Text) theText.clone();
        jan.setFontStyle("text-decoration: none;");
        jan.setText(cal.getShortNameOfMonth(1,iwc).substring(0,3));
      Text feb = (Text) theText.clone();
        feb.setFontStyle("text-decoration: none;");
        feb.setText(cal.getShortNameOfMonth(2,iwc).substring(0,3));
      Text mar = (Text) theText.clone();
        mar.setFontStyle("text-decoration: none;");
        mar.setText(cal.getShortNameOfMonth(3,iwc).substring(0,3));
      Text apr = (Text) theText.clone();
        apr.setFontStyle("text-decoration: none;");
        apr.setText(cal.getShortNameOfMonth(4,iwc).substring(0,3));
      Text may = (Text) theText.clone();
        may.setFontStyle("text-decoration: none;");
        may.setText(cal.getShortNameOfMonth(5,iwc).substring(0,3));
      Text jun = (Text) theText.clone();
        jun.setFontStyle("text-decoration: none;");
        jun.setText(cal.getShortNameOfMonth(6,iwc).substring(0,3));
      Text jul = (Text) theText.clone();
        jul.setFontStyle("text-decoration: none;");
        jul.setText(cal.getShortNameOfMonth(7,iwc).substring(0,3));
      Text aug = (Text) theText.clone();
        aug.setFontStyle("text-decoration: none;");
        aug.setText(cal.getShortNameOfMonth(8,iwc).substring(0,3));
      Text sep = (Text) theText.clone();
        sep.setFontStyle("text-decoration: none;");
        sep.setText(cal.getShortNameOfMonth(9,iwc).substring(0,3));
      Text oct = (Text) theText.clone();
        oct.setFontStyle("text-decoration: none;");
        oct.setText(cal.getShortNameOfMonth(10,iwc).substring(0,3));
      Text nov = (Text) theText.clone();
        nov.setFontStyle("text-decoration: none;");
        nov.setText(cal.getShortNameOfMonth(11,iwc).substring(0,3));
      Text dec = (Text) theText.clone();
        dec.setFontStyle("text-decoration: none;");
        dec.setText(cal.getShortNameOfMonth(12,iwc).substring(0,3));

      Link lJan = new Link(jan,Booking.class);
        lJan.setBold();
        lJan.addParameter("year",_fromStamp.getYear());
        lJan.addParameter("month",1);
        lJan.addParameter("day",_fromStamp.getDate());
      Link lFeb = new Link(feb,Booking.class);
        lFeb.setBold();
        lFeb.addParameter("year",_fromStamp.getYear());
        lFeb.addParameter("month",2);
        lFeb.addParameter("day",_fromStamp.getDate());
      Link lMar = new Link(mar,Booking.class);
        lMar.setBold();
        lMar.addParameter("year",_fromStamp.getYear());
        lMar.addParameter("month",3);
        lMar.addParameter("day",_fromStamp.getDate());
      Link lApr = new Link(apr,Booking.class);
        lApr.setBold();
        lApr.addParameter("year",_fromStamp.getYear());
        lApr.addParameter("month",4);
        lApr.addParameter("day",_fromStamp.getDate());
      Link lMay = new Link(may,Booking.class);
        lMay.setBold();
        lMay.addParameter("year",_fromStamp.getYear());
        lMay.addParameter("month",5);
        lMay.addParameter("day",_fromStamp.getDate());
      Link lJun = new Link(jun,Booking.class);
        lJun.setBold();
        lJun.addParameter("year",_fromStamp.getYear());
        lJun.addParameter("month",6);
        lJun.addParameter("day",_fromStamp.getDate());
      Link lJul = new Link(jul,Booking.class);
        lJul.setBold();
        lJul.addParameter("year",_fromStamp.getYear());
        lJul.addParameter("month",7);
        lJul.addParameter("day",_fromStamp.getDate());
      Link lAug = new Link(aug,Booking.class);
        lAug.setBold();
        lAug.addParameter("year",_fromStamp.getYear());
        lAug.addParameter("month",8);
        lAug.addParameter("day",_fromStamp.getDate());
      Link lSep = new Link(sep,Booking.class);
        lSep.setBold();
        lSep.addParameter("year",_fromStamp.getYear());
        lSep.addParameter("month",9);
        lSep.addParameter("day",_fromStamp.getDate());
      Link lOct = new Link(oct,Booking.class);
        lOct.setBold();
        lOct.addParameter("year",_fromStamp.getYear());
        lOct.addParameter("month",10);
        lOct.addParameter("day",_fromStamp.getDate());
      Link lNov = new Link(nov,Booking.class);
        lNov.setBold();
        lNov.addParameter("year",_fromStamp.getYear());
        lNov.addParameter("month",11);
        lNov.addParameter("day",_fromStamp.getDate());
      Link lDec = new Link(dec,Booking.class);
        lDec.setBold();
        lDec.addParameter("year",_fromStamp.getYear());
        lDec.addParameter("month",12);
        lDec.addParameter("day",_fromStamp.getDate());

      table.add(lJan,1,1);
      table.add(lFeb,2,1);
      table.add(lMar,3,1);
      table.add(lApr,4,1);
      table.add(lMay,1,2);
      table.add(lJun,2,2);
      table.add(lJul,3,2);
      table.add(lAug,4,2);
      table.add(lSep,1,3);
      table.add(lOct,2,3);
      table.add(lNov,3,3);
      table.add(lDec,4,3);




      int month = _fromStamp.getMonth();
      int year = _fromStamp.getYear();
      int lengthOfMonth = cal.getLengthOfMonth(month, year);

      idegaTimestamp temp = new idegaTimestamp(1, month , year);
      int iBookings = 0;

      List depDays = new Vector();
      int seats = 0;
      int minSeats = 0;

      if (_tour != null) {
        seats = _tour.getTotalSeats();
        if (_tour.getNumberOfDays() > 1) {
          depDays = TourBusiness.getDepartureDays(iwc, _tour);
        }else {
          depDays = TourBusiness.getDepartureDays(iwc,_tour, _fromStamp, _toStamp);
        }
      }else {
          depDays = TravelStockroomBusiness.getDepartureDays(iwc, _product);
      }



      try {
        if (_contract != null) {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            if (!TravelStockroomBusiness.getIfExpired(_contract, temp))
            if (TravelStockroomBusiness.getIfDay(iwc,_contract,_product,temp)) {
              if (seats > 0 && seats <= Booker.getNumberOfBookings(_productId, temp) ) {
                sm.setDayColor(temp, colorForFullyBooked);
                sm.setDayFontColor(temp, colorForFullyBookedText);
              }else {
                sm.setDayColor(temp, colorForAvailableDay);
                sm.setDayFontColor(temp, colorForAvailableDayText);
              }
            }
          }
        }
        else if (_supplier != null) {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            if (seats > 0 && seats <= Booker.getNumberOfBookings(_productId, temp) ) {
              sm.setDayColor(temp, colorForFullyBooked);
              sm.setDayFontColor(temp, colorForFullyBookedText);
            }else {
              sm.setDayColor(temp, colorForAvailableDay);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
          }
          for (int i = 1; i <= lengthOfMonth; i++) {
            if (Inquirer.getInqueredSeats(_productId, temp, true) > 0) {
              sm.setDayColor(temp, colorForInquery);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
            temp.addDays(1);
          }
        }
        else if (_reseller != null) {
          for (int i = 0; i < depDays.size(); i++) {
            temp = (idegaTimestamp) depDays.get(i);
            iBookings = Booker.getNumberOfBookings(_productId, temp);
            if (seats > 0 && seats <= iBookings ) {
              sm.setDayColor(temp, colorForFullyBooked);
              sm.setDayFontColor(temp, colorForFullyBookedText);
            }else if (iBookings >= minSeats && iBookings <= seats) {
              sm.setDayColor(temp, colorForAvailableDay);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
          }
          for (int i = 1; i <= lengthOfMonth; i++) {
            if (Inquirer.getInqueredSeats(_productId, temp,_resellerId, true) > 0) {
              sm.setDayColor(temp, colorForInquery);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
            temp.addDays(1);
          }
        }
      }catch (TravelStockroomBusiness.ServiceNotFoundException snfe) {
        snfe.printStackTrace(System.err);
      }catch (TravelStockroomBusiness.TimeframeNotFoundException tfnfe) {
        tfnfe.printStackTrace(System.err);
      }

      Table legend = new Table();
        legend.setBorder(0);
        legend.setCellpadding(0);
        legend.setCellspacing(2);

        Text avail = (Text) theText.clone();
          avail.setText(iwrb.getLocalizedString("travel.colorForAvailableDay","Available"));
        Text inq = (Text) theText.clone();
          inq.setText(iwrb.getLocalizedString("travel.colorForInquiry","Inquiry"));
        Text today = (Text) theText.clone();
          today.setText(iwrb.getLocalizedString("travel.today","Today"));
        Text full = (Text) theText.clone();
          full.setText(iwrb.getLocalizedString("travel.fully_booked","Fully booked"));

        legend.add(avail,1,1);
        legend.setColor(3,1,colorForAvailableDay);
        legend.setWidth(3,1,"18");
        legend.setHeight(1,"14");
        legend.add(inq,1,2);
        legend.setColor(3,2,colorForInquery);
        legend.setWidth(3,2,"18");
        legend.setHeight(2,"14");
        legend.add(today,1,3);
        legend.setColor(3,3,colorForToday);
        legend.setWidth(3,3,"18");
        legend.setHeight(3,"14");
        legend.add(full,1,4);
        legend.setColor(3,4,colorForFullyBooked);
        legend.setWidth(3,4,"18");
        legend.setHeight(4,"14");
      table.setAlignment(1,6,"center");

      table.mergeCells(1,5,4,5);
      table.add(sm,1,5);
      table.mergeCells(1,6,4,6);
      table.add(legend,1,6);

      return table;
  }


  public void setProduct(Product product) {
    _product = product;
    _productId = product.getID();
    try {
      _supplier = new Supplier(product.getSupplierId());
      _service = TravelStockroomBusiness.getService(product);
      _timeframe = _service.getTimeframe();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setTour(Tour tour) {
    this._tour = tour;
    try {
      setProduct(new Product(tour.getID() ));
    }catch (SQLException s) {
      s.printStackTrace(System.err);
    }
  }

  public void setContract(Contract contract) {
    _contract = contract;
  }

  public void setReseller(Reseller reseller) {
    _reseller = reseller;
    _resellerId = reseller.getID();
  }

  private void setTimestampsAuto() {
    idegaTimestamp stamp = idegaTimestamp.RightNow();
    _fromStamp =  new idegaTimestamp(1,stamp.getMonth(), stamp.getYear());
    _toStamp   = new idegaTimestamp(cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear()), stamp.getMonth(), stamp.getYear());
  }

  public void setTimestamp(idegaTimestamp stamp) {
    _fromStamp =  new idegaTimestamp(1,stamp.getMonth(), stamp.getYear());
    _toStamp   = new idegaTimestamp(cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear()), stamp.getMonth(), stamp.getYear());
  }

  public void setTimeframe(idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    _fromStamp = new idegaTimestamp(fromStamp);
    _toStamp = new idegaTimestamp(toStamp);
  }

}
