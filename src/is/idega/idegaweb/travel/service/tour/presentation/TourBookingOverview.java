package is.idega.idegaweb.travel.service.tour.presentation;

import java.rmi.*;
import java.sql.*;
import java.util.*;

import javax.ejb.*;

import com.idega.block.trade.stockroom.data.*;
import com.idega.business.*;
import com.idega.data.*;
import com.idega.presentation.*;
import com.idega.presentation.text.*;
import com.idega.util.*;
import is.idega.idegaweb.travel.business.*;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.service.presentation.*;
import is.idega.idegaweb.travel.service.tour.business.*;
import is.idega.idegaweb.travel.service.tour.data.*;

/**
 * <p>Title: idegaWeb TravelBooking</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: idega</p>
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TourBookingOverview extends AbstractBookingOverview {

  private String CELL_WIDTH = "50";
  private Tour _tour;
  private TourHome _tHome;

  public TourBookingOverview() {
  }

  public TourBookingOverview(IWContext iwc) throws RemoteException{
    super.main(iwc);
    this.init(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    _tHome = (TourHome) IDOLookup.getHome(Tour.class);
  }

  public Table getBookingOverviewTable(IWContext iwc, Collection products) throws CreateException, RemoteException, FinderException {
    System.out.println("Repps, getting if possible");
      Table table = new Table();
      table.setCellspacing(1);
      table.setCellpadding(4);
      table.setColor(super.WHITE);
      table.setWidth("90%");
      int row = 1;

      Text dateText = (Text) theText.clone();
      dateText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.date_sm","date"));
      Text nameText = (Text) theText.clone();
      nameText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.product_name_sm","name of product"));
      Text countText = (Text) theText.clone();
      countText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.count_sm","count"));
      Text assignedText = (Text) theText.clone();
      assignedText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.assigned_small_sm","assigned"));
      Text inqText = (Text) theText.clone();
      inqText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.inqueries_sm","inquiries"));
      Text bookedText = (Text) theText.clone();
      bookedText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.booked_sm","booked"));
      Text availableText = (Text) theText.clone();
      availableText.setText(super.getTravelSessionManager(iwc).getIWResourceBundle().getLocalizedString("travel.available_small_sm","avail."));


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

      Product prod;
      int supplierId = 0;
      if (_supplier  != null)
        supplierId = _supplier.getID();
      IWCalendar cal = new IWCalendar();
      int dayOfWeek;
      boolean upALine = false;

      Service service;

      int iCount = 0;
      int iBooked =0;
      int iInquery=0;
      int iAvailable=0;
      int iAssigned=0;

      String theColor = super.GRAY;

      IWTimestamp fromStamp = super.getTimestampFrom(iwc);
      IWTimestamp toStamp = super.getTimestampTo(iwc);

      IWTimestamp tempStamp = new IWTimestamp(fromStamp);
      ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
      ServiceDay sDay = sDayHome.create();

      toStamp.addDays(1);
      while (toStamp.isLaterThan(tempStamp)) {
        dayOfWeek = cal.getDayOfWeek(tempStamp.getYear(), tempStamp.getMonth(), tempStamp.getDay());

        upALine = false;
        ++row;
        dateTextBold = (Text) theSmallBoldText.clone();
        dateTextBold.setText(tempStamp.getLocaleDate(iwc));
        dateTextBold.setFontColor(super.BLACK);
        table.add(dateTextBold,1,row);
        table.setRowColor(row, super.GRAY);

        boolean bContinue= false;
        ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);

        Iterator iter = products.iterator();
        while (iter.hasNext()) {
          try {
            prod = (Product) iter.next();

            if (_supplier != null) {
              bContinue = getTravelStockroomBusiness(iwc).getIfDay(iwc,prod,tempStamp);
            }else if (_reseller != null) {
              bContinue = getTravelStockroomBusiness(iwc).getIfDay(iwc,_contract,prod,tempStamp);
            }
            if (bContinue) {
              iCount = 0;
              iBooked =0;
              iInquery=0;
              iAvailable=0;
              iAssigned=0;
              service = getTravelStockroomBusiness(iwc).getService(prod);
              _tour = getTourBusiness(iwc).getTour(prod);

              if (_supplier != null) {
                sDay = sDay.getServiceDay(((Integer) service.getPrimaryKey()).intValue(), tempStamp.getDayOfWeek());
                if (sDay != null) {
                  iCount = sDay.getMax();
                  if (iCount < 1) {
                    iCount = _tour.getTotalSeats();
                  }
                }else {
                  iCount = _tour.getTotalSeats();
                }

//                iCount = _tour.getTotalSeats();
                iBooked = getBooker(iwc).getNumberOfBookings(((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                iAssigned = getAssigner(iwc).getNumberOfAssignedSeats(prod, tempStamp);

                int resellerBookings = getBooker(iwc).getNumberOfBookingsByResellers(((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                if (iAssigned != 0) {
                  iAssigned = iAssigned - resellerBookings;
                }

                iInquery = getInquirer(iwc).getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(), tempStamp, true);//getInqueredSeats(service.getID() ,tempStamp, true);
                //iInquery = Inquirer.getInqueredSeats(service.getID() ,tempStamp, true);
                iAvailable = iCount - iBooked - iAssigned;
              }else if (_reseller != null) {
                iCount = _contract.getAlotment();
                iBooked = getBooker(iwc).getNumberOfBookingsByReseller(_reseller.getID() ,((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                iAssigned = 0;

                iInquery = getInquirer(iwc).getInquiryHome().getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(),tempStamp,_reseller.getID(), true);
                iAvailable = iCount - iBooked - iAssigned -iInquery;
                iCount = iCount -iBooked;
              }
              countTextBold.setText(Integer.toString(iCount));


              nameTextBold  = (Text) theSmallBoldText.clone();
              nameTextBold.setText(service.getName(super.getTravelSessionManager(iwc).getLocaleId() ));

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

              Link btnNanar = new Link(super.getTravelSessionManager(iwc).getIWResourceBundle().getImage("buttons/closer.gif"));
              btnNanar.addParameter(PARAMETER_CLOSER_LOOK_DATE,tempStamp.toSQLDateString());
              btnNanar.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterProductId, prod.getID());
              Link btnBook = new Link(super.getTravelSessionManager(iwc).getIWResourceBundle().getImage("buttons/book.gif"), is.idega.idegaweb.travel.presentation.Booking.class);
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
              if (_supplier != null) {
                table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,8,row);
                table.add(btnBook,8,row);
              } else if (_reseller != null) {
                if (!getTravelStockroomBusiness(iwc).getIfExpired(_contract, tempStamp))
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



      table.setWidth(3,CELL_WIDTH);
      table.setWidth(4,CELL_WIDTH);
      table.setWidth(5,CELL_WIDTH);
      table.setWidth(6,CELL_WIDTH);
      table.setWidth(7,CELL_WIDTH);

      table.setColumnAlignment(1,"left");
      table.setColumnAlignment(2,"left");
      table.setColumnAlignment(3,"center");
      table.setColumnAlignment(4,"center");
      table.setColumnAlignment(5,"center");
      table.setColumnAlignment(6,"center");
      table.setColumnAlignment(7,"center");
      table.setColumnAlignment(8,"right");

      return table;
  }


  protected TourBusiness getTourBusiness(IWContext iwc) throws RemoteException{
    return (TourBusiness) IBOLookup.getServiceInstance(iwc, TourBusiness.class);

  }
}