package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;

import javax.ejb.CreateException;

import com.idega.block.trade.business.CurrencyBusiness;
import com.idega.data.GenericEntity;
import com.idega.data.IDOLegacyEntity;
import com.idega.data.IDOLookup;


/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class SettingsBMPBean extends GenericEntity implements Settings{

  private static final String COLUMN_NAME_DOUBLE_CONFIRMATION = "DOUBLE_CONFIRMATION";
  private static final String COLUMN_NAME_EMAIL_AFTER_ONLINE = "EMAIL_ONLINE";
  private static final String COLUMN_NAME_CURRENCY_ID = "CURRENCY_ID";
  private static final String ENTITY_NAME = "TR_SETTINGS";


  public SettingsBMPBean(){
  }

  public SettingsBMPBean(int id)throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    this.addAttribute(getIDColumnName());
    this.addAttribute(COLUMN_NAME_DOUBLE_CONFIRMATION, "double confirmation", true, true, Boolean.class);
    this.addAttribute(COLUMN_NAME_EMAIL_AFTER_ONLINE, "receive email after online booking", true, true, Boolean.class);
    this.addAttribute(COLUMN_NAME_CURRENCY_ID, "currency id", true, true, Integer.class);

    this.addManyToManyRelationShip(Supplier.class);
    this.addManyToManyRelationShip(Reseller.class);
  }

  public String getEntityName() {
    return ENTITY_NAME;
  }

  public void setDefaultValues() {
    this.setIfDoubleConfirmation(true);
    this.setIfEmailAfterOnlineBooking(false);
  }

  public Integer ejbPostCreate(IDOLegacyEntity entity) throws CreateException{
    return null;
  }
//  public Integer ejbCreate(IDOLegacyEntity entity)throws CreateException{
  public Integer ejbCreate(IDOLegacyEntity entity) throws CreateException{
    try {
      SettingsHome shome = (SettingsHome)IDOLookup.getHome(Settings.class);
      Settings settings = shome.create();
      settings.store();
      ((SettingsBMPBean)settings).addTo(entity);
      return (Integer) settings.getPrimaryKey();
    }catch (RemoteException re) {
      re.printStackTrace(System.err);
    }catch (SQLException sql) {
      sql.printStackTrace(System.err);
    }
    return null;

  }

  /** Getters */
  public boolean getIfDoubleConfirmation() {
    return getBooleanColumnValue(COLUMN_NAME_DOUBLE_CONFIRMATION);
  }

  public boolean getIfEmailAfterOnlineBooking() {
    return getBooleanColumnValue(COLUMN_NAME_EMAIL_AFTER_ONLINE);
  }

  public int getCurrencyId() {
    int currId = getIntColumnValue(COLUMN_NAME_CURRENCY_ID);
    if (currId < 1) {
      currId = CurrencyBusiness.getCurrencyHolder("ISK").getCurrencyID();
      this.setCurrencyId(currId);
      this.store();
      System.out.println("[SettingBMPBean] Backwards compatability : setting currencyId = "+currId);
    }else if (currId == 1) {
      currId = CurrencyBusiness.getCurrencyHolder(CurrencyBusiness.defaultCurrency).getCurrencyID();
      this.setCurrencyId(currId);
      this.store();
      System.out.println("[SettingBMPBean] Backwards compatability : changing currencyId from 1 to "+currId);
    }
    return currId;
  }


  /** Setters */
  public void setIfDoubleConfirmation(boolean doubleConfirmation) {
    setColumn(COLUMN_NAME_DOUBLE_CONFIRMATION, doubleConfirmation);
  }

  public void setIfEmailAfterOnlineBooking(boolean emailAfterOnlineBooking) {
    setColumn(COLUMN_NAME_EMAIL_AFTER_ONLINE, emailAfterOnlineBooking);
  }

  public void setCurrencyId(int currencyId) {
    setColumn(COLUMN_NAME_CURRENCY_ID, currencyId);
  }

  /** Finders */
}