package com.idega.block.trade.stockroom.data;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import com.idega.block.trade.data.CreditCardInformation;
import com.idega.block.trade.stockroom.business.SupplierManager;
import com.idega.core.accesscontrol.data.PermissionGroup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.location.data.Address;
import com.idega.data.EntityFinder;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

/**
 * Title: IW Trade Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *             <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson </a> <br>
 *             <a href="mailto:gimmi@idega.is">Grímur Jónsson </a>
 * @version 1.0
 */

public class SupplierBMPBean extends com.idega.data.GenericEntity implements Supplier{

	private String newName;

	public SupplierBMPBean() {
		super();
	}

	public SupplierBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameName(), "Name", true, true, String.class);
		addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class, 500);
		addAttribute(getColumnNameGroupID(), "Hópur", true, true, Integer.class, "many_to_one", SupplierStaffGroup.class);
		addAttribute(getColumnNameIsValid(), "Í notkun", true, true, Boolean.class);
		/* can this be removed */
		addAttribute(getColumnNameTPosMerchantID(), "ViÝskiptamannanumer", true, true, Integer.class);

		this.addManyToManyRelationShip(Address.class, "SR_SUPPLIER_IC_ADDRESS");
		this.addManyToManyRelationShip(Phone.class, "SR_SUPPLIER_IC_PHONE");
		this.addManyToManyRelationShip(Email.class, "SR_SUPPLIER_IC_EMAIL");
		this.addManyToManyRelationShip(ProductCategory.class, "SR_SUPPLIER_PRODUCT_CATEGORY");
		this.addManyToManyRelationShip(Reseller.class);

		this.addManyToManyRelationShip(CreditCardInformation.class, "SR_SUPPLIER_CC_INFORMATION");

		addIndex("IDX_SUPP_1", new String[]{getIDColumnName(), getColumnNameIsValid()});
		addIndex("IDX_SUPP_1", new String[]{getColumnNameIsValid()});
	}

	public void insertStartData() throws Exception {
	}

	public void setDefaultValues() {
		setIsValid(true);
	}

	public static String getSupplierTableName() {
		return "SR_SUPPLIER";
	}

	public static String getColumnNameName() {
		return "NAME";
	}

	public static String getColumnNameDescription() {
		return "DESCRIPTION";
	}

	public static String getColumnNameGroupID() {
		return "IC_GROUP_ID";
	}

	public static String getColumnNameIsValid() {
		return "IS_VALID";
	}

	public static String getColumnNameTPosMerchantID() {
		return "TPOS_MERCHANT_ID";
	}

	public String getEntityName() {
		return getSupplierTableName();
	}

	public String getName() {
		return getStringColumnValue(getColumnNameName());
	}

	public void setName(String name) {
		newName = name;
	}

	public String getDescription() {
		return getStringColumnValue(getColumnNameDescription());
	}

	public void setDescription(String description) {
		setColumn(getColumnNameDescription(), description);
	}

	public void setGroupId(int id) {
		setColumn(getColumnNameGroupID(), id);
	}

	public int getGroupId() {
		return getIntColumnValue(getColumnNameGroupID());
	}

	public void setIsValid(boolean isValid) {
		setColumn(getColumnNameIsValid(), isValid);
	}

	public boolean getIsValid() {
		return getBooleanColumnValue(getColumnNameIsValid());
	}

	public Address getAddress() throws SQLException {
		Address address = null;
		List addr = getAddresses();
		if (addr != null) {
			address = (Address) addr.get(addr.size() - 1);
		}
		return address;
	}

	public List getAddresses() throws SQLException {
		return EntityFinder.findRelated(this, com.idega.core.location.data.AddressBMPBean.getStaticInstance(Address.class));
	}

	public List getPhones() throws SQLException {
		return EntityFinder.findRelated(this, com.idega.core.contact.data.PhoneBMPBean.getStaticInstance(Phone.class));
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

	public List getPhones(int PhoneTypeId) throws SQLException {
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

	public Email getEmail() throws SQLException {
		Email email = null;
		List emails = getEmails();
		if (emails != null) {
			email = (Email) emails.get(emails.size() - 1);
		}
		return email;
	}

	public List getEmails() throws SQLException {
		return EntityFinder.findRelated(this, com.idega.core.contact.data.EmailBMPBean.getStaticInstance(Email.class));
	}

	public static Supplier[] getValidSuppliers() throws SQLException {
		return (Supplier[]) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class).findAllByColumnOrdered(com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameIsValid(), "Y", com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameName());
	}

	public Collection ejbFindAll() throws FinderException {
		return this.idoFindAllIDsByColumnOrderedBySQL(this.getColumnNameIsValid(), "'Y'", getColumnNameName());
	}

	public void update() throws SQLException {
		if (newName != null) {
			PermissionGroup pGroup = SupplierManager.getPermissionGroup(this);
			pGroup.setName(newName + "_" + this.getID() + SupplierManager.permissionGroupNameExtention);
			pGroup.update();
			SupplierStaffGroup sGroup = SupplierManager.getSupplierStaffGroup(this);
			sGroup.setName(newName + "_" + this.getID());
			sGroup.update();
			setColumn(getColumnNameName(), newName);
			newName = null;
		}
		super.update();
	}

	public void insert() throws SQLException {
		if (newName != null) {
			setColumn(getColumnNameName(), newName);
		}
		super.insert();
	}

	public int getTPosMerchantId() {
		return getIntColumnValue(getColumnNameTPosMerchantID());
	}

	public Settings getSettings() throws FinderException, RemoteException, CreateException {
		Collection coll = null;
		try {
			coll = this.idoGetRelatedEntities(Settings.class);
		}
		catch (IDOException ido) {
			//throw new CreateException(ido.getMessage());
		}
		SettingsHome shome = (SettingsHome) IDOLookup.getHome(Settings.class);
		if (coll.size() == 1) {
			Iterator iter = coll.iterator();
			return (Settings) iter.next();
		}
		else if (coll.size() > 1) {
			/** @todo fixa vitlaus gogn... setja removeFrom thegar thad virkar */
			debug("Settings data wrong for Supplier " + getID());
			Iterator iter = coll.iterator();
			Settings set;
			while (iter.hasNext()) {
				set = (Settings) iter.next();
				if (!iter.hasNext()) { return set; }
			}
			return null;
		}
		else {
			return shome.create(this);
		}
	}

	public void setCreditCardInformation(Collection pks) throws IDORemoveRelationshipException, IDOAddRelationshipException, EJBException {
		if (pks != null) {
			Iterator iter = pks.iterator();
			Object obj;
			while (iter.hasNext()) {
				obj = iter.next();
				try {
					if (obj instanceof CreditCardInformation) {
						addCreditCardInformation((CreditCardInformation) obj);
					}
					else {
						addCreditCardInformationPK(obj);
					}
				}
				catch (Exception e) {
					log("SupplierBMPBean : error adding cc info, probably primaryKey error : " + e.getMessage());
				}
			}
		}
	}

	public void addCreditCardInformationPK(Object pk) throws IDOAddRelationshipException {
		this.idoAddTo(CreditCardInformation.class, pk);
	}

	public void addCreditCardInformation(CreditCardInformation info) throws IDOAddRelationshipException, EJBException {
		if (info != null) {
			addCreditCardInformationPK(info.getPrimaryKey());
		}
	}

	public Collection getCreditCardInformation() throws IDORelationshipException {
		return this.idoGetRelatedEntities(CreditCardInformation.class);
	}

	public Collection getProductCategories() throws IDORelationshipException {
		return this.idoGetRelatedEntities(ProductCategory.class);
	}

	public Collection ejbFindWithTPosMerchant() throws FinderException {
		IDOQuery query = this.idoQuery();
		query.appendSelectAllFrom(this).appendWhereEqualsQuoted(getColumnNameIsValid(), "Y").appendAnd().append("(").append(getColumnNameTPosMerchantID()).append(" is not null").appendOr().appendEquals(getColumnNameTPosMerchantID(), -1).append(")").appendOrderBy(getColumnNameName());

		return this.idoFindPKsByQuery(query);
	}
	
	public Collection ejbFindAllByGroupID(int groupID) throws FinderException {
		Table table = new Table(this);
		Column groupColumn = new Column(table, getColumnNameGroupID());
		Column validColumn = new Column(table, getColumnNameIsValid());
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(groupColumn, MatchCriteria.EQUALS, groupID));
		query.addCriteria(new MatchCriteria(validColumn, MatchCriteria.EQUALS, true));
		System.out.println(query.toString());
		return this.idoFindPKsBySQL(query.toString());
	}
}

