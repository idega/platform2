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
import com.idega.block.trade.stockroom.business.SupplierManagerBusiness;
import com.idega.block.trade.stockroom.business.SupplierManagerBusinessBean;
import com.idega.business.IBOLookup;
import com.idega.core.contact.data.Email;
import com.idega.core.contact.data.Phone;
import com.idega.core.file.data.ICFile;
import com.idega.core.location.data.Address;
import com.idega.core.location.data.PostalCode;
import com.idega.data.EntityFinder;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.AND;
import com.idega.data.query.Column;
import com.idega.data.query.Criteria;
import com.idega.data.query.InCriteria;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.OR;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.presentation.IWContext;
import com.idega.user.data.Group;

/**
 * Title: IW Trade Description: Copyright: Copyright (c) 2001 Company: idega.is
 * 
 * @author 2000 - idega team -<br>
 *             <a href="mailto:gummi@idega.is">Guðmundur Ágúst Sæmundsson </a> <br>
 *             <a href="mailto:gimmi@idega.is">Grímur Jónsson </a>
 * @version 1.0
 */

public class SupplierBMPBean extends GenericEntity implements Supplier{

	private String newName;
	private static String COLUMN_SUPPLIER_MANAGER_ID = "SUPPLIER_MANAGER_ID";
	private static String COLUMN_ORGANIZATION_ID = "ORGANIZATION_ID";
	public static final String COLUMN_NAME_NAME_ALL_CAPS = "NAME_CAPS";
	private static final String COLUMN_IC_FILE_ID = "IC_FILE_ID";

	public SupplierBMPBean() {
		super();
	}

	public SupplierBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(getColumnNameName(), "Name", true, true, String.class);
		addAttribute(COLUMN_NAME_NAME_ALL_CAPS, "nafn i storun", true, true, String.class);
		addAttribute(getColumnNameDescription(), "Lýsing", true, true, String.class, 500);
		addAttribute(getColumnNameGroupID(), "Hópur", true, true, Integer.class, "many_to_one", SupplierStaffGroup.class);
		addAttribute(getColumnNameIsValid(), "Í notkun", true, true, Boolean.class);
		addAttribute(COLUMN_SUPPLIER_MANAGER_ID, "supplier manager", true, true, Integer.class, MANY_TO_ONE, Group.class);
		addAttribute(COLUMN_ORGANIZATION_ID, "organization ID", true, true, String.class, 20);
		/* can this be removed */
		addAttribute(getColumnNameTPosMerchantID(), "ViÝskiptamannanumer", true, true, Integer.class);

		this.addManyToManyRelationShip(Address.class, "SR_SUPPLIER_IC_ADDRESS");
		this.addManyToManyRelationShip(Phone.class, "SR_SUPPLIER_IC_PHONE");
		this.addManyToManyRelationShip(Email.class, "SR_SUPPLIER_IC_EMAIL");

		this.addManyToManyRelationShip(ProductCategory.class, "SR_SUPPLIER_PRODUCT_CATEGORY");
		this.addManyToManyRelationShip(Reseller.class);
		this.addManyToManyRelationShip(CreditCardInformation.class, "SR_SUPPLIER_CC_INFORMATION");

		this.addManyToOneRelationship(COLUMN_IC_FILE_ID, ICFile.class);
		
		addIndex("IDX_SUPP_1", new String[]{getIDColumnName(), getColumnNameIsValid()});
		addIndex("IDX_SUPP_2", new String[]{getColumnNameIsValid()});
		addIndex("IDX_SUPP_4", new String[]{COLUMN_SUPPLIER_MANAGER_ID, getColumnNameIsValid()});
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
		setColumn(COLUMN_NAME_NAME_ALL_CAPS, name.toUpperCase());
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

