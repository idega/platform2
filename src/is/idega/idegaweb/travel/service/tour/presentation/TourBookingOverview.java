package is.idega.idegaweb.travel.service.tour.presentation;

import is.idega.idegaweb.travel.business.ServiceNotFoundException;
import is.idega.idegaweb.travel.business.TimeframeNotFoundException;
import is.idega.idegaweb.travel.data.Contract;
import is.idega.idegaweb.travel.data.ContractHome;
import is.idega.idegaweb.travel.data.GeneralBooking;
import is.idega.idegaweb.travel.data.Inquery;
import is.idega.idegaweb.travel.data.Service;
import is.idega.idegaweb.travel.data.ServiceDay;
import is.idega.idegaweb.travel.data.ServiceDayHome;
import is.idega.idegaweb.travel.interfaces.Booking;
import is.idega.idegaweb.travel.presentation.BookingDeleterWindow;
import is.idega.idegaweb.travel.presentation.LinkGenerator;
import is.idega.idegaweb.travel.presentation.VoucherWindow;
import is.idega.idegaweb.travel.service.presentation.AbstractBookingOverview;
import is.idega.idegaweb.travel.service.presentation.BookingForm;
import is.idega.idegaweb.travel.service.tour.business.TourBooker;
import is.idega.idegaweb.travel.service.tour.business.TourBusiness;
import is.idega.idegaweb.travel.service.tour.business.TourNotFoundException;
import is.idega.idegaweb.travel.service.tour.data.Tour;
import is.idega.idegaweb.travel.service.tour.data.TourHome;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.data.Product;
import com.idega.block.trade.stockroom.data.ProductHome;
import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.business.IBOLookup;
import com.idega.data.IDOLookup;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.idegaweb.presentation.CalendarParameters;
import com.idega.presentation.IWContext;
import com.idega.presentation.Table;
import com.idega.presentation.text.Link;
import com.idega.presentation.text.Text;
import com.idega.user.data.User;
import com.idega.user.data.UserHome;
import com.idega.util.IWCalendar;
import com.idega.util.IWTimestamp;

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
  private IWResourceBundle _iwrb;
  private Supplier _supplier;
  private Reseller _reseller;
  private Service _service;

  public TourBookingOverview() {
  }

  public TourBookingOverview(IWContext iwc) throws RemoteException{
    super.main(iwc);
    this.init(iwc);
  }

  private void init(IWContext iwc) throws RemoteException{
    _tHome = (TourHome) IDOLookup.getHome(Tour.class);
    _iwrb = super.getTravelSessionManager(iwc).getIWResourceBundle();
    _supplier = super.getTravelSessionManager(iwc).getSupplier();
    _reseller = super.getTravelSessionManager(iwc).getReseller();
  }

  public Table getBookingOverviewTable(IWContext iwc, Collection products) throws CreateException, RemoteException, FinderException {
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
      ServiceDay sDay;// = sDayHome.create();

      toStamp.addDays(1);
      while (toStamp.isLaterThan(tempStamp)) {
        dayOfWeek = cal.getDayOfWeek(tempStamp.getYear(), tempStamp.getMonth(), tempStamp.getDay());

        upALine = false;
        ++row;
        dateTextBold = (Text) theSmallBoldText.clone();
        cal = new IWCalendar(tempStamp);
        dateTextBold.setText(cal.getLocaleDate());
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
              bContinue = getTravelStockroomBusiness(iwc).getIfDay(iwc,getContract(iwc, prod),prod,tempStamp);
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
	            	iCount = getTourBusiness(iwc).getMaxBookings(prod, tempStamp);
//                if (sDay != null) {
//                  iCount = sDay.getMax();
//                  if (iCount < 1) {
//                    iCount = _tour.getTotalSeats();
//                  }
//                }else {
//                  iCount = _tour.getTotalSeats();
//                }
//	            	if (iCount < 1) {
//	            		iCount = _tour.getTotalSeats();
//	            	}
	            	
//                iCount = _tour.getTotalSeats();
//	            	if (iCount == BookingForm.UNLIMITED_AVAILABILITY) {
//	            		iCount = 0;
//	            	}

                iBooked = getTourBooker(iwc).getBookingsTotalCount(((Integer) service.getPrimaryKey()).intValue(), tempStamp, -1);
                iAssigned = getAssigner(iwc).getNumberOfAssignedSeats(prod, tempStamp);

                int resellerBookings = getBooker(iwc).getBookingsTotalCountByResellers(((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                if (iAssigned != 0) {
                  iAssigned = iAssigned - resellerBookings;
                }

                iInquery = getInquirer(iwc).getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(), tempStamp, true);//getInqueredSeats(service.getID() ,tempStamp, true);
                //iInquery = Inquirer.getInqueredSeats(service.getID() ,tempStamp, true);
                iAvailable = iCount - iBooked - iAssigned;
              }else if (_reseller != null) {
                iCount = getContract(iwc, prod).getAlotment();
                iBooked = getBooker(iwc).getBookingsTotalCountByReseller(_reseller.getID() ,((Integer) service.getPrimaryKey()).intValue(), tempStamp);
                iAssigned = 0;

                iInquery = getInquirer(iwc).getInquiryHome().getInqueredSeats(((Integer) service.getPrimaryKey()).intValue(),tempStamp,_reseller.getID(), true);
                iAvailable = iCount - iBooked - iAssigned -iInquery;
                //iCount = iCount -iBooked;
              }
//              countTextBold.setText(Integer.toString(iCount));


              nameTextBold  = (Text) theSmallBoldText.clone();
              nameTextBold.setText(service.getName(super.getTravelSessionManager(iwc).getLocaleId() ));

              assignedTextBold = (Text) theSmallBoldText.clone();
              assignedTextBold.setText(Integer.toString(iAssigned));
              countTextBold = (Text) theSmallBoldText.clone();
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
              if (iCount != BookingForm.UNLIMITED_AVAILABILITY) {
                table.add(countTextBold,3,row);
                table.add(availableTextBold,7,row);
              }

              table.add(btnNanar,8,row);
              if (_supplier != null) {
                table.add(Text.NON_BREAKING_SPACE+Text.NON_BREAKING_SPACE,8,row);
                table.add(btnBook,8,row);
              } else if (_reseller != null) {
                if (!getTravelStockroomBusiness(iwc).getIfExpired(getContract(iwc, prod), tempStamp))
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



  public Table getDetailedInfo(IWContext iwc, Product product, IWTimestamp stamp) throws FinderException, CreateException, RemoteException {
    _tour = _tHome.findByPrimaryKey(product.getPrimaryKey());

    Contract contract = null;
    if ((_reseller != null) && (product != null)){
      try {
      		ContractHome cHome = (ContractHome) IDOLookup.getHome(Contract.class);
  				contract = cHome.findByProductAndReseller(product.getID(), _reseller.getID());
        /*  Contract[] contracts = (Contract[]) (is.idega.idegaweb.travel.data.ContractBMPBean.getStaticInstance(Contract.class)).findAllByColumn(is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameResellerId(), Integer.toString(_reseller.getID()), is.idega.idegaweb.travel.data.ContractBMPBean.getColumnNameServiceId(), Integer.toString(product.getID()) );
          if (contracts.length > 0) {
            contract = contracts[0];
          }*/
      }catch (FinderException sql) {
          sql.printStackTrace(System.err);
      }

    }


    Table table = new Table();
    table.setWidth("95%");
    table.setBorder(0);
    table.setCellspacing(1);
    table.setCellpadding(2);
    table.setColor(super.WHITE);

    int row = 1;

    Text dateTextBold = (Text) theSmallBoldText.clone();
    Text nameTextBold = (Text) theSmallBoldText.clone();
    Text countTextBold = (Text) theSmallBoldText.clone();
    Text assignedTextBold = (Text) theSmallBoldText.clone();
    Text inqTextBold = (Text) theSmallBoldText.clone();
    Text bookedTextBold = (Text) theSmallBoldText.clone();
    Text availableTextBold = (Text) theSmallBoldText.clone();
    Text hotelPickupTextBold = (Text) theSmallBoldText.clone();

    table.add(getHeaderText(_iwrb.getLocalizedString("travel.date","Date")), 1, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.product_name","Product name")), 2, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.count","Count")), 3, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.assigned","Assigned")), 4, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.inquiries","Inquiries")), 5, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.booked","Booked")), 6, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.available","Available")), 7, row);
    table.add(getHeaderText(_iwrb.getLocalizedString("travel.booked_by","Booked by")), 8, row);
    table.add(getHeaderText(Text.NON_BREAKING_SPACE), 9, row);


    table.setRowColor(row, super.backgroundColor);

