/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.sql.Timestamp;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author bluebottle
 *
 */
public interface EconomaJournalLog extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setSchoolCategoryString
	 */
	public void setSchoolCategoryString(String category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getSchoolCategoryString
	 */
	public String getSchoolCategoryString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setEventFileCreated
	 */
	public void setEventFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setEventFileDeleted
	 */
	public void setEventFileDeleted();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setEventFileSent
	 */
	public void setEventFileSent();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setLocalizedEventKey
	 */
	public void setLocalizedEventKey(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getEventFileCreated
	 */
	public String getEventFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getEventFileDeleted
	 */
	public String getEventFileDeleted();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getEventFileSent
	 */
	public String getEventFileSent();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getLocalizedEventKey
	 */
	public String getLocalizedEventKey();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setEventDate
	 */
	public void setEventDate(Timestamp date);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getEventDate
	 */
	public Timestamp getEventDate();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setUserId
	 */
	public void setUserId(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaJournalLogBMPBean#getUser
	 */
	public User getUser();

}
