package is.idega.idegaweb.travel.presentation;


import com.idega.idegaweb.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;
import com.idega.block.calendar.presentation.SmallCalendar;
import com.idega.block.calendar.business.CalendarBusiness;
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

  private String backgroundColor = super.backgroundColor;

  private String colorForAvailableDay = TravelManager.ORANGE;
  private String colorForAvailableDayText = BLACK;
  private String colorForInquery = TravelManager.YELLOW;
  private String colorForFullyBooked = TravelManager.RED;
  private String colorForFullyBookedText = BLACK;
  private String colorForToday = "#71CBFB";

  private idegaCalendar cal = new idegaCalendar();
  public SmallCalendar sm = new SmallCalendar();

  private Product _product;
  private Service _service;
  private Tour _tour;
  private Timeframe _timeframe;
  private Contract _contract;
  private Reseller _reseller;
  private idegaTimestamp _fromStamp;
  private idegaTimestamp _toStamp;
  private Class _class = Booking.class;

  private int _productId;
  private int _resellerId;

  private Vector parameterName  = new Vector();
  private Vector parameterValue = new Vector();
  private idegaTimestamp _stamp = idegaTimestamp.RightNow();

  private String _fontColor;
  private boolean _viewInquiries = true;


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
      sm.useNextAndPreviousLinks(true);
      sm.setTextColor("WHITE");
      sm.setDaysAsLink(true);
      sm.showNameOfDays(true);
      sm.setHeaderTextColor(super.textColor);
      sm.setDayTextColor(super.textColor);
      sm.T.setBorderColor(this.backgroundColor);
      sm.setBackgroundColor(this.backgroundColor);
      sm.setHeaderColor(this.backgroundColor);
      sm.setDayCellColor(this.backgroundColor);
      sm.setBodyColor("#8484D6");
      sm.setInActiveCellColor("#666666");
      sm.setColorToday(colorForToday);
      sm.useColorToday(true);
