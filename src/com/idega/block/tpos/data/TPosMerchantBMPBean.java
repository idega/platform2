package com.idega.block.tpos.data;
import com.idega.data.GenericEntity;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class TPosMerchantBMPBean extends GenericEntity{

  private static final String _EntityName = "TPOS_MERCHANT";
  private static final String _ColumnNameName = "MERCHANT_NAME";
  private static final String _ColumnNameMerchantID = "MERCHANT_ID";
  private static final String _ColumnNameLocationID = "LOCATION_ID";
  private static final String _ColumnNameUser = "USER_ID";
  private static final String _ColumnNamePassword = "PASSW";
  private static final String _ColumnNamePosID = "POS_ID";
  private static final String _ColumnNameKeyReceivedPassword = "KEY_RCV_PASSW";


  public TPosMerchantBMPBean() {
  }


  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(this._ColumnNameName,"Merchant Name", true, true, String.class);
    addAttribute(this._ColumnNameMerchantID,"Merchant ID", true, true, String.class);
    addAttribute(this._ColumnNameLocationID,"Location ID", true, true, String.class);
    addAttribute(this._ColumnNameUser,"User", true, true, String.class);
    addAttribute(this._ColumnNamePassword,"Password", true, true, String.class);
    addAttribute(this._ColumnNamePosID,"Pos ID", true, true, String.class);
    addAttribute(this._ColumnNameKeyReceivedPassword,"KeyRcvPassw", true, true, String.class);
  }


  public String getEntityName() {
    return _EntityName;
  }

  /** SETTERS */
  public void setName(String name) {
    setMerchantName(name);
  }

  public void setMerchantName(String name) {
    setColumn(this._ColumnNameName, name);
  }

  public void setMerchantID(String id) {
    setColumn(this._ColumnNameMerchantID, id);
  }

  public void setLoactionID(String id) {
    setColumn(this._ColumnNameLocationID, id);
  }

  public void setUserID(String id) {
    setColumn(this._ColumnNameUser, id);
  }

  public void setPassword(String password) {
    setColumn(this._ColumnNamePassword, password);
  }

  public void setPosID(String id) {
    setColumn(this._ColumnNamePosID, id);
  }

  public void setKeyReceivedPassword(String keyRcvPassw) {
    setColumn(this._ColumnNameKeyReceivedPassword, keyRcvPassw);
  }

  /** GETTERS */
  public String getName() {
    return getMerchantName();
  }

  public String getMerchantName() {
    return getStringColumnValue(this._ColumnNameName);
  }

  public String getMerchantID() {
    return getStringColumnValue(this._ColumnNameMerchantID);
  }

  public String getLocationID() {
    return getStringColumnValue(this._ColumnNameLocationID);
  }

  public String getUserID() {
    return getStringColumnValue(this._ColumnNameUser);
  }

  public String getPassword() {
    return getStringColumnValue(this._ColumnNamePassword);
  }

  public String getPosID() {
    return getStringColumnValue(this._ColumnNamePosID);
  }

  public String getKeyReceivedPassword() {
    return getStringColumnValue(this._ColumnNameKeyReceivedPassword);
  }
}