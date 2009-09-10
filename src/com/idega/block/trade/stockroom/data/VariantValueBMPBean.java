package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import javax.ejb.FinderException;
import com.idega.data.*;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class VariantValueBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.VariantValue {

  public VariantValueBMPBean() {
  }

  public VariantValueBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameVariantId(), "variant_id", true, true, Integer.class, super.MANY_TO_ONE, Variant.class);
    addAttribute(getColumnNameVariantValue(), "value", true, true, String.class);
  }
  public String getEntityName() {
    return getTableNameVariantValue();
  }

  public static String getTableNameVariantValue() {return "SR_VARIANT_VALUE";}
  public static String getColumnNameVariantId() {return "SR_VARIANT_ID";}
  public static String getColumnNameVariantValue() {return "SR_VARIANT_VALUE";}


  public void setVariantId(int id) {
    setColumn(getColumnNameVariantId(), id);
  }

  public void setValue(String value) {
    setColumn(getColumnNameVariantValue(), value);
  }


  public String getName() {
    return getValue();
  }

  public int getVariantId() {
    return getIntColumnValue(getColumnNameVariantId());
  }

  public Variant getVariant() throws RemoteException, FinderException {
    return getVariantHome().findByPrimaryKey(new Integer(getVariantId()));
  }

  public String getValue() {
    return getStringColumnValue(getColumnNameVariantValue());
  }

  private VariantHome getVariantHome() throws RemoteException {
    return (VariantHome) IDOLookup.getHome(Variant.class);
  }

}
