package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.block.trade.stockroom.data.TravelAddress;
import com.idega.data.EntityControl;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.SimpleQuerier;
import com.idega.util.IWTimestamp;
import com.idega.util.ListUtil;

/**
 * Title:        IW Travel
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class InqueryBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.Inquery {

  public InqueryBMPBean(){
          super();
  }

  public InqueryBMPBean(int id)throws SQLException{
          super(id);
  }
  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getNameColumnName(), "Name", true, true, String.class, 255);
    addAttribute(getEmailColumnName(), "Tölvupóstur", true, true, String.class, 255);
    addAttribute(getInqueryColumnName(), "Fyrirspurn",true ,true, String.class, 510);
    addAttribute(getInqueryDateColumnName(), "Dagur sem spurt er um", true ,true, java.sql.Timestamp.class);
    addAttribute(getInqueryPostDateColumnName(), "Dagur þegar spurt var", true ,true, java.sql.Timestamp.class);
    addAttribute(getAnsweredColumnName(), "Svarað", true,true, Boolean.class);
    addAttribute(getAnswerDateColumnName(), "Hvenær var svarað", true, true, java.sql.Timestamp.class);
    addAttribute(getServiceIDColumnName(), "Vara", true, true, Integer.class, "many-to-one", Service.class);
    addAttribute(getNumberOfSeatsColumnName(), "sæti", true, true, Integer.class);
    addAttribute(getBookingIdColumnName(), "bókun", true, true, Integer.class);

    this.addManyToManyRelationShip(Reseller.class);
    
    addIndex(getBookingIdColumnName());
    addIndex(getServiceIDColumnName());
  }


  public String getEntityName(){
    return getInqueryTableName();
  }
  public String getName(){
    return getStringColumnValue(getNameColumnName());
  }

  public void setName(String name){
    setColumn(getNameColumnName(),name);
  }

  public Service getService() {
    return (Service) getColumnValue(getServiceIDColumnName());
  }

  public int getServiceID() {
    return getIntColumnValue(getServiceIDColumnName());
  }

  public void setServiceID(int id) {
    setColumn(getServiceIDColumnName(), id);
  }

  public String getEmail() {
    return getStringColumnValue(getEmailColumnName());
  }

  public void setEmail(String email) {
    setColumn(getEmailColumnName(), email);
  }

  public String getInquery() {
    return getStringColumnValue(getInqueryColumnName());
  }

  public void setInquery(String inquery) {
    setColumn(getInqueryColumnName(), inquery);
  }

  public Timestamp getInqueryDate() {
    return (Timestamp) getColumnValue(getInqueryDateColumnName());
  }

  public void setInqueryDate(Timestamp timestamp) {
    setColumn(getInqueryDateColumnName(), timestamp);
  }

  public Timestamp getInqueryPostDate() {
    return (Timestamp) getColumnValue(getInqueryPostDateColumnName());
  }

  public void setInqueryPostDate(Timestamp timestamp) {
    setColumn(getInqueryPostDateColumnName(), timestamp);
  }

  public boolean getIfAnswered() {
    return getAnswered();
  }

  public boolean getAnswered() {
    return getBooleanColumnValue(getAnsweredColumnName());
  }

  public void setIfAnswered(boolean answered) {
    setAnswered(answered);
  }

  public void setAnswered(boolean answered) {
    setColumn(getAnsweredColumnName(), answered);
  }

  public Timestamp getAnswerDate() {
    return (Timestamp) getColumnValue(getAnswerDateColumnName());
  }

  public void setAnswerDate(Timestamp timestamp) {
    setColumn(getAnswerDateColumnName(), timestamp);
  }

  public int getNumberOfSeats() {
    return getIntColumnValue(getNumberOfSeatsColumnName());
  }

  public void setNumberOfSeats(int numberOfSeats) {
    setColumn(getNumberOfSeatsColumnName(),numberOfSeats);
  }

  public void setBookingId(int bookingId) {
    setColumn(getBookingIdColumnName(), bookingId);
  }

  public int getBookingId() {
    return getIntColumnValue(getBookingIdColumnName());
  }

  public GeneralBooking getBooking() throws FinderException, RemoteException {
   return ((is.idega.idegaweb.travel.data.GeneralBookingHome)com.idega.data.IDOLookup.getHome(GeneralBooking.class)).findByPrimaryKey(new Integer(getBookingId()));
  }

  public static String getInqueryTableName(){return "TB_INQUERY";}
  public static String getNameColumnName() {return "NAME";}
  public static String getEmailColumnName() {return "EMAIL";}
  public static String getInqueryColumnName() {return "INQUERY";}
  public static String getInqueryDateColumnName() {return "INQUERY_DATE";}
  public static String getInqueryPostDateColumnName() {return "INQUERY_POST_DATE";}
  public static String getAnsweredColumnName() {return "ANSWERED";}
  public static String getAnswerDateColumnName() {return "ANSWER_DATE";}
  public static String getServiceIDColumnName() {return "TB_SERVICE_ID";}
  public static String getNumberOfSeatsColumnName() {return "NUMBER_OF_SEATS";}
  public static String getBookingIdColumnName() {return "TB_BOOKING_ID";}

  public int ejbHomeGetInqueredSeats(int serviceId, IWTimestamp stamp, int resellerId, boolean unansweredOnly) throws FinderException{
    int returner = 0;
    try {
      Reseller res = (Reseller)com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT sum(i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getNumberOfSeatsColumnName()+") FROM "+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryTableName()+" i");
        if (resellerId != -1) {
          buffer.append(", "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r, "+middleTable+" mi");
        }
        buffer.append(" WHERE ");
        if (resellerId != -1) {
          buffer.append("i."+getIDColumnName()+" = mi."+getIDColumnName());
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());
          buffer.append(" AND ");
        }

        if (unansweredOnly) {
        buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getAnsweredColumnName() +" = 'N'");
        }

        buffer.append(" AND ");
        buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
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
      throw new FinderException(e.getMessage());
//      e.printStackTrace(System.err);
    }
    return returner;
  }

  public Collection ejbFindInqueries(int serviceId, IWTimestamp stamp, int resellerId, boolean unansweredOnly, String orderBy) throws FinderException {
    return ejbFindInqueries(serviceId, stamp, resellerId, unansweredOnly, null, orderBy);
  }

  public Collection ejbFindInqueries(int serviceId, IWTimestamp stamp, int resellerId, boolean unansweredOnly, TravelAddress travelAddress ,String orderBy) throws FinderException {
    Collection inqueries = null;
    if (orderBy == null) orderBy = "";
      Reseller res = (Reseller)com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class);
      String middleTable = EntityControl.getManyToManyRelationShipTableName(Inquery.class, Reseller.class);
      String middleTableBookingAddress = EntityControl.getManyToManyRelationShipTableName(TravelAddress.class, GeneralBooking.class);

      StringBuffer buffer = new StringBuffer();
        buffer.append("SELECT i.* FROM "+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryTableName()+" i, "+GeneralBookingBMPBean.getBookingTableName()+" b");
        if (travelAddress != null) {
          buffer.append(" ,"+middleTableBookingAddress+" ba");
        }

        if (resellerId != -1) {
          buffer.append(" , "+com.idega.block.trade.stockroom.data.ResellerBMPBean.getResellerTableName()+" r, "+middleTable+" mi");
        }
        buffer.append(" WHERE ");
        buffer.append(" i."+this.getBookingIdColumnName()+" = b."+GeneralBookingBMPBean.getBookingTableName()+"_ID");
        buffer.append(" AND ");
        if (travelAddress != null) {
          buffer.append("ba."+GeneralBookingBMPBean.getBookingTableName()+"_ID = b."+GeneralBookingBMPBean.getBookingTableName()+"_ID");
          buffer.append(" AND ");
          buffer.append("ba."+travelAddress.getIDColumnName()+" = "+travelAddress.getID());
          buffer.append(" AND ");
        }

        if (resellerId != -1) {
          buffer.append("i."+getIDColumnName()+" = mi."+getIDColumnName());
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = mi."+res.getIDColumnName());
          buffer.append(" AND ");
        }

        if (unansweredOnly) {
          buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getAnsweredColumnName() +" = 'N'");
          buffer.append(" AND ");
        }

        buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getServiceIDColumnName()+" = "+serviceId);
        buffer.append(" AND ");
        buffer.append("i."+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryDateColumnName() +" like '"+stamp.toSQLDateString()+"%'");
        if (resellerId != -1) {
          buffer.append(" AND ");
          buffer.append("r."+res.getIDColumnName()+" = "+resellerId);
        }

        if (!orderBy.equals("")) {
          buffer.append(" ORDER BY i."+orderBy);
        }

      inqueries = this.idoFindPKsBySQL(buffer.toString());
    return inqueries;
  }

  public List getMultibleInquiries(Inquery inquiry) throws RemoteException, FinderException{
    List list = new Vector();

      StringBuffer buff = new StringBuffer();
        buff.append("SELECT * FROM "+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryTableName());
        buff.append(" WHERE ");
        if (inquiry.getAnswerDate() != null) {
          buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getAnswerDateColumnName()+" = '"+inquiry.getAnswerDate()+"'");
        }else {
          buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getAnswerDateColumnName()+" is null");
        }
        buff.append(" AND ");
        if (inquiry.getAnswered()) {
          buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getAnsweredColumnName()+" = 'Y'");
        }else {
          buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getAnsweredColumnName()+" = 'N'");
        }
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getEmailColumnName()+" = '"+inquiry.getEmail()+"'");
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryColumnName()+" = '"+inquiry.getInquery()+"'");
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryPostDateColumnName()+" = '"+inquiry.getInqueryPostDate()+"'");
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getNameColumnName()+" = '"+inquiry.getName()+"'");
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getNumberOfSeatsColumnName()+" = "+inquiry.getNumberOfSeats());
        buff.append(" AND ");
        buff.append(is.idega.idegaweb.travel.data.InqueryBMPBean.getServiceIDColumnName()+" = "+inquiry.getServiceID());
        buff.append(" ORDER BY "+is.idega.idegaweb.travel.data.InqueryBMPBean.getInqueryDateColumnName());

        //System.err.println(buff.toString());
        Collection coll = this.idoFindPKsBySQL(buff.toString());
        list = ListUtil.convertCollectionToList(coll);
//      list = EntityFinder.findAll(inquiry, buff.toString());

    return list;
  }


  public Collection getResellers() throws IDORelationshipException{
    return this.idoGetRelatedEntities(Reseller.class);
  }

  public void addReseller(Reseller reseller) throws IDOAddRelationshipException{
    this.idoAddTo(reseller);
  }
}

