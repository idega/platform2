/*
 * $Id: PostingParametersBMPBean.java,v 1.30 2004/01/14 16:05:08 palli Exp $
 *
 * Copyright (C) 2003 Agura IT. All Rights Reserved.
 *
 * This software is the proprietary information of Agura IT AB.
 * Use is subject to license terms.
 * 
 */
package se.idega.idegaweb.commune.accounting.posting.data;
        
import java.util.Collection;
import java.sql.Date;
import java.sql.Timestamp;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOQuery;
import com.idega.block.school.data.SchoolManagementType;
import com.idega.block.school.data.SchoolType;
import com.idega.block.school.data.SchoolYear;
import se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
import se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
import com.idega.block.school.data.SchoolStudyPath;
import com.idega.util.CalendarMonth;

/**
 * PostingParameters Holds information about default posting info.
 * It is used to match a posting and get its posting accounts etc
 * 
 * Other submodules will use this data to search for a match on 
 * Period, Activity, Regulation sec, Company type and Commune belonging.
 * When you have a hit you can retrive accounting data such as accounts, resources, activity codes 
 * etc. These values are always mirrored in "Own entries" and "Double entries". See Book-Keeping
 * terms.
 *  
 * @see se.idega.idegaweb.commune.accounting.posting.data.PostingString;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.ActivityType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.RegulationSpecType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CompanyType;
 * @see se.idega.idegaweb.commune.accounting.regulations.data.CommuneBelongingType;
 * <p>
 * $Id: PostingParametersBMPBean.java,v 1.30 2004/01/14 16:05:08 palli Exp $
 * 
 * @author <a href="http://www.lindman.se">Kjell Lindman</a>
 * @version $Revision: 1.30 $
 */
public class PostingParametersBMPBean extends GenericEntity implements PostingParameters {
	
	private static final String ENTITY_NAME = "cacc_posting_parameters";

	private static final String COLUMN_PERIOD_FROM = "periode_from";
	private static final String COLUMN_PERIOD_TO = "periode_to";
	private static final String COLUMN_CHANGED_DATE = "changed_date";
	private static final String COLUMN_CHANGED_SIGN = "changed_sign";

	private static final String COLUMN_ACTIVITY_ID = "activity_id";
	private static final String COLUMN_REG_SPEC_TYPE_ID = "reg_spec_type_id";
	private static final String COLUMN_COMPANY_TYPE = "company_type_id";
	private static final String COLUMN_COMMUNE_BELONGING_ID = "commune_belonging_id";
	private static final String COLUMN_SCHOOL_YEAR1_ID = "school_year1_id";
	private static final String COLUMN_SCHOOL_YEAR2_ID = "school_year2_id";
	private static final String COLUMN_STUDY_PATH_ID = "study_path_id";
	
