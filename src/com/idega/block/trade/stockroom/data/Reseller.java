package com.idega.block.trade.stockroom.data;

import com.idega.data.*;
import com.idega.core.data.*;

import java.util.List;
import java.util.Vector;
import java.sql.SQLException;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class Reseller extends TreeableEntity {

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

  public List getPhones(int PhoneTypeId) throws SQLException{
    Vector phones = new Vector();
    List allPhones = getPhones();
    if (allPhones != null) {
      Phone temp = null;
      for (int i = 0; i < allPhones.size(); i++) {
        temp = (Phone) allPhones.get(i);
        if (temp.getPhoneTypeId() == PhoneTypeId) {
          phones.add(temp);
        }
      }
    }
    return phones;
  }

  public List getEmails() throws SQLException {
    return EntityFinder.findRelated(this,Email.getStaticInstance(Email.class));
  }

  public Email getEmail() throws SQLException{
    Email returner = null;
    List list = getEmails();
    if (list != null) {
      if (list.size() > 0) {
        returner = (Email) list.get(list.size() -1);
      }
    }
    return returner;
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

  public Address getAddress() throws SQLException {
    Address address = null;
    List addr = getAddresses();
    if (addr !=null) {
      address = (Address) addr.get(addr.size() -1);
    }
    return address;
  }

  public List getAddresses() throws SQLException{
    return EntityFinder.findRelated(this,Address.getStaticInstance(Address.class));
  }

  public List getHomePhone() throws SQLException {
    return getPhones(Phone.getHomeNumberID());
  }

  public List getFaxPhone() throws SQLException {
    return getPhones(Phone.getFaxNumberID());
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