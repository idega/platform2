package is.idega.idegaweb.travel.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.trade.stockroom.data.Reseller;
import com.idega.data.IDOQuery;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ContractBMPBean extends com.idega.data.GenericEntity implements is.idega.idegaweb.travel.data.Contract {

  public ContractBMPBean() {
    super();
  }

  public ContractBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameServiceId(), "ServiceID", true, true, Integer.class, "many-to-one",Service.class);
    addAttribute(getColumnNameResellerId(), "ResellerID", true, true, Integer.class, "many-to-one",Reseller.class);
    addAttribute(getColumnNameAlotment(), "Fjöldi sæta", true, true, Integer.class);
    addAttribute(getColumnNameFrom(), "Virkt frá", true, true, Timestamp.class);
    addAttribute(getColumnNameTo(), "Virkt til", true, true, Timestamp.class);
    addAttribute(getColumnNameDiscount(), "Afsláttur", true, true, String.class);
    addAttribute(getColumnNameExpiresDaysBeforeDeparture(), "dagar fyrir brottför", true, true, Integer.class);
    addIndex(getColumnNameServiceId());
    addIndex("IDX_CON_SERVICE", new String[]{getIDColumnName(), getColumnNameServiceId()});

  }
  public String getEntityName() {
    return getContractTableName();
  }

  // Setters
  public void setServiceId(int id) {
    setColumn(getColumnNameServiceId(), id);
  }

  public void setService(Service service) throws RemoteException{
    setServiceId(((Integer)service.getPrimaryKey()).intValue());
  }

  public void setResellerId(int id) {
    setColumn(getColumnNameResellerId(), id);
  }

  public void setReseller(Reseller reseller) {
    setResellerId(reseller.getID());
  }

  public void setAlotment(int alotment) {
    setColumn(getColumnNameAlotment(), alotment);
  }

  public void setFrom(Timestamp from) {
    setColumn(getColumnNameFrom(),from);
  }

  public void setTo(Timestamp to) {
    setColumn(getColumnNameTo(),to);
  }

  public void setDiscount(String discount) {
    setColumn(getColumnNameDiscount(), discount);
  }

  public void setExpireDays(int daysBeforeDeparture) {
    setColumn(getColumnNameExpiresDaysBeforeDeparture(), daysBeforeDeparture);
  }

  // getters
  public int getServiceId() {
    return getIntColumnValue(getColumnNameServiceId());
  }

  public Service getService() throws RemoteException, FinderException {
    return ((is.idega.idegaweb.travel.data.ServiceHome)com.idega.data.IDOLookup.getHome(Service.class)).findByPrimaryKey(new Integer(getServiceId()));
  }

  public int getResellerId() {
    return getIntColumnValue(getColumnNameResellerId());
  }

  public Reseller getReseller() throws SQLException {
    return ((com.idega.block.trade.stockroom.data.ResellerHome)com.idega.data.IDOLookup.getHomeLegacy(Reseller.class)).findByPrimaryKeyLegacy(getResellerId());
  }

  public int getAlotment() {
    return getIntColumnValue(getColumnNameAlotment());
  }

  public Timestamp getFrom() {
    return (Timestamp) getColumnValue(getColumnNameFrom());
  }

  public Timestamp getTo() {
    return (Timestamp) getColumnValue(getColumnNameTo());
  }

  public String getDiscount() {
    return getStringColumnValue(getColumnNameDiscount());
  }

  public int getExpireDays() {
    return getIntColumnValue(getColumnNameExpiresDaysBeforeDeparture());
  }
  
  public Collection ejbFindByProductId(int productId) throws FinderException {
  	return this.idoFindAllIDsByColumnBySQL(getColumnNameServiceId(), Integer.toString(productId));
  }

  public Collection ejbFindByResellerId(int resellerID) throws FinderException {
  	return super.idoFindAllIDsByColumnBySQL(getColumnNameResellerId(), Integer.toString(resellerID));
  }
  
  public Object ejbFindByProductAndReseller(int productId, int resellerId) throws FinderException {
  	IDOQuery query = idoQuery();
  	query.appendSelectAllFrom(this)
		.appendWhereEquals(getColumnNameServiceId(), productId)
		.appendAndEquals(getColumnNameResellerId(), resellerId);
  	return this.idoFindOnePKByQuery(query);
  }

  public static String getContractTableName() { return "TB_CONTRACT";}
  public static String getColumnNameServiceId() { return "TB_SERVICE_ID";}
  public static String getColumnNameResellerId() { return "SR_RESELLER_ID";}
  public static String getColumnNameAlotment() { return "ALOTMENT";}
  public static String getColumnNameFrom() { return "ACTIVE_FROM";}
  public static String getColumnNameTo() { return "ACTIVE_TO";}
  public static String getColumnNameDiscount() { return "DISCOUNT";}
  public static String getColumnNameExpiresDaysBeforeDeparture() { return "VALID_DAYS_BEFORE";}
}
