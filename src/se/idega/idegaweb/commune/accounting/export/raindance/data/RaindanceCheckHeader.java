/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.raindance.data;

import java.sql.Timestamp;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface RaindanceCheckHeader extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setSchoolCategoryString
	 */
	public void setSchoolCategoryString(String category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getSchoolCategoryString
	 */
	public String getSchoolCategoryString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setStatusFileCreated
	 */
	public void setStatusFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setStatus
	 */
	public void setStatus(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getStatusFileCreated
	 */
	public String getStatusFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setEventDate
	 */
	public void setEventDate(Timestamp date);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getEventDate
	 */
	public Timestamp getEventDate();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setEventStartTime
	 */
	public void setEventStartTime(Timestamp time);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getEventStartTime
	 */
	public Timestamp getEventStartTime();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#setEventEndTime
	 */
	public void setEventEndTime(Timestamp time);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.raidance.data.RaindanceCheckHeaderBMPBean#getEventEndTime
	 */
	public Timestamp getEventEndTime();

}
