
package is.idega.idegaweb.travel.presentation;

import javax.ejb.FinderException;
import java.util.*;
import com.idega.business.IBOLookup;
import com.idega.idegaweb.*;
import java.rmi.RemoteException;
import com.idega.block.calendar.business.CalendarBusiness;
import com.idega.data.IDOLookup;
import com.idega.core.user.data.User;
import com.idega.presentation.Block;
import com.idega.presentation.text.*;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.block.trade.stockroom.data.*;
import com.idega.block.trade.stockroom.business.*;
import com.idega.block.calendar.presentation.SmallCalendar;
import com.idega.util.idegaTimestamp;
import com.idega.util.idegaCalendar;
import com.idega.core.accesscontrol.business.AccessControl;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import java.sql.SQLException;

import is.idega.idegaweb.travel.business.Assigner;
import is.idega.idegaweb.travel.business.Booker;
import is.idega.idegaweb.travel.business.Inquirer;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.tour.data.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.service.tour.presentation.*;
import is.idega.idegaweb.travel.interfaces.Booking;
/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class BookingOverview extends TravelManager {

  private IWBundle bundle;
  private IWResourceBundle iwrb;

  private Supplier supplier;
  private Reseller reseller;
  private Contract contract;

  private Product product;
  private int _productId = -1;
//  private TravelStockroomBusiness tsb;

  private Service service;
  private Timeframe timeframe;
  private Tour tour;

  private idegaTimestamp fromStamp;
  private idegaTimestamp toStamp;

  private String cellWidth = "50";

  private String closerLookIdParameter = "viewServiceId";
  private String closerLookDateParameter = "viewServiceDate";
  private String bookParameter = "bookService";

  private String parameterDeleteBooking = "bookingOverviewDeleteBooking";
  private String bookingOverviewAction = "bookingOverviewAction";
  private String parameterBookingId = "bookingOverviewBookingId";
  private String parameterViewAll = "-109";

  public BookingOverview() {
  }

  public void add(PresentationObject mo) {
    super.add(mo);
  }


  public void main(IWContext iwc) throws Exception {
      super.main(iwc);
      initialize(iwc);
      supplier = super.getSupplier();

      if (super.isLoggedOn(iwc)) {
        if (reseller != null && contract == null) {
          product = null;
        }
        displayForm(iwc);
        super.addBreak();
      }else {
        add(super.getLoggedOffTable(iwc));
      }
  }

  public void initialize(IWContext iwc) throws RemoteException{
      bundle = super.getBundle();
      iwrb = super.getResourceBundle();

      supplier = super.getSupplier();
      reseller = super.getReseller();

      String productId = iwc.getParameter(com.idega.block.trade.stockroom.data.ProductBMPBean.getProductEntityName());
      try {
        if (productId == null) {
          productId = (String) iwc.getSessionAttribute("TB_BOOKING_PRODUCT_ID");
        }else {
          _productId = Integer.parseInt(productId);
          iwc.setSessionAttribute("TB_BOOKING_PRODUCT_ID",productId);
        }
        if (productId != null && _productId != -1 && !productId.equals(parameterViewAll)) {
          //System.err.println(" productId = "+productId);
          //System.err.println("_productId = "+_productId);
          product = ProductBusiness.getProduct(_productId);
          service = getTravelStockroomBusiness(iwc).getService(product);
          tour = getTourBusiness(iwc).getTour(product);
          timeframe = getTravelStockroomBusiness(iwc).getTimeframe(product);
        }
      }catch (ServiceNotFoundException snfe) {
          snfe.printStackTrace(System.err);
      }catch (TimeframeNotFoundException tfnfe) {
          tfnfe.printStackTrace(System.err);
      }catch (TourNotFoundException tnfe) {
          tnfe.printStackTrace(System.err);
      }catch (SQLException sql) {sql.printStackTrace(System.err);}


      if ((reseller != null) && (product != null)){
        try {
            Contract[] contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId(), Integer.toString(reseller.getID()), is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
            if (contracts.length > 0) {
              contract = contracts[0];
            }
        }catch (SQLException sql) {
            sql.printStackTrace(System.err);
        }

      }

      fromStamp = getFromIdegaTimestamp(iwc);
      toStamp = getToIdegaTimestamp(iwc);

  }

  public void displayForm(IWContext iwc) throws Exception{

      Form form = new Form();
      Table topTable = getTopTable(iwc);
        form.add(topTable);
        form.add(Text.BREAK);
      Table table = new Table();

      String action = iwc.getParameter(this.bookingOverviewAction);
      if (action == null) action = "";

      String view = iwc.getParameter(closerLookDateParameter);
      if (action.equals(""))
      if (view != null) action = "view";

      if (action.equals("")) {
         table = getContentTable(iwc);
      }else if (action.equals("view")) {
         table = getViewService(iwc);
      }else if (action.equals(this.parameterDeleteBooking)) {
         deleteBooking(iwc);
         table = getViewService(iwc);
      }


      form.add(table);

      Table par = new Table(1,1);
        par.setAlignment(1,1,"right");
        par.setAlignment("center");
        par.setWidth("90%");
        par.add(new PrintButton(iwrb.getImage("buttons/print.gif")),1,1);
        form.add(Text.BREAK);
        form.add(par);


      int row = 0;
      add(Text.getBreak());
      add(form);
  }


  // BUSINESS
  public idegaTimestamp getFromIdegaTimestamp(IWContext iwc) {
    idegaTimestamp stamp = null;
    String from_time = iwc.getParameter("active_from");
    if (from_time!= null) {
      stamp = new idegaTimestamp(from_time);
    } else {
      stamp = idegaTimestamp.RightNow();
    }
    return stamp;
  }

  // BUSINESS
  public idegaTimestamp getToIdegaTimestamp(IWContext iwc) {
    idegaTimestamp stamp = null;
    String from_time = iwc.getParameter("active_to");
    if (from_time!= null) {
      stamp = new idegaTimestamp(from_time);
    } else {
      stamp = idegaTimestamp.RightNow();
      stamp.addDays(15);
    }
    return stamp;
  }


  public Table getTopTable(IWContext iwc) {
      Table topTable = new Table(5,2);
        topTable.setBorder(0);
        topTable.setWidth("90%");

      Text tframeText = (Text) theText.clone();
          tframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          tframeText.addToText(":");

      DropdownMenu trip = null;
        if (supplier != null) {
          trip = ProductBusiness.getDropdownMenuWithProducts(iwc, supplier.getID());
          // new DropdownMenu(ProductBusiness.getProducts(supplier.getID()));
        }else if (reseller != null){
          trip = ResellerManager.getDropdownMenuWithProducts(iwc, reseller.getID());
        }

        trip.addMenuElementFirst(parameterViewAll,iwrb.getLocalizedString("travel.all_products","All products"));


          if (product != null) {
              trip.setSelectedElement(Integer.toString(product.getID()));
          }


      DateInput active_from = new DateInput("active_from");
          active_from.setDate(fromStamp.getSQLDate());
      DateInput active_to = new DateInput("active_to");
          active_to.setDate(toStamp.getSQLDate());

      Text tfFromText = (Text) theText.clone();
          tfFromText.setText(iwrb.getLocalizedString("travel.from","from"));
      Text tfToText = (Text) theText.clone();
          tfToText.setText(iwrb.getLocalizedString("travel.to","to"));


      Text nameText = (Text) theText.clone();
          nameText.setText(iwrb.getLocalizedString("travel.product_name_lg","Name of product"));
          nameText.addToText(":");
      Text timeframeText = (Text) theText.clone();
          timeframeText.setText(iwrb.getLocalizedString("travel.timeframe_only","Timeframe"));
          timeframeText.addToText(":");
      Text generalOverviewText = (Text) theText.clone();
          generalOverviewText.setText(iwrb.getLocalizedString("travel.general_overview","General overview"));
          generalOverviewText.addToText(":");
      Text inqOnlyText = (Text) theText.clone();
          inqOnlyText.setText(iwrb.getLocalizedString("travel.inqueries_only","Inqueries only"));
          inqOnlyText.addToText(":");

      topTable.setColumnAlignment(1,"right");
      topTable.setColumnAlignment(2,"left");
      topTable.add(nameText,1,1);
      topTable.add(trip,2,1);
      topTable.add(timeframeText,1,2);
      topTable.add(tfFromText,1,2);
      topTable.add(active_from,2,2);
      topTable.add(tfToText,2,2);
      topTable.add(active_to,2,2);
      topTable.mergeCells(2,1,4,1);
      topTable.mergeCells(2,2,4,2);


      topTable.setAlignment(5,2,"right");
      topTable.add(new SubmitButton(iwrb.getImage("buttons/get.gif")),5,2);

      return topTable;
  }

  public Table getContentHeader(IWContext iwc) {
      Table table = new Table(2,1);
      table.setWidth("95%");

      String mode = iwc.getParameter("mode");
      if (mode== null) mode="";


      Text headerText = (Text) theBoldText.clone();
          if (mode.equals("inqueries_only")) {
              headerText.setText(iwrb.getLocalizedString("travel.inqueries","Inqueries"));
          }else {
              headerText.setText(iwrb.getLocalizedString("travel.overview","Overview"));
          }
      Text timeText = (Text) theBoldText.clone();
          timeText.setText(fromStamp.getLocaleDate(iwc)+" - "+toStamp.getLocaleDate(iwc));

      table.setAlignment(1,1,"left");
      table.add(headerText,1,1);
      table.setAlignment(2,1,"right");
      table.add(timeText,2,1);

      return table;
  }

  public Table getContentTable(IWContext iwc) throws Exception {
      Table table = new Table();
        table.setCellspacing(1);
        table.setCellpadding(4);
        table.setColor(super.WHITE);
        table.setWidth("90%");

      int row = 1;
      if (_productId != -1) {

          boolean viewAll = false;
          int productId = _productId;
          if (productId == Integer.parseInt(this.parameterViewAll) ) viewAll = true;

          Text dateText = (Text) theText.clone();
              dateText.setText(iwrb.getLocalizedString("travel.date_sm","date"));
          Text nameText = (Text) theText.clone();
              nameText.setText(iwrb.getLocalizedString("travel.product_name_sm","name of product"));
          Text countText = (Text) theText.clone();
              countText.setText(iwrb.getLocalizedString("travel.count_sm","count"));
          Text assignedText = (Text) theText.clone();
              assignedText.setText(iwrb.getLocalizedString("travel.assigned_small_sm","assigned"));
          Text inqText = (Text) theText.clone();
              inqText.setText(iwrb.getLocalizedString("travel.inqueries_sm","inquiries"));
          Text bookedText = (Text) theText.clone();
              bookedText.setText(iwrb.getLocalizedString("travel.booked_sm","booked"));
          Text availableText = (Text) theText.clone();
              availableText.setText(iwrb.getLocalizedString("travel.available_small_sm","avail."));


          Text dateTextBold = (Text) theSmallBoldText.clone();
          Text nameTextBold = (Text) theSmallBoldText.clone();
          Text countTextBold = (Text) theSmallBoldText.clone();
          Text assignedTextBold = (Text) theSmallBoldText.clone();
          Text inqTextBold = (Text) theSmallBoldText.clone();
          Text bookedTextBold = (Text) theSmallBoldText.clone();
          Text availableTextBold = (Text) theSmallBoldText.clone();

          table.add(dateText,1,row);
          table.add(nameText,2,row);
          table.add(countText,3,row);
          table.add(assignedText,4,row);
          table.add(inqText,5,row);
          table.add(bookedText,6,row);
          table.add(availableText,7,row);
          table.add("&nbsp;",8,row);

          table.setRowColor(row, super.backgroundColor);

          List products;
          Product prod;
          int supplierId = 0;
          if (supplier != null)
            supplierId = supplier.getID();
          idegaCalendar cal = new idegaCalendar();
          int dayOfWeek;
          boolean upALine = false;

          Service service;
          Tour tour;

          int iCount = 0;
          int iBooked =0;
          int iInquery=0;
          int iAvailable=0;
          int iAssigned=0;

          String theColor = super.GRAY;

          idegaTimestamp tempStamp = new idegaTimestamp(fromStamp);
          ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
          ServiceDay sDay = sDayHome.create();

          toStamp.addDays(1);
          while (toStamp.isLaterThan(tempStamp)) {
              dayOfWeek = cal.getDayOfWeek(tempStamp.getYear(), tempStamp.getMonth(), tempStamp.getDay());
              try {
                  if (viewAll) {
                    products = ProductBusiness.getProducts(supplierId, tempStamp);
                  }
                  else {
                    products = new Vector();
                      products.add(ProductBusiness.getProduct(productId));
                  }
              }catch (SQLException sql) {
                sql.printStackTrace(System.err);
                products = ProductBusiness.getProducts(supplierId, tempStamp);
              }
              upALine = false;
              ++row;
              dateTextBold = (Text) theSmallBoldText.clone();
                  dateTextBold.setText(tempStamp.getLocaleDate(iwc));
                  dateTextBold.setFontColor(super.BLACK);
              table.add(dateTextBold,1,row);
              table.setRowColor(row, super.GRAY);

              boolean bContinue= false;

              for (int i = 0; i < products.size(); i++) {
                  try {
                    prod = (Product) products.get(i);
                      if (supplier != null) {
                        bContinue = getTravelStockroomBusiness(iwc).getIfDay(iwc,prod,tempStamp);
                      }else if (reseller != null) {
                        bContinue = getTravelStockroomBusiness(iwc).getIfDay(iwc,contract,prod,tempStamp);
                      }
                      if (bContinue) {
                          iCount = 0;
                          iBooked =0;
                          iInquery=0;
                          iAvailable=0;
                          iAssigned=0;
                          service = getTravelStockroomBusiness(iwc).getService(prod);
                          tour = getTourBusiness(iwc).getTour(prod);

                          if (supplier != null) {
                            sDay = sDay.getServiceDay(((Integer) service.getPrimaryKey()).intValue(), tempStamp.getDayOfWeek());
                            if (sDay != null) {
                              iCount = sDay.getMax();
                              if (iCount < 1) {
                                iCount = tour.getTotalSeats();
                              }
                            }else {
                              iCount = tour.getTotalSeats();
                            }

                            //iCount = tour.getTotalSeats();
                            iBooked = getBooker(iwc).getNumberOfBookings(((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                            iAssigned = getAssigner(iwc).getNumberOfAssignedSeats(prod, tempStamp);

                            int resellerBookings = getBooker(iwc).getNumberOfBookingsByResellers(((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                            if (iAssigned != 0) {
                              iAssigned = iAssigned - resellerBookings;
                            }

                            iInquery = getInquirer(iwc).getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(), tempStamp, true);//getInqueredSeats(service.getID() ,tempStamp, true);
                            //iInquery = Inquirer.getInqueredSeats(service.getID() ,tempStamp, true);
                            iAvailable = iCount - iBooked - iAssigned;
                          }else if (reseller != null) {
                            iCount = contract.getAlotment();
                            iBooked = getBooker(iwc).getNumberOfBookingsByReseller(reseller.getID() ,((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                            iAssigned = 0;

                            iInquery = getInquirer(iwc).getInquiryHome().getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(),tempStamp,reseller.getID(), true);
                            iAvailable = iCount - iBooked - iAssigned -iInquery;
                            iCount = iCount -iBooked;
                          }
//                          countTextBold.setText(Integer.toString(iCount));


                          nameTextBold  = (Text) theSmallBoldText.clone();
                            nameTextBold.setText(service.getName());

                          assignedTextBold = (Text) theSmallBoldText.clone();
                            assignedTextBold.setText(Integer.toString(iAssigned));
                          countTextBold = (Text) super.theSmallBoldText.clone();
                            countTextBold.setText(Integer.toString(iCount));
                          inqTextBold = (Text) theSmallBoldText.clone();
                            inqTextBold.setText(Integer.toString(iInquery));
                          bookedTextBold = (Text) theSmallBoldText.clone();
                            bookedTextBold.setText(Integer.toString(iBooked));
                          availableTextBold = (Text) theSmallBoldText.clone();
                            availableTextBold.setText(Integer.toString(iAvailable));

                            nameTextBold.setFontColor(super.BLACK);
                            countTextBold.setFontColor(super.BLACK);
                            assignedTextBold.setFontColor(super.BLACK);
                            inqTextBold.setFontColor(super.BLACK);
                            bookedTextBold.setFontColor(super.BLACK);
                            availableTextBold.setFontColor(super.BLACK);

                          Link btnNanar = new Link(iwrb.getImage("buttons/closer.gif"));
                            btnNanar.addParameter(closerLookDateParameter,tempStamp.toSQLDateString());
                            btnNanar.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterProductId, prod.getID());
                          Link btnBook = new Link(iwrb.getImage("buttons/book.gif"), is.idega.idegaweb.travel.presentation.Booking.class);
                            btnBook.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterProductId, prod.getID());
                            btnBook.addParameter("year",tempStamp.getYear());
                            btnBook.addParameter("month",tempStamp.getMonth());
                            btnBook.addParameter("day",tempStamp.getDay());

                          table.add(nameTextBold,2,row);
                          table.add(assignedTextBold,4,row);
                          table.add(inqTextBold,5,row);
                          table.add(bookedTextBold,6,row);
                          if (iCount > 0) {
                            table.add(countTextBold,3,row);
                            table.add(availableTextBold,7,row);
                          }
/*
                          table.setColor(1,row,theColor);
                          table.setColor(2,row,theColor);
                          table.setColor(3,row,theColor);
                          table.setColor(4,row,theColor);
                          table.setColor(5,row,theColor);
                          table.setColor(6,row,theColor);
                          table.setColor(7,row,theColor);
*/
                          table.add(btnNanar,8,row);
                          if (supplier != null) {
                            table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,8,row);
                            table.add(btnBook,8,row);
                          } else if (reseller != null) {
                            if (!getTravelStockroomBusiness(iwc).getIfExpired(contract, tempStamp))
                              table.add(btnBook,8,row);
                          }
                          table.setRowColor(row,theColor);
                          if (iInquery > 0) {
                            table.setColor(5,row,super.YELLOW);
                          }
                          ++row;
                          upALine = true;
                      }
                  }catch (SQLException sql) {
                    sql.printStackTrace(System.err);
                  }catch (ServiceNotFoundException snfe) {
                    snfe.printStackTrace(System.err);
                  }catch (TourNotFoundException tnfe) {
                    tnfe.printStackTrace(System.err);
                  }catch (TimeframeNotFoundException tfnfe) {
                    tfnfe.printStackTrace(System.err);
                  }
              }
              if (upALine) --row;

              tempStamp.addDays(1);

          }
          toStamp.addDays(-1);



          table.setWidth(3,cellWidth);
          table.setWidth(4,cellWidth);
          table.setWidth(5,cellWidth);
          table.setWidth(6,cellWidth);
          table.setWidth(7,cellWidth);

          table.setColumnAlignment(1,"left");
          table.setColumnAlignment(2,"left");
          table.setColumnAlignment(3,"center");
          table.setColumnAlignment(4,"center");
          table.setColumnAlignment(5,"center");
          table.setColumnAlignment(6,"center");
          table.setColumnAlignment(7,"center");
          table.setColumnAlignment(8,"right");


      }
      else {
        table.add(iwrb.getLocalizedString("travel.please_select_a_product","Please select a product"));
      }
      return table;

  }


  public Table getViewService(IWContext iwc) throws Exception {
    String view_id = iwc.getParameter(this.closerLookIdParameter);
    String view_date = iwc.getParameter(this.closerLookDateParameter);


      Table table = new Table();
        table.setWidth("95%");
        table.setBorder(0);
        table.setCellspacing(1);
        table.setCellpadding(2);
        table.setColor(super.WHITE);

        int row = 1;
/*
          Text dateText = (Text) theText.clone();
              dateText.setText(iwrb.getLocalizedString("travel.date_sm","date"));
          Text nameText = (Text) theText.clone();
              nameText.setText(iwrb.getLocalizedString("travel.trip_name_sm","name of trip"));
          Text countText = (Text) theText.clone();
              countText.setText(iwrb.getLocalizedString("travel.count_sm","count"));
          Text assignedText = (Text) theText.clone();
              assignedText.setText(iwrb.getLocalizedString("travel.assigned_small_sm","assigned"));
          Text inqText = (Text) theText.clone();
              inqText.setText(iwrb.getLocalizedString("travel.inqueries_small_sm","inq."));
          Text bookedText = (Text) theText.clone();
              bookedText.setText(iwrb.getLocalizedString("travel.booked_sm","booked"));
          Text availableText = (Text) theText.clone();
              availableText.setText(iwrb.getLocalizedString("travel.available_small_sm","avail."));
          Text hotelPickupText = (Text) theText.clone();
              hotelPickupText.setText(iwrb.getLocalizedString("travel.booked_by","Booked by"));
*/

          Text dateTextBold = (Text) theSmallBoldText.clone();
          Text nameTextBold = (Text) theSmallBoldText.clone();
          Text countTextBold = (Text) theSmallBoldText.clone();
          Text assignedTextBold = (Text) theSmallBoldText.clone();
          Text inqTextBold = (Text) theSmallBoldText.clone();
          Text bookedTextBold = (Text) theSmallBoldText.clone();
          Text availableTextBold = (Text) theSmallBoldText.clone();
          Text hotelPickupTextBold = (Text) theSmallBoldText.clone();

          table.add(getHeaderText(iwrb.getLocalizedString("travel.date","Date")), 1, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.product_name","Product name")), 2, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.count","Count")), 3, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.assigned","Assigned")), 4, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.inquiries","Inquiries")), 5, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.booked","Booked")), 6, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.available","Available")), 7, row);
          table.add(getHeaderText(iwrb.getLocalizedString("travel.booked_by","Booked by")), 8, row);
          table.add(getHeaderText(Text.NON_BREAKING_SPACE), 9, row);

