package com.idega.block.school.data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Vector;

import javax.ejb.FinderException;

import com.idega.core.location.data.Address;
import com.idega.core.location.data.Commune;
import com.idega.data.GenericEntity;
import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOCompositePrimaryKeyException;
import com.idega.data.IDOEntityDefinition;
import com.idega.data.IDOEntityField;
import com.idega.data.IDOException;
import com.idega.data.IDOLookup;
import com.idega.data.IDOLookupException;
import com.idega.data.IDOQuery;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.Group;
import com.idega.user.data.GroupRelation;
import com.idega.user.data.User;
import com.idega.user.data.UserBMPBean;

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
 *         Last modified: $Date: 2004/07/01 07:35:36 $ by $Author: laddi $
 * @version $Revision: 1.118 $
 */

public class SchoolClassMemberBMPBean extends GenericEntity implements SchoolClassMember {

	public final static String SCHOOLCLASSMEMBERID = "sch_class_member_id";
	public final static String SCHOOLCLASSMEMBER = "sch_class_member";
	public final static String MEMBER = "ic_user_id";
	public final static String NOTES = "notes";
	public final static String SCHOOLCLASS = "sch_school_class_id";
	public final static String SCHOOL_YEAR = "sch_school_year_id";
	public final static String SCHOOL_TYPE = "sch_school_type_id";
	public final static String REGISTER_DATE = "register_date";
	public final static String REGISTRATION_CREATED_DATE = "registration_created_date";
	public final static String REMOVED_DATE = "removed_date";
	public final static String REGISTRATOR = "registrator";
	public final static String NEEDS_SPECIAL_ATTENTION = "NEEDS_SPECIAL_ATTENTION";
	public final static String SPECIALLY_PLACED = "SPECIALLY_PLACED";
	public final static String LANGUAGE = "LANGUAGE";
	public final static String INVOICE_INTERVAL = "invoice_int";
	public final static String LATEST_INVOICE_DATE = "latest_invoice_date";
	public final static String PLACEMENT_PARAGRAPH = "placement_paragraph";
	public final static String COMPENSATION_BY_AGREEMENT = "comp_by_agreement";
	public static final String STUDY_PATH = "study_path";
	private final static String PK = "school_class_member.invoice_interval.";
	private final static String KEY_INVOICE_INTERVAL_VALUE_TERM = PK + "term";
	private final static String KEY_INVOICE_INTERVAL_VALUE_MONTH = PK + "month";
	private final static String KEY_INVOICE_INTERVAL_VALUE_QUARTER = PK + "quarter";
	private final static String KEY_INVOICE_INTERVAL_VALUE_YEAR = PK + "year";

	public void initializeAttributes() {
		this.addAttribute(getIDColumnName());
		this.addAttribute(MEMBER, "classmember", true, true, Integer.class, MANY_TO_ONE, com.idega.core.user.data.User.class);
		this.addAttribute(SCHOOLCLASS, "class", true, true, Integer.class, MANY_TO_ONE, SchoolClass.class);
		this.addAttribute(SCHOOL_TYPE, "school type", true, true, Integer.class, MANY_TO_ONE, SchoolType.class);
		this.addAttribute(SCHOOL_YEAR, "school year", true, true, Integer.class, MANY_TO_ONE, SchoolYear.class);
		this.addAttribute(NOTES, "notes", true, true, String.class, 255);
		this.addAttribute(REGISTER_DATE, "registerdate", true, true, Timestamp.class);
		this.addAttribute(REGISTRATION_CREATED_DATE, "inregisterdate", true, true, Timestamp.class);
		this.addAttribute(REMOVED_DATE, "removeddate", true, true, Timestamp.class);
		this.addAttribute(REGISTRATOR, "registrator", true, true, Integer.class, MANY_TO_ONE, com.idega.core.user.data.User.class);
		this.addAttribute(NEEDS_SPECIAL_ATTENTION, "Needs special attention", true, true, Boolean.class);
		this.addAttribute(SPECIALLY_PLACED, "Specially placed", true, true, Boolean.class);
		this.addAttribute(LANGUAGE, "Language", true, true, String.class);
		this.addAttribute(INVOICE_INTERVAL, "Invoice interval", true, true, String.class);
		this.addAttribute(LATEST_INVOICE_DATE, "Latest invoice date", true, true, Timestamp.class);
		this.addAttribute(PLACEMENT_PARAGRAPH, "placement paragraph", true, true, String.class, 100);
		this.addAttribute(COMPENSATION_BY_AGREEMENT, "Compensation by agreement", true, true, Boolean.class);
		this.addAttribute(STUDY_PATH, "study_path", true, true, Integer.class, MANY_TO_ONE, SchoolStudyPath.class);
		this.addManyToManyRelationShip(SchoolClass.class, "sch_sub_group_placements");
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
		return (User) getColumnValue(MEMBER);
	}

	public void setStudent(User student) {
		setColumn(MEMBER, student);
	}

	public void setSchoolClassId(int id) {
		this.setColumn(SCHOOLCLASS, id);
	}

	public void setSchoolClass(SchoolClass group) {
		this.setColumn(SCHOOLCLASS, group);
	}

	public int getSchoolClassId() {
		return this.getIntColumnValue(SCHOOLCLASS);
	}

	public SchoolClass getSchoolClass() {
		return (SchoolClass) this.getColumnValue(SCHOOLCLASS);
	}

	public void setSchoolTypeId(int id) {
		setColumn(SCHOOL_TYPE, id);
	}

