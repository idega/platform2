package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.FinderException;
import com.idega.block.trade.stockroom.business.ResellerManager;
import com.idega.block.trade.stockroom.business.ResellerManagerBean;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.EntityFinder;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.TreeableEntity;
import com.idega.data.TreeableEntityBMPBean;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;

/**
 * Title:        idegaWeb TravelBooking
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:      idega
 * @author <a href="mailto:gimmi@idega.is">Grimur Jonsson</a>
 * @version 1.0
 */

public class ResellerBMPBean extends TreeableEntityBMPBean implements Reseller, TreeableEntity{
  private String newName;
	private static String COLUMN_ORGANIZATION_ID = "ORGANIZATION_ID";

  public ResellerBMPBean() {
    super();
  }

  public ResellerBMPBean(int id) throws SQLException{
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(getColumnNameName(), "name", true, true, String.class);
    addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class);
    addAttribute(getColumnNameGroupID(),"Hópur", true, true, Integer.class, "many_to_one", ResellerStaffGroup.class);
    addAttribute(getColumnNameIsValid(),"is valid", true, true, Boolean.class);
    addAttribute(getColumnNameReferenceNumber(), "Tilvisunarnúmer", true, true, String.class);
    addAttribute(getColumnNameTPosMerchantID(), "Viðskiptanumer", true, true, Integer.class);
		addAttribute(COLUMN_SUPPLIER_MANAGER_ID, "supplier manager", true, true, Integer.class, MANY_TO_ONE, Group.class);
		addAttribute(COLUMN_ORGANIZATION_ID, "organization ID", true, true, String.class, 20);

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
    newName = name;

    //setColumn(getColumnNameName(), name);
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
    return EntityFinder.findRelated(this,com.idega.core.contact.data.PhoneBMPBean.getStaticInstance(Phone.class));
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
    return EntityFinder.findRelated(this,com.idega.core.contact.data.EmailBMPBean.getStaticInstance(Email.class));
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
    return EntityFinder.findRelated(this,com.idega.core.location.data.AddressBMPBean.getStaticInstance(Address.class));
  }

  public List getHomePhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getHomeNumberID());
  }

  public List getFaxPhone() throws SQLException {
    return getPhones(com.idega.core.contact.data.PhoneBMPBean.getFaxNumberID());
  }

  /**
   * @deprecated Replaced with findAll( supplierManager )
   */
  public static Reseller[] getValidResellers() throws SQLException {
		try {
			throw new Exception("ERRRROR : Using a wrong method : getValidResellers()    !!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
    return (Reseller[]) com.idega.block.trade.stockroom.data.ResellerBMPBean.getStaticInstance(Reseller.class).findAllByColumnOrdered(com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameIsValid(),"Y",com.idega.block.trade.stockroom.data.ResellerBMPBean.getColumnNameName());
  }

  public Reseller getParent() {
    return (Reseller) getParentEntity();
  }

  public void delete() throws SQLException{
    this.setIsValid(false);
    this.update();
  }
/*
  public int getTPosMerchantId() {
    return getIntColumnValue(getColumnNameTPosMerchantID());
  }

  public void setTPosMerchantId(int id) {
	setTPosMerchantId(new Integer(id));
  }

  public void setTPosMerchantId(Integer id) {
	setColumn(getColumnNameTPosMerchantID(), id);
  }

  public TPosMerchant getTPosMerchant() throws RemoteException, FinderException {
    TPosMerchantHome merchantHome = (TPosMerchantHome) IDOLookup.getHome(TPosMerchant.class);
    return merchantHome.findByPrimaryKey(new Integer(getTPosMerchantId()));
  }
*/
  public static String getResellerTableName()         {return "SR_RESELLER";}
  public static String getColumnNameName()            {return "NAME";}
  public static String getColumnNameDescription()     {return "DESCRIPTION";}
  public static String getColumnNameGroupID()         {return "IC_GROUP_ID";}
  public static String getColumnNameIsValid()         {return "IS_VALID";}
  public static String getColumnNameReferenceNumber() {return "REFERENCE_NUMBER";}
  public static String getColumnNameTPosMerchantID()  {return "TPOS_MERCHANT_ID";}
	private static String COLUMN_SUPPLIER_MANAGER_ID = "SUPPLIER_MANAGER_ID";

	public int getSupplierManagerID() {
		return getIntColumnValue(COLUMN_SUPPLIER_MANAGER_ID);
	}

	public Group getSupplierManager() {
		return (Group) getColumnValue(COLUMN_SUPPLIER_MANAGER_ID);
	}
	
	public void setSupplierManager(Group group) {
		setColumn(COLUMN_SUPPLIER_MANAGER_ID, group);
	}
 	
	public void setSupplierManagerPK(Object pk) {
		setColumn(COLUMN_SUPPLIER_MANAGER_ID, pk);
	}

  
  public void update() throws SQLException {
    if (newName != null) {
    	try {
	    	ResellerManager rm = (ResellerManager) IBOLookup.getServiceInstance(IWContext.getInstance(), ResellerManager.class);
	      Group pGroup = rm.getPermissionGroup(this);
	        pGroup.setName(newName+"_"+this.getID()+ResellerManagerBean.permissionGroupNameExtention);
	        pGroup.store();
	      ResellerStaffGroup sGroup = rm.getResellerStaffGroup(this);
	        sGroup.setName(newName+"_"+this.getID());
	        sGroup.store();
	      setColumn(getColumnNameName(),newName);
	      newName = null;
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new SQLException(e.getMessage());
    	}
    }
    super.update();
  }

  public void insert() throws SQLException {
    if (newName != null) {
      setColumn(getColumnNameName(),newName);
    }
    super.insert();
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
      debug("Settings data wrong for Reseller "+getID());
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

	public Collection ejbFindAllByGroupID(Object groupPK) throws FinderException {
		Table table = new Table(this);
		Column groupColumn = new Column(table, getColumnNameGroupID());
		Column validColumn = new Column(table, getColumnNameIsValid());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(groupColumn, MatchCriteria.EQUALS, groupPK));
		query.addCriteria(new MatchCriteria(validColumn, MatchCriteria.EQUALS, true));
		return this.idoFindPKsByQuery(query);
	}
  
	public Collection ejbFindAllBySupplierManager(Group suppMan) throws FinderException {
		Table table = new Table(this);
		Column suppManColumn = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		Column validColumn = new Column(table, getColumnNameIsValid());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(suppManColumn, MatchCriteria.EQUALS, suppMan));
		query.addCriteria(new MatchCriteria(validColumn, MatchCriteria.EQUALS, true));
		return this.idoFindPKsByQuery(query);
	}

	public String getOrganizationID() {
		return getStringColumnValue(COLUMN_ORGANIZATION_ID);
	}

	public void setOrganizationID(String organizationId) {
		setColumn(COLUMN_ORGANIZATION_ID, organizationId);
	}

}
