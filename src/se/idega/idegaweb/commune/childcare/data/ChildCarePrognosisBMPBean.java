/*
 * Created on 25.3.2003
 */
package se.idega.idegaweb.commune.childcare.data;

import java.sql.Date;

import javax.ejb.FinderException;

import com.idega.block.school.data.School;
import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;

/**
 * @author laddi
 */
public class ChildCarePrognosisBMPBean extends GenericEntity implements ChildCarePrognosis {

	private final static String ENTITY_NAME = "comm_childcare_prognosis";
	
	private final static String PROVIDER_ID = "provider_id";
	private final static String UPDATED_DATE = "updated_date";
	private final static String THREE_MONTHS_PROGNOSIS = "three_months_prognosis";
	private final static String ONE_YEAR_PROGNOSIS = "one_year_prognosis";
	private final static String THREE_MONTHS_PRIORITY = "three_months_priority";
	private final static String ONE_YEAR_PRIORITY = "one_year_priority";

	/**
	 * @see com.idega.data.IDOLegacyEntity#getEntityName()
	 */
	public String getEntityName() {
		return ENTITY_NAME;
	}

	/**
	 * @see com.idega.data.IDOLegacyEntity#initializeAttributes()
	 */
	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(UPDATED_DATE,"",true,true,java.sql.Date.class);
		addAttribute(THREE_MONTHS_PROGNOSIS,"",true,true,java.lang.Integer.class);
		addAttribute(ONE_YEAR_PROGNOSIS,"",true,true,java.lang.Integer.class);
		addAttribute(THREE_MONTHS_PRIORITY,"",true,true,java.lang.Integer.class);
		addAttribute(ONE_YEAR_PRIORITY,"",true,true,java.lang.Integer.class);
		addOneToOneRelationship(PROVIDER_ID,School.class);
	}

	/**
	 * @return int
	 */
	public int getOneYearPrognosis() {
		return getIntColumnValue(ONE_YEAR_PROGNOSIS);
	}

	/**
	 * @return int
	 */
	public int getOneYearPriority() {
		return getIntColumnValue(ONE_YEAR_PRIORITY);
	}

	/**
	 * @return int
	 */
	public int getProviderID() {
		return getIntColumnValue(PROVIDER_ID);
	}

	/**
	 * @return int
	 */
	public int getThreeMonthsPrognosis() {
		return getIntColumnValue(THREE_MONTHS_PROGNOSIS);
	}

	/**
	 * @return int
	 */
	public int getThreeMonthsPriority() {
		return getIntColumnValue(THREE_MONTHS_PRIORITY);
	}

	/**
	 * @return Date
	 */
	public Date getUpdatedDate() {
		return (Date)getColumnValue(UPDATED_DATE);
	}

	/**
	 * Sets the oneYearPrognosis.
	 * @param oneYearPrognosis The oneYearPrognosis to set
	 */
	public void setOneYearPrognosis(int oneYearPrognosis) {
		setColumn(ONE_YEAR_PROGNOSIS,oneYearPrognosis);
	}

	/**
	 * Sets the oneYearPriority.
	 * @param oneYearPriority The oneYearPriority to set
	 */
	public void setOneYearPriority(int oneYearPriority) {
		setColumn(ONE_YEAR_PRIORITY,oneYearPriority);
	}

	/**
	 * Sets the providerID.
	 * @param providerID The providerID to set
	 */
	public void setProviderID(int providerID) {
		setColumn(PROVIDER_ID,providerID);
	}

	/**
	 * Sets the threeMonthsPrognosis.
	 * @param threeMonthsPrognosis The threeMonthsPrognosis to set
	 */
	public void setThreeMonthsPrognosis(int threeMonthsPrognosis) {
		setColumn(THREE_MONTHS_PROGNOSIS,threeMonthsPrognosis);
	}

	/**
	 * Sets the threeMonthsPriority.
	 * @param threeMonthsPriority The threeMonthsPriority to set
	 */
	public void setThreeMonthsPriority(int threeMonthsPriority) {
		setColumn(THREE_MONTHS_PRIORITY,threeMonthsPriority);
	}

	/**
	 * Sets the updatedDate.
	 * @param updatedDate The updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		setColumn(UPDATED_DATE,updatedDate);
	}
	
	public Integer ejbFindPrognosis(int providerID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(PROVIDER_ID, providerID);
		return (Integer) idoFindOnePKByQuery(sql);
	}
}