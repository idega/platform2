/**
 * 
 */
package se.idega.idegaweb.commune.accounting.export.economa.data;

import java.sql.Timestamp;

import com.idega.block.school.data.SchoolCategory;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface EconomaCheckHeader extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setSchoolCategoryString
	 */
	public void setSchoolCategoryString(String category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setSchoolCategory
	 */
	public void setSchoolCategory(SchoolCategory category);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getSchoolCategoryString
	 */
	public String getSchoolCategoryString();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getSchoolCategory
	 */
	public SchoolCategory getSchoolCategory();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setStatusFileCreated
	 */
	public void setStatusFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setStatus
	 */
	public void setStatus(String key);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getStatusFileCreated
	 */
	public String getStatusFileCreated();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getStatus
	 */
	public String getStatus();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setEventDate
	 */
	public void setEventDate(Timestamp date);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getEventDate
	 */
	public Timestamp getEventDate();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setEventStartTime
	 */
	public void setEventStartTime(Timestamp time);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getEventStartTime
	 */
	public Timestamp getEventStartTime();

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#setEventEndTime
	 */
	public void setEventEndTime(Timestamp time);

	/**
	 * @see se.idega.idegaweb.commune.accounting.export.economa.data.EconomaCheckHeaderBMPBean#getEventEndTime
	 */
	public Timestamp getEventEndTime();

}