/*
          table.add(dateText,1,row);
          table.add(nameText,2,row);
          table.add(countText,3,row);
          table.add(assignedText,4,row);
          table.add(inqText,5,row);
          table.add(bookedText,6,row);
          table.add(availableText,7,row);
          table.add(hotelPickupText,8,row);
          table.add(Text.NON_BREAKING_SPACE, 9,row);
*/
          table.setRowColor(row, super.backgroundColor);

          idegaTimestamp currentStamp = new idegaTimestamp(view_date);
          int seats = 0;
          int assigned = 0;
          int iInqueries = 0;
          int booked = 0;
          int available = 0;

          if (supplier != null) {
            ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
            ServiceDay sDay = sDayHome.create();
            sDay = sDay.getServiceDay(((Integer) service.getPrimaryKey()).intValue(), currentStamp.getDayOfWeek());
            if (sDay != null) {
              seats = sDay.getMax();
              if (seats < 1) {
                seats = tour.getTotalSeats();
              }
            }else {
              seats = tour.getTotalSeats();
            }
//            seats = tour.getTotalSeats();
            assigned = getAssigner(iwc).getNumberOfAssignedSeats(((Integer) service.getPrimaryKey()).intValue(), currentStamp);
            iInqueries = getInquirer(iwc).getInqueredSeats(service.getID() , currentStamp, true);
            booked = getBooker(iwc).getNumberOfBookings(service.getID(), currentStamp);
            available = seats - booked;
          }else if (reseller != null) {
            seats = contract.getAlotment();
            assigned = 0;
            iInqueries = getInquirer(iwc).getInquiryHome().getInqueredSeats(service.getID() , currentStamp, reseller.getID(), true);
            booked = getBooker(iwc).getNumberOfBookingsByReseller(reseller.getID(),service.getID(), currentStamp);
            available = seats - booked - iInqueries;
          }

          dateTextBold.setText(currentStamp.getLocaleDate(iwc));
          nameTextBold.setText(service.getName());
          countTextBold.setText(Integer.toString(seats));
          availableTextBold.setText(Integer.toString(available));
          assignedTextBold.setText(Integer.toString(assigned));
          inqTextBold.setText(Integer.toString(iInqueries));
          bookedTextBold.setText(Integer.toString(booked));
            dateTextBold.setFontColor(super.BLACK);
            nameTextBold.setFontColor(super.BLACK);
            countTextBold.setFontColor(super.BLACK);
            availableTextBold.setFontColor(super.BLACK);
            assignedTextBold.setFontColor(super.BLACK);
            inqTextBold.setFontColor(super.BLACK);
            bookedTextBold.setFontColor(super.BLACK);

          ++row;
          table.add(dateTextBold,1,row);
          table.add(nameTextBold,2,row);
          table.setAlignment(3,row,"center");
          table.add(assignedTextBold,4,row);
          table.add(inqTextBold,5,row);
          table.add(bookedTextBold,6,row);
          if (seats > 0) {
            table.add(countTextBold,3,row);
            table.add(availableTextBold,7,row);
          }

          table.setRowColor(row, super.GRAY);

          Text Tname;
          Text Temail;
          Text TbookedBy = (Text) super.theSmallBoldText.clone();
          Text Tbooked;

          java.util.List emails;
          // ------------------ ASSIGNED -----------------------
          if (assigned > 0) {
            Reseller[] resellers = ResellerManager.getResellers(service.getID(), currentStamp);
            for (int i = 0; i < resellers.length; i++) {
              ++row;

              Tname = (Text) super.theSmallBoldText.clone();
                Tname.setText(resellers[i].getName());
              Temail = (Text) super.theSmallBoldText.clone();

              Tname.setFontColor(super.BLACK);
              Temail.setFontColor(super.BLACK);

              try {
                emails = resellers[i].getEmails();
                if (emails != null) {
                  for (int j = 0; j < emails.size(); j++) {
                    if (j > 0) Temail.addToText(", ");
                    Temail.addToText( ((com.idega.core.data.Email) emails.get(j)).getEmailAddress());
                  }
                }
              }catch (SQLException sql) {sql.printStackTrace(System.err);}
              //                Temail.setText(reseller[i].getEmail());
              Tbooked = (Text) super.theSmallBoldText.clone();
                Tbooked.setText(Integer.toString(getAssigner(iwc).getNumberOfAssignedSeats(service.getID(), resellers[i].getID() ,currentStamp)));

              table.mergeCells(2,row,3, row);
              table.add(Tname,1,row);
              table.add(Temail,3,row);
              table.setAlignment(3,row,"left");
              table.add(Tbooked,4,row);
              table.setRowColor(row, super.GRAY);
            }
          }


          List addresses = ProductBusiness.getDepartureAddresses(product, true);
          TravelAddress trAddress;
          int addressesSize = addresses.size();
          int tempRow;
          int tempTotal;
          int tempBookings;
          int tempAvail;
          int tempInq;
          Collection travelAddressIds;
