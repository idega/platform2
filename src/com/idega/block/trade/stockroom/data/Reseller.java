package com.idega.block.trade.stockroom.data;

import com.idega.data.*;
import com.idega.core.data.*;

import java.util.List;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Reseller extends GenericEntity {

  public Reseller() {
    super();
  }

  public Reseller(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "name", true, true, String.class);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class);
    addAttribute(getColumnNameGroupID(),"Hópur", true, true, Integer.class, "many_to_one", ResellerStaffGroup.class);
    addAttribute(getColumnNameIsValid(),"is valid", true, true, Boolean.class);
    addAttribute(getColumnNameReferenceNumber(), "Tilvisunarnúmer", true, true, String.class);

    this.addManyToManyRelationShip(Address.class);
    this.addManyToManyRelationShip(Email.class);
    this.addManyToManyRelationShip(Phone.class);

  }
  public String getEntityName() {
    return getResellerTableName();
  }

  public void setDefaultValues() {
    this.setIsValid(true);
  }

  public String getName() {
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name) {
    setColumn(getColumnNameName(), name);
  }

  public String getDescription() {
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description) {
    setColumn(getColumnNameDescription(), description);
  }

  public void setGroupId(int id){
    setColumn(getColumnNameGroupID(), id);
  }

  public int getGroupId(){
    return getIntColumnValue(getColumnNameGroupID());
  }

  public List getPhones() throws SQLException {
    return EntityFinder.findRelated(this,Phone.getStaticInstance(Phone.class));
  }

  public List getEmails() throws SQLException {
    return EntityFinder.findRelated(this,Email.getStaticInstance(Email.class));
  }

  public void setIsValid(boolean isValid) {
    setColumn(getColumnNameIsValid(), isValid);
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getColumnNameIsValid());
  }

  public String getReferenceNumber() {
    return getStringColumnValue(getColumnNameReferenceNumber());
  }

  public void setReferenceNumber(String key) {
    setColumn(getColumnNameReferenceNumber(), key);
  }

  public void delete() throws SQLException{
    this.setIsValid(false);
    this.update();
  }

  public static String getResellerTableName()         {return "SR_RESELLER";}
  public static String getColumnNameName()            {return "NAME";}
  public static String getColumnNameDescription()     {return "DESCRIPTION";}
  public static String getColumnNameGroupID()         {return "IC_GROUP_ID";}
  public static String getColumnNameIsValid()         {return "IS_VALID";}
  public static String getColumnNameReferenceNumber() {return "REFERENCE_NUMBER";}

}