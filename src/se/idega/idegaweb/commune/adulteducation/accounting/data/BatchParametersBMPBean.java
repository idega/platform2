package se.idega.idegaweb.commune.adulteducation.accounting.data;

import javax.ejb.FinderException;

import com.idega.block.school.data.SchoolSeason;
import com.idega.block.school.data.SchoolType;
import com.idega.data.GenericEntity;
import com.idega.data.query.MatchCriteria;
import com.idega.data.query.SelectQuery;
import com.idega.data.query.Table;
import com.idega.data.query.WildCardColumn;

public class BatchParametersBMPBean extends GenericEntity implements BatchParameters {

	private static final String ENTITY_NAME = "cacc_vux_batch_param";

	private static final String COLUMN_BATCH_FROM_DATE = "batch_from_date";

	private static final String COLUMN_BATCH_TO_DATE = "batch_to_date";

	private static final String COLUMN_WEEK_LIMIT = "week_limit";

	private static final String COLUMN_LONG_PERCENTAGE = "long_percentage";

	private static final String COLUMN_LONG_GRADE_PERCENTAGE = "long_grade_percentage";

	private static final String COLUMN_SHORT_PERCENTAGE = "short_percentage";

	private static final String COLUMN_SHORT_GRADE_PERCENTAGE = "short_grade_percentage";

	private static final String COLUMN_FLEX_PERIOD = "flex_period_id";

	private static final String COLUMN_SCHOOL_TYPE = "sch_school_type_id";

	private static final String COLUMN_INCLUDED_IN_BATCH = "included_in_batch";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_BATCH_FROM_DATE, "Batch from date", true, true, Integer.class);
		addAttribute(COLUMN_BATCH_TO_DATE, "Batch to date", true, true, Integer.class);
		addAttribute(COLUMN_WEEK_LIMIT, "Week limit", true, true, Integer.class);
		addAttribute(COLUMN_LONG_PERCENTAGE, "Long percentage", true, true, Integer.class);
		addAttribute(COLUMN_LONG_GRADE_PERCENTAGE, "Long grade percentage", true, true, Integer.class);
		addAttribute(COLUMN_SHORT_PERCENTAGE, "Short percentage", true, true, Integer.class);
		addAttribute(COLUMN_SHORT_GRADE_PERCENTAGE, "Short grade percentage", true, true, Integer.class);
		addManyToOneRelationship(COLUMN_FLEX_PERIOD, "Flexi period id", SchoolSeason.class);
		addManyToOneRelationship(COLUMN_SCHOOL_TYPE, "School type id", SchoolType.class);
		addAttribute(COLUMN_INCLUDED_IN_BATCH, "Included in batch", true, true, Boolean.class);
	}

	public int getBatchFromDate() {
		return getIntColumnValue(COLUMN_BATCH_FROM_DATE);
	}
	
	public void setBatchFromDate(int fromDate) {
		setColumn(COLUMN_BATCH_FROM_DATE, fromDate);
	}
	
	public int getBatchToDate() {
		return getIntColumnValue(COLUMN_BATCH_TO_DATE);
	}
	
	public void setBatchToDate(int toDate) {
		setColumn(COLUMN_BATCH_TO_DATE, toDate);
	}
	
	public int getWeekLimit() {
		return getIntColumnValue(COLUMN_WEEK_LIMIT);
	}
	
	public void setWeekLimit(int weekLimit) {
		setColumn(COLUMN_WEEK_LIMIT, weekLimit);
	}
	
	public int getLongPercentage() {
		return getIntColumnValue(COLUMN_LONG_PERCENTAGE);
	}
	
	public void setLongPercentage(int percentage) {
		setColumn(COLUMN_LONG_PERCENTAGE, percentage);
	}
	
	public int getLongGradePercentage() {
		return getIntColumnValue(COLUMN_LONG_GRADE_PERCENTAGE);
	}
	
	public void setLongGradePercentage(int percentage) {
		setColumn(COLUMN_LONG_GRADE_PERCENTAGE, percentage);
	}
	
	public int getShortPercentage() {
		return getIntColumnValue(COLUMN_SHORT_PERCENTAGE);
	}
	
	public void setShortPercentage(int percentage) {
		setColumn(COLUMN_SHORT_PERCENTAGE, percentage);
	}
	
	public int getShortGradePercentage() {
		return getIntColumnValue(COLUMN_SHORT_GRADE_PERCENTAGE);
	}
	
	public void setShortGradePercentage(int percentage) {
		setColumn(COLUMN_SHORT_GRADE_PERCENTAGE, percentage);
	}

	public int getFlexPeriodId() {
		return getIntColumnValue(COLUMN_FLEX_PERIOD);
	}
	
	public void setFlexPeriodId(int id) {
		setColumn(COLUMN_FLEX_PERIOD, id);
	}
	
	public SchoolSeason getFlexPeriod() {
		return (SchoolSeason) getColumnValue(COLUMN_FLEX_PERIOD);
	}
	
	public void setFlexPeriod(SchoolSeason period) {
		setColumn(COLUMN_FLEX_PERIOD, period);
	}
	
	public int getSchoolTypeId() {
		return getIntColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public void setSchoolTypeId(int id) {
		setColumn(COLUMN_SCHOOL_TYPE, id);
	}
	
	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(COLUMN_SCHOOL_TYPE);
	}
	
	public void setSchoolType(SchoolType type) {
		setColumn(COLUMN_SCHOOL_TYPE, type);
	}

	public boolean getIncludedInBatch() {
		return getBooleanColumnValue(COLUMN_INCLUDED_IN_BATCH, false);
	}
	
	public void setIncludedInBatch(boolean included) {
		setColumn(COLUMN_INCLUDED_IN_BATCH, included);
	}
	
	public Object ejbFindBySchoolType(SchoolType schoolType) throws FinderException {
		return ejbFindBySchoolTypeId(((Integer) schoolType.getPrimaryKey()).intValue());
	}
	
	public Object ejbFindBySchoolTypeId(int schoolTypeId) throws FinderException {
		Table table = new Table(this);

		SelectQuery query = new SelectQuery(table);
		query.addColumn(new WildCardColumn(table));
		query.addCriteria(new MatchCriteria(table, COLUMN_SCHOOL_TYPE, MatchCriteria.EQUALS, schoolTypeId));

		return idoFindOnePKByQuery(query);
	}
}