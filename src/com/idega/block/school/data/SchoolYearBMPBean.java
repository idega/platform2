package com.idega.block.school.data;

import com.idega.data.*;

import java.util.Collection;

import javax.ejb.FinderException;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author <br>
 *         <a href="mailto:aron@idega.is">Aron Birkir </a> <br>
 * @version 1.0
 */

public class SchoolYearBMPBean extends GenericEntity implements SchoolYear {

	public final static String SCHOOLYEAR = "SCH_SCHOOL_YEAR";
	public final static String NAME = "YEAR_NAME";
	public final static String INFO = "YEAR_INFO";
	public final static String LOCALIZED_KEY = "localized_key";
	public final static String AGE = "YEAR_AGE";
	public final static String SCHOOL_TYPE = "SCHOOL_TYPE";
	public final static String SCHOOL_CATEGORY = "SCHOOL_CATEGORY";
	public final static String IS_SELECTABLE = "is_selectable";

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(NAME, "Name", true, true, String.class);
		addAttribute(INFO, "Info", true, true, String.class);
		addAttribute(AGE, "Age", true, true, Integer.class);
		addAttribute(LOCALIZED_KEY, "Localized key", true, true, String.class);
		addAttribute(IS_SELECTABLE, "Is selectable", true, true, Boolean.class);
		setNullable(NAME, false);
		setUnique(NAME, true);
		this.addManyToOneRelationship(SCHOOL_TYPE, SchoolType.class);
		this.addManyToOneRelationship(SCHOOL_CATEGORY, SchoolCategory.class);
	}

	public String getEntityName() {
		return SCHOOLYEAR;
	}

	public String getName() {
		return getSchoolYearName();
	}

	public void setSchoolYearName(String name) {
		this.setColumn(NAME, name);
	}

	public String getSchoolYearName() {
		return getStringColumnValue(NAME);
	}

	public void setSchoolYearInfo(String info) {
		this.setColumn(INFO, info);
	}

	public String getSchoolYearInfo() {
		return getStringColumnValue(INFO);
	}

	public void setSchoolYearAge(int age) {
		this.setColumn(AGE, age);
	}

	public int getSchoolYearAge() {
		return getIntColumnValue(AGE);
	}

	public int getSchoolTypeId() {
		int schoolTypeId = getIntColumnValue(SCHOOL_TYPE);

		if (schoolTypeId < 1) {
			System.out.println("SchoolYearBMPBean : SchoolTypeId = " + schoolTypeId + " ... trying to find a new one");
			String name = this.getSchoolYearName();
			if (name != null) {
				if (name.equalsIgnoreCase("f")) {
					System.out.println("... NEEDS backwards compatability ...");
				}
				else {
					System.out.println("... NEEDS backwards compatability ...");
				}
			}
			else {
				System.out.println("... SchoolYear has name = NULL (pk=" + getPrimaryKey().toString() + ")");
			}
		}

		return schoolTypeId;
	}

	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE);
	}

	public void setSchoolTypeId(int id) {
		setColumn(SCHOOL_TYPE, id);
	}

	public Object getSchoolCategoryPK() {
		return getColumnValue(SCHOOL_CATEGORY);
	}

	public SchoolCategory getSchoolCategory() {
		return (SchoolCategory) getColumnValue(SCHOOL_CATEGORY);
	}

	public void setSchoolCategory(Object schoolCategory) {
		setColumn(SCHOOL_CATEGORY, schoolCategory);
	}
	
	public boolean isSelectable() {
		return getBooleanColumnValue(IS_SELECTABLE, true);
	}
	
	public void setIsSelectable(boolean isSelectable) {
		setColumn(IS_SELECTABLE, isSelectable);
	}
	
	public String getLocalizedKey() {
		return getStringColumnValue(LOCALIZED_KEY);
	}
	
	public void setLocalizedKey(String localizedKey) {
		setColumn(LOCALIZED_KEY, localizedKey);
	}

	/**
	 * Gets the previous schoolYear from the age of this schoolYear-1
	 * 
	 * @return @throws
	 *         FinderException If no SchoolYear is found
	 */
	public SchoolYear getPreviousSchoolYearFromAge() throws FinderException {
		return ((SchoolYearHome) this.getEJBLocalHome()).findPreviousSchoolYearFromAge(this);
	}

	public Collection getSchoolYears(SchoolType schoolType) throws FinderException {
		return this.idoFindAllIDsByColumnBySQL(SCHOOL_TYPE, schoolType.getPrimaryKey().toString());
	}

	public Collection ejbFindAllSchoolYears() throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendOrderBy(NAME);
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllSchoolYearBySchoolType(int schoolTypeId) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName());
		sql.appendWhereEquals(SCHOOL_TYPE, schoolTypeId);
		sql.appendOrderBy(getIDColumnName());
		return super.idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllSchoolYearBySchoolCategory(SchoolCategory schoolCategory) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName());
		sql.appendWhereEquals(SCHOOL_CATEGORY, schoolCategory);
		sql.appendOrderBy(getIDColumnName());
		return super.idoFindPKsByQuery(sql);
	}
	
	public Collection ejbFindAllSchoolYearsBySchoolCategory(SchoolCategory schoolCategory, boolean showSelectable) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName());
		sql.appendWhereEquals(SCHOOL_CATEGORY, schoolCategory);
		if (showSelectable) {
			sql.appendAnd().appendLeftParenthesis().append(IS_SELECTABLE).appendEqualSign().append(showSelectable);
			sql.appendOr().append(IS_SELECTABLE).appendIsNull().appendRightParenthesis();
		}
		else {
			sql.appendAndEquals(IS_SELECTABLE, showSelectable);
		}
		sql.appendOrderBy(getIDColumnName());
		return super.idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllBySchoolAndSchoolCategory(School school, SchoolCategory schoolCategory, boolean showSelectable) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).append(" y, sch_school_sch_school_year sy");
		sql.appendWhereEquals(SCHOOL_CATEGORY, schoolCategory);
		if (showSelectable) {
			sql.appendAnd().appendLeftParenthesis().append(IS_SELECTABLE).appendEqualSign().append(showSelectable);
			sql.appendOr().append(IS_SELECTABLE).appendIsNull().appendRightParenthesis();
		}
		else {
			sql.appendAndEquals(IS_SELECTABLE, showSelectable);
		}
		sql.appendAndEquals("y." + getIDColumnName(), "sy." + getIDColumnName());
		sql.appendAndEquals("sy.sch_school_id", school);
		sql.appendOrderBy("y." + getIDColumnName());
		return super.idoFindPKsByQuery(sql);
	}

	/**
	 * @deprecated
	 */
	public Collection ejbFindAllByAge(int age) throws javax.ejb.FinderException {
		return super.idoFindPKsBySQL("select * from " + getEntityName() + " where " + AGE + " like '" + age + "'");
	}

	public Collection ejbFindAllByAge(SchoolType schoolType, int age) throws javax.ejb.FinderException {
		return super.idoFindPKsBySQL("select * from " + getEntityName() + " where " + AGE + " like '" + age + "' AND " + SCHOOL_TYPE + " = '" + schoolType.getPrimaryKey().toString() + "'");
	}

	public Collection ejbFindAllByIDs(String[] schoolYearIDs) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).appendWhere().append(getIDColumnName()).appendInArray(schoolYearIDs);
		query.appendOrderBy(AGE);
		return idoFindPKsByQuery(query);
	}

	/**
	 * @deprecated
	 */
	public Integer ejbFindByYearName(String name) throws javax.ejb.FinderException {
		return (Integer) super.idoFindOnePKBySQL("select * from " + getEntityName() + " where " + NAME + " like '" + name + "'");
	}

	public Integer ejbFindByYearName(SchoolType schoolType, String name) throws javax.ejb.FinderException {
		return (Integer) super.idoFindOnePKBySQL("select * from " + getEntityName() + " where " + NAME + " like '" + name + "' AND " + SCHOOL_TYPE + " = '" + schoolType.getPrimaryKey().toString() + "'");
	}

	public Integer ejbFindByYearName(SchoolCategory schoolCategory, String name) throws javax.ejb.FinderException {
		return (Integer) super.idoFindOnePKBySQL("select * from " + getEntityName() + " where " + NAME + " like '" + name + "' AND " + SCHOOL_CATEGORY + " = '" + schoolCategory.getPrimaryKey().toString() + "'");
	}

	public Collection ejbFindBySchoolCategory(String schoolCategory) throws javax.ejb.FinderException {
		IDOQuery query = idoQuery();
		query.appendSelect().append("y.*").appendFrom().append(getEntityName()).append(" y, sch_school_type t ");
		query.appendWhereEquals("y." + SCHOOL_TYPE, "t.sch_school_type_id").appendAndEqualsQuoted("t.school_category", schoolCategory);
		return idoFindPKsByQuery(query);
	}

	/**
	 * Finds the year (the first found) from the schoolYearAge of year-1. <br>
	 * Tries to first find the hear by the same SchoolType as parameter year , if
	 * no are found it finds by all schoolTypes. <br>
	 * 
	 * @param year
	 *          the year to find previous year for
	 * @return @throws
	 *         FinderException
	 */
	public Integer ejbFindPreviousSchoolYearFromAge(SchoolYear year) throws FinderException {

		int previousYearAge = year.getSchoolYearAge() - 1;

		Collection coll = this.ejbFindAllByAge(year.getSchoolType(), previousYearAge);
		if (coll.isEmpty()) {
			coll = this.ejbFindAllByAge(previousYearAge);
		}
		if (!coll.isEmpty()) {
			return (Integer) coll.iterator().next();
		}
		else {
			throw new IDOFinderException("SchoolYear: No Previous SchoolYears found");
		}
	}

}