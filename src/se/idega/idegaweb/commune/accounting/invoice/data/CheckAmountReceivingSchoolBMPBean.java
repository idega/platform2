package se.idega.idegaweb.commune.accounting.invoice.data;

import com.idega.block.school.data.School;
import com.idega.block.school.data.SchoolBMPBean;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import java.util.Collection;
import javax.ejb.FinderException;

/**
 * Last modified: $Date: 2004/03/22 13:01:14 $ by $Author: staffan $
 *
 * @author <a href="http://www.staffannoteberg.com">Staffan Nöteberg</a>
 * @version $Revision: 1.1 $
 */
public class CheckAmountReceivingSchoolBMPBean extends GenericEntity
	implements CheckAmountReceivingSchool {
	public static final String ENTITY_NAME
		= "cacc_ca_receiving_school";

	public static final String COLUMN_CHECK_AMOUNT_BROADCAST_ID
		= CheckAmountBroadcastBMPBean.ENTITY_NAME + "_id";
	public static final String COLUMN_SCHOOL_ID = SchoolBMPBean.SCHOOL + "_id";
	public static final String COLUMN_PAYMENT_RECORD_COUNT
		= "payment_record_count";
	public static final String COLUMN_IS_BY_EMAIL = "is_by_email";
	
	public String getEntityName () {
		return ENTITY_NAME;
	}

	public void initializeAttributes () {
		addAttribute (getIDColumnName ());
		addManyToOneRelationship (COLUMN_CHECK_AMOUNT_BROADCAST_ID,
															CheckAmountBroadcast.class);
		addManyToOneRelationship (COLUMN_SCHOOL_ID, School.class);
		addAttribute (COLUMN_PAYMENT_RECORD_COUNT, "", true, true, Integer.class);
		addAttribute (COLUMN_IS_BY_EMAIL, "", true, true, Boolean.class);
	}

	public int getCheckAmountBroadcastId () {
		return getIntColumnValue (COLUMN_CHECK_AMOUNT_BROADCAST_ID);
	}

	public CheckAmountBroadcast getCheckAmountBroadcast () {
		return (CheckAmountBroadcast) getColumnValue
				(COLUMN_CHECK_AMOUNT_BROADCAST_ID);
	}

	public int getSchoolId () {
		return getIntColumnValue (COLUMN_SCHOOL_ID);
	}

	public School getSchool () {
		return (School) getColumnValue (COLUMN_SCHOOL_ID);
	}

	public int getPaymentRecordCount () {
		return getIntColumnValue (COLUMN_PAYMENT_RECORD_COUNT);
	}

	public boolean isByEmail () {
		return getBooleanColumnValue (COLUMN_IS_BY_EMAIL);
	}

	public void setCheckAmountBroadcastId (final int id) {
		setColumn (COLUMN_CHECK_AMOUNT_BROADCAST_ID, id);
	}

	public void setCheckAmountBroadcast
		(final CheckAmountBroadcast checkAmountBroadcats) {
		setColumn (COLUMN_CHECK_AMOUNT_BROADCAST_ID, checkAmountBroadcats);
	}

	public void setSchoolId (final int id) {
		setColumn (COLUMN_SCHOOL_ID, id);
	}

	public void setSchool (final School school) {
		setColumn (COLUMN_SCHOOL_ID, school);
	}

	public void setPaymentRecordCount (final int count) {
		setColumn (COLUMN_PAYMENT_RECORD_COUNT, count);
	}

	public void setIsByEmail (final boolean isByEmail) {
		setColumn (COLUMN_IS_BY_EMAIL, isByEmail);
	}

	public Collection ejbFindEmailedProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEquals (COLUMN_CHECK_AMOUNT_BROADCAST_ID, broadcastInfo);
		sql.appendAndEquals (COLUMN_IS_BY_EMAIL, true);
		return idoFindPKsByQuery (sql);
	}

	public Collection ejbFindPaperMailedProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEquals (COLUMN_CHECK_AMOUNT_BROADCAST_ID, broadcastInfo);
		sql.appendAndEquals (COLUMN_IS_BY_EMAIL, false);
		sql.appendAnd ();
		sql.append (0).appendLessThanSign ().append (COLUMN_PAYMENT_RECORD_COUNT);
		return idoFindPKsByQuery (sql);
	}

	public Collection ejbFindIgnoredProvidersByCheckAmountBroadcast
		(final CheckAmountBroadcast broadcastInfo) throws FinderException {
		final IDOQuery sql = idoQuery ();
		sql.appendSelectAllFrom (this);
		sql.appendWhereEquals (COLUMN_CHECK_AMOUNT_BROADCAST_ID, broadcastInfo);
		sql.appendAnd ().append (0).appendGreaterThanOrEqualsSign ();
		sql.append (COLUMN_PAYMENT_RECORD_COUNT);
		return idoFindPKsByQuery (sql);
	}
}
