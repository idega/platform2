package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;
import java.util.Iterator;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.core.location.data.Country;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.data.query.Column;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.Order;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;
import com.idega.user.data.Group;

/**
 * @author gimmi
 */
public class ServiceSearchEngineBMPBean extends GenericEntity implements ServiceSearchEngine {

	private static String TABLE_NAME = "TB_SERVICE_SEARCH_ENGINE";
	private static String COLUMN_NAME = "OWNER_NAME";
	private static String COLUMN_BOOKING_CODE = "BOOKING_CODE";
	private static String COLUMN_IS_VALID = "IS_VALID";
	private static String COLUMN_GROUP_ID = "GROUP_ID";
	private static String MIDDLE_TABLE_SUPPLIER_SEARCH_ENGINE = "sr_supplier_service_engine";
	private static String MIDDLE_TABLE_COUNTRY_SEARCH_ENGINE = "tb_service_engine_country";
	private static String COLUMN_SUPPLIER_MANAGER_ID = "SUPPLIER_MANAGER_ID";
	
	public String getEntityName() {
		return TABLE_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_NAME, "name", String.class, 200);
		addAttribute(COLUMN_BOOKING_CODE, "code", String.class, 200);
		addAttribute(COLUMN_IS_VALID, "is valid", Boolean.class);
//		addAttribute(COLUMN_GROUP_ID, "staff group Id");
		addAttribute(COLUMN_GROUP_ID, "staff group Id", true, true, Integer.class, super.ONE_TO_ONE, ServiceSearchEngineStaffGroup.class);
		addAttribute(COLUMN_SUPPLIER_MANAGER_ID, "supplier manager", true, true, Integer.class, MANY_TO_ONE, Group.class);

		this.setUnique(COLUMN_NAME, true);
		this.setNullable(COLUMN_NAME, false);
		this.setUnique(COLUMN_BOOKING_CODE, true);
		this.setNullable(COLUMN_BOOKING_CODE, false);
		this.addManyToManyRelationShip(Supplier.class, MIDDLE_TABLE_SUPPLIER_SEARCH_ENGINE);

		this.addManyToManyRelationShip(Country.class, MIDDLE_TABLE_COUNTRY_SEARCH_ENGINE);
	}
	
	public void setDefaultValues() {
		setColumn(COLUMN_IS_VALID, true);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public void setCode(String code) {
		setColumn(COLUMN_BOOKING_CODE, code);
	}
	
	public void setStaffGroupID(int groupID) {
		setColumn(COLUMN_GROUP_ID, groupID);
	}
	
	public int getStaffGroupID() {
		return getIntColumnValue(COLUMN_GROUP_ID);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public String getCode() {
		return getStringColumnValue(COLUMN_BOOKING_CODE);
	}
	
	public boolean getIsValid() {
		return getBooleanColumnValue(COLUMN_IS_VALID);
	}
	
	public void addSupplier(Supplier supplier) throws IDOAddRelationshipException {
		this.idoAddTo(supplier);
	}
	
	public void removeSupplier(Supplier supplier) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(supplier);
	}
	
	public void removeAllSuppliers() throws IDORemoveRelationshipException {
		this.idoRemoveFrom(Supplier.class);
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
	
	public Object ejbFindByName(String name) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_NAME, name);
	}
	
	public Object ejbFindByCode(String code) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_BOOKING_CODE, code);
	}
	
	public Collection ejbFindAll() throws FinderException {
		try {
			throw new Exception("ERRRROR : ServiceSearchEngine : Using a wrong method : findAll()    !!!!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.idoFindAllIDsByColumnOrderedBySQL(COLUMN_IS_VALID, "'Y'", COLUMN_NAME);
	}
		
	public Collection ejbFindAll(Group supplierManager) throws FinderException {
		Table table = new Table(this);
		Column isValid = new Column(table, COLUMN_IS_VALID);
		Column suppMan = new Column(table, COLUMN_SUPPLIER_MANAGER_ID);
		Column name = new Column(table, COLUMN_NAME);
		Order order = new Order(name, true);
		
		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(isValid, MatchCriteria.EQUALS, true));
		query.addCriteria(new MatchCriteria(suppMan, MatchCriteria.EQUALS, supplierManager.getPrimaryKey()));
		query.addOrder(order);
		
		return this.idoFindPKsByQuery(query);
	}
	
	public Collection getCountries() throws IDORelationshipException {
		return this.idoGetRelatedEntities(Country.class);
	}
	
	/**
	 * Removes the countries already connected and sets the countries as the new ones
	 * @param countries
	 * @throws IDORemoveRelationshipException
	 * @throws IDOAddRelationshipException
	 */
	public void setCountries(Collection countries) throws IDORemoveRelationshipException, IDOAddRelationshipException {
		this.idoRemoveFrom(Country.class);
		if (countries != null) {
			Iterator iter = countries.iterator();
			while (iter.hasNext()) {
				this.idoAddTo((Country) iter.next());
			}
		}
	}
	
	public void remove() throws RemoveException {
		this.setColumn(COLUMN_IS_VALID, false);
		this.store();
	}

	public Object ejbFindByGroupID(int groupID) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_GROUP_ID, Integer.toString(groupID));
	}
	
	public Collection getSuppliers() throws IDORelationshipException {
		return this.idoGetRelatedEntities(Supplier.class);
	}
}