//            IWTimestamp currentStamp = new IWTimestamp(view_date);
    int seats = 0;
    int assigned = 0;
    int iInqueries = 0;
    int booked = 0;
    int available = 0;

    if (_supplier != null) {
    	seats = getTourBusiness(iwc).getMaxBookings(product, stamp);
//    	if (seats < 1) {
//    		seats = _tour.getTotalSeats();
//    	}
//    	if (seats == BookingForm.UNLIMITED_AVAILABILITY) {
//    		seats = 0;
//    	}
//      ServiceDayHome sDayHome = (ServiceDayHome) IDOLookup.getHome(ServiceDay.class);
//      ServiceDay sDay;// = sDayHome.create();
//      sDay = sDayHome.findByServiceAndDay(((Integer) product.getPrimaryKey()).intValue(), stamp.getDayOfWeek());
//      if (sDay != null) {
//        seats = sDay.getMax();
//        if (seats < 1) {
//          seats = _tour.getTotalSeats();
//        }
//      }else {
//        seats = _tour.getTotalSeats();
//      }

//      seats = _tour.getTotalSeats();
      assigned = getAssigner(iwc).getNumberOfAssignedSeats(((Integer) product.getPrimaryKey()).intValue(), stamp);
      iInqueries = getInquirer(iwc).getInqueredSeats(product.getID() , stamp, true);
      booked = getBooker(iwc).getBookingsTotalCount(product.getID(), stamp, -1);
      available = seats - booked;
    }else if (_reseller != null) {
      seats = contract.getAlotment();
      assigned = 0;
      iInqueries = getInquirer(iwc).getInquiryHome().getInqueredSeats(product.getID() , stamp, _reseller.getID(), true);
      booked = getBooker(iwc).getBookingsTotalCountByReseller(_reseller.getID(),product.getID(), stamp);
      available = seats - booked - iInqueries;
    }

    IWCalendar cal =new IWCalendar(stamp);
    dateTextBold.setText(cal.getLocaleDate());
    nameTextBold.setText(product.getProductName(super.getLocaleId()));
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
    if (seats != BookingForm.UNLIMITED_AVAILABILITY) {
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
      Reseller[] resellers = getContractBusiness(iwc).getResellers(product.getID(), stamp);
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
              Temail.addToText( ((com.idega.core.contact.data.Email) emails.get(j)).getEmailAddress());
            }
          }
          }catch (SQLException sql) {sql.printStackTrace(System.err);}
          //                Temail.setText(_reseller[i].getEmail());
          Tbooked = (Text) super.theSmallBoldText.clone();
          Tbooked.setFontColor(super.BLACK);
          Tbooked.setText(Integer.toString(getAssigner(iwc).getNumberOfAssignedSeats(product.getID(), resellers[i].getID() ,stamp)));

          table.mergeCells(2,row,3, row);
          table.add(Tname,1,row);
          table.add(Temail,3,row);
          table.setAlignment(3,row,"left");
          table.add(Tbooked,4,row);
          table.setRowColor(row, super.GRAY);
      }
    }


    //List addresses = product.getDepartureAddresses(true);
		List addresses = super.getProductBusiness(iwc).getDepartureAddresses(product, stamp, true);    TravelAddress trAddress;
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
      Link answerLink = new Link(_iwrb.getLocalizedImageButton("travel.answer","Answer"),is.idega.idegaweb.travel.presentation.Booking.class);
      answerLink.addParameter(CalendarParameters.PARAMETER_YEAR, stamp.getYear());
      answerLink.addParameter(CalendarParameters.PARAMETER_MONTH, stamp.getMonth());
      answerLink.addParameter(CalendarParameters.PARAMETER_DAY, stamp.getDay());
      Inquery[] inqueries = null;

      int[] iNumbers;
      if (_supplier != null) inqueries = getInquirer(iwc).getInqueries(product.getID(), stamp, true, trAddress, is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
      if (_reseller != null) {
        //inqueries = inqueries = getInquirer(iwc).getInqueries(product.getID(), stamp, true, trAddress, is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
                      Collection coll = getInquirer(iwc).getInquiryHome().findInqueries(product.getID(), stamp, _reseller.getID(),true, trAddress,is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName());
                      inqueries = getInquirer(iwc).collectionToInqueryArray(coll);
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

      //            int tempBookingTest = getBooker(iwc).getGeneralBookingHome().getNumberOfBookings(product.getID(), stamp, null, -1, new int[]{}, getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(product, trAddress) );
      //            table.add(getHeaderText(Integer.toString(tempBookingTest)), 6, row);

      // ------------------ BOOKINGS ------------------------
      Link changeLink = new Link(_iwrb.getImage("buttons/change.gif"),is.idega.idegaweb.travel.presentation.Booking.class);
      Link deleteLink = new Link(_iwrb.getImage("buttons/delete.gif"));
      deleteLink.setWindowToOpen(BookingDeleterWindow.class);
      Booking[] bookings = {};
      GeneralBooking booking;
      int[] bNumbers;

      if (this._supplier != null) {
        bookings = getBooker(iwc).getBookings(((Integer) product.getPrimaryKey()).intValue(), stamp, trAddress);
      }else if (this._reseller != null) {
        Collection coll = getBooker(iwc).getGeneralBookingHome().findBookings(_reseller.getID(), product.getID(), stamp, trAddress);
        bookings = getBooker(iwc).collectionToBookingsArray(coll);
      }

      Object serviceType;
      User bUser;
      int depAddressId = -1;
      TravelAddress ta;
      Reseller bReseller;
      int idForLink = -1;
      for (int i = 0; i < bookings.length; i++) {
        ++row;
        booking = ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(bookings[i].getPrimaryKey());
        serviceType = getBooker(iwc).getServiceType(product.getID());
        ta = getBooker(iwc).getDepartureAddress(booking);
        if (ta != null) {
        	depAddressId = ta.getID();
        }else {
        	depAddressId = -1;
        }
        

        Tname = (Text) super.theSmallBoldText.clone();
        Tname.setText(bookings[i].getName());
        bNumbers = getBooker(iwc).getMultipleBookingNumber(booking);
        if ( bNumbers[0] != 0 ) {
          Tname.addToText(Text.NON_BREAKING_SPACE+"( "+bNumbers[0]+" / "+bNumbers[1]+" )");
          idForLink = bNumbers[2];
        }else {
        	idForLink = booking.getID();
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
	        bUser = ((UserHome)com.idega.data.IDOLookup.getHome(User.class)).findByPrimaryKey(new Integer(bookings[i].getUserId()));
	        bReseller = getResellerManager(iwc).getReseller(bUser);
	        TbookedBy.setText(bUser.getName());
	        if (bReseller != null) {
	          if (this._reseller != bReseller) {
	            TbookedBy.addToText(" ( "+bReseller.getName()+" ) ");
	          }
	        }
        }else {
          TbookedBy.setText(_iwrb.getLocalizedString("travel.online","Online"));
        }

        link = VoucherWindow.getVoucherLink(idForLink);
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
        link.addParameter(is.idega.idegaweb.travel.presentation.Booking.parameterBookingId,idForLink);
				link.addParameter(BookingForm.parameterDepartureAddressId, trAddress.getID());
        table.add(link, 9, row);
        table.add(Text.NON_BREAKING_SPACE,9,row);

        link = (Link) deleteLink.clone();
        link.addParameter(BookingDeleterWindow.bookingIdParameter,idForLink);
        table.add(link, 9, row);

      }

      table.add(getHeaderText(Integer.toString(seats)), 3, tempRow);
      table.add(getHeaderText(Integer.toString(assigned)), 4, tempRow);
      table.add(getHeaderText(Integer.toString(tempInq)), 5, tempRow);
      table.add(getHeaderText(Integer.toString(tempTotal)), 6, tempRow);
      if (seats != BookingForm.UNLIMITED_AVAILABILITY) {
        travelAddressIds = super.getTravelStockroomBusiness(iwc).getTravelAddressIdsFromRefill(product, trAddress);
        // TODO Mismunandi eftir hvort supplier eda reseller...
        tempAvail = seats - getBooker(iwc).getGeneralBookingHome().getBookingsTotalCount(( (Integer) product.getPrimaryKey()).intValue(), stamp, null, -1, new int[]{}, travelAddressIds );
        table.add(getHeaderText(Integer.toString(tempAvail)), 7, tempRow);
      }

      Link daLink = LinkGenerator.getLink(iwc, product.getID(), is.idega.idegaweb.travel.presentation.Booking.class);
      daLink.addParameter(TourBookingForm.parameterDepartureAddressId, trAddress.getID());
      daLink.setPresentationObject(_iwrb.getImage("buttons/book.gif"));
      table.add(Text.NON_BREAKING_SPACE, 1, tempRow);
      table.add(daLink, 9, tempRow);
      table.setAlignment(9, tempRow,  Table.HORIZONTAL_ALIGN_RIGHT);
    }

    ++row;
    table.mergeCells(1,row,6,row);
    availableTextBold.setText(Integer.toString(available));
    availableTextBold.setFontColor(super.BLACK);
    Text Tavail = (Text) super.theSmallBoldText.clone();
    Tavail.setFontColor(super.BLACK);
    Tavail.setText(_iwrb.getLocalizedString("travel.available_seats","Available seats"));
    if (seats != BookingForm.UNLIMITED_AVAILABILITY) {
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

    table.setWidth(3,CELL_WIDTH);
    table.setWidth(4,CELL_WIDTH);
    table.setWidth(5,CELL_WIDTH);
    table.setWidth(6,CELL_WIDTH);
    table.setWidth(7,CELL_WIDTH);
    table.setWidth(9,"140");

    return table;
  }


  protected TourBusiness getTourBusiness(IWContext iwc) throws RemoteException{
    return (TourBusiness) IBOLookup.getServiceInstance(iwc, TourBusiness.class);

  }

  public TourBooker getTourBooker(IWContext iwc) throws RemoteException {
    return (TourBooker) IBOLookup.getServiceInstance(iwc, TourBooker.class);
  }


}