	public void setSchoolType(SchoolType type) {
		setColumn(SCHOOL_TYPE, type);
	}

	public int getSchoolTypeId() {
		return getIntColumnValue(SCHOOL_TYPE);
	}

	public SchoolType getSchoolType() {
		return (SchoolType) getColumnValue(SCHOOL_TYPE);
	}

	public void setSchoolYear(int id) {
		setColumn(SCHOOL_YEAR, id);
	}

	public void setSchoolYear(SchoolYear year) {
		setColumn(SCHOOL_YEAR, year);
	}

	public int getSchoolYearId() {
		return getIntColumnValue(SCHOOL_YEAR);
	}

	public SchoolYear getSchoolYear() {
		return (SchoolYear) getColumnValue(SCHOOL_YEAR);
	}

	public void setRegisterDate(Timestamp stamp) {
		this.setColumn(REGISTER_DATE, stamp);
	}

	public Timestamp getRegisterDate() {
		return (Timestamp) this.getColumnValue(REGISTER_DATE);
	}

	public void setRegistrationCreatedDate(Timestamp stamp) {
		this.setColumn(REGISTRATION_CREATED_DATE, stamp);
	}

	public Timestamp getRegistrationCreatedDate() {
		return (Timestamp) this.getColumnValue(REGISTRATION_CREATED_DATE);
	}

	public void setRemovedDate(Timestamp stamp) {
		this.setColumn(REMOVED_DATE, stamp);
	}

	public Timestamp getRemovedDate() {
		return (Timestamp) this.getColumnValue(REMOVED_DATE);
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

	public String getInvoiceInterval() {
		return getStringColumnValue(INVOICE_INTERVAL);
	}

	public void setInvoiceInterval(String interval) {
		setColumn(INVOICE_INTERVAL, interval);
	}

	public Timestamp getLatestInvoiceDate() {
		return (Timestamp) getColumnValue(LATEST_INVOICE_DATE);
	}

	public void setLatestInvoiceDate(Timestamp date) {
		setColumn(LATEST_INVOICE_DATE, date);
	}

	public String getPlacementParagraph() {
		return this.getStringColumnValue(PLACEMENT_PARAGRAPH);
	}

	public void setPlacementParagraph(String placementParagraph) {
		this.setColumn(PLACEMENT_PARAGRAPH, placementParagraph);
	}

	public boolean getHasCompensationByAgreement() {
		return getBooleanColumnValue(COMPENSATION_BY_AGREEMENT, false);
	}

	public void setHasCompensationByAgreement(boolean hasCompensation) {
		setColumn(COMPENSATION_BY_AGREEMENT, hasCompensation);
	}

	public void setStudyPathId(int id) {
		this.setColumn(STUDY_PATH, id);
	}

	public void setStudyPath(SchoolStudyPath studyPath) {
		this.setColumn(STUDY_PATH, studyPath);
	}

	public void setStudyPathToNull() {
		removeFromColumn(STUDY_PATH);
	}

	public int getStudyPathId() {
		return this.getIntColumnValue(STUDY_PATH);
	}

	public Collection ejbFindAllOrderedByRegisterDate(User user) throws FinderException {
		int userID = ((Integer) user.getPrimaryKey()).intValue();
		return ejbFindAllOrderedByRegisterDate(userID);
	}

	public Collection ejbFindAllOrderedByRegisterDate(int userID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhere()

		.append(MEMBER).appendEqualSign().append(userID).appendAnd()

		.appendLeftParenthesis().append(REMOVED_DATE).appendGreaterThanOrEqualsSign().append(REGISTER_DATE).appendOr().append(REMOVED_DATE).appendIsNull().appendRightParenthesis()

		.append(" ORDER BY " + REGISTER_DATE + " DESC");

		String sqlStr = sql.toString();

		return idoFindPKsBySQL(sqlStr);
	}

	public Collection ejbFindBySchoolClass(SchoolClass schoolClass) throws FinderException {
		return ejbFindBySchoolClass(((Integer) schoolClass.getPrimaryKey()).intValue());
	}

	public Collection ejbFindBySchoolClass(int schoolClassID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhereEquals(SCHOOLCLASS, schoolClassID);
		sql.appendAndIsNull(REMOVED_DATE);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolClasses(Collection schoolClasses) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelect().append(" m.* ").appendFrom().append(getEntityName()).append(" m, ic_user u");
		sql.appendWhere().append("m.").append(MEMBER).appendEqualSign().append("u.ic_user_id");
		sql.appendAnd().append("m.").append(SCHOOLCLASS).appendInCollection(schoolClasses);
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolClassAndYear(int schoolClassID, int schoolYearID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhereEquals(SCHOOLCLASS, schoolClassID);
		if (schoolYearID != -1)
			sql.appendAndEquals(SCHOOL_YEAR, schoolYearID);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolClassAndYearAndStudyPath(SchoolClass group, SchoolYear schoolYear, SchoolStudyPath studyPath) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhereEquals(SCHOOLCLASS, group);
		if (schoolYear != null) {
			sql.appendAndEquals(SCHOOL_YEAR, schoolYear);
		}
		if (studyPath != null) {
			sql.appendAndEquals(STUDY_PATH, studyPath);
		}

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByStudent(User student) throws FinderException {
		return ejbFindByStudent(((Integer) student.getPrimaryKey()).intValue());
	}

	public Collection ejbFindByStudent(int studentID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getEntityName()).appendWhere().append(MEMBER).appendEqualSign().append(studentID);

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByStudentAndTypes(int studentID, Collection schoolTypes) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this);
		sql.appendWhereEquals(MEMBER, studentID);
		sql.appendAnd().append(SCHOOL_TYPE).appendIn().appendLeftParenthesis();
		sql.appendCommaDelimited(schoolTypes);
		sql.appendRightParenthesis();

		return super.idoFindPKsBySQL(sql.toString());
	}

	public java.util.Collection ejbFindAllBySchoolAndUsersWithSchoolYearAndNotRemoved(int schoolId, java.util.Collection users) throws javax.ejb.FinderException {
		IDOQuery sql = idoQuery();

		final String M_ = "m."; // sql alias for school class member
		final String C_ = "c."; // sql alias for school class

		sql.appendSelect().append("m.*");
		sql.appendFrom().append(getEntityName() + " m");
		if (0 < schoolId) {
			sql.append(", " + SchoolClassBMPBean.SCHOOLCLASS + " c");
		}
		sql.appendWhereIsNull(M_ + REMOVED_DATE);
		sql.appendAndIsNotNull(M_ + SCHOOL_YEAR);
		if (0 < schoolId) {
			sql.appendAndEquals(C_ + SchoolClassBMPBean.SCHOOLCLASS + "_id", M_ + SCHOOLCLASS);
			sql.appendAndEquals(C_ + SchoolClassBMPBean.SCHOOL, schoolId);
		}
		if (null != users && 0 < users.size()) {
			sql.appendAnd().append(M_ + MEMBER);
			sql.appendIn(idoQuery().appendCommaDelimited(users));
		}
		sql.appendAnd().append(M_ + REGISTER_DATE);
		sql.appendLessThanSign();
		sql.append(new Date(System.currentTimeMillis()));
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByStudentAndSchoolAndTypes(int studentID, int schoolID, Collection schoolTypes) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelect().append(" m.* ").appendFrom().append(getEntityName()).append(" m, sch_school_class c");
		sql.appendWhere().append("m." + MEMBER).appendEqualSign().append(studentID);
		sql.appendAndEquals("m." + SCHOOLCLASS, "c.sch_school_class_id");
		sql.appendAndEquals("c.school_id", schoolID);
		sql.appendAnd().append("m.").append(SCHOOL_TYPE).appendIn().appendLeftParenthesis();
		sql.appendCommaDelimited(schoolTypes);
		sql.appendRightParenthesis();
		sql.appendOrderBy(REGISTER_DATE + " desc");

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSchoolClass(User user, SchoolClass schoolClass) throws FinderException {
		return ejbFindByUserAndSchoolClass(((Integer) user.getPrimaryKey()).intValue(), ((Integer) schoolClass.getPrimaryKey()).intValue());
	}

	public Integer ejbFindByUserAndSchoolClass(int userID, int schoolClassID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this).appendWhere().append(MEMBER).appendEqualSign().append(userID).appendAnd().append(SCHOOLCLASS).appendEqualSign().append(schoolClassID);

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		return ejbFindByUserAndSeason(((Integer) user.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue());
	}

	public Integer ejbFindByUserAndSeason(int userID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSchool(int userID, int schoolID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public int ejbHomeGetNumberOfPlacingsAtSchool(int userID, int schoolID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");

		return this.idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetNumberOfPlacings(int userID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(MEMBER, userID);

		return this.idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetNumberOfPlacings(int userID, int schoolClassID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).appendWhereEquals(MEMBER, userID).appendAndEquals(SCHOOLCLASS, schoolClassID);

		return this.idoGetNumberOfRecords(sql);
	}

	public int ejbHomeGetNumberOfSubGroupPlacings(int userID, int schoolClassID) throws IDOException {
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this).append("c, sch_sub_group_placements p");
		sql.appendWhereEquals("c."+MEMBER, userID);
		sql.appendAndEquals("p."+SCHOOLCLASS, schoolClassID);
		sql.appendAndEquals("c.sch_class_member_id", "p.sch_class_member_id");

		return this.idoGetNumberOfRecords(sql);
	}

	public Collection ejbFindAllSubGroupPlacements(int userID, int schoolID, int seasonID) throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).append(" mb, ").append(SchoolClassBMPBean.SCHOOLCLASS).append(" cl");
		query.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID);
		query.appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID);
		query.appendAnd().appendLeftParenthesis().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendRightParenthesis();
		query.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		query.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID);
		query.appendAnd().append("cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).appendEqualSign().append(true);

		return this.idoFindPKsByQuery(query);
	}

	public Integer ejbFindLatestByUserAndSchool(int userID, int schoolID) throws FinderException {
		return ejbFindLatestByUserAndSchool(userID, schoolID, null);
	}

	public Integer ejbFindLatestByUserAndSchool(int userID, int schoolID, Collection schoolTypes) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhereEquals(" mb." + MEMBER, userID);
		sql.appendAndEquals("cl." + SchoolClassBMPBean.SCHOOL, schoolID);
		sql.appendAnd().appendLeftParenthesis().append("cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().append(true).appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).appendIsNull().appendRightParenthesis();
		sql.appendAndEquals(" mb." + SCHOOLCLASS, "cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolTypes != null) {
			sql.appendAnd().append("mb." + this.SCHOOL_TYPE).appendInCollection(schoolTypes);
		}

		sql.appendOrderBy(REGISTER_DATE + " desc");
		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindLatestByUser(User user) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		/*
		 * .appendAnd() .appendLeftParenthesis() .append("mb." + REMOVED_DATE + " is
		 * null") .appendOr() .append("mb." + REMOVED_DATE)
		 * .appendGreaterThanOrEqualsSign() .append("mb." + REGISTER_DATE)
		 * .appendRightParenthesis();
		 */

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindLatestByUserAndSchoolType(User user, SchoolType type) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName())