	/**
	 * @deprecated Replaced with findAll( supplierManager );
	 */
	public static Supplier[] getValidSuppliers() throws SQLException {
		try {
			throw new Exception("ERRRROR : Using a wrong method : getValidSuppliers(), should be findAll ( supplierManager )    !!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (Supplier[]) com.idega.block.trade.stockroom.data.SupplierBMPBean.getStaticInstance(Supplier.class).findAllByColumnOrdered(com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameIsValid(), "Y", com.idega.block.trade.stockroom.data.SupplierBMPBean.getColumnNameName());
	}
	
	public Collection ejbFindAll(Group supplierManager) throws FinderException {
		Table table = new Table(this);
		Column isValid = new Column(table, this.getColumnNameIsValid());
		Column suppMan = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		Column name = new Column(table, getColumnNameName());
		Order order = new Order(name, true);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(isValid, MatchCriteria.EQUALS, true));
		query.addCriteria(new MatchCriteria(suppMan, MatchCriteria.EQUALS, supplierManager.getPrimaryKey()));
		query.addOrder(order);
		
		return this.idoFindPKsByQuery(query);
		
//		return this.idoFindAllIDsByColumnOrderedBySQL(this.getColumnNameIsValid(), "'Y'", getColumnNameName());
	}

	public void update() throws SQLException {
		if (newName != null) {
			try {
				SupplierManagerBusiness sm = (SupplierManagerBusiness) IBOLookup.getServiceInstance(IWContext.getInstance(), SupplierManagerBusiness.class);
				Group pGroup = sm.getPermissionGroup(this);
				pGroup.setName(newName + "_" + this.getID() + SupplierManagerBusinessBean.permissionGroupNameExtention);
				pGroup.store();
				SupplierStaffGroup sGroup = sm.getSupplierStaffGroup(this);
				sGroup.setName(newName + "_" + this.getID());
				sGroup.store();
				setColumn(getColumnNameName(), newName);
				System.out.println("Supplier : setting updateName");
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
		return this.idoFindPKsByQuery(query);
	}
		
	public Collection ejbFindByPostalCodes(Group supplierManager, String[] from, String[] to, Collection criterias, String supplierName) throws IDORelationshipException, FinderException {
		int fromLength = from.length;
		int toLength = to.length;
		if (fromLength != toLength) {
			throw new FinderException("From and To arrays must be of same size");
		}
		
		Table table = new Table(this);
		Column suppMan = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		Column valid = new Column(table, getColumnNameIsValid());

		SelectQuery query = new SelectQuery(table);
		query.setAsDistinct(true);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(suppMan, MatchCriteria.EQUALS, supplierManager.getPrimaryKey()));
		query.addCriteria(new MatchCriteria(valid, MatchCriteria.EQUALS, true));
		if (fromLength > 0) {
			Table address = new Table(Address.class);
			Table postal = new Table(PostalCode.class);
			Column postalCode = new Column(postal, "POSTAL_CODE");

			query.addJoin(table, address);
			query.addJoin(address, postal);
			Vector crits = new Vector();
			for (int i = 0; i < fromLength; i++) {
				if (to[i] == null) {
					crits.add(new MatchCriteria(postalCode, MatchCriteria.EQUALS, from[i]));
				} else {
					AND and = new AND(new MatchCriteria(postalCode, MatchCriteria.GREATEREQUAL, from[i]), new MatchCriteria(postalCode, MatchCriteria.LESSEQUAL, to[i]));
					crits.add(and);
				}
			}
			if (fromLength == 1) {
				query.addCriteria( (Criteria) crits.get(0));
			} else {
				OR mainOR = new OR( (Criteria) crits.get(0), (Criteria) crits.get(1));
				
				for ( int i = 2; i < fromLength; i++) {
					mainOR = new OR(mainOR, (Criteria) crits.get(i));
				}
				
				query.addCriteria(mainOR);
			}
		}
		
		if (supplierName != null) {
			query.addCriteria(new MatchCriteria(new Column(table, COLUMN_NAME_NAME_ALL_CAPS), MatchCriteria.LIKE, "%"+supplierName.toUpperCase()+"%"));
		}
		
		if (criterias != null) {
			Iterator iter = criterias.iterator();
			while (iter.hasNext()) {
				query.addCriteria((Criteria) iter.next());
			}
		}
		
		query.addOrder(table, getColumnNameName(), true);
//		System.out.println(query.toString());
		return idoFindPKsByQuery(query);
	}

	public String getOrganizationID() {
		return getStringColumnValue(COLUMN_ORGANIZATION_ID);
	}

	public void setOrganizationID(String organizationId) {
		setColumn(COLUMN_ORGANIZATION_ID, organizationId);
	}
	
	public ICFile getICFile() {
		return (ICFile) getColumnValue(COLUMN_IC_FILE_ID);
	}
	
	public void setICFile(int fileID) {
		setColumn(COLUMN_IC_FILE_ID, fileID);
	}
	
	public void setICFile(ICFile file) {
		setColumn(COLUMN_IC_FILE_ID, file);
	}
	
	public Collection ejbFindAllWithoutCreditCardMerchant(Group supplierManager) throws IDORelationshipException, FinderException {
		Table table = new Table(this);
		Table ccTable = new Table(CreditCardInformation.class);
		Table middleTable = null;
		
		IDOEntityDefinition source = table.getEntityDefinition();
		IDOEntityDefinition destination = ccTable.getEntityDefinition();

		IDOEntityDefinition[] definitions = source.getManyToManyRelatedEntities();
		if (definitions != null && definitions.length > 0) {
			for (int i = 0; i < definitions.length; i++) {
				IDOEntityDefinition definition = definitions[i];
				if (destination.equals(definition)) {
					try {
						String middleTableName = source.getMiddleTableNameForRelation(destination.getSQLTableName());
						middleTable = new Table(middleTableName);
					} catch (Exception e) {}
				}
			}
		}
		
		if (middleTable == null) {
			throw new IDORelationshipException("Middletable not found for "+table.getName()+" and "+ccTable.getName());
		}
		
		SelectQuery sub = new SelectQuery(middleTable);
		sub.addColumn(new Column(middleTable, getIDColumnName()));
		
		Column idCol = new Column(table, getIDColumnName());
		Column suppMan = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		Column isValid = new Column(table, getColumnNameIsValid());
		
		SelectQuery q = new SelectQuery(table);
		q.addColumn(idCol);
		q.addCriteria(new InCriteria(idCol, sub, true));
		q.addCriteria(new MatchCriteria(isValid, MatchCriteria.EQUALS, true));
		q.addCriteria(new MatchCriteria(suppMan, MatchCriteria.EQUALS, supplierManager.getPrimaryKey()));
		//q.addJoin(table, ccTable);
		
		return idoFindPKsByQuery(q);
	}
}

