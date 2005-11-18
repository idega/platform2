/**
 * 
 */
package se.idega.idegaweb.commune.adulteducation.accounting.data;


import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.data.IDOEntity;

/**
 * @author bluebottle
 *
 */
public interface BatchParameters extends IDOEntity {
	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getBatchFromDate
	 */
	public int getBatchFromDate();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setBatchFromDate
	 */
	public void setBatchFromDate(int fromDate);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getBatchToDate
	 */
	public int getBatchToDate();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setBatchToDate
	 */
	public void setBatchToDate(int toDate);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getWeekLimit
	 */
	public int getWeekLimit();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setWeekLimit
	 */
	public void setWeekLimit(int weekLimit);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getLongPercentage
	 */
	public int getLongPercentage();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setLongPercentage
	 */
	public void setLongPercentage(int percentage);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getLongGradePercentage
	 */
	public int getLongGradePercentage();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setLongGradePercentage
	 */
	public void setLongGradePercentage(int percentage);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getShortPercentage
	 */
	public int getShortPercentage();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setShortPercentage
	 */
	public void setShortPercentage(int percentage);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getShortGradePercentage
	 */
	public int getShortGradePercentage();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setShortGradePercentage
	 */
	public void setShortGradePercentage(int percentage);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getFlexPeriodId
	 */
	public int getFlexPeriodId();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setFlexPeriodId
	 */
	public void setFlexPeriodId(int id);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getFlexPeriod
	 */
	public SchoolSeason getFlexPeriod();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setFlexPeriod
	 */
	public void setFlexPeriod(SchoolSeason period);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getSchoolTypeId
	 */
	public int getSchoolTypeId();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setSchoolTypeId
	 */
	public void setSchoolTypeId(int id);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType type);

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#getIncludedInBatch
	 */
	public boolean getIncludedInBatch();

	/**
	 * @see se.idega.idegaweb.commune.adulteducation.accounting.data.BatchParametersBMPBean#setIncludedInBatch
	 */
	public void setIncludedInBatch(boolean included);

}
