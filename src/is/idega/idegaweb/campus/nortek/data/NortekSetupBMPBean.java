package is.idega.idegaweb.campus.nortek.data;

import java.util.Collection;
import java.util.Iterator;

import javax.ejb.FinderException;

import com.idega.block.category.data.ICCategory;
import com.idega.block.finance.data.AccountKey;
import com.idega.block.finance.data.TariffGroup;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class NortekSetupBMPBean extends GenericEntity implements NortekSetup {
	public static String ENTITY_NAME = "nt_setup";

	protected static String COLUMN_FINANCE_CATEGORY = "finance_category";

	protected static String COLUMN_TARIFF_GROUP = "tariff_group";

	protected static String COLUMN_ACCOUNT_KEY = "account_key";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_FINANCE_CATEGORY, ICCategory.class);
		addManyToOneRelationship(COLUMN_TARIFF_GROUP, TariffGroup.class);
		addManyToOneRelationship(COLUMN_ACCOUNT_KEY, AccountKey.class);
	}

	// getters
	public ICCategory getFinanceCategory() {
		return (ICCategory) getColumnValue(COLUMN_FINANCE_CATEGORY);
	}

	public TariffGroup getTariffGroup() {
		return (TariffGroup) getColumnValue(COLUMN_TARIFF_GROUP);
	}

	public AccountKey getAccountKey() {
		return (AccountKey) getColumnValue(COLUMN_ACCOUNT_KEY);
	}

	// setters
	public void setFinanceCategory(ICCategory entity) {
		setColumn(COLUMN_FINANCE_CATEGORY, entity);
	}

	public void setTariffGroup(TariffGroup group) {
		setColumn(COLUMN_TARIFF_GROUP, group);
	}

	public void setAccountKey(AccountKey key) {
		setColumn(COLUMN_ACCOUNT_KEY, key);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);

		return idoFindPKsByQuery(query);
	}
	
	public Object ejbFindEntry() throws FinderException {
		Collection col = ejbFindAll();
		
		if (col != null && !col.isEmpty()) {
			Iterator it = col.iterator();
			if (it.hasNext()) {
				return it.next();
			}
		}
		
		return null;
	}
}