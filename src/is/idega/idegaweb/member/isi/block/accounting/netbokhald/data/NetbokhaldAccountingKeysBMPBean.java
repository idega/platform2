package is.idega.idegaweb.member.isi.block.accounting.netbokhald.data;

import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class NetbokhaldAccountingKeysBMPBean extends GenericEntity implements
		NetbokhaldAccountingKeys {

	protected final static String ENTITY_NAME = "nb_acc_key";
	
	protected final static String COLUMN_SETUP_ID = "setup_id";
	
	protected final static String COLUMN_TYPE = "type";
	
	protected final static String COLUMN_KEY = "internal_key";
	
	protected final static String COLUMN_DEBET_KEY = "debet_key";
	
	protected final static String COLUMN_CREDIT_KEY = "credit_key";
	
	public final static String TYPE_ASSESSMENT = "A";
	
	public final static String TYPE_PAYMENT = "P";
	
	public final static String TYPE_CREDITCARD = "C";
	
	public final static String TYPE_BANK = "B";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SETUP_ID, NetbokhaldSetup.class);
		addAttribute(COLUMN_TYPE, "Type", String.class);
		addAttribute(COLUMN_KEY, "Key", Integer.class);
		addAttribute(COLUMN_DEBET_KEY, "Debet key", String.class);
		addAttribute(COLUMN_CREDIT_KEY, "Credit key", String.class);
	}
	
	//getters
	public NetbokhaldSetup getSetup() {
		return (NetbokhaldSetup) getColumnValue(COLUMN_SETUP_ID);
	}
	
	public String getType() {
		return getStringColumnValue(COLUMN_TYPE);
	}
	
	public int getKey() {
		return getIntColumnValue(COLUMN_KEY, -1);
	}
	
	public String getDebetKey() {
		return getStringColumnValue(COLUMN_DEBET_KEY);
	}
	
	public String getCreditKey() {
		return getStringColumnValue(COLUMN_CREDIT_KEY);
	}
	
	//setters
	public void setSetup(NetbokhaldSetup setup) {
		setColumn(COLUMN_SETUP_ID, setup);
	}
	
	public void setType(String type) {
		setColumn(COLUMN_TYPE, type);
	}
	
	public void setKey(String key) {
		setColumn(COLUMN_KEY, key);
	}
	
	public void setDebetKey(String key) {
		setColumn(COLUMN_DEBET_KEY, key);
	}
	
	public void setCreditKey(String key) {
		setColumn(COLUMN_CREDIT_KEY, key);
	}
	
	//ejb
	public Collection ejbFindAllBySetupID(NetbokhaldSetup setup) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this);
		query.appendWhereEqualsQuoted(COLUMN_SETUP_ID, setup.getExternalID());
		
		System.out.println("sql = " + query.toString());
		
		return idoFindPKsByQuery(query);
	}
}