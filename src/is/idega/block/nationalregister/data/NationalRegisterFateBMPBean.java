package is.idega.block.nationalregister.data;

import java.util.Collection;

import javax.ejb.FinderException;

import is.idega.block.nationalregister.business.NationalRegisterConstants;

import com.idega.data.GenericEntity;
import com.idega.data.IDOLookup;
import com.idega.data.IDOQuery;

public class NationalRegisterFateBMPBean extends GenericEntity implements
		NationalRegisterFate {

	protected final static String ENTITY_NAME = "reg_nat_is_fate";

	protected final static String COLUMN_FATE_CODE = "fate_code";

	protected final static String COLUMN_FATE_STRING = "fate_string";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_FATE_CODE, "fate code", String.class);
		addAttribute(COLUMN_FATE_STRING, "fate string", String.class);
	}

	public void insertStartData() throws Exception {
		try {
			NationalRegisterFate deceased = ((NationalRegisterFateHome) IDOLookup
					.getHome(NationalRegisterFate.class)).create();
			deceased.setFateCode(NationalRegisterConstants.FATE_DECEASED);
			deceased.store();

			NationalRegisterFate changePid = ((NationalRegisterFateHome) IDOLookup
					.getHome(NationalRegisterFate.class)).create();
			changePid
					.setFateCode(NationalRegisterConstants.FATE_CHANGE_PERSONAL_ID);
			deceased.store();

			NationalRegisterFate removed = ((NationalRegisterFateHome) IDOLookup
					.getHome(NationalRegisterFate.class)).create();
			deceased.setFateCode(NationalRegisterConstants.FATE_REMOVED);
			deceased.store();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Setters
	public void setFateCode(String fateCode) {
		setColumn(COLUMN_FATE_CODE, fateCode);
	}

	public void setFateString(String fateString) {
		setColumn(COLUMN_FATE_STRING, fateString);
	}

	// Getters
	public String getFateCode() {
		return getStringColumnValue(COLUMN_FATE_CODE);
	}

	public String getFateString() {
		return getStringColumnValue(COLUMN_FATE_STRING);
	}
	
	// SQL
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		
		return idoFindPKsByQuery(sql);
	}
	
	public Object ejbFindByFateCode(String fateCode) throws FinderException {
		IDOQuery query = this.idoQueryGetSelect();
		query.appendWhereEqualsQuoted(COLUMN_FATE_CODE, fateCode);
		
		return idoFindOnePKByQuery(query);
	}
}