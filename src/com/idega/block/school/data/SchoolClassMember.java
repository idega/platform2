package com.idega.block.school.data;


public interface SchoolClassMember extends com.idega.data.IDOEntity
{
	
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

	
 public int getClassMemberId();
 public boolean getHasCompensationByInvoice();
 public java.lang.String getInvoiceInterval();
 public java.lang.String getLanguage();
 public java.sql.Timestamp getLatestInvoiceDate();
 public boolean getNeedsSpecialAttention();
 public java.lang.String getNotes();
 public java.lang.String getPlacementParagraph();
 public java.sql.Timestamp getRegisterDate();
 public int getRegistratorId();
 public java.sql.Timestamp getRemovedDate();
 public com.idega.block.school.data.SchoolClass getSchoolClass();
 public int getSchoolClassId();
 public boolean getSpeciallyPlaced();
 public com.idega.user.data.User getStudent();
 public void initializeAttributes();
 public void setClassMemberId(int p0);
 public void setHasCompensationByInvoice(boolean p0);
 public void setInvoiceInterval(java.lang.String p0);
 public void setLanguage(java.lang.String p0);
 public void setLatestInvoiceDate(java.sql.Timestamp p0);
 public void setNeedsSpecialAttention(boolean p0);
 public void setNotes(java.lang.String p0);
 public void setPlacementParagraph(java.lang.String p0);
 public void setRegisterDate(java.sql.Timestamp p0);
 public void setRegistratorId(int p0);
 public void setRemovedDate(java.sql.Timestamp p0);
 public void setSchoolClassId(int p0);
 public void setSpeciallyPlaced(boolean p0);
}
