package com.idega.block.school.data;

import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;


public interface SchoolClassMember extends com.idega.data.IDOEntity
{
	public final static String FIELD_SCHOOLCLASSMEMBERID = "sch_class_member_id";
	public final static String FIELD_SCHOOLCLASSMEMBER = "sch_class_member";
	public final static String FIELD_MEMBER = "ic_user_id";
	public final static String FIELD_NOTES = "notes";
	public final static String FIELD_SCHOOLCLASS = "sch_school_class_id";
	public final static String FIELD_REGISTER_DATE = "register_date";
	public final static String FIELD_REMOVED_DATE = "removed_date";
	public final static String FIELD_REGISTRATOR = "registrator";
	public final static String FIELD_NEEDS_SPECIAL_ATTENTION = "NEEDS_SPECIAL_ATTENTION";
	public final static String FIELD_SPECIALLY_PLACED = "SPECIALLY_PLACED";
	public final static String FIELD_LANGUAGE = "LANGUAGE";
	//Added for the kompliterings project
	public final static String FIELD_COMPENSATION_BY_INVOICE = "comp_by_invoice";
	public final static String FIELD_INVOICE_INTERVAL = "invoice_int";
	public final static String FIELD_LATEST_INVOICE_DATE = "latest_invoice_date";
	public int getClassMemberId();
	
 public boolean getHasCompensationByAgreement();
 public java.lang.String getInvoiceInterval();
 public java.lang.String getLanguage();
 public java.sql.Timestamp getLatestInvoiceDate();
 public boolean getNeedsSpecialAttention();
 public java.lang.String getNotes();
 public java.lang.String getPlacementParagraph();
 public java.sql.Timestamp getRegisterDate();
 public java.sql.Timestamp getRegistrationCreatedDate();
 public int getRegistratorId();
 public java.sql.Timestamp getRemovedDate();
 public com.idega.block.school.data.SchoolClass getSchoolClass();
 public int getSchoolClassId();
 public com.idega.block.school.data.SchoolType getSchoolType();
 public int getSchoolTypeId();
 public com.idega.block.school.data.SchoolYear getSchoolYear();
 public int getSchoolYearId();
 public boolean getSpeciallyPlaced();
 public com.idega.user.data.User getStudent();
 public int getStudyPathId();
 public void initializeAttributes();
 public void setClassMemberId(int p0);
 public void setHasCompensationByAgreement(boolean p0);
 public void setInvoiceInterval(java.lang.String p0);
 public void setLanguage(java.lang.String p0);
 public void setLatestInvoiceDate(java.sql.Timestamp p0);
 public void setNeedsSpecialAttention(boolean p0);
 public void setNotes(java.lang.String p0);
 public void setPlacementParagraph(java.lang.String p0);
 public void setRegisterDate(java.sql.Timestamp p0);
 public void setRegistrationCreatedDate(java.sql.Timestamp p0);
 public void setRegistratorId(int p0);
 public void setRemovedDate(java.sql.Timestamp p0);
 public void setSchoolClassId(int p0);
 public void setSchoolTypeId(int p0);
 public void setSchoolYear(int p0);
 public void setSpeciallyPlaced(boolean p0);
 public void setStudyPathId(int p0);
 public void setStudyPathToNull();

	public java.util.Collection getSubGroups() throws IDORelationshipException;
	public void addToGroup(SchoolClass group) throws IDOAddRelationshipException;
	public void addToSchoolStudyPath(SchoolStudyPath schoolStudyPath) throws IDOAddRelationshipException;
	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException;
	public void removeFromGroup(SchoolClass group) throws IDORemoveRelationshipException;
}
