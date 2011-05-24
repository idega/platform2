package is.idega.idegaweb.campus.block.allocation.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.block.finance.data.AccountKey;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.user.data.User;

public class ContractTariffBMPBean extends GenericEntity implements
		ContractTariff {

	private static final String ENTITY_NAME = "cam_contract_tariff";
	private static final String COLUMN_CONTRACT = "contract_id";
	private static final String COLUMN_NAME = "name";
	private static final String COLUMN_PRICE = "price";
	private static final String COLUMN_ACCOUNT_KEY = "account_key_id";
	private static final String COLUMN_USE_INDEX = "use_index";
	private static final String COLUMN_INDEX_TYPE = "index_type";
	private static final String COLUMN_INDEX_UPDATED = "index_updated";
	private static final String COLUMN_DELETED = "deleted";
	private static final String COLUMN_DELETED_BY = "deleted_by";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		
		addManyToOneRelationship(COLUMN_CONTRACT, Contract.class);
		addAttribute(COLUMN_NAME, "Name", String.class);
		addAttribute(COLUMN_PRICE, "Price", Float.class);
		addManyToOneRelationship(COLUMN_ACCOUNT_KEY, AccountKey.class);
		addAttribute(COLUMN_USE_INDEX, "Use index", Boolean.class);
		addAttribute(COLUMN_INDEX_TYPE, "Index type", String.class, 10);
		addAttribute(COLUMN_INDEX_UPDATED, "Index updated", Timestamp.class);
		addAttribute(COLUMN_DELETED, "Deleted", Boolean.class);
		addManyToOneRelationship(COLUMN_DELETED_BY, User.class);
	}
	
	//getters
	public Contract getContract() {
		return (Contract) getColumnValue(COLUMN_CONTRACT);
	}
	
	public String getName() {
		return getStringColumnValue(COLUMN_NAME);
	}
	
	public float getPrice() {
		return getFloatColumnValue(COLUMN_PRICE, 0.0f);
	}
	
	public AccountKey getAccountKey() {
		return (AccountKey) getColumnValue(COLUMN_ACCOUNT_KEY);
	}
	
	public boolean getUseIndex() {
		return getBooleanColumnValue(COLUMN_USE_INDEX, false);
	}
	
	public String getIndexType() {
		return getStringColumnValue(COLUMN_INDEX_TYPE);
	}
	
	public Timestamp getIndexUpdated() {
		return getTimestampColumnValue(COLUMN_INDEX_UPDATED);
	}
	
	public boolean getIsDeleted() {
		return getBooleanColumnValue(COLUMN_DELETED, false);
	}
	
	public User getDeletedBy() {
		return (User) getColumnValue(COLUMN_DELETED_BY);
	}
	
	//setters
	public void setContract(Contract contract) {
		setColumn(COLUMN_CONTRACT, contract);
	}
	
	public void setName(String name) {
		setColumn(COLUMN_NAME, name);
	}
	
	public void setPrice(float price) {
		setColumn(COLUMN_PRICE, price);
	}
	
	public void setAccountKey(AccountKey key) {
		setColumn(COLUMN_ACCOUNT_KEY, key);
	}

	public void setAccountKey(int keyID) {
		setColumn(COLUMN_ACCOUNT_KEY, keyID);
	}

	public void setUseIndex(boolean useIndex) {
		setColumn(COLUMN_USE_INDEX, useIndex);
	}
	
	public void setIndexType(String indexType) {
		setColumn(COLUMN_INDEX_TYPE, indexType);
	}
	
	public void setIndexUpdated(Timestamp updated) {
		setColumn(COLUMN_INDEX_UPDATED, updated);
	}
	
	public void setIsDeleted(boolean isDeleted) {
		setColumn(COLUMN_DELETED, isDeleted);
	}
	
	public void setDeletedBy(User user) {
		setColumn(COLUMN_DELETED_BY, user);
	}
	
	//ejb
	public Collection ejbFindByContract(Contract contract) throws FinderException {
		IDOQuery query = super.idoQueryGetSelect();
		query.appendWhereEquals(COLUMN_CONTRACT, contract);
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_DELETED);
		query.appendIsNull();
		query.appendOrEquals(COLUMN_DELETED, false);
		query.appendRightParenthesis();
		
		return super.idoFindPKsByQuery(query);
	}
}