/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.sql.Timestamp;


import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;
import com.idega.user.data.User;

/**
 * @author bluebottle
 *
 */
public interface RaindanceJournalLog extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setSchoolCategoryString
	 */
	public void setSchoolCategoryString(String category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getSchoolCategoryString
	 */
	public String getSchoolCategoryString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setEventFileCreated
	 */
	public void setEventFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setEventFileDeleted
	 */
	public void setEventFileDeleted();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setEventFileSent
	 */
	public void setEventFileSent();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setLocalizedEventKey
	 */
	public void setLocalizedEventKey(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getEventFileCreated
	 */
	public String getEventFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getEventFileDeleted
	 */
	public String getEventFileDeleted();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getEventFileSent
	 */
	public String getEventFileSent();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getLocalizedEventKey
	 */
	public String getLocalizedEventKey();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setEventDate
	 */
	public void setEventDate(Timestamp date);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getEventDate
	 */
	public Timestamp getEventDate();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setUserId
	 */
	public void setUserId(int id);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#setUser
	 */
	public void setUser(User user);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getUserId
	 */
	public int getUserId();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raindance.data.RaindanceJournalLogBMPBean#getUser
	 */
	public User getUser();

}
