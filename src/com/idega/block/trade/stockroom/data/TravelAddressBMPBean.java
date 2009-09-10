package com.idega.block.trade.stockroom.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import com.idega.core.location.data.Address;
import com.idega.util.IWTimestamp;
import com.idega.util.text.TextSoap;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2002 - <a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */

public class TravelAddressBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.TravelAddress {

  public static final int ADDRESS_TYPE_DEPARTURE = 0;
  public static final int ADDRESS_TYPE_ARRIVAL = 1;

  public TravelAddressBMPBean() {
    super();
  }

  public TravelAddressBMPBean(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
//    this.addAttribute(getColumnNameAddressId(), "addressId", true, true, Integer.class);
    this.addAttribute(getColumnNameTime(), "time", true, true, Timestamp.class);
    this.addAttribute(getColumnNameAddressTypeId(), "addressutýpa", true, true, Integer.class);
    this.addAttribute(getColumnNameRefillStock() , "fylla á byrgðir", true, true, Boolean.class);

    this.addOneToOneRelationship(getColumnNameAddressId(), Address.class);
    this.addManyToManyRelationShip( Product.class, "SR_PRODUCT_SR_ADDRESS" );
  }

  public String getEntityName() {
    return getTravelAddressTableName();
  }

  public int getAddressId() {
    return getIntColumnValue(getColumnNameAddressId());
  }

  public Address getAddress() {
	  return (Address) getColumnValue(getColumnNameAddressId());
  }

  public String getName() {
    IWTimestamp timestamp = new IWTimestamp(getTime());

    return getStreetName()+" - "+TextSoap.addZero(timestamp.getHour())+":"+TextSoap.addZero(timestamp.getMinute());
  }

  public String getStreetName() {
    return getAddress().getStreetName();
  }

  public Timestamp getTime() {
    return (Timestamp) getColumnValue(getColumnNameTime());
  }

  public int getAddressType() {
    return getIntColumnValue(getColumnNameAddressTypeId());
  }

  public boolean getRefillStock() {
    return getBooleanColumnValue(getColumnNameRefillStock());
  }

  public void setAddressId(int addressId) {
    setColumn(getColumnNameAddressId(), addressId);
  }

  public void setAddress(Address address) {
    setAddressId(address.getID());
  }

  public void setTime(Timestamp stamp) {
    setColumn(getColumnNameTime(), stamp);
  }

  public void setTime(IWTimestamp stamp) {
    setTime(stamp.getTimestamp());
  }

  public void setAddressTypeId(int id) {
    setColumn(getColumnNameAddressTypeId(), id);
  }

  public void setRefillStock(boolean replenish) {
    setColumn(getColumnNameRefillStock(), replenish);
  }


  public static String getTravelAddressTableName() { return "SR_ADDRESS";}
  public static String getColumnNameAddressId() { return "IC_ADDRESS_ID";}
  public static String getColumnNameTime() { return "DP_AR_TIME";}
  public static String getColumnNameAddressTypeId() {return "SR_ADDRESS_TYPE_ID";}
  public static String getColumnNameRefillStock() {return "REFILL_STOCK";}
}