//          int trAddrBookings = 0;
          for (int g = 0; g < addressesSize; g++) {
            trAddress = (TravelAddress) addresses.get(g);
            ++row;
            tempRow = row;
            tempTotal = 0;
            tempAvail = 0;
            tempInq = 0;
            table.mergeCells(1, row, 2, row);
            table.add(getHeaderText(trAddress.getName()), 1, row);
            table.setRowColor(row, super.backgroundColor);

            Link link;
            // ------------------ INQUERIES ------------------------
            Link answerLink = new Link(iwrb.getLocalizedImageButton("travel.answer","Answer"),is.idega.idegaweb.travel.presentation.Booking.class);
              answerLink.addParameter(CalendarBusiness.PARAMETER_YEAR, currentStamp.getYear());
              answerLink.addParameter(CalendarBusiness.PARAMETER_MONTH, currentStamp.getMonth());
              answerLink.addParameter(CalendarBusiness.PARAMETER_DAY, currentStamp.getDay());
            Inquery[] inqueries = null;

            int[] iNumbers;
            if (supplier != null) inqueries = getInquirer(iwc).getInqueries(service.getID(), currentStamp, true, trAddress, is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
            if (reseller != null) {
              inqueries = inqueries = getInquirer(iwc).getInqueries(service.getID(), currentStamp, true, trAddress, is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
//              Collection coll = getInquirer(iwc).getInquiryHome().findInqueries(service.getID(), currentStamp, reseller.getID(),true, trAddress,is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
//              inqueries = getInquirer(iwc).collectionToInqueryArray(coll);
            }
            for (int i = 0; i < inqueries.length; i++) {
              ++row;
              iNumbers = getInquirer(iwc).getMultibleInquiriesNumber(inqueries[i]);
              tempInq += inqueries[i].getNumberOfSeats();
              Tname = (Text) super.theSmallBoldText.clone();
                Tname.setText(inqueries[i].getName());
                if ( iNumbers[0] != 0 ) {
                  Tname.addToText(Text.NON_BREAKING_SPACE+"( "+iNumbers[0]+" / "+iNumbers[1]+" )");
                }
              Temail = (Text) super.theSmallBoldText.clone();
                Temail.setText(inqueries[i].getEmail());
              Tbooked = (Text) super.theSmallBoldText.clone();
                Tbooked.setText(Integer.toString(inqueries[i].getNumberOfSeats()));

                Tname.setFontColor(super.BLACK);
                Temail.setFontColor(super.BLACK);
                Tbooked.setFontColor(super.BLACK);

              table.mergeCells(2,row,4, row);
              table.add(Tname,1,row);
              table.add(Temail,2,row);
              table.setAlignment(3,row,"left");
              table.add(Tbooked,5,row);

              table.setRowColor(row, super.YELLOW);
  //            table.setRowColor(row, super.GRAY);

              link = (Link) answerLink.clone();
                link.addParameter(TourBookingForm.parameterDepartureAddressId, trAddress.getID());
              table.add(link, 9, row);

            }

//            int tempBookingTest = getBooker(iwc).getGeneralBookingHome().getNumberOfBookings(product.getID(), currentStamp, null, -1, new int[]{}, getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(product, trAddress) );
//            table.add(getHeaderText(Integer.toString(tempBookingTest)), 6, row);

            // ------------------ BOOKINGS ------------------------
            Link changeLink = new Link(iwrb.getImage("buttons/change.gif"),is.idega.idegaweb.travel.presentation.Booking.class);
            Link deleteLink = new Link(iwrb.getImage("buttons/delete.gif"));
              deleteLink.setWindowToOpen(BookingDeleterWindow.class);
            Booking[] bookings = {};
            GeneralBooking booking;
            int[] bNumbers;

            if (this.supplier != null) {
              bookings = getBooker(iwc).getBookings(((Integer) this.service.getPrimaryKey()).intValue(), currentStamp, trAddress);
            }else if (this.reseller != null) {
              Collection coll = getBooker(iwc).getGeneralBookingHome().findBookings(reseller.getID(), service.getID(), currentStamp);
              bookings = getBooker(iwc).collectionToBookingsArray(coll);
            }

            Object serviceType;
            User bUser;
            Reseller bReseller;
            for (int i = 0; i < bookings.length; i++) {
                ++row;
                booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(bookings[i].getPrimaryKey());
                serviceType = getBooker(iwc).getServiceType(this.service.getID());

                Tname = (Text) super.theSmallBoldText.clone();
                  Tname.setText(bookings[i].getName());
                bNumbers = getBooker(iwc).getMultipleBookingNumber(booking);
                if ( bNumbers[0] != 0 ) {
                  Tname.addToText(Text.NON_BREAKING_SPACE+"( "+bNumbers[0]+" / "+bNumbers[1]+" )");
                }

                tempBookings = bookings[i].getTotalCount();
                tempTotal += tempBookings;
//                trAddrBookings += tempBookings;

                Temail = (Text) super.theSmallBoldText.clone();
                  Temail.setText(bookings[i].getEmail());
                Tbooked = (Text) super.theSmallBoldText.clone();
                  Tbooked.setText(Integer.toString(tempBookings));

                Tname.setFontColor(super.BLACK);
                Temail.setFontColor(super.BLACK);
                Tbooked.setFontColor(super.BLACK);

                TbookedBy = (Text) super.theSmallBoldText.clone();
                  TbookedBy.setFontColor(super.BLACK);
                if (bookings[i].getUserId() != -1) {
                  bUser = ((com.idega.core.user.data.UserHome)com.idega.data.IDOLookup.getHomeLegacy(User.class)).findByPrimaryKeyLegacy(bookings[i].getUserId());
                  bReseller = ResellerManager.getReseller(bUser);
                    TbookedBy.setText(bUser.getName());
                    if (bReseller != null) {
                      if (this.reseller != bReseller) {
                        TbookedBy.addToText(" ( "+bReseller.getName()+" ) ");
                      }
                    }
                }else {
                    TbookedBy.setText(iwrb.getLocalizedString("travel.online","Online"));
                }

                link = VoucherWindow.getVoucherLink(bookings[i]);
                  link.setText(Tname);

                table.mergeCells(2, row, 5, row);
                table.add(link, 1, row);
                table.add(Temail, 2, row);
                table.setAlignment(3, row, "left");
                table.add(Tbooked, 6, row);
                table.add(TbookedBy, 8, row);

                table.setRowColor(row, super.GRAY);

                link = (Link) changeLink.clone();
                  link.addParameter(is.idega.idegaweb.travel.presentation.Booking.BookingAction,is.idega.idegaweb.travel.presentation.Booking.parameterUpdateBooking);
                  link.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterBookingId,bookings[i].getID());
                table.add(link, 9, row);
                table.add(Text.NON_BREAKING_SPACE,9,row);

                link = (Link) deleteLink.clone();
                  link.addParameter(BookingDeleterWindow.bookingIdParameter,bookings[i].getID());
                table.add(link, 9, row);

            }

            table.add(getHeaderText(Integer.toString(seats)), 3, tempRow);
            table.add(getHeaderText(Integer.toString(assigned)), 4, tempRow);
            table.add(getHeaderText(Integer.toString(tempInq)), 5, tempRow);
            table.add(getHeaderText(Integer.toString(tempTotal)), 6, tempRow);
            if (seats > 0) {
              travelAddressIds = super.getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(product, trAddress);
              tempAvail = seats - getBooker(iwc).getGeneralBookingHome().getNumberOfBookings(( (Integer) product.getPrimaryKey()).intValue(), currentStamp, null, -1, new int[]{}, travelAddressIds );
              table.add(getHeaderText(Integer.toString(tempAvail)), 7, tempRow);
            }

            Link daLink = LinkGenerator.getLink(iwc, product.getID(), is.idega.idegaweb.travel.presentation.Booking.class);
              daLink.addParameter(TourBookingForm.parameterDepartureAddressId, trAddress.getID());
              daLink.setPresentationObject(iwrb.getImage("buttons/book.gif"));
            table.add(Text.NON_BREAKING_SPACE, 1, tempRow);
            table.add(daLink, 1, tempRow);
          }

        ++row;
        table.mergeCells(1,row,6,row);
        availableTextBold.setText(Integer.toString(available));
          availableTextBold.setFontColor(super.BLACK);
        Text Tavail = (Text) super.theSmallBoldText.clone();
          Tavail.setFontColor(super.BLACK);
          Tavail.setText(iwrb.getLocalizedString("travel.available_seats","Available seats"));
        if (seats > 0) {
          table.add(Tavail, 1, row);
          table.add(availableTextBold, 7, row);
        }
        table.setRowColor(row, super.GRAY);



    table.setColumnAlignment(1,"left");
    table.setColumnAlignment(2,"left");
    table.setColumnAlignment(3,"center");
    table.setColumnAlignment(4,"center");
    table.setColumnAlignment(5,"center");
    table.setColumnAlignment(6,"center");
    table.setColumnAlignment(7,"center");
    table.setColumnAlignment(8,"left");

    table.setWidth(3,cellWidth);
    table.setWidth(4,cellWidth);
    table.setWidth(5,cellWidth);
    table.setWidth(6,cellWidth);
    table.setWidth(7,cellWidth);
    table.setWidth(9,"140");

    return table;

  }

  public void deleteBooking(IWContext iwc) throws RemoteException, FinderException{
    String lBookingId = iwc.getParameter(this.parameterBookingId);
    if (lBookingId != null) {
      getBooker(iwc).deleteBooking(Integer.parseInt(lBookingId));
    }
  }

  private TourBusiness getTourBusiness(IWApplicationContext iwac) throws RemoteException {
    return (TourBusiness) IBOLookup.getServiceInstance(iwac, TourBusiness.class);
  }
}
