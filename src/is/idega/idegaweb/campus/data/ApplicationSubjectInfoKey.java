/*
 * Created on Mar 29, 2004
 *
 */
package is.idega.idegaweb.campus.data;

import com.idega.data.PrimaryKey;

/**
 * ApartmentAccountEntryKey
 * @author aron 
 * @version 1.0
 */
public class ApplicationSubjectInfoKey extends PrimaryKey {
	
	private String COLUMN_SUBJECT= ApplicationSubjectInfoBMPBean.SUBJECTID;
	private String COLUMN_STATUS = ApplicationSubjectInfoBMPBean.STATUS;
	
	/**
	 * @param batchID
	 * @param contractID
	 */
	public ApplicationSubjectInfoKey(Object subjectID, Object status) {
		this();
		setSubject(subjectID);
		setStatus(status);
	}
	
	public ApplicationSubjectInfoKey() {
		super();
	}
	
	public void setSubject(Object subjectID) {
		setPrimaryKeyValue(COLUMN_SUBJECT,subjectID);
	}

	public Object getSubject() {
		return getPrimaryKeyValue(COLUMN_SUBJECT);
	}

	public void setStatus(Object status) {
		setPrimaryKeyValue(COLUMN_STATUS, status);
	}

	public Object getStatus() {
		return getPrimaryKeyValue(COLUMN_STATUS);
	}
}