//      sm.setSelectedHighlighted(true);
//      sm.setSelectedHighlightColor(colorForToday);

      sm.setDayFontColor(idegaTimestamp.RightNow(),this.backgroundColor);
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
        if (_fromStamp.isLaterThan(_toStamp)) {
          _toStamp = new idegaTimestamp(_fromStamp);
          _toStamp.addMonths(1);
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
          table.setColor(this.backgroundColor);
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

      if (this._fontColor != null) {
        jan.setFontColor(_fontColor);
        feb.setFontColor(_fontColor);
        mar.setFontColor(_fontColor);
        apr.setFontColor(_fontColor);
        may.setFontColor(_fontColor);
        jun.setFontColor(_fontColor);
        jul.setFontColor(_fontColor);
        aug.setFontColor(_fontColor);
        sep.setFontColor(_fontColor);
        oct.setFontColor(_fontColor);
        nov.setFontColor(_fontColor);
        dec.setFontColor(_fontColor);
      }

      Link lJan = new Link(jan,_class);
        lJan.setBold();
        lJan.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lJan.addParameter(CalendarBusiness.PARAMETER_MONTH,1);
        lJan.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lFeb = new Link(feb,_class);
        lFeb.setBold();
        lFeb.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lFeb.addParameter(CalendarBusiness.PARAMETER_MONTH,2);
        lFeb.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lMar = new Link(mar,_class);
        lMar.setBold();
        lMar.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lMar.addParameter(CalendarBusiness.PARAMETER_MONTH,3);
        lMar.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lApr = new Link(apr,_class);
        lApr.setBold();
        lApr.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lApr.addParameter(CalendarBusiness.PARAMETER_MONTH,4);
        lApr.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lMay = new Link(may,_class);
        lMay.setBold();
        lMay.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lMay.addParameter(CalendarBusiness.PARAMETER_MONTH,5);
        lMay.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lJun = new Link(jun,_class);
        lJun.setBold();
        lJun.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lJun.addParameter(CalendarBusiness.PARAMETER_MONTH,6);
        lJun.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lJul = new Link(jul,_class);
        lJul.setBold();
        lJul.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lJul.addParameter(CalendarBusiness.PARAMETER_MONTH,7);
        lJul.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lAug = new Link(aug,_class);
        lAug.setBold();
        lAug.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lAug.addParameter(CalendarBusiness.PARAMETER_MONTH,8);
        lAug.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lSep = new Link(sep,_class);
        lSep.setBold();
        lSep.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lSep.addParameter(CalendarBusiness.PARAMETER_MONTH,9);
        lSep.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lOct = new Link(oct,_class);
        lOct.setBold();
        lOct.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lOct.addParameter(CalendarBusiness.PARAMETER_MONTH,10);
        lOct.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lNov = new Link(nov,_class);
        lNov.setBold();
        lNov.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lNov.addParameter(CalendarBusiness.PARAMETER_MONTH,11);
        lNov.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());
      Link lDec = new Link(dec,_class);
        lDec.setBold();
        lDec.addParameter(CalendarBusiness.PARAMETER_YEAR,_stamp.getYear());
        lDec.addParameter(CalendarBusiness.PARAMETER_MONTH,12);
        lDec.addParameter(CalendarBusiness.PARAMETER_DAY,_stamp.getDay());

      for (int i = 0; i < parameterName.size(); i++) {
        sm.addParameterToLink((String) parameterName.get(i), (String) parameterValue.get(i));
        lJan.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lFeb.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lMar.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lApr.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lMay.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lJun.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lJul.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lAug.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lSep.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lOct.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lNov.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
        lDec.addParameter((String) parameterName.get(i), (String) parameterValue.get(i));
      }


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

      List depDays = this.getDepartureDays(iwc, false);
      int seats = 0;
      int minSeats = 0;

      if (_tour != null) {
        seats = _tour.getTotalSeats();
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
          int resellerId = _contract.getResellerId();
          if (this._viewInquiries) {
            temp = new idegaTimestamp(1, month , year);
            for (int i = 1; i <= lengthOfMonth; i++) {
              if (Inquirer.getInqueredSeats(_productId, temp,resellerId, true) > 0) {
                sm.setDayColor(temp, colorForInquery);
                sm.setDayFontColor(temp,colorForAvailableDayText);
              }
              temp.addDays(1);
            }
          }

        }
        else if (_supplier != null) {
          for (int i = 0; i < depDays.size(); i++) {
            //System.err.println("trying");
            temp = (idegaTimestamp) depDays.get(i);
            if (seats > 0 && seats <= Booker.getNumberOfBookings(_productId, temp) ) {
            //System.err.println("bookings...");
              sm.setDayColor(temp, colorForFullyBooked);
              sm.setDayFontColor(temp, colorForFullyBookedText);
            }else {
            //System.err.println("not..");
              sm.setDayColor(temp, colorForAvailableDay);
              sm.setDayFontColor(temp,colorForAvailableDayText);
            }
          }
          if (this._viewInquiries) {
            temp = new idegaTimestamp(1, month , year);
            for (int i = 1; i <= lengthOfMonth; i++) {
              if (Inquirer.getInqueredSeats(_productId, temp, true) > 0) {
                sm.setDayColor(temp, colorForInquery);
                sm.setDayFontColor(temp,colorForAvailableDayText);
              }
              temp.addDays(1);
            }
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
          if (this._viewInquiries) {
            temp = new idegaTimestamp(1, month , year);
            for (int i = 1; i <= lengthOfMonth; i++) {
//              System.err.println("Reseller != null : "+Inquirer.getInqueredSeats(_productId, temp,_resellerId, true));
              if (Inquirer.getInqueredSeats(_productId, temp,_resellerId, true) > 0) {
                sm.setDayColor(temp, colorForInquery);
                sm.setDayFontColor(temp,colorForAvailableDayText);
              }
              temp.addDays(1);
            }
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

        if (this._fontColor != null) {
          avail.setFontColor(_fontColor);
          inq.setFontColor(_fontColor);
          today.setFontColor(_fontColor);
          full.setFontColor(_fontColor);
        }

        int lRow = 1;
        legend.add(avail,1,1);
        legend.setColor(3,1,colorForAvailableDay);
        legend.setWidth(3,1,"18");
        legend.setHeight(1,"14");
        if (this._viewInquiries) {
          ++lRow;
          legend.add(inq,1,lRow);
          legend.setColor(3,lRow,colorForInquery);
          legend.setWidth(3,lRow,"18");
          legend.setHeight(lRow,"14");
        }
        ++lRow;
        legend.add(today,1,lRow);
        legend.setColor(3,lRow,colorForToday);
        legend.setWidth(3,lRow,"18");
        legend.setHeight(lRow,"14");
        ++lRow;
        legend.add(full,1,lRow);
        legend.setColor(3,lRow,colorForFullyBooked);
        legend.setWidth(3,lRow,"18");
        legend.setHeight(lRow,"14");
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
      try {
        _tour = new Tour(_productId);
      }catch (SQLException sql) {}
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
    _stamp = new idegaTimestamp(stamp);
    _fromStamp =  new idegaTimestamp(1,stamp.getMonth(), stamp.getYear());
    _toStamp   = new idegaTimestamp(cal.getLengthOfMonth(stamp.getMonth(), stamp.getYear()), stamp.getMonth(), stamp.getYear());
  }

  public void setTimeframe(idegaTimestamp fromStamp, idegaTimestamp toStamp) {
    _fromStamp = new idegaTimestamp(fromStamp);
    _toStamp = new idegaTimestamp(toStamp);
  }

  public void setClassToLinkTo(Class classToLinkTo) {
    _class = classToLinkTo;
  }

  public void addParameterToLink(String name, int value) {
    addParameterToLink(name, Integer.toString(value));
  }
  public void addParameterToLink(String name, String value) {
    parameterName.add(name);
    parameterValue.add(value);
  }

  public void setBackgroundColor(String color) {
    this.backgroundColor = color;
      sm.T.setBorderColor(this.backgroundColor);
      sm.setBackgroundColor(this.backgroundColor);
      sm.setHeaderColor(this.backgroundColor);
      sm.setDayCellColor(this.backgroundColor);
  }

  public void setInActiveCellColor(String color) {
    sm.setInActiveCellColor(color);
  }

  public void setFontColor(String color) {
    _fontColor = color;
    sm.setHeaderTextColor(_fontColor);
    sm.setTextColor(_fontColor);
    sm.setHeaderColor(_fontColor);
  }

  public void setFullyBookedColor(String color) {
    this.colorForFullyBooked = color;
  }

  public void setAvailableDayColor(String color) {
    this.colorForAvailableDay = color;
  }

  public void setInquiryColor(String color) {
    this.colorForInquery = color;
  }

  public void setTodayColor(String color) {
    this.colorForToday = color;
  }

  public void showInquiries(boolean show) {
    _viewInquiries = show;
  }

  public List getDepartureDays(IWContext iwc) {
    return getDepartureDays(iwc, false);
  }

  public List getDepartureDays(IWContext iwc, boolean showPast) {
    List depDays = new Vector();
      if (_tour != null) {
        if (_tour.getNumberOfDays() > 1) {
          if (_timeframe.getIfYearly()) {
            depDays = TourBusiness.getDepartureDays(iwc,_tour, _fromStamp, _toStamp);
          }else {
            depDays = TourBusiness.getDepartureDays(iwc, _tour);
          }
        }else {
          depDays = TourBusiness.getDepartureDays(iwc,_tour, _fromStamp, _toStamp);
        }
      }else {
          depDays = TravelStockroomBusiness.getDepartureDays(iwc, _product, _fromStamp, _toStamp, showPast);
      }
    return depDays;
  }

}
