package is.idega.idegaweb.travel.business;

import java.sql.SQLException;
import java.util.*;
import com.idega.util.idegaTimestamp;
import com.idega.data.*;
import com.idega.block.trade.stockroom.data.*;
import is.idega.idegaweb.travel.data.Inquery;
import com.idega.presentation.*;
import com.idega.presentation.ui.*;
import com.idega.presentation.text.*;

import is.idega.idegaweb.travel.presentation.TravelManager;
import com.idega.idegaweb.IWResourceBundle;
import com.idega.presentation.IWContext;
import is.idega.idegaweb.travel.data.*;
import is.idega.idegaweb.travel.interfaces.Booking;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Inquirer {

  public Inquirer() {
  }

  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, boolean unansweredOnly) {
    return Inquirer.getInqueredSeats(serviceId, stamp, -1, unansweredOnly);
  }

  public static int getInqueredSeats(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly) {
    int returner = 0;
    try {
      Inquery inq = (Inquery) Inquery.getStaticInstance(Inquery.class);
      Reseller res = (Reseller)Reseller.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT sum(i."+Inquery.getNumberOfSeatsColumnName()+") FROM "+Inquery.getInqueryTableName()+" i");
        if (resellerId != -1) {
          buffer.append(", "+Reseller.getResellerTableName()+" r, "+middleTable+" mi");
        }
        buffer.append(" WHERE ");
        if (resellerId != -1) {
          buffer.append("i."+inq.getIDColumnName()+" = mi."+inq.getIDColumnName());
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());
          buffer.append(" AND ");
        }

        if (unansweredOnly) {
        buffer.append("i."+Inquery.getAnsweredColumnName() +" = 'N'");
        }

        buffer.append(" AND ");
        buffer.append("i."+Inquery.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+Inquery.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = "+resellerId);
        }
      String[] bufferReturn = SimpleQuerier.executeStringQuery(buffer.toString());
      if (bufferReturn != null)
        if (bufferReturn.length > 0) {
          if (bufferReturn[0] != null)
          returner = Integer.parseInt(bufferReturn[0]);
        }

    }catch (Exception e) {
      e.printStackTrace(System.err);
    }
    return returner;
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, boolean unansweredOnly) {
    return Inquirer.getInqueries(serviceId, stamp, -1, unansweredOnly, Inquery.getInqueryDateColumnName());
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, boolean unansweredOnly, String orderBy) {
    return Inquirer.getInqueries(serviceId, stamp, -1, unansweredOnly, orderBy);
  }


  public static Inquery[] getInqueries(int serviceId, idegaTimestamp stamp, int resellerId, boolean unansweredOnly, String orderBy) {
    Inquery[] inqueries = {};
    if (orderBy == null) orderBy = "";
    try {
      Inquery inq = (Inquery) Inquery.getStaticInstance(Inquery.class);
      Reseller res = (Reseller)Reseller.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT i.* FROM "+Inquery.getInqueryTableName()+" i");
        if (resellerId != -1) {
          buffer.append(" , "+Reseller.getResellerTableName()+" r, "+middleTable+" mi");
        }
        buffer.append(" WHERE ");
        if (resellerId != -1) {
          buffer.append("i."+inq.getIDColumnName()+" = mi."+inq.getIDColumnName());
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());
          buffer.append(" AND ");
        }

        if (unansweredOnly) {
        buffer.append("i."+Inquery.getAnsweredColumnName() +" = 'N'");
        }

        buffer.append(" AND ");
        buffer.append("i."+Inquery.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+Inquery.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = "+resellerId);
        }

        if (!orderBy.equals("")) {
          buffer.append(" ORDER BY "+orderBy);
        }

      inqueries = (Inquery[]) (Inquery.getStaticInstance(Inquery.class)).findAll(buffer.toString());
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }

    return inqueries;
  }

  public static int sendInquery(String name,String email, idegaTimestamp inqueryDate, int productId, int numberOfSeats, int bookingId, Reseller reseller) throws SQLException {
    String sInquery = "Are the available seats this day";


    int returner = -1;
        Inquery inq = new Inquery();
          inq.setAnswered(false);
          inq.setEmail(email);
          inq.setInqueryDate(inqueryDate.getTimestamp());
          inq.setInquery(sInquery);
          inq.setInqueryPostDate(idegaTimestamp.getTimestampRightNow());
          inq.setName(name);
          inq.setServiceID(productId);
          inq.setNumberOfSeats(numberOfSeats);
          inq.setBookingId(bookingId);
        inq.insert();

        if (reseller != null) {
          inq.addTo(reseller);
        }
      returner = inq.getID();
    return returner;
  }

  public static int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, Supplier supplier) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, true, supplier, null);
  }

  public static int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, boolean sendMail, Supplier supplier) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, sendMail ,supplier, null);
  }

  public static int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, Supplier supplier, Reseller reseller) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, true, supplier, reseller);
  }

  public static int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, boolean sendMail, Supplier supplier, Reseller reseller) {
    String mailHost = "mail.idega.is";

    String mailSubject = "NAT "+iwrb.getLocalizedString("travel.idega.inquiry","Inquiry");
    StringBuffer responseString = new StringBuffer();

    javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
        tm.begin();
        com.idega.util.SendMail sm = new com.idega.util.SendMail();
        Inquery inquery = new Inquery(inquiryId);
        Booking booking = inquery.getBooking();
        Service tempService = booking.getService();
        List inquiries = getMultibleInquiries(inquery);

        responseString.append(iwrb.getLocalizedString("travel.dear","Dear"));
        responseString.append(" "+inquery.getName()+",\n\n");
        responseString.append(iwrb.getLocalizedString("travel.regarding_you_inquiry_about","Regarding your inquiry about"));
        responseString.append(" "+inquery.getNumberOfSeats()+" ");
        responseString.append(iwrb.getLocalizedString("travel.spaces_for_the_service","spaces for the service"));
        responseString.append(" \""+tempService.getName()+"\" ");
        if (inquiries.size() == 1) {
          responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
          responseString.append(" "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
        }else {
          booking = ((Inquery) inquiries.get(0)).getBooking();
          Booking booking2 = ((Inquery) inquiries.get(inquiries.size()-1)).getBooking();
          responseString.append(new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+" - "+new idegaTimestamp(booking2.getBookingDate()).getLocaleDate(iwc));
        }
        responseString.append("\n\n");

        /**
         * @todo hondlar svara inquiry sem er hluti af grúbbu....
         */


        if (book == false) {
            responseString.append(iwrb.getLocalizedString("travel.request_is_denied","Request is denied."));
        }else if (book == true) {
            responseString.append(iwrb.getLocalizedString("travel.request_is_granted_booking_confirmed","Request is granted. Booking has been confimed"));
        }

        for (int i = 0; i < inquiries.size(); i++) {
          inquery = (Inquery) inquiries.get(i);
          inquery.setAnswered(true);
          inquery.setAnswerDate(idegaTimestamp.getTimestampRightNow());
          inquery.update();
          if (book) {
            booking = inquery.getBooking();
            booking.setIsValid(true);
            booking.update();
          }
        }


        Reseller[] resellers = (Reseller[]) inquery.findRelated((Reseller) Reseller.getStaticInstance(Reseller.class));
        try {
          if (supplier != null) {
            if (sendMail) {
              sm.send(supplier.getEmail().getEmailAddress(),inquery.getEmail(), "","",mailHost,mailSubject,responseString.toString());
            }
            if (reseller == null) {  // if this is not a reseller deleting his own inquiry
              if (resellers != null) { // if there was a reseller who send the inquiry
                responseString = new StringBuffer();
                responseString.append(iwrb.getLocalizedString("travel.regarding_you_inquiry_about","Regarding your inquiry about"));
                responseString.append(" "+inquery.getNumberOfSeats()+" ");
                responseString.append(iwrb.getLocalizedString("travel.spaces_for_the_service","spaces for the service"));
                responseString.append(" \""+tempService.getName()+"\" ");
                responseString.append(iwrb.getLocalizedString("travel.for","for"));
                responseString.append(" "+inquery.getName()+",\n\n");
                if (inquiries.size() == 1) {
                  responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
                  responseString.append(" "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
                }else {
                  booking = ((Inquery) inquiries.get(0)).getBooking();
                  Booking booking2 = ((Inquery) inquiries.get(inquiries.size()-1)).getBooking();
                  responseString.append(new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+" - "+new idegaTimestamp(booking2.getBookingDate()).getLocaleDate(iwc));
                }
//                responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
//                responseString.append(" "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
                responseString.append("\n\n");
                if (book == false) {
                    responseString.append(iwrb.getLocalizedString("travel.request_is_denied","Request is denied."));
                }else if (book == true) {
                    responseString.append(iwrb.getLocalizedString("travel.request_is_granted_booking_confirmed","Request is granted. Booking has been confimed"));
                }

                //responseString.append("T - Svar við fyrirspurn varðandi "+inquery.getNumberOfSeats()+" sæti fyrir \""+inquery.getName()+"\" í ferðina \""+tempService.getName()+"\" þann "+new idegaTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+"\n");
                for (int i = 0; i < resellers.length; i++) {
                  if (resellers[i].getEmail() != null) {
                    if (sendMail) {
                      sm.send(supplier.getEmail().getEmailAddress(),resellers[i].getEmail().getEmailAddress(), "","",mailHost,mailSubject,responseString.toString());
                    }
                  }
                }
              }
            }
          }
          tm.commit();
        }catch (javax.mail.internet.AddressException ae) {
          throw ae;
        }
        return 0;
      }catch (Exception e) {
        e.printStackTrace(System.err);
        try {
          tm.rollback();
        }catch (javax.transaction.SystemException sy) {
          sy.printStackTrace(System.err);
        }
        return 1;
        //displayForm(iwc, getInquiryResponseError());
      }

  }

  public static Table getInquiryResponseError(IWResourceBundle iwrb) {
    Table table = new Table();
      Text text = new Text();
        text.setFontStyle(TravelManager.theTextStyle);
        text.setFontColor(TravelManager.WHITE);
        text.setText(iwrb.getLocalizedString("travel.error_in_inquiry_response","Operation not completed. The e-mail addresses are probably wrong."));
      table.add(text,1,1);
      table.add(new BackButton(iwrb.getImage("buttons/back.gif")),1,2);
      table.add(Text.NON_BREAKING_SPACE,1,3);

    return table;
  }

  public static List getMultibleInquiries(Inquery inquiry) {
    List list = new Vector();
    try {

      StringBuffer buff = new StringBuffer();
        buff.append("SELECT * FROM "+Inquery.getInqueryTableName());
        buff.append(" WHERE ");
        if (inquiry.getAnswerDate() != null) {
          buff.append(Inquery.getAnswerDateColumnName()+" = '"+inquiry.getAnswerDate()+"'");
        }else {
          buff.append(Inquery.getAnswerDateColumnName()+" is null");
        }
        buff.append(" AND ");
        if (inquiry.getAnswered()) {
          buff.append(Inquery.getAnsweredColumnName()+" = 'Y'");
        }else {
          buff.append(Inquery.getAnsweredColumnName()+" = 'N'");
        }
        buff.append(" AND ");
        buff.append(Inquery.getEmailColumnName()+" = '"+inquiry.getEmail()+"'");
        buff.append(" AND ");
        buff.append(Inquery.getInqueryColumnName()+" = '"+inquiry.getInquery()+"'");
        buff.append(" AND ");
        buff.append(Inquery.getInqueryPostDateColumnName()+" = '"+inquiry.getInqueryPostDate()+"'");
        buff.append(" AND ");
        buff.append(Inquery.getNameColumnName()+" = '"+inquiry.getName()+"'");
        buff.append(" AND ");
        buff.append(Inquery.getNumberOfSeatsColumnName()+" = "+inquiry.getNumberOfSeats());
        buff.append(" AND ");
        buff.append(Inquery.getServiceIDColumnName()+" = "+inquiry.getServiceID());
        buff.append(" ORDER BY "+Inquery.getInqueryDateColumnName());

        //System.err.println(buff.toString());
      list = EntityFinder.findAll(inquiry, buff.toString());

    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return list;
  }


}
