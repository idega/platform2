package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.block.school.data.SchoolCategory;
import com.idega.block.school.data.SchoolCategoryBMPBean;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import java.sql.Timestamp;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/23 14:04:06 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.2 $
 */
public class CheckAmountBroadcastBMPBean extends GenericEntity
	implements CheckAmountBroadcast {
	public static final String ENTITY_NAME = "cacc_ca_broadcast";

	public static final String COLUMN_STARTTIME = "start_time";
	public static final String COLUMN_ENDTIME = "end_time";
	public static final String COLUMN_SCHOOL_CATEGORY_ID
		= SchoolCategoryBMPBean.COLUMN_CATEGORY;
	public static final String COLUMN_SCHOOL_COUNT = "school_count";
	
	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute (getIDColumnName());
		addAttribute (COLUMN_STARTTIME, "", true, true, Timestamp.class);
		addAttribute (COLUMN_ENDTIME, "", true, true, Timestamp.class);
		addManyToOneRelationship (COLUMN_SCHOOL_CATEGORY_ID, SchoolCategory.class);
		addAttribute (COLUMN_SCHOOL_COUNT, "", true, true, Integer.class);
	}

	public String getSchoolCategoryId () {
		return getStringColumnValue (COLUMN_SCHOOL_CATEGORY_ID);
	}

	public SchoolCategory getSchoolCategory () {
		return (SchoolCategory) getColumnValue (COLUMN_SCHOOL_CATEGORY_ID);
	}

	public Timestamp getStartTime () {
		return (Timestamp) getColumnValue (COLUMN_STARTTIME);
	}

	public Timestamp getEndTime () {
		return (Timestamp) getColumnValue (COLUMN_ENDTIME);
	}

	public int getSchoolCount () {
		return getIntColumnValue (COLUMN_SCHOOL_COUNT);
	}

	public void setSchoolCategoryId (final String id) {
		setColumn (COLUMN_SCHOOL_CATEGORY_ID, id);
	}

	public void setSchoolCategory (final SchoolCategory schoolCategory) {
		setColumn (COLUMN_SCHOOL_CATEGORY_ID, schoolCategory);
	}

	public void setStartTime (final Timestamp startTime) {
		setColumn (COLUMN_STARTTIME, startTime);
	}

	public void setEndTime (final Timestamp endTime) {
		setColumn (COLUMN_ENDTIME, endTime);
	}

	public void setSchoolCount (final int count) {
		setColumn (COLUMN_SCHOOL_COUNT, count);
	}

	public Integer ejbFindLatestBySchoolCategoryId (final String schoolCategoryId)
		throws FinderException {
		final IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEqualsQuoted (COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryId);
		sql.appendOrderByDescending (COLUMN_STARTTIME);
		return (Integer) idoFindOnePKByQuery (sql);
	}

	public Collection ejbFindOlderByTimestamp
		(final String schoolCategoryId, final Timestamp timestamp)
		throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEqualsQuoted (COLUMN_SCHOOL_CATEGORY_ID, schoolCategoryId);
		sql.appendAnd ().append (timestamp).appendGreaterThanSign ();
		sql.append (COLUMN_STARTTIME);
		return idoFindPKsByQuery (sql);
	}
}