		.appendWhere().append(MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append(SCHOOL_TYPE).appendEqualSign().append(type.getPrimaryKey())

		/*
		 * .appendAnd() .appendLeftParenthesis() .append("mb." + REMOVED_DATE + " is
		 * null") .appendOr() .append("mb." + REMOVED_DATE)
		 * .appendGreaterThanOrEqualsSign() .append("mb." + REGISTER_DATE)
		 * .appendRightParenthesis();
		 */

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindLatestByUserAndSchCategory(User user, SchoolCategory cat) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")

		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")

		.appendAnd().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + cat.getPrimaryKey() + "'")

		/*
		 * .appendAnd() .appendLeftParenthesis() .append("mb." + REMOVED_DATE + " is
		 * null") .appendOr() .append("mb." + REMOVED_DATE)
		 * .appendGreaterThanOrEqualsSign() .append("mb." + REGISTER_DATE)
		 * .appendRightParenthesis()
		 */

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Integer ejbFindLatestByUserAndSchCategoryAndSeason(User user, SchoolCategory cat, SchoolSeason season) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")

		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(season.getPrimaryKey())

		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")

		.appendAnd().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + cat.getPrimaryKey() + "'")

		/*
		 * .appendAnd() .appendLeftParenthesis() .append("mb." + REMOVED_DATE + " is
		 * null") .appendOr() .append("mb." + REMOVED_DATE)
		 * .appendGreaterThanOrEqualsSign() .append("mb." + REGISTER_DATE)
		 * .appendRightParenthesis()
		 */

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}
	
	public Collection ejbFindActiveByCategorySeasonAndSchools(SchoolCategory cat, SchoolSeason season, String[] schoolIds, boolean notInSchools) throws FinderException {
		
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp")
		.appendWhere().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")
		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")
		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")
		.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(season.getPrimaryKey())
		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")
		.appendAnd().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + cat.getPrimaryKey() + "'")
		.appendAnd().appendLeftParenthesis().append("mb." + REMOVED_DATE + " is null").appendOr() 
		.append("mb." + REMOVED_DATE).appendGreaterThanOrEqualsSign().append(new Date(System.currentTimeMillis())).appendRightParenthesis();
		if (schoolIds != null) {
			if (notInSchools) {
				sql.appendAnd().append("cl.school_id").appendNotInArray(schoolIds);
			} else {
				sql.appendAnd().append("cl.school_id").appendInArray(schoolIds);			
			}
		}
		return this.idoFindPKsByQuery(sql);
	}

	public Collection ejbFindAllByUserAndSchoolCategory(User user, SchoolCategory cat) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")

		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")

		.appendAnd().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + cat.getPrimaryKey() + "'")

		.appendOrderBy(REGISTER_DATE + " desc");

		return idoFindPKsBySQL(sql.toString());
	}

	/**
	 * Gets latest placement for user in school_categories elementary and high
	 * school. Checks that removed_date is after register_date, otherwise the
	 * placement is invalid.
	 * 
	 * @param user
	 * @return @throws
	 *         FinderException
	 */
	public Integer ejbFindLatestFromElemAndHighSchoolByUser(User user) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")

		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		/*
		 * .appendAnd() .appendLeftParenthesis() .append("mb." + REMOVED_DATE + " is
		 * null") .appendOr() .append("mb." + REMOVED_DATE)
		 * .appendGreaterThanOrEqualsSign() .append("mb." + REGISTER_DATE)
		 * .appendRightParenthesis()
		 */

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")

		.appendAnd().append("(tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + SchoolCategoryBMPBean.CATEGORY_ELEMENTARY_SCHOOL + "'").appendOr().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + SchoolCategoryBMPBean.CATEGORY_HIGH_SCHOOL + "')")

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	/**
	 * Gets latest placement for user and season in school_categories elementary
	 * and high school. Checks that removed_date is after register_date, otherwise
	 * the placement is invalid.
	 * 
	 * @param user
	 * @return @throws
	 *         FinderException
	 */
	public Integer ejbFindLatestFromElemAndHighSchoolByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		IDOQuery sql = idoQuery();

		sql.appendSelectAllFrom(this.getTableName() + " mb" + ", " + SchoolClassBMPBean.SCHOOLCLASS + " cl, " + SchoolTypeBMPBean.SCHOOLTYPE + " tp, " + SchoolSeasonBMPBean.SCHOOLSEASON + " ss ")

		.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user.getPrimaryKey())

		.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y")

		.appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)")

		.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id")

		.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append("ss." + SchoolSeasonBMPBean.SCHOOLSEASON + "_id")

		.appendAnd().append("ss." + SchoolSeasonBMPBean.SCHOOLSEASON + "_id").appendEqualSign().append(season)

		.appendAnd().append("mb." + SCHOOL_TYPE).appendEqualSign().append("tp." + SchoolTypeBMPBean.SCHOOLTYPE + "_id")

		.appendAnd().append("(tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + SchoolCategoryBMPBean.CATEGORY_ELEMENTARY_SCHOOL + "'").appendOr().append("tp." + SchoolTypeBMPBean.SCHOOLCATEGORY).appendEqualSign().append("'" + SchoolCategoryBMPBean.CATEGORY_HIGH_SCHOOL + "')")

		.appendOrderBy(REGISTER_DATE + " desc");

		return (Integer) this.idoFindOnePKBySQL(sql.toString());
	}

	public Collection ejbFindByStudentAndSchool(int userID, int schoolID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendOrderBy(REGISTER_DATE + " desc");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllByUserAndSeason(User user, SchoolSeason season) throws FinderException {
		return ejbFindAllByUserAndSeason(((Integer) user.getPrimaryKey()).intValue(), ((Integer) season.getPrimaryKey()).intValue());
	}

	public Collection ejbFindAllBySchoolStudyPath(final SchoolStudyPath studyPath) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(getTableName());
		sql.appendWhere();
		sql.append(STUDY_PATH);
		sql.appendEqualSign();
		sql.append(studyPath.getPrimaryKey().toString());
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllByUserAndSeason(int userID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindAllCurrentInvoiceCompensationBySchoolTypeAndSchools(final String operationalField, final Collection schools) throws FinderException {
		final IDOQuery sql = idoQuery();
		final String M_ = "m."; // sql alias for school Class member
		final String T_ = "t."; // sql alias for school type
		final String U_ = "u."; // sql alias for user
		final String C_ = "c."; // sql alias for school class

		// SELECT m.sch_class_member_id FROM sch_class_member m, sch_school_type t,
		// IC_USER u, SCH_SCHOOL_CLASS c

		sql.appendSelect().append(M_ + SCHOOLCLASSMEMBERID);
		sql.appendFrom(new String[]{getTableName(), SchoolTypeBMPBean.SCHOOLTYPE, UserBMPBean.TABLE_NAME, SchoolClassBMPBean.SCHOOLCLASS}, new String[]{"m", "t", "u", "c"});

		// WHERE m.ic_user_id=u.IC_USER_ID
		// AND m.sch_school_type_id=t.sch_school_type_id
		// AND t.school_category='...'
		// AND m.sch_school_class_id=c.SCH_SCHOOL_CLASS_id

		sql.appendWhereEquals(M_ + MEMBER, U_ + User.FIELD_USER_ID);
		sql.appendAndEquals(M_ + SCHOOL_TYPE, T_ + SchoolTypeBMPBean.SCHOOLTYPE + "_id");
		sql.appendAndEqualsQuoted(T_ + SchoolTypeBMPBean.SCHOOLCATEGORY, operationalField);
		sql.appendAndEquals(M_ + SCHOOLCLASS, C_ + SchoolClassBMPBean.SCHOOLCLASS + "_id");

		// AND ((c.sch_school_season_id IS NULL AND m.removed_date IS NULL )
		// OR c.sch_school_season_id in
		//  (SELECT sch_school_season_id FROM sch_school_season
		//   WHERE season_start in
		//    (SELECT max (season_start) FROM
		//    (SELECT * FROM sch_school_season WHERE season_start< '<today>' ))))

		sql.appendAnd().appendLeftParenthesis().appendLeftParenthesis();
		sql.append(C_ + SchoolClassBMPBean.SEASON).appendIsNull();
		sql.appendAndIsNull(M_ + REMOVED_DATE);
		sql.appendRightParenthesis().appendOr();
		sql.append(C_ + SchoolClassBMPBean.SEASON).append(" in ");
		sql.appendLeftParenthesis();
		sql.appendSelect().append(SchoolSeasonBMPBean.SCHOOLSEASON + "_id");
		sql.appendFrom().append(SchoolSeasonBMPBean.SCHOOLSEASON);
		sql.appendWhere(SchoolSeasonBMPBean.START + " in ");
		sql.appendLeftParenthesis();
		sql.appendSelect().append(" max ").appendLeftParenthesis();
		sql.append(SchoolSeasonBMPBean.START).appendRightParenthesis();
		sql.appendFrom().appendLeftParenthesis();
		sql.appendSelectAllFrom(SchoolSeasonBMPBean.SCHOOLSEASON);
		sql.appendWhere(SchoolSeasonBMPBean.START).appendLessThanSign();
		sql.append(new Date(System.currentTimeMillis()));
		sql.appendRightParenthesis().appendRightParenthesis();
		sql.appendRightParenthesis().appendRightParenthesis();

		// AND (m.invoice_int in (...)
		// OR m.comp_by_agreement = true
		// OR c.school_id in (...))

		sql.appendAnd().appendLeftParenthesis();
		sql.append(M_ + INVOICE_INTERVAL).append(" in ").appendLeftParenthesis();
		sql.appendCommaDelimitedWithinSingleQuotes(ejbHomeGetInvoiceIntervalTypes());
		sql.appendRightParenthesis();
		sql.appendOrEquals(M_ + COMPENSATION_BY_AGREEMENT, true);
		sql.appendOr().append(C_ + SchoolClassBMPBean.SCHOOL).append(" in ");
		sql.appendLeftParenthesis().appendCommaDelimited(schools);
		sql.appendRightParenthesis().appendRightParenthesis();

		// ORDER BY u.PERSONAL_ID

		sql.appendOrderBy(U_ + User.FIELD_PERSONAL_ID);

		return idoFindPKsBySQL(sql.toString());
	}

	public Integer ejbFindByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID) throws FinderException {
		return ejbFindByUserAndSchoolAndSeason(userID, schoolID, seasonID, null);
	}

	public Integer ejbFindByUserAndSchoolAndSeason(int userID, int schoolID, int seasonID, Collection schoolTypes) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID);
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).appendEqualSign().appendWithinSingleQuotes("N").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).append(" is null)");
		sql.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolTypes != null) {
			sql.appendAnd().append("mb." + this.SCHOOL_TYPE).appendInCollection(schoolTypes);
		}
		return (Integer) idoFindOnePKBySQL(sql.toString());
	}
	public Integer ejbFindByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws FinderException{
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID);
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).appendEqualSign().appendWithinSingleQuotes("N").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).append(" is null)");
		sql.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendAnd().append(" mb." + STUDY_PATH).appendEqualSign().append(studyPathID);

		return (Integer) idoFindOnePKBySQL(sql.toString());
	}
	
	public int ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath(int userID, int schoolID, int seasonID, int studyPathID) throws IDOException{
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(userID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID);
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).appendEqualSign().appendWithinSingleQuotes("N").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).append(" is null)");
		sql.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (studyPathID != -1) {
			sql.appendAnd().append(" mb." + STUDY_PATH).appendEqualSign().append(studyPathID);
		}

		return idoGetNumberOfRecords(sql.toString());
	}
	
	public int ejbHomeCountByUserAndSchoolAndSeasonAndStudyPath(User user, School school, SchoolSeason season, SchoolStudyPath studyPath) throws IDOException{
		IDOQuery sql = idoQuery();
		sql.appendSelectCountFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl");
		sql.appendWhere().append(" mb." + MEMBER).appendEqualSign().append(user);
		sql.appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(season);
		if (school != null) {
			sql.appendAnd().append("cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(school);
		}
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)");
		sql.appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).appendEqualSign().appendWithinSingleQuotes("N").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_SUB_GROUP).append(" is null)");
		sql.appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (studyPath != null) {
			sql.appendAnd().append(" mb." + STUDY_PATH).appendEqualSign().append(studyPath);
		}

		return idoGetNumberOfRecords(sql.toString());
	}
	
	

	public Collection ejbFindBySchoolAndSeasonAndYear(int schoolID, int seasonID, int yearID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("mb." + SCHOOL_YEAR).appendEqualSign().append(yearID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchoolAndSeason(int schoolID, int seasonID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAnd().append("cl." + SchoolClassBMPBean.SEASON).appendEqualSign().append(seasonID).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");

		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		if (schoolClassID != -1)
			sql.appendAndEquals("mb." + SCHOOLCLASS, schoolClassID);
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, Date date) throws FinderException {
		return ejbFindBySchool(schoolID, schoolClassID, null, date);
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, sch_school_type t, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendAndEquals("mb." + SCHOOL_TYPE, "t.sch_school_type_id");
		if (schoolCategory != null)
			sql.appendAndEqualsQuoted("t.school_category", schoolCategory);
		if (schoolClassID != -1)
			sql.appendAndEquals("mb." + SCHOOLCLASS, schoolClassID);
		//sql.appendAnd().appendLeftParenthesis().append(REGISTER_DATE).appendLessThanOrEqualsSign().append(date)
		//.appendOr().append(REGISTER_DATE).append(" is
		// null").appendRightParenthesis();
		sql.appendAnd().appendLeftParenthesis().append(REMOVED_DATE).appendGreaterThanOrEqualsSign().append(date).appendOr().append(REMOVED_DATE).append(" is null").appendRightParenthesis();
		sql.appendOrderBy("u.last_name, u.first_name, u.middle_name");
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, Date date, boolean showNotYetActive) throws FinderException {
		return ejbFindBySchool(schoolID, schoolClassID, null, date, showNotYetActive);
	}

	public Collection ejbFindBySchool(int schoolID, int schoolClassID, String schoolCategory, Date date, boolean showNotYetActive) throws FinderException {
		IDOQuery sql = idoQuery();
		sql.appendSelectAllFrom(this.getTableName() + " mb" + "," + SchoolClassBMPBean.SCHOOLCLASS + " cl, sch_school_type t, ic_user u").appendWhere().append(" cl." + SchoolClassBMPBean.SCHOOL).appendEqualSign().append(schoolID).appendAndEquals("u.ic_user_id", "mb." + MEMBER).appendAnd().append("(cl." + SchoolClassBMPBean.COLUMN_VALID).appendEqualSign().appendWithinSingleQuotes("Y").appendOr().append("cl." + SchoolClassBMPBean.COLUMN_VALID).append(" is null)").appendAnd().append(" mb." + SCHOOLCLASS).appendEqualSign().append("cl." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendAndEquals("mb." + SCHOOL_TYPE, "t.sch_school_type_id");
		if (schoolCategory != null)
			sql.appendAndEqualsQuoted("t.school_category", schoolCategory);
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
		return ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(season, null, maxAge);
	}

	/**
	 * Finds all Students who are on the last year in their school. Does not take
	 * into account years with age higher than 14 by default.
	 * 
	 * @param season
	 * @param year
	 * @return @throws
	 *         FinderException
	 * @throws RemoteException
	 */
	public Collection ejbFindAllLastYearStudentsBySeasonAndYear(SchoolSeason season, SchoolYear year) throws FinderException {
		int maxAge = 14;
		return ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(season, year, maxAge);
	}

	/**
	 * Finds all Students who are on the last year in their school
	 * 
	 * @param season
	 * @param year
	 * @param maxAge
	 *          The age to be maximum taken into account for a last year.
	 * @return @throws
	 *         FinderException
	 * @throws RemoteException
	 */
	public Collection ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge(SchoolSeason season, SchoolYear year, int maxAge) throws FinderException {
		if (season == null) {
			throw new FinderException("ejbFindAllLastYearStudentsBySeasonAndYearAndMaximumAge: SchoolSeason not set");
		}
		final StringBuffer sql = new StringBuffer();
		int seasonId = ((Integer) season.getPrimaryKey()).intValue();
		int yearId = -1;
		if (year != null) {
			yearId = ((Integer) year.getPrimaryKey()).intValue();
		}
		//int maxAge = 14;
		sql.append("select student.*");
		sql.append(" from sch_school_sch_school_year school,sch_school_year schoolyear, sch_school_class class, " + getTableName() + " student");
		sql.append(" where school.sch_school_year_id = schoolyear.sch_school_year_id");
		sql.append(" and student." + SCHOOL_YEAR + " = school.sch_school_year_id");
		sql.append(" and class.school_id = school.sch_school_id");
		sql.append(" and class.sch_school_season_id = " + seasonId);
		sql.append(" and class.sch_school_class_id = student." + SCHOOLCLASS);
		if (year != null) {
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

	public Collection ejbFindAllBySeasonAndSchoolYear(SchoolSeason season, SchoolYear schoolYear) throws FinderException {

		final String[] tableNames = new String[]{SCHOOLCLASSMEMBER, SchoolClassBMPBean.SCHOOLCLASS};
		final String[] tableIds = new String[]{"m", "c"};
		IDOQuery sql = idoQuery();
		sql.appendSelect();
		sql.append("m." + SCHOOLCLASSMEMBERID);
		sql.appendFrom(tableNames, tableIds);
		sql.appendWhereEquals("m." + SCHOOL_YEAR, schoolYear);
		sql.appendAndEquals("m." + SCHOOLCLASS, "c." + SchoolClassBMPBean.SCHOOLCLASS + "_id");
		sql.appendAndEquals("c." + SchoolClassBMPBean.SEASON, season);
		return idoFindIDsBySQL(sql.toString());
	}

	public int ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDate(Group citizenGroup, Date date, Collection classes, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException {
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
			query.appendFrom(tables, variables);
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
			if (relationStatus != null) {
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
			subQuery.append(SCHOOLCLASS);
			subQuery.appendInCollection(classes);

			query.appendNotIn(subQuery);

			System.out.println("SQL -> " + this.getClass() + ":" + query);

			return idoGetNumberOfRecords(query);
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}

		return -1;
	}

	/**
	 * The above function will fail because there are too many items generated in
	 * subQuery.appendInCollection(classes) . This function does the same thing,
	 * but creates another subquery instead of the list of classes as above.
	 */
	public int ejbHomeGetNumberOfUsersNotAssignedToClassOnGivenDateNew(Group citizenGroup, Date date, SchoolSeason schoolSeason, Date firstDateOfBirth, Date lastDateOfBirth) throws IDOException, IDOLookupException {
		try {

			IDOEntityDefinition usrDef = IDOLookup.getEntityDefinitionForClass(User.class);
			String usrIdColumn = usrDef.getPrimaryKeyDefinition().getField().getSQLFieldName();
			IDOEntityDefinition grRelDef = IDOLookup.getEntityDefinitionForClass(GroupRelation.class);

			IDOEntityDefinition schoolClassDef = IDOLookup.getEntityDefinitionForClass(SchoolClass.class);
			String schoolClassIdColumn = schoolClassDef.getPrimaryKeyDefinition().getField().getSQLFieldName();

			IDOEntityField dateOfBirth = usrDef.findFieldByUniqueName(User.FIELD_DATE_OF_BIRTH);

			IDOEntityDefinition addressDef = IDOLookup.getEntityDefinitionForClass(Address.class);
			IDOEntityDefinition communeDef = IDOLookup.getEntityDefinitionForClass(Commune.class);

			//relationStatus could be as parameter to this method
			String[] relationStatus = new String[1];
			relationStatus[0] = GroupRelation.STATUS_ACTIVE;

			String[] tables = new String[5];
			String[] variables = new String[5];
			//table name
			tables[0] = usrDef.getSQLTableName();
			//	as variable
			variables[0] = "u";
			//table name
			tables[1] = grRelDef.getSQLTableName();
			//	as variable
			variables[1] = "gr_rel";
			tables[2] = addressDef.getSQLTableName();
			//	as variable
			variables[2] = "a";
			tables[3] = communeDef.getSQLTableName();
			//	as variable
			variables[3] = "c";
			tables[4] = "IC_USER_ADDRESS";
			//	as variable
			variables[4] = "ua";

			IDOQuery query = this.idoQuery();

			query.appendSelectCount();
			//from
			query.appendFrom(tables, variables);
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
			if (relationStatus != null) {
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
			query.append(".IC_USER_ID");
			query.appendEqualSign();
			query.append(variables[4]);
			query.append(".ic_address_id");

			query.appendAnd();
			query.append(variables[4]);
			query.append(".ic_address_id");
			query.appendEqualSign();
			query.append(variables[2]);
			query.append(".ic_address_id");

			query.appendAnd();
			query.append(variables[2]);
			query.append(".ic_commune_id");
			query.appendEqualSign();
			query.append(variables[3]);
			query.append(".ic_commune_id");

			query.appendAnd();
			query.appendEqualsQuoted(variables[3] + ".default_commune", "Y");

			//			u.IC_USER_ID=ua.ic_user_id
			//			AND ua.ic_address_id=a.ic_address_id
			//			AND a.ic_commune_id=c.ic_commune_id
			//			AND c.default_commune='Y'

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
			subQuery.append(SCHOOLCLASS);

			IDOQuery subSubQuery = this.idoQuery();

			subSubQuery.appendSelect();
			subSubQuery.append(schoolClassIdColumn); //" SCH_SCHOOL_CLASS_ID "
			subSubQuery.appendFrom();
			subSubQuery.append(schoolClassDef.getSQLTableName());
			subSubQuery.appendWhereEquals("SCH_SCHOOL_SEASON_ID", schoolSeason.getPrimaryKey().toString());

			subQuery.appendIn(subSubQuery);
			query.appendNotIn(subQuery);

			System.out.println("SQL -> " + this.getClass() + ":" + query);

			return idoGetNumberOfRecords(query);
		}
		catch (IDOCompositePrimaryKeyException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public Collection ejbFindAllByUserAndPeriodAndSchoolCategory(User child, Date period, SchoolCategory category) throws FinderException {
		final IDOQuery sql = idoQuery();
		final String M_ = "m."; // sql alias for school Class member
		final String T_ = "t."; // sql alias for school type
		final Object userId = child.getPrimaryKey();
		sql.appendSelectAllFrom(getTableName() + " m").append(',' + SchoolTypeBMPBean.SCHOOLTYPE + " t").appendWhere().appendEquals(M_ + MEMBER, userId + "");
		if (null != category) {
			sql.appendAndEquals(M_ + SCHOOL_TYPE, T_ + SchoolTypeBMPBean.SCHOOLTYPE + "_id");
			sql.appendAndEqualsQuoted(T_ + SchoolTypeBMPBean.SCHOOLCATEGORY, category.getCategory());
		}
		if (null != period) {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTime(period);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			final Date fromDate = new Date(calendar.getTimeInMillis());
			final long millisInDay = 1000 * 60 * 60 * 24;
			final long maxMillisInMonth = millisInDay * 31;
			calendar.setTimeInMillis(calendar.getTimeInMillis() + maxMillisInMonth);
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			final Date toDate = new Date(calendar.getTimeInMillis() - millisInDay);
			sql.appendAnd().append(toDate).appendGreaterThanOrEqualsSign().append(M_ + REGISTER_DATE);
			sql.appendAnd().appendLeftParenthesis().append(M_ + REMOVED_DATE).appendIsNull().appendOr().append(fromDate).appendLessThanOrEqualsSign().append(M_ + REMOVED_DATE).appendRightParenthesis();
		}
		return idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindByCategorydManagementCommune(String category, String managementType, int communeId, int seasonId) throws FinderException {
		String today = (new Date(System.currentTimeMillis())).toString();
		IDOQuery query = idoQuery();
		query.appendSelect();
		query.append("cm.*");
		query.appendFrom();
		query.append("sch_class_member cm,");
		query.append("sch_school_class sc,");
		query.append("sch_school s,");
		query.append("sch_school_type st,");
		query.append("ic_user u");
		query.appendWhere();
		query.appendEquals("cm.ic_user_id", "u.ic_user_id");

		query.appendAnd().appendLeftParenthesis();
		query.append("cm.removed_date is null");
		query.appendOr().append("cm.removed_date").appendGreaterThanSign().append("'" + today + "'");
		query.appendRightParenthesis();

		query.appendAnd().append("cm.register_date").appendLessThanOrEqualsSign().append("'" + today + "'");
		query.appendAndEquals("sc.sch_school_season_id", seasonId);
		query.appendAndEquals("cm.sch_school_type_id", "st.sch_school_type_id");
		query.appendAndEqualsQuoted("st.school_category", category);
		query.appendAndEquals("s.commune", communeId);
		query.appendAndEquals("sc.school_id", "s.sch_school_id");
		query.appendAndEquals("sc.sch_school_class_id", "cm.sch_school_class_id");
		query.appendAndEqualsQuoted("s.management_type", managementType);

		return idoFindPKsByQuery(query);
	}

	/**
	 * This method returns a Collection with the domain of allowed values (types)
	 * for the invoice_int column
	 * 
	 * @return @author Borgman
	 */
	public Collection ejbHomeGetInvoiceIntervalTypes() {
		String[] typeArr = {KEY_INVOICE_INTERVAL_VALUE_MONTH, KEY_INVOICE_INTERVAL_VALUE_QUARTER, KEY_INVOICE_INTERVAL_VALUE_TERM, KEY_INVOICE_INTERVAL_VALUE_YEAR};

		Vector typeVec = new Vector();

		for (int i = 0; i < typeArr.length; i++) {
			typeVec.add(i, typeArr[i]);
		}

		return typeVec;
	}

	public Collection ejbFindSubGroupPlacements() throws FinderException {
		IDOQuery query = idoQuery();
		query.appendSelectAllFrom(this).append(" m, sch_school_class c");
		query.appendWhereEquals("m."+this.SCHOOLCLASS, "c.sch_school_class_id");
		query.appendAndEquals("c.sub_group", true);
		return idoFindPKsByQuery(query);
	}
	
	public Collection getSubGroups() throws IDORelationshipException {
		return this.idoGetRelatedEntities(SchoolClass.class);
	}

	public void addToGroup(SchoolClass group) throws IDOAddRelationshipException {
		this.idoAddTo(group);
	}
	
	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException {
		super.idoAddTo(year);
	}
	
	
	public void removeFromGroup(SchoolClass group) throws IDORemoveRelationshipException {
		this.idoRemoveFrom(group);
	}
}