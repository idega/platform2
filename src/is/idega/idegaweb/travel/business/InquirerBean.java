package is.idega.idegaweb.travel.business;

import com.idega.business.IBOLookup;
import com.idega.business.IBOServiceBean;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import java.rmi.RemoteException;

import com.idega.core.contact.data.Email;
import com.idega.util.SendMail;
import com.idega.block.trade.stockroom.business.ProductBusiness;
import javax.mail.MessagingException;
import java.sql.SQLException;
import java.util.*;
import com.idega.util.IWTimestamp;
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

public class InquirerBean extends IBOServiceBean implements Inquirer{

  public InquirerBean() {
  }

  public int getInqueredSeats(int serviceId, IWTimestamp stamp, boolean unansweredOnly) throws RemoteException, FinderException{
    return getInquiryHome().getInqueredSeats(serviceId, stamp, -1, unansweredOnly);
  }

  /**
   * @deprecated
   */
  public Inquery[] getInqueries(int serviceId, IWTimestamp stamp, boolean unansweredOnly) throws RemoteException, FinderException {
    return this.collectionToInqueryArray(getInquiryHome().findInqueries(serviceId, stamp, -1, unansweredOnly, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryDateColumnName()));
  }

  /**
   * @deprecated
   */
  public Inquery[] getInqueries(int serviceId, IWTimestamp stamp, boolean unansweredOnly, String orderBy)  throws RemoteException, FinderException{
    return this.collectionToInqueryArray(getInquiryHome().findInqueries(serviceId, stamp, -1, unansweredOnly, orderBy));
  }

  public Inquery[] getInqueries(int serviceId, IWTimestamp stamp, boolean unansweredOnly, TravelAddress travelAddress)  throws RemoteException, FinderException{
    return getInqueries(serviceId, stamp, unansweredOnly, travelAddress, is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryDateColumnName());
  }
  public Inquery[] getInqueries(int serviceId, IWTimestamp stamp, boolean unansweredOnly, TravelAddress travelAddress, String orderBy)  throws RemoteException, FinderException{
    return this.collectionToInqueryArray(getInquiryHome().findInqueries(serviceId, stamp,-1, unansweredOnly, travelAddress, orderBy ));
  }

  public int sendInquery(String name,String email, IWTimestamp inqueryDate, int productId, int numberOfSeats, String comment, int bookingId, Reseller reseller)  throws RemoteException, FinderException, CreateException {
  		if ( comment == null || comment.equals("")) {
  			comment = "Are the available seats this day";
  		}
    try {
      int returner = -1;
          Inquery inq = ((is.idega.idegaweb.travel.data.InqueryHome)com.idega.data.IDOLookup.getHome(Inquery.class)).create();
            inq.setAnswered(false);
            inq.setEmail(email);
            inq.setInqueryDate(inqueryDate.getTimestamp());
            inq.setInquery(comment);
            inq.setInqueryPostDate(IWTimestamp.getTimestampRightNow());
            inq.setName(name);
            inq.setServiceID(productId);
            inq.setNumberOfSeats(numberOfSeats);
            inq.setBookingId(bookingId);
          inq.store();

          if (reseller != null) {
            inq.addReseller(reseller);
  //          inq.addTo(reseller);
          }
        returner = ((Integer)inq.getPrimaryKey()).intValue();
        System.err.println("Inquirer : inq.getID() = "+returner);
        return returner;
    }catch (IDOAddRelationshipException are) {
      throw new CreateException(are.getMessage());
    }
  }

