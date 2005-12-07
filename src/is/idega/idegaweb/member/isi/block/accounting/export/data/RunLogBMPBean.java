package is.idega.idegaweb.member.isi.block.accounting.export.data;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

public class RunLogBMPBean extends GenericEntity implements RunLog {

	protected final static String ENTITY_NAME = "isi_run_log";

	protected final static String COLUMN_CREATED_DATE = "created_date";

	protected final static String COLUMN_CREATED_BY = "created_by";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_CREATED_DATE, "Created date", Timestamp.class);
		addAttribute(COLUMN_CREATED_BY, "Created by", String.class);
	}

	// getters
	public Timestamp getCreatedDate() {
		return getTimestampColumnValue(COLUMN_CREATED_DATE);
	}

	public String getCreatedBy() {
		return getStringColumnValue(COLUMN_CREATED_BY);
	}

	// Setters
	public void setCreatedDate(Timestamp created) {
		setColumn(COLUMN_CREATED_DATE, created);
	}

	public void setCreatedBy(String name) {
		setColumn(COLUMN_CREATED_BY, name);
	}

	// ejb
	public Collection ejbFindAll() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);

		return idoFindPKsBySQL(sql.toString());
	}
}