package com.idega.block.school.data;

import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author <br><a href="mailto:aron@idega.is">Aron Birkir</a><br>
 * Last modified: $Date: 2003/10/13 09:09:13 $ by $Author: staffan $
 * @version $Revision: 1.41 $
 */

public class SchoolClassMemberBMPBean extends GenericEntity implements SchoolClassMember {
	public final static String SCHOOLCLASSMEMBERID = "sch_class_member_id";
	public final static String SCHOOLCLASSMEMBER = "sch_class_member";
	public final static String MEMBER = "ic_user_id";
	public final static String NOTES = "notes";
	public final static String SCHOOLCLASS = "sch_school_class_id";
	public final static String REGISTER_DATE = "register_date";
	public final static String REMOVED_DATE = "removed_date";
	public final static String REGISTRATOR = "registrator";
	public final static String NEEDS_SPECIAL_ATTENTION = "NEEDS_SPECIAL_ATTENTION";
	public final static String SPECIALLY_PLACED = "SPECIALLY_PLACED";
	public final static String LANGUAGE = "LANGUAGE";
	//Added for the kompliterings project
	public final static String COMPENSATION_BY_INVOICE = "comp_by_invoice";
	public final static String INVOICE_INTERVAL = "invoice_int";
	public final static String LATEST_INVOICE_DATE = "latest_invoice_date";
	// Borgman added field for kompletteringprojekt
	public final static String PLACEMENT_PARAGRAPH = "placement_paragraph";
	// Borgman added String constants for INVOICE_INTERVALL column values
	private final static String PK = "school_class_member.invoice_interval.";
	private final static String KEY_INVOICE_INTERVAL_VALUE_TERM = PK + "month";
	private final static String KEY_INVOICE_INTERVAL_VALUE_MONTH = PK + "term";
	private final static String KEY_INVOICE_INTERVAL_VALUE_YEAR = PK + "year";

	
	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(MEMBER, "classmember", true, true, Integer.class, MANY_TO_ONE, com.idega.core.user.data.User.class);
		this.addAttribute(SCHOOLCLASS, "class", true, true, Integer.class, MANY_TO_ONE, SchoolClass.class);
		this.addAttribute(NOTES, "notes", true, true, String.class, 255);
		this.addAttribute(REGISTER_DATE, "registerdate", true, true, Timestamp.class);
		this.addAttribute(REMOVED_DATE, "removeddate", true, true, Timestamp.class);
		this.addAttribute(REGISTRATOR, "registrator", true, true, Integer.class, MANY_TO_ONE, com.idega.core.user.data.User.class);
		this.addAttribute(NEEDS_SPECIAL_ATTENTION, "Needs special attention", true, true, Boolean.class);
		this.addAttribute(SPECIALLY_PLACED, "Specially placed", true, true, Boolean.class);
		this.addAttribute(LANGUAGE, "Language", true, true, String.class);
		this.addAttribute(COMPENSATION_BY_INVOICE, "Compensation by invoice", true, true, Boolean.class);
		this.addAttribute(INVOICE_INTERVAL, "Invoice interval", true, true, String.class);
		this.addAttribute(LATEST_INVOICE_DATE, "Latest invoice date", true, true, Timestamp.class);
		/* Borgman 9 sept 2003 */
		this.addAttribute(PLACEMENT_PARAGRAPH, "placement paragraph", true, true, String.class, 100);
	}
	public String getEntityName() {
		return SCHOOLCLASSMEMBER;
	}
	public void setClassMemberId(int id) {
		this.setColumn(MEMBER, id);
	}
	public int getClassMemberId() {
		return this.getIntColumnValue(MEMBER);
	}
	public User getStudent() {
		return (User)getColumnValue(MEMBER);
	}
	public void setSchoolClassId(int id) {
		this.setColumn(SCHOOLCLASS, id);
	}
	public int getSchoolClassId() {
		return this.getIntColumnValue(SCHOOLCLASS);
	}
	public SchoolClass getSchoolClass() {
		return (SchoolClass)this.getColumnValue(SCHOOLCLASS);
	}
	public void setRegisterDate(Timestamp stamp) {
		this.setColumn(REGISTER_DATE, stamp);
	}
	public Timestamp getRegisterDate() {
		return (Timestamp)this.getColumnValue(REGISTER_DATE);
	}
	public void setRemovedDate(Timestamp stamp) {
		this.setColumn(REMOVED_DATE, stamp);
	}
	public Timestamp getRemovedDate() {
		return (Timestamp)this.getColumnValue(REMOVED_DATE);
	}
	public void setRegistratorId(int id) {
		this.setColumn(REGISTRATOR, id);
	}
	public int getRegistratorId() {
		return this.getIntColumnValue(REGISTRATOR);
	}
	public void setNotes(String notes) {
		this.setColumn(NOTES, notes);
	}
	public String getNotes() {
		return this.getStringColumnValue(NOTES);
	}
	

	public void setNeedsSpecialAttention(boolean needsAttention) {
		this.setColumn(NEEDS_SPECIAL_ATTENTION, needsAttention);
	}
	public boolean getNeedsSpecialAttention() {
		return getBooleanColumnValue(NEEDS_SPECIAL_ATTENTION, false);
	}

	public void setSpeciallyPlaced(boolean speciallyPlaced) {
		this.setColumn(SPECIALLY_PLACED, speciallyPlaced);
	}
	public boolean getSpeciallyPlaced() {
		return getBooleanColumnValue(SPECIALLY_PLACED, false);
	}

	public void setLanguage(String language) {
		this.setColumn(LANGUAGE, language);
	}
	public String getLanguage() {
		return this.getStringColumnValue(LANGUAGE);
	}

	public boolean getHasCompensationByInvoice() {
		return getBooleanColumnValue(COMPENSATION_BY_INVOICE,false);
	}
	
	public void setHasCompensationByInvoice(boolean hasCompensation) {
		setColumn(COMPENSATION_BY_INVOICE,hasCompensation);
	}
	
	public String getInvoiceInterval() {
		return getStringColumnValue(INVOICE_INTERVAL);
	}
	
	public void setInvoiceInterval(String interval) {
		setColumn(INVOICE_INTERVAL,interval);
	}
	
	public Timestamp getLatestInvoiceDate() {
		return (Timestamp)getColumnValue(LATEST_INVOICE_DATE);
	}
	
	public void setLatestInvoiceDate(Timestamp date) {
		setColumn(LATEST_INVOICE_DATE,date);
	}
	
	public String getPlacementParagraph() {
		return this.getStringColumnValue(PLACEMENT_PARAGRAPH);
	}

	public void setPlacementParagraph(String placementParagraph) {
		this.setColumn(PLACEMENT_PARAGRAPH, placementParagraph);
	}


	public Collection ejbFindBySchoolClass(SchoolClass schoolClass) throws FinderException, RemoteException {
		return ejbFindBySchoolClass(((Integer)schoolClass.getPrimaryKey()).intValue());
	}

	public Collection ejbFindBySchoolClass(int schoolClassID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhere();
		sql.append(this.SCHOOLCLASS).appendEqualSign().append(schoolClassID);
        sql.appendAndIsNull(REMOVED_DATE);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByStudent(User student) throws FinderException, RemoteException {
		return ejbFindByStudent(((Integer)student.getPrimaryKey()).intValue());
	}

	public Collection ejbFindByStudent(int studentID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhere().append(MEMBER).appendEqualSign().append(studentID);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByStudentAndTypes(int studentID, Collection schoolTypes) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelect().append(" m.* ").appendFrom().append(getEntityName()).append(" m, sch_school s, sch_school_sch_school_type st, sch_school_class c");
		sql.appendWhere().append("m." + MEMBER).appendEqualSign().append(studentID);
		sql.appendAndEquals("m." + SCHOOLCLASS, "c.sch_school_class_id");
		sql.appendAndEquals("c.school_id", "s.sch_school_id");
		sql.appendAndEquals("s.sch_school_id", "st.sch_school_id");
		sql.appendAnd().append("st.sch_school_type_id").appendIn().appendLeftParenthesis();
		sql.appendCommaDelimited(schoolTypes);
		sql.appendRightParenthesis();

		//System.out.println("SQL: "+sql.toString());
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException, RemoteException {
		return ejbFindByUserAndSchoolClass(((Integer)user.getPrimaryKey()).intValue(), ((Integer)schoolClass.getPrimaryKey()).intValue());
	}

	public Integer ejbFindByUserAndSchoolClass(int userID, int schoolClassID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhere().append(MEMBER).appendEqualSign().append(userID).appendAnd().append(SCHOOLCLASS).appendEqualSign().append(schoolClassID);

		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSeason(User user, SchoolSeason season) throws FinderException, RemoteException {
		return ejbFindByUserAndSeason(((Integer)user.getPrimaryKey()).intValue(), ((Integer)season.getPrimaryKey()).intValue());
	}

	public Integer ejbFindByUserAndSeason(int userID, int seasonID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		//System.err.println(sql.toString());
		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSchool(int userID, int schoolID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		//System.err.println(sql.toString());
		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindLatestByUserAndSchool(int userID, int schoolID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendOrderBy(REGISTER_DATE + " desc");
		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}
	
	public Integer ejbFindLatestByUser(User user) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl")
		
		.appendWhere()
		.append(" mb." + MEMBER)
		.appendEqualSign()
		.append(user.getPrimaryKey())
		
		.appendAnd()
		.append("(cl." + SchoolClassBMPBean.COLUMN_VALID)
		.appendEqualSign()
		.appendWithinSingleQuotes("Y")
		
		.appendOr()
		.append("cl." + SchoolClassBMPBean.COLUMN_VALID)
		.append(" is null)")
		
		.appendAnd()
		.append(" mb." + SCHOOLCLASS)
		.appendEqualSign()
		.append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		
		sql.appendOrderBy(REGISTER_DATE + " desc");
		
		
		return (Integer)this.idoFindOnePKBySQL(sql.toString());
	}

	public Collection ejbFindByStudentAndSchool(int userID, int schoolID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendOrderBy(REGISTER_DATE + " desc");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllByUserAndSeason(User user, SchoolSeason season) throws FinderException, RemoteException {
		return ejbFindAllByUserAndSeason(((Integer)user.getPrimaryKey()).intValue(), ((Integer)season.getPrimaryKey()).intValue());
	}

	public Collection ejbFindAllByUserAndSeason(int userID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return idoFindPKsBySQL(sql.toString());
	}
	
	public Collection ejbFindAllBySeasonAndInvoiceInterval(int seasonID, String invoiceInterval) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhere().append(" mb." + INVOICE_INTERVAL).appendEqualSign().appendWithinSingleQuotes(invoiceInterval);
		sql.appendAnd();
		sql.append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID);
		sql.appendAnd();
		sql.append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y");
		sql.appendOr();
		sql.append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");
		sql.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return idoFindPKsBySQL(sql.toString());		
	}

	public Integer ejbFindByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return (Integer)idoFindOnePKBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOLYEAR).appendEqualSign().append(yearID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolAndSeason(int schoolID, int seasonID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		//System.err.println(sql.toString());
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + this.MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolClassID != -1)
			sql.appendAndEquals("mb." + SCHOOLCLASS, schoolClassID);
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, Date date) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + this.MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolClassID != -1)
			sql.appendAndEquals("mb." + SCHOOLCLASS, schoolClassID);
		//sql.appendAnd().appendLeftParenthesis().append(REGISTER_DATE).appendLessThanOrEqualsSign().append(date)
		//.appendOr().append(REGISTER_DATE).append(" is null").appendRightParenthesis();
		sql.appendAnd().appendLeftParenthesis().append(REMOVED_DATE).appendGreaterThanOrEqualsSign().append(date).appendOr().append(REMOVED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, Date date, boolean showNotYetActive) throws FinderException, RemoteException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + this.MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolClassID != -1)
			sql.appendAndEquals("mb." + SCHOOLCLASS, schoolClassID);
		if (showNotYetActive) {
			sql.appendAnd().append(REGISTER_DATE).appendGreaterThanSign().append(date);
		}
		else {
			sql.appendAnd().append(REGISTER_DATE).appendLessThanOrEqualsSign().append(date);
		}
		sql.appendAnd().appendLeftParenthesis().append(REMOVED_DATE).appendGreaterThanOrEqualsSign().append(date).appendOr().append(REMOVED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllLastYearStudentsBySeasonAndMaximumAge(SchoolSeason season, int maxAge) throws FinderException {
		return ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(season,null,maxAge);
	}

	/**
	 * Finds all Students who are on the last year in their school. Does not take into account years with age higher than 14 by default.
	 * @param season
	 * @param year
	 * @return
	 * @throws FinderException
	 * @throws RemoteException
	 */
	public Collection ejbFindAllLastYearStudentsBySeasonAndYear(SchoolSeason season,SchoolYear year) throws FinderException {
		int maxAge=14;
		return  ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(season,year,maxAge);
	}

	/**
	 * Finds all Students who are on the last year in their school
	 * @param season
	 * @param year
	 * @param maxAge The age to be maximum taken into account for a last year.
	 * @return
	 * @throws FinderException
	 * @throws RemoteException
	 */
	public Collection ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(SchoolSeason season,SchoolYear year,int maxAge) throws FinderException {
		if(season==null){
			throw new FinderException("ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge: SchoolSeason not set");
		}
		final StringBuffer sql = new StringBuffer();
		int seasonId = ((Integer)season.getPrimaryKey()).intValue();
		int yearId=-1;
		if(year!=null){
			yearId = ((Integer)year.getPrimaryKey()).intValue();
		}		
		//int maxAge = 14;
		sql.append("select student.*");
		sql.append(" from sch_school_sch_school_year school,sch_school_year schoolyear, sch_school_class class, " + getTableName() + " student");
		sql.append(" where school.sch_school_year_id = schoolyear.sch_school_year_id");
		sql.append(" and class.sch_school_year_id = school.sch_school_year_id");
		sql.append(" and class.school_id = school.sch_school_id");
		sql.append(" and class.sch_school_season_id = " + seasonId);
		sql.append(" and class.sch_school_class_id = student." + SCHOOLCLASS);
		if(year!=null){
			sql.append(" and schoolyear.sch_school_year_id = " + yearId);
		}
		sql.append(" and exists");
		sql.append(" (");
		sql.append(" select s.sch_school_id, max (y.year_age)");
		sql.append(" from sch_school_sch_school_year s, sch_school_year y");
		sql.append(" where s.sch_school_year_id = y.sch_school_year_id");
		sql.append(" group by s.sch_school_id");
		sql.append(" having max (y.year_age) <=").append(maxAge);
		sql.append(" and schoolyear.year_age = max (y.year_age)");
		//sql.append (" and school.sch_school_year_id =s.sch_school_year_id");
		sql.append(" and school.sch_school_id = s.sch_school_id");
		sql.append(" )");
		return idoFindIDsBySQL(sql.toString());
	}
	
	public int ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDate(Group citizenGroup, Date date, Collection classes, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException{
		try {
			
			IDOEntityDefinition usrDef = IDOLookup.getEntityDefinitionForClass(User.class);
			String usrIdColumn = usrDef.getPrimaryKeyDefinition().getField().getSQLFieldName();
			IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);


			IDOEntityField dateOfBirth = usrDef.findFieldByUniqueName(User.FIELD_DATE_OF_BIRTH);

			//relationStatus could be as parameter to this method
			String[] relationStatus = new String[1];
			relationStatus[0] = GroupRelation.STATUS_ACTIVE;
		  	
			String[] tables = new String[2];
			String[] variables = new String[2];
			//table name
			tables[0] = usrDef.getSQLTableName();
			//	as variable
			variables[0] = "u";
			//table name
			tables[1] = grRelDef.getSQLTableName();
			//	as variable
			variables[1] = "gr_rel";

			IDOQuery query = this.idoQuery();
			
			query.appendSelectCount();
			//from
			query.appendFrom(tables,variables);
			//where
			query.appendWhere();
			query.append(variables[1]);
			query.append(".");
			query.append(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_GROUP).getSQLFieldName());
			query.appendEqualSign();
			query.append(citizenGroup.getPrimaryKey());
			//and
			query.appendAnd();
			query.append(variables[1]);
			query.append(".");
			query.append(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_RELATED_GROUP).getSQLFieldName());
			query.appendEqualSign();
			query.append(variables[0]);
			query.append(".");
			query.append(usrIdColumn);
			
			
			//and if relationstatus
			if(relationStatus!= null){
				//and
				query.appendAnd();
				query.append(variables[1]);
				query.append(".");
				query.append(grRelDef.findFieldByUniqueName(GroupRelation.FIELD_STATUS).getSQLFieldName());
				query.appendInArrayWithSingleQuotes(relationStatus);		
			}
			
			query.appendAnd();
			query.append(variables[0]);
			query.append(".");
			query.append(dateOfBirth);
			query.appendGreaterThanOrEqualsSign();
			query.append(firstDateOfBirth);
			
			query.appendAnd();
			query.append(variables[0]);
			query.append(".");
			query.append(dateOfBirth);
			query.appendLessThanOrEqualsSign();
			query.append(lastDateOfBirth);
			
			query.appendAnd();
			query.append(variables[0]);
			query.append(".");
			query.append(usrIdColumn);
			
			IDOQuery subQuery = this.idoQuery();
			
			subQuery.appendSelect();
			subQuery.append(" usr.");
			subQuery.append(usrIdColumn);
			subQuery.appendFrom();
			subQuery.append(this.getEntityName());
			subQuery.append(" cm, ");
			subQuery.append(usrDef.getSQLTableName());
			subQuery.append(" usr ");
			subQuery.appendWhere();
			subQuery.append("usr.");
			subQuery.append(usrIdColumn);
			subQuery.appendEqualSign();
			subQuery.append("cm.");
			subQuery.append(MEMBER);
			
			subQuery.appendAnd();
			subQuery.append("cm.");
			subQuery.append(REGISTER_DATE);
			subQuery.appendLessThanOrEqualsSign();
			subQuery.append(date);
			
			subQuery.appendAnd();
			subQuery.append("cm.");
			subQuery.append(this.SCHOOLCLASS);
			subQuery.appendInCollection(classes);
			
			query.appendNotIn(subQuery);
			
			System.out.println("SQL -> "+this.getClass()+":"+query);
					
			return idoGetNumberOfRecords(query);
		} catch (IDOCompositePrimaryKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return -1;
	}
	 
	 /**
	  * This method returns a Collection with the domain of allowed values (types) 
	  * for the invoice_int column 
	  * @return 
	  * @author Borgman
	  */
	 public Collection ejbHomeGetInvoiceIntervalTypes() {
	 	String[] typeArr = { 	KEY_INVOICE_INTERVAL_VALUE_MONTH,
										KEY_INVOICE_INTERVAL_VALUE_TERM, 
										KEY_INVOICE_INTERVAL_VALUE_YEAR	};
										
		Vector typeVec = new Vector();
		for (int i = 0; i < typeArr.length; i++) {
			typeVec.addElement(typeArr[i]);
		}
		return typeVec;	 	
	 }

}
