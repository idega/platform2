package is.idega.idegaweb.travel.block.search.data;

import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;

import com.idega.block.trade.stockroom.data.Supplier;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;

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
		this.setUnique(COLUMN_NAME, true);
		this.setNullable(COLUMN_NAME, false);
		this.setUnique(COLUMN_BOOKING_CODE, true);
		this.setNullable(COLUMN_BOOKING_CODE, false);
		this.addManyToManyRelationShip(Supplier.class, MIDDLE_TABLE_SUPPLIER_SEARCH_ENGINE);
		
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
		/*
		try {
			this.removeFrom(Supplier.class);
		} catch (SQLException sql) {
			throw new IDORemoveRelationshipException(sql.getMessage());
		}
		*/
		this.idoRemoveFrom(Supplier.class);
	}

	public Object ejbFindByName(String name) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_NAME, name);
	}
	
	public Object ejbFindByCode(String code) throws FinderException {
		return this.idoFindOnePKByColumnBySQL(COLUMN_BOOKING_CODE, code);
	}
	
	public Collection ejbFindAll() throws FinderException {
		return this.idoFindAllIDsByColumnOrderedBySQL(COLUMN_IS_VALID, "'Y'", COLUMN_NAME);
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
