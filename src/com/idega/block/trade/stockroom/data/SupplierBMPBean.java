package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

import com.idega.block.tpos.data.TPosMerchant;
import com.idega.block.tpos.data.TPosMerchantHome;
import com.idega.block.trade.stockroom.business.SupplierManager;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.EntityFinder;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDORelationshipException;

/**
 * Title:        IW Trade
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega.is
 * @author 2000 - idega team - <br><a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson</a><br><a href="mailto:gimmi@idega.is">Grímur Jónsson</a>
 * @version 1.0
 */


public class SupplierBMPBean extends com.idega.data.GenericEntity implements com.idega.block.trade.stockroom.data.Supplier {

  private String newName;

  public SupplierBMPBean(){
          super();
  }
  public SupplierBMPBean(int id)throws SQLException{
          super(id);
  }

  public void initializeAttributes(){
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "Name", true, true, String.class);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class,500);
    addAttribute(getColumnNameGroupID(),"Hópur", true, true, Integer.class, "many_to_one", SupplierStaffGroup.class);
    addAttribute(getColumnNameIsValid(),"Í notkun",true, true, Boolean.class);
    addAttribute(getColumnNameTPosMerchantID(), "Viðskiptamannanumer", true, true, Integer.class);

    this.addManyToManyRelationShip(Address.class,"SR_SUPPLIER_IC_ADDRESS");
    this.addManyToManyRelationShip(Phone.class,"SR_SUPPLIER_IC_PHONE");
    this.addManyToManyRelationShip(Email.class,"SR_SUPPLIER_IC_EMAIL");
    this.addManyToManyRelationShip(ProductCategory.class, "SR_SUPPLIER_PRODUCT_CATEGORY" );
    this.addManyToManyRelationShip(Reseller.class);
  }

  public void insertStartData()throws Exception{
  }

  public void setDefaultValues() {
    setIsValid(true);
  }

  public static String getSupplierTableName(){return "SR_SUPPLIER";}
  public static String getColumnNameName() {return "NAME";}
  public static String getColumnNameDescription() {return "DESCRIPTION";}
  public static String getColumnNameGroupID() {return "IC_GROUP_ID";}
  public static String getColumnNameIsValid() {return "IS_VALID";}
  public static String getColumnNameTPosMerchantID()  {return "TPOS_MERCHANT_ID";}

  public String getEntityName(){
    return getSupplierTableName();
  }

  public String getName(){
    return getStringColumnValue(getColumnNameName());
  }

  public void setName(String name) {
    newName = name;
  }

  public String getDescription(){
    return getStringColumnValue(getColumnNameDescription());
  }

  public void setDescription(String description){
    setColumn(getColumnNameDescription(),description);
  }

  public void setGroupId(int id){
    setColumn(getColumnNameGroupID(), id);
  }

  public int getGroupId(){
    return getIntColumnValue(getColumnNameGroupID());
  }

  public void setIsValid(boolean isValid) {
    setColumn(getColumnNameIsValid(),isValid);
  }

  public boolean getIsValid() {
    return getBooleanColumnValue(getColumnNameIsValid());
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
    return EntityFinder.findRelated(this,com.idega.core.location.data.AddressBMPBean.getStaticInstance(Address.class));
  }


  public List getPhones() throws SQLException {
    return EntityFinder.findRelated(this,com.idega.core.contact.data.PhoneBMPBean.getStaticInstance(Phone.class));
  }

  public List getHomePhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
  }

  public List getFaxPhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
  }

  public List getWorkPhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getWorkNumberID());
  }

  public List getMobilePhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getMobileNumberID());
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

  public Email getEmail() throws SQLException{
    Email email = null;
    List emails = getEmails();
    if (emails != null) {
      email = (Email) emails.get(emails.size() - 1);
    }
    return email;
  }

  public List getEmails() throws SQLException {
    return EntityFinder.findRelated(this,com.idega.core.contact.data.EmailBMPBean.getStaticInstance(Email.class));
  }

  public static Supplier[] getValidSuppliers() throws SQLException {
    return (Supplier[]) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class).findAllByColumnOrdered(com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameIsValid(),"Y",com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameName());
  }

  public void update() throws SQLException {
    if (newName != null) {
      PermissionGroup pGroup = SupplierManager.getPermissionGroup(this);
        pGroup.setName(newName+"_"+this.getID()+SupplierManager.permissionGroupNameExtention);
        pGroup.update();
      SupplierStaffGroup sGroup = SupplierManager.getSupplierStaffGroup(this);
        sGroup.setName(newName+"_"+this.getID());
        sGroup.update();
      setColumn(getColumnNameName(),newName);
      newName = null;
    }
    super.update();
  }

  public void insert() throws SQLException {
    if (newName != null) {
      setColumn(getColumnNameName(),newName);
    }
    super.insert();
  }
  public int getTPosMerchantId() {
    return getIntColumnValue(getColumnNameTPosMerchantID());
  }

  public TPosMerchant getTPosMerchant() throws RemoteException, FinderException {
    TPosMerchantHome merchantHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
    return merchantHome.findByPrimaryKey(new Integer(getTPosMerchantId()));
  }

  public void setTPosMerchantId(int id) {
    setColumn(getColumnNameTPosMerchantID(), id);
  }

  public Settings getSettings() throws FinderException, RemoteException, CreateException {
    Collection coll = null;
    try{
      coll = this.idoGetRelatedEntities(Settings.class);
    }
    catch(IDOException ido){
      //throw new CreateException(ido.getMessage());
    }
    SettingsHome shome = (SettingsHome)IDOLookup.getHome(Settings.class);
    if (coll.size() == 1) {
      Iterator iter = coll.iterator();
      return (Settings) iter.next();
    }else if (coll.size() > 1) {
      /** @todo fixa vitlaus gogn... setja removeFrom thegar thad virkar */
      debug("Settings data wrong for Supplier "+getID());
      Iterator iter = coll.iterator();
      Settings set;
      while (iter.hasNext()) {
        set = (Settings) iter.next();
        if (!iter.hasNext()) {
          return set;
        }
      }
      return null;
    } else {
      return shome.create(this);
    }
  }

  public Collection getProductCategories() throws IDORelationshipException{
    return this.idoGetRelatedEntities(ProductCategory.class);
  }

}