  public int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, Supplier supplier) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, true, supplier, null);
  }

  public int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, boolean sendMail, Supplier supplier) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, sendMail ,supplier, null);
  }

  public int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, Supplier supplier, Reseller reseller) {
    return inquiryResponse(iwc, iwrb, inquiryId, book, true, supplier, reseller);
  }

  public int inquiryResponse(IWContext iwc, IWResourceBundle iwrb, int inquiryId, boolean book, boolean sendMail, Supplier supplier, Reseller reseller) {
    String mailHost = "mail.idega.is";

    String mailSubject = "NAT "+iwrb.getLocalizedString("travel.idega.inquiry","Inquiry");
    StringBuffer responseString = new StringBuffer();

    javax.transaction.TransactionManager tm = com.idega.transaction.IdegaTransactionManager.getInstance();
    try {
        tm.begin();
        com.idega.util.SendMail sm = new com.idega.util.SendMail();
        Inquery inquery = ((is.idega.idegaweb.travel.data.InqueryHome)com.idega.data.IDOLookup.getHome(Inquery.class)).findByPrimaryKey(new Integer(inquiryId));
        Booking booking = inquery.getBooking();
        Service tempService = booking.getService();
        List inquiries = getInquiryHome().create().getMultibleInquiries(inquery);

        responseString.append(iwrb.getLocalizedString("travel.dear","Dear"));
        responseString.append(" "+inquery.getName()+",\n\n");
        responseString.append(iwrb.getLocalizedString("travel.regarding_you_inquiry_about","Regarding your inquiry about"));
        responseString.append(" "+inquery.getNumberOfSeats()+" ");
        responseString.append(iwrb.getLocalizedString("travel.spaces_for_the_service","spaces for the service"));
        responseString.append(" \""+tempService.getName(iwc.getCurrentLocaleId())+"\" ");
        if (inquiries.size() == 1) {
          responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
          responseString.append(" "+new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
        }else {
          booking = getInquiryHome().findByPrimaryKey(inquiries.get(0)).getBooking();
//          booking = ((Inquery) inquiries.get(0)).getBooking();
          Booking booking2 = getInquiryHome().findByPrimaryKey(inquiries.get(inquiries.size()-1)).getBooking();
//          Booking booking2 = ((Inquery) inquiries.get(inquiries.size()-1)).getBooking();
          responseString.append(new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+" - "+new IWTimestamp(booking2.getBookingDate()).getLocaleDate(iwc));
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
          inquery = getInquiryHome().findByPrimaryKey(inquiries.get(i));
//          inquery = (Inquery) inquiries.get(i);
          inquery.setAnswered(true);
          inquery.setAnswerDate(IWTimestamp.getTimestampRightNow());
          inquery.store();
          if (book) {
            booking = inquery.getBooking();
            booking.setIsValid(true);
            booking.store();
          }
        }

        Collection resellersCol = inquery.getResellers();
        Reseller[] resellers = (Reseller[]) resellersCol.toArray(new Reseller[]{});
//        Reseller[] resellers = (Reseller[]) inquery.findRelated((Reseller) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class));
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
                responseString.append(" \""+tempService.getName(iwc.getCurrentLocaleId())+"\" ");
                responseString.append(iwrb.getLocalizedString("travel.for","for"));
                responseString.append(" "+inquery.getName()+",\n\n");
                if (inquiries.size() == 1) {
                  responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
                  responseString.append(" "+new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
                }else {
                  booking = getInquiryHome().findByPrimaryKey(inquiries.get(0)).getBooking();
                  Booking booking2 = getInquiryHome().findByPrimaryKey(inquiries.get(inquiries.size()-1)).getBooking();
//                  booking = ((Inquery) inquiries.get(0)).getBooking();
//                  Booking booking2 = ((Inquery) inquiries.get(inquiries.size()-1)).getBooking();
                  responseString.append(new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+" - "+new IWTimestamp(booking2.getBookingDate()).getLocaleDate(iwc));
                }
//                responseString.append(iwrb.getLocalizedString("travel.on_the","on the"));
//                responseString.append(" "+new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc));
                responseString.append("\n\n");
                if (book == false) {
                    responseString.append(iwrb.getLocalizedString("travel.request_is_denied","Request is denied."));
                }else if (book == true) {
                    responseString.append(iwrb.getLocalizedString("travel.request_is_granted_booking_confirmed","Request is granted. Booking has been confimed"));
                }

                //responseString.append("T - Svar við fyrirspurn varðandi "+inquery.getNumberOfSeats()+" sæti fyrir \""+inquery.getName()+"\" í ferðina \""+tempService.getName()+"\" þann "+new IWTimestamp(booking.getBookingDate()).getLocaleDate(iwc)+"\n");
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

  public Table getInquiryResponseError(IWResourceBundle iwrb) {
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


  /**
   * returns int[], int[0] is number of current booking, int[1] is total bookings number
   */
  public int[] getMultibleInquiriesNumber(Inquery inquiry) throws RemoteException, CreateException, FinderException {
    List list = getInquiryHome().create().getMultibleInquiries(inquiry);
    int[] returner = new  int[2];
    if (list == null || list.size() < 2 ) {
      returner[0] = 0;
      returner[1] = 0;
    }else {
      returner[0] = list.indexOf(inquiry) + 1;
      returner[1] = list.size();
    }
    return returner;
  }

  /**
   * returns 0 if valid, else -1
   */
  public int sendInquiryEmails(IWContext iwc, IWResourceBundle iwrb, int inquiryId) throws RemoteException{
    boolean sendEmail = false;
    boolean doubleSendSuccessful = false;

    try {

      InqueryHome iHome = (InqueryHome) IDOLookup.getHome(Inquery.class);
      ProductHome pHome = (ProductHome) IDOLookup.getHome(Product.class);
      SupplierHome sHome = (SupplierHome) IDOLookup.getHome(Supplier.class);

      Inquery inq = iHome.findByPrimaryKey(new Integer(inquiryId));
      Product prod = pHome.findByPrimaryKey(new Integer(inq.getServiceID()));
      Supplier suppl = sHome.findByPrimaryKey(prod.getSupplierId());
      Settings settings = suppl.getSettings();
      List inqs = getInquiryHome().create().getMultibleInquiries(inq);
      int inqsSize = inqs.size();
      Inquery tempInq;


      Email sEmail = suppl.getEmail();
      String suppEmail = "";
      if (sEmail != null) {
        suppEmail = sEmail.getEmailAddress();
      }
      String inqEmail = inq.getEmail();

      if (settings.getIfDoubleConfirmation()) {
        try {
          sendEmail = true;
          StringBuffer mailText = new StringBuffer();
//          mailText.append(iwrb.getLocalizedString("travel.inquiry.this_is_an_automatic_response_to_your_inquiry","This is an automatic response to your inquiry."));
          mailText.append(iwrb.getLocalizedString("travel.inquiry_double_confirmation","This is an automatic response to your inquiry.\nIt will be answered as soon as possible."));

          mailText.append("\n\n").append(iwrb.getLocalizedString("travel.your_inquiry_was",   "Your inquiry was")).append(" : ");
          mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(inq.getName());
          mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness().getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
          if (inqsSize == 1) {
            mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(new IWTimestamp(inq.getInqueryDate()).getLocaleDate(iwc));
          }else {
            for (int i = 0; i < inqsSize; i++) {
              tempInq = getInquiryHome().findByPrimaryKey(inqs.get(i));
//              tempInq = (Inquery) inqs.get(i);
              if (i == 0) {
                mailText.append("\n").append(iwrb.getLocalizedString("travel.dates",   "Dates :"));
              }
              mailText.append("\n\t").append(new IWTimestamp(tempInq.getInqueryDate()).getLocaleDate(iwc));
            }
          }
          mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(inq.getNumberOfSeats());

          mailText.append("\n\n").append(iwrb.getLocalizedString("travel.inquiry.reply_to_this_email_if_you_wish","Please reply to this email if you wish to make changes to your inquiry or if the information is incorrect."));


          SendMail sm = new SendMail();
            sm.send(suppEmail, inqEmail, "", "", "mail.idega.is", "Inquiry",mailText.toString());
          doubleSendSuccessful = true;
        }catch (MessagingException me) {
          doubleSendSuccessful = false;
          me.printStackTrace(System.err);
        }
      }

      if (settings.getIfEmailAfterOnlineBooking()) {
        try {
          String subject = "Inquiry";

          StringBuffer mailText = new StringBuffer();
          mailText.append(iwrb.getLocalizedString("travel.email_after_online_inquiry","You have just received an inquiry through nat.sidan.is."));
          mailText.append("\n\n").append(iwrb.getLocalizedString("travel.the_inquiry_was",   "The inquiry was")).append(" : ");
          mailText.append("\n").append(iwrb.getLocalizedString("travel.name",   "Name    ")).append(" : ").append(inq.getName());
          mailText.append("\n").append(iwrb.getLocalizedString("travel.service","Service ")).append(" : ").append(getProductBusiness().getProductNameWithNumber(prod, true, iwc.getCurrentLocaleId()));
          if (inqsSize == 1) {
            mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(new IWTimestamp(inq.getInqueryDate()).getLocaleDate(iwc));
          }else {
            for (int i = 0; i < inqsSize; i++) {
              tempInq = getInquiryHome().findByPrimaryKey(inqs.get(i));
//              tempInq = (Inquery) inqs.get(i);
              if (i == 0) {
                mailText.append("\n").append(iwrb.getLocalizedString("travel.dates",   "Dates :"));
              }
              mailText.append("\n\t").append(new IWTimestamp(tempInq.getInqueryDate()).getLocaleDate(iwc));
            }
          }
//          mailText.append("\n").append(iwrb.getLocalizedString("travel.date",   "Date    ")).append(" : ").append(new IWTimestamp(inq.getInqueryDate()).getLocaleDate(iwc));
          mailText.append("\n").append(iwrb.getLocalizedString("travel.seats",  "Seats   ")).append(" : ").append(inq.getNumberOfSeats());
          if (doubleSendSuccessful) {
            mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_been_sent","Double confirmation has been sent."));
          }else {
            mailText.append("\n\n").append(iwrb.getLocalizedString("travel.double_confirmation_has_not_been_sent","Double confirmation has NOT been sent."));
            mailText.append("\n").append("   - ").append(iwrb.getLocalizedString("travel.email_was_probably_incorrect","E-mail was probably incorrect."));
            subject = "Inquiry - double confirmation failed!";
          }


          SendMail sm = new SendMail();
            sm.send(suppEmail, suppEmail, "", "", "mail.idega.is", subject,mailText.toString());
        }catch (MessagingException me) {
          me.printStackTrace(System.err);
        }
      }

      return 0;
    }catch (FinderException fe) {
      fe.printStackTrace(System.err);
    }catch (CreateException ce) {
      ce.printStackTrace(System.err);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return -1;
  }

  public InqueryHome getInquiryHome() throws RemoteException {
    return (InqueryHome) IDOLookup.getHome(Inquery.class);
  }
  public Inquery[] collectionToInqueryArray(Collection coll) {
    return (Inquery[]) coll.toArray(new Inquery[]{});
  }

  private ProductBusiness getProductBusiness() throws RemoteException {
    return (ProductBusiness) IBOLookup.getServiceInstance(getIWApplicationContext(), ProductBusiness.class);
  }
}