	private static final String COLUMN_OWN_POSTING_STRING = "own_posting_string";
	private static final String COLUMN_DOUBLE_POSTING_STRING = "double_posting_string";

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_PERIOD_FROM, "Period from", true, true, Date.class);
		addAttribute(COLUMN_PERIOD_TO, "Period  tom", true, true, Date.class);
		addAttribute(COLUMN_CHANGED_DATE, "Ändrings datum", true, true, java.sql.Timestamp.class);
		addAttribute(COLUMN_CHANGED_SIGN, "Ändrings sign", true, true, String.class);
		
		addAttribute(COLUMN_ACTIVITY_ID, "Verksamhet", true, true, 
						Integer.class, "many-to-one", SchoolType.class);
						
		addAttribute(COLUMN_REG_SPEC_TYPE_ID, "Regelspecificationstyp", true, true, 
						Integer.class, "many-to-one", RegulationSpecType.class);
						
		addAttribute(COLUMN_COMPANY_TYPE, "Bolagstyp", true, true, 
						String.class, "many-to-one", SchoolManagementType.class);
		
		addAttribute(COLUMN_COMMUNE_BELONGING_ID, "Kommuntillhörighet", true, true, 
						Integer.class, "many-to-one", CommuneBelongingType.class);

		addAttribute(COLUMN_SCHOOL_YEAR1_ID, "Skolår 1", true, true, 
						Integer.class, "many-to-one", SchoolYear.class);

		addAttribute(COLUMN_SCHOOL_YEAR2_ID, "Skolår 2", true, true, 
						Integer.class, "many-to-one", SchoolYear.class);

		addAttribute(COLUMN_STUDY_PATH_ID, "Study path", true, true, 
						Integer.class, "many-to-one", SchoolStudyPath.class);
	
		addAttribute(COLUMN_OWN_POSTING_STRING, "Egen konteringsstring", true, true, String.class);
		addAttribute(COLUMN_DOUBLE_POSTING_STRING, "Mot konteringsstring", true, true, String.class);
		
		setNullable (COLUMN_ACTIVITY_ID, true);
		setNullable (COLUMN_REG_SPEC_TYPE_ID, true);
		setNullable (COLUMN_COMPANY_TYPE, true);
		setNullable (COLUMN_COMMUNE_BELONGING_ID, true);
		setNullable (COLUMN_SCHOOL_YEAR1_ID, true);
		setNullable (COLUMN_SCHOOL_YEAR2_ID, true);
		setNullable (COLUMN_STUDY_PATH_ID, true);
	}
	
	public String getPostingString() {return getStringColumnValue(COLUMN_OWN_POSTING_STRING);}
	public void setPostingString(String data) {setColumn(COLUMN_OWN_POSTING_STRING, data); }

	public String getDoublePostingString() {return getStringColumnValue(COLUMN_DOUBLE_POSTING_STRING);}
	public void setDoublePostingString(String data) {setColumn(COLUMN_DOUBLE_POSTING_STRING, data); }


	public Timestamp getChangedDate(){
		return (Timestamp) getColumnValue(COLUMN_CHANGED_DATE);
	}
	
	public void setChangedDate(Timestamp date){
		setColumn(COLUMN_CHANGED_DATE, date);
	}
	
	public String getChangedSign() {
		return (String) getColumnValue(COLUMN_CHANGED_SIGN);
	}

	public void setChangedSign(String sign) {
		setColumn(COLUMN_CHANGED_SIGN, sign);
	}

	public void setPeriodFrom(Date period) { 
		setColumn(COLUMN_PERIOD_FROM, period); 
	}
	
	public void setPeriodTo(Date period) {
		CalendarMonth month = new CalendarMonth(period);
		setColumn(COLUMN_PERIOD_TO, month.getLastDateOfMonth()); 
	}
	
	public void setActivity(int id) {
		if (id != 0) { 
			setColumn(COLUMN_ACTIVITY_ID, id); 
		} else {
			removeFromColumn(COLUMN_ACTIVITY_ID);
		}			 
	}
	
	public void setRegSpecType(int id) { 
		if (id != 0) { 
			setColumn(COLUMN_REG_SPEC_TYPE_ID, id); 
		} else {
			removeFromColumn(COLUMN_REG_SPEC_TYPE_ID);
		}			 
	}
	
	public void setCompanyType(String id) { 
		if (id.compareTo("0") != 0) {
			setColumn(COLUMN_COMPANY_TYPE, id); 
		} else {
			removeFromColumn(COLUMN_COMPANY_TYPE);
		}			 
	}
	
	public void setCommuneBelonging(int id) { 
		if (id != 0) { 
			setColumn(COLUMN_COMMUNE_BELONGING_ID, id);
		} else {
			removeFromColumn(COLUMN_COMMUNE_BELONGING_ID);
		}			 
	}

	public void setSchoolYear1(int id) { 
		if (id != 0) { 
			setColumn(COLUMN_SCHOOL_YEAR1_ID, id);
		} else {
			removeFromColumn(COLUMN_SCHOOL_YEAR1_ID);
		}			 
	}

	public void setSchoolYear2(int id) { 
		if (id != 0) { 
			setColumn(COLUMN_SCHOOL_YEAR2_ID, id);
		} else {
			removeFromColumn(COLUMN_SCHOOL_YEAR2_ID);
		}			 
	}

	public void setStudyPath(int id) { 
		if (id != 0) { 
			setColumn(COLUMN_STUDY_PATH_ID, id);
		} else {
			removeFromColumn(COLUMN_STUDY_PATH_ID);
		}			 
	}

	public Date getPeriodFrom() {
		return (Date) getColumnValue(COLUMN_PERIOD_FROM);
	}

	public Date getPeriodTo() {
		return (Date) getColumnValue(COLUMN_PERIOD_TO);
	}

	public SchoolType getActivity() {
		return (SchoolType) getColumnValue(COLUMN_ACTIVITY_ID);
	}

	public RegulationSpecType getRegSpecType() {
		return (RegulationSpecType) getColumnValue(COLUMN_REG_SPEC_TYPE_ID);
	}
	
	public SchoolManagementType getCompanyType() {
		return (SchoolManagementType) getColumnValue(COLUMN_COMPANY_TYPE);
	}

	public SchoolYear getSchoolYear1() {
		return (SchoolYear) getColumnValue(COLUMN_SCHOOL_YEAR1_ID);
	}

	public SchoolYear getSchoolYear2() {
		return (SchoolYear) getColumnValue(COLUMN_SCHOOL_YEAR2_ID);
	}

	public SchoolStudyPath getStudyPath() {
		return (SchoolStudyPath) getColumnValue(COLUMN_STUDY_PATH_ID);
	}

	public CommuneBelongingType getCommuneBelonging() {
		return (CommuneBelongingType) getColumnValue(COLUMN_COMMUNE_BELONGING_ID);
	}
	
	public Collection ejbFindPostingParametersByPeriod(Date from, Date to) throws FinderException {
		to = getEndOfMonth(to);		
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIOD_FROM);
		sql.appendGreaterThanOrEqualsSign().append("'"+from+"'");
		sql.appendAnd().append(COLUMN_PERIOD_TO);
		sql.appendLessThanOrEqualsSign().append("'"+to+"'");
		sql.appendOrderByDescending(COLUMN_PERIOD_FROM);
		sql.append(", ");
		sql.append(COLUMN_ACTIVITY_ID);
		sql.append(", ");
		sql.append(COLUMN_REG_SPEC_TYPE_ID);
		System.out.println("Simple query: "+sql.toString());
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindPostingParametersByDate(Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhere(COLUMN_PERIOD_FROM);
		sql.appendLessThanOrEqualsSign().append("'"+date+"'");
		sql.appendAnd().append(COLUMN_PERIOD_TO);
		sql.appendGreaterThanOrEqualsSign().append("'"+date+"'");
		sql.appendOrderByDescending(COLUMN_PERIOD_FROM);
		sql.append(", ");
		sql.append(COLUMN_ACTIVITY_ID);
		sql.append(", ");
		sql.append(COLUMN_REG_SPEC_TYPE_ID);
		
		return idoFindPKsBySQL(sql.toString());
	}


	public Collection ejbFindAllPostingParameters() throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendOrderByDescending(COLUMN_PERIOD_FROM);
		sql.append(", ");
		sql.append(COLUMN_ACTIVITY_ID);
		sql.append(", ");
		sql.append(COLUMN_REG_SPEC_TYPE_ID);
		return idoFindPKsBySQL(sql.toString());
	}

	public Object ejbFindPostingParameter(
			Date date, 
			int act_id, 
			int reg_id, 
			String com_id, 
			int com_bel_id,
			int school_year_id1,
			int school_year_id2
		 ) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this);

		if (date != null) {
			sql.appendWhere(COLUMN_PERIOD_FROM);
			sql.appendLessThanOrEqualsSign().append("'"+date+"'");
			sql.appendAnd().append(COLUMN_PERIOD_TO);
			sql.appendGreaterThanOrEqualsSign().append("'"+date+"'");
		} else {
			return null;
		}

		if (act_id > 0) {
			sql.appendAndEquals(COLUMN_ACTIVITY_ID, act_id);
		}

		if (reg_id > 0) {
			sql.appendAndEquals(COLUMN_REG_SPEC_TYPE_ID, reg_id);
		}

		if (com_id != null) {
			if (com_id.length() != 0) {
				sql.appendAndEqualsQuoted(COLUMN_COMPANY_TYPE, com_id);
			}
		}

		if (com_bel_id > 0) {
			sql.appendAndEquals(COLUMN_COMMUNE_BELONGING_ID, com_bel_id);
		}

		if (school_year_id1 > 0) {
			sql.appendAndEquals(COLUMN_SCHOOL_YEAR1_ID, school_year_id1);
		}

		if (school_year_id2 > 0) {
			sql.appendAndEquals(COLUMN_SCHOOL_YEAR2_ID, school_year_id2);
		}
		return idoFindOnePKByQuery(sql);
	}

	/**
	 * Small mutation of the function above, to reflect what I think that it really is supposed to do.
	 * (JJ)
	 * @param date
	 * @param act_id
	 * @param reg_id
	 * @param com_id
	 * @param com_bel_id
	 * @param school_year_id
	 * @return
	 * @throws FinderException
	 */
	public Object ejbFindPostingParameter(
			Date date, 
			int act_id, 
			int reg_id, 
			String com_id, 
			int com_bel_id,
			int school_year,
			int study_path_id,
			boolean no_study_path
		 ) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this);

        boolean sqlHasWhere = false;

		if (date != null) {
            if (sqlHasWhere) {
                sql.appendAnd ();
            } else {
                sql.appendWhere ();
                sqlHasWhere = true;
            }
			sql.append(COLUMN_PERIOD_FROM);
			sql.appendLessThanOrEqualsSign().append("'"+date+"'");
			sql.appendAnd().append(COLUMN_PERIOD_TO);
			sql.appendGreaterThanOrEqualsSign().append("'"+date+"'");
		} else {
            logDebug ("No date set in ejbFindPostingParameter (null"
                      + act_id + ", " + reg_id + ", " + com_id + ", " 
                      + com_bel_id + ", "	+ school_year);
			return null;
		}

		if (act_id > 0) {
            if (sqlHasWhere) {
                sql.appendAnd ();
            } else {
                sql.appendWhere ();
                sqlHasWhere = true;
            }
			sql.appendEquals(COLUMN_ACTIVITY_ID, act_id);
		}

		if (reg_id > 0) {
            if (sqlHasWhere) {
                sql.appendAnd ();
            } else {
                sql.appendWhere ();
                sqlHasWhere = true;
            }
			sql.appendEquals(COLUMN_REG_SPEC_TYPE_ID, reg_id);
		}

		if (com_id != null) {
			if (com_id.length() != 0) {
                if (sqlHasWhere) {
                    sql.appendAnd ();
                } else {
                    sql.appendWhere ();
                    sqlHasWhere = true;
                }
				sql.appendEqualsQuoted(COLUMN_COMPANY_TYPE, com_id);
			}
		}

		if (com_bel_id > 0) {
            if (sqlHasWhere) {
                sql.appendAnd ();
            } else {
                sql.appendWhere ();
                sqlHasWhere = true;
            }
			sql.appendEquals(COLUMN_COMMUNE_BELONGING_ID, com_bel_id);
		}
		
		if (no_study_path) {
			if (sqlHasWhere) {
				sql.appendAnd();
			}
			else {
				sql.appendWhere();
				sqlHasWhere = true;
			}
			
			sql.append(COLUMN_STUDY_PATH_ID);
			sql.append(" is null ");
			
		}
		else if (study_path_id > 0) {
			if (sqlHasWhere) {
				sql.appendAnd();
			}
			else {
				sql.appendWhere();
				sqlHasWhere = true;
			}
			
			sql.appendEquals(COLUMN_STUDY_PATH_ID, study_path_id);
		}

		if (school_year > 0) {
            if (sqlHasWhere) {
                sql.appendAnd ();
            } else {
                sql.appendWhere ();
                sqlHasWhere = true;
            }
			sql.append(COLUMN_SCHOOL_YEAR1_ID);
			sql.appendLessThanOrEqualsSign().append("'"+school_year+"'");
			sql.appendAnd().append(COLUMN_SCHOOL_YEAR2_ID);
			sql.appendGreaterThanOrEqualsSign().append("'"+school_year+"'");
		}

		return idoFindOnePKByQuery(sql);
	}


	public Object ejbFindPostingParameter(int act, int reg, int comt, int comb ) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_ACTIVITY_ID, act);
		sql.appendEquals(COLUMN_REG_SPEC_TYPE_ID, reg);
		sql.appendEquals(COLUMN_COMPANY_TYPE, comt);
		sql.appendEquals(COLUMN_COMMUNE_BELONGING_ID, comb);
		return idoFindOnePKByQuery(sql);
	}


	public Object ejbFindPostingParameter(
			Date from,
			Date to,
			String ownPosting,
			String doublePosting,
			int activityType,
			int regSpecType,
			String companyType,
			int communeBelonging,
			int schoolYear1,
			int schoolYear2,
			int studyPath) throws FinderException {
		to = getEndOfMonth(to);		
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(COLUMN_ACTIVITY_ID, activityType);
		sql.appendAndEqualsQuoted(COLUMN_PERIOD_FROM, from.toString());
		sql.appendAndEqualsQuoted(COLUMN_PERIOD_TO, to.toString());
		sql.appendAndEqualsQuoted(COLUMN_OWN_POSTING_STRING, ownPosting);
		sql.appendAndEqualsQuoted(COLUMN_DOUBLE_POSTING_STRING, doublePosting);
		sql.appendAndEqualsQuoted(COLUMN_COMPANY_TYPE, companyType);
		sql.appendAndEquals(COLUMN_REG_SPEC_TYPE_ID, regSpecType);
		sql.appendAndEquals(COLUMN_COMMUNE_BELONGING_ID, communeBelonging);
		sql.appendAndEquals(COLUMN_SCHOOL_YEAR1_ID, schoolYear1);
		sql.appendAndEquals(COLUMN_SCHOOL_YEAR2_ID, schoolYear2);
		sql.appendAndEquals(COLUMN_STUDY_PATH_ID, studyPath);
		System.out.println(sql.toString());
		return idoFindOnePKByQuery(sql);
	}

	public Object ejbFindPostingParameter(int id) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhereEquals(getIDColumnName(), id);
		return idoFindOnePKByQuery(sql);
	}

/*
 * This is a fix to always make sure the last date in the (to) month is covered
 * See nacp377 
 */
	private Date getEndOfMonth(Date date) {
		CalendarMonth fixedDate = new CalendarMonth(date);
		return fixedDate.getLastDateOfMonth();
	}	

}
