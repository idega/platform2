/*
 * $Id: SchoolClassMember.java,v 1.39 2004/12/07 20:39:18 laddi Exp $
 * Created on 7.12.2004
 *
 * Copyright (C) 2004 Idega Software hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 */
package com.idega.block.school.data;

import java.sql.Timestamp;
import java.util.Collection;


import com.idega.data.IDOAddRelationshipException;
import com.idega.data.IDOEntity;
import com.idega.data.IDORelationshipException;
import com.idega.data.IDORemoveRelationshipException;
import com.idega.user.data.User;


/**
 * Last modified: $Date: 2004/12/07 20:39:18 $ by $Author: laddi $
 * 
 * @author <a href="mailto:laddi@idega.com">laddi</a>
 * @version $Revision: 1.39 $
 */
public interface SchoolClassMember extends IDOEntity {

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

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setClassMemberId
	 */
	public void setClassMemberId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getClassMemberId
	 */
	public int getClassMemberId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getStudent
	 */
	public User getStudent();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setStudent
	 */
	public void setStudent(User student);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolClassId
	 */
	public void setSchoolClassId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolClass
	 */
	public void setSchoolClass(SchoolClass group);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolClassId
	 */
	public int getSchoolClassId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolClass
	 */
	public SchoolClass getSchoolClass();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolTypeId
	 */
	public void setSchoolTypeId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolType
	 */
	public void setSchoolType(SchoolType type);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolTypeId
	 */
	public int getSchoolTypeId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolType
	 */
	public SchoolType getSchoolType();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolYear
	 */
	public void setSchoolYear(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSchoolYear
	 */
	public void setSchoolYear(SchoolYear year);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolYearId
	 */
	public int getSchoolYearId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSchoolYear
	 */
	public SchoolYear getSchoolYear();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setRegisterDate
	 */
	public void setRegisterDate(Timestamp stamp);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getRegisterDate
	 */
	public Timestamp getRegisterDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setRegistrationCreatedDate
	 */
	public void setRegistrationCreatedDate(Timestamp stamp);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getRegistrationCreatedDate
	 */
	public Timestamp getRegistrationCreatedDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setRemovedDate
	 */
	public void setRemovedDate(Timestamp stamp);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getRemovedDate
	 */
	public Timestamp getRemovedDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setRegistratorId
	 */
	public void setRegistratorId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getRegistratorId
	 */
	public int getRegistratorId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setNotes
	 */
	public void setNotes(String notes);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getNotes
	 */
	public String getNotes();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setNeedsSpecialAttention
	 */
	public void setNeedsSpecialAttention(boolean needsAttention);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getNeedsSpecialAttention
	 */
	public boolean getNeedsSpecialAttention();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setSpeciallyPlaced
	 */
	public void setSpeciallyPlaced(boolean speciallyPlaced);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSpeciallyPlaced
	 */
	public boolean getSpeciallyPlaced();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setLanguage
	 */
	public void setLanguage(String language);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getLanguage
	 */
	public String getLanguage();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getInvoiceInterval
	 */
	public String getInvoiceInterval();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setInvoiceInterval
	 */
	public void setInvoiceInterval(String interval);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getLatestInvoiceDate
	 */
	public Timestamp getLatestInvoiceDate();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setLatestInvoiceDate
	 */
	public void setLatestInvoiceDate(Timestamp date);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getPlacementParagraph
	 */
	public String getPlacementParagraph();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setPlacementParagraph
	 */
	public void setPlacementParagraph(String placementParagraph);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getHasCompensationByAgreement
	 */
	public boolean getHasCompensationByAgreement();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setHasCompensationByAgreement
	 */
	public void setHasCompensationByAgreement(boolean hasCompensation);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setStudyPathId
	 */
	public void setStudyPathId(int id);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setStudyPath
	 */
	public void setStudyPath(SchoolStudyPath studyPath);

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#setStudyPathToNull
	 */
	public void setStudyPathToNull();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getStudyPathId
	 */
	public int getStudyPathId();

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#getSubGroups
	 */
	public Collection getSubGroups() throws IDORelationshipException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#addToGroup
	 */
	public void addToGroup(SchoolClass group) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#addSchoolYear
	 */
	public void addSchoolYear(SchoolYear year) throws IDOAddRelationshipException;

	/**
	 * @see com.idega.block.school.data.SchoolClassMemberBMPBean#removeFromGroup
	 */
	public void removeFromGroup(SchoolClass group) throws IDORemoveRelationshipException;

}
