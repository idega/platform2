/*
 * $Id: ApplicationBMPBean.java,v 1.5.2.1 2006/10/18 13:53:58 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.GenericEntity;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationBMPBean extends GenericEntity implements Application{
	public static final String ENTITY_NAME = "app_application";

	private static final String COLUMN_SUBJECT = "app_subject_id";

	private static final String COLUMN_APPLICANT = "app_applicant_id";

	private static final String COLUMN_SUBMITTED = "submitted";

	private static final String COLUMN_STATUS = "status";

	private static final String COLUMN_STATUS_CHANGED = "status_changed";

	public static final String STATUS_SUBMITTED = Status.SUBMITTED.toString();

	public static final String STATUS_APPROVED = Status.APPROVED.toString();

	public static final String STATUS_REJECTED = Status.REJECTED.toString();

	public static final String STATUS_SIGNED = Status.SIGNED.toString();

	public static final String STATUS_GARBAGE = Status.GARBAGE.toString();

	public ApplicationBMPBean() {
		super();
	}

	public ApplicationBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addManyToOneRelationship(COLUMN_SUBJECT, ApplicationSubject.class);
		addManyToOneRelationship(COLUMN_APPLICANT, Applicant.class);
		addAttribute(COLUMN_SUBMITTED, "Submitted", Timestamp.class);
		addAttribute(COLUMN_STATUS, "Status", String.class, 1);
		addAttribute(COLUMN_STATUS_CHANGED, "Status changed", Timestamp.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public static String getEntityTableName() {
		return ENTITY_NAME;
	}

	public static String getSubjectIdColumnName() {
		return COLUMN_SUBJECT;
	}

	public static String getStatusColumnName() {
		return COLUMN_STATUS;
	}

	public static String getApplicantIdColumnName() {
		return COLUMN_APPLICANT;
	}

	public static String getSubmittedColumnName() {
		return COLUMN_SUBMITTED;
	}

	public static String getStatusChangedColumnName() {
		return COLUMN_STATUS_CHANGED;
	}

	public void setSubjectId(int id) {
		setColumn(COLUMN_SUBJECT, id);
	}

	public void setSubjectId(Integer id) {
		setColumn(COLUMN_SUBJECT, id);
	}

	public int getSubjectId() {
		return getIntColumnValue(COLUMN_SUBJECT);
	}

	public ApplicationSubject getSubject() {
		return (ApplicationSubject) getColumnValue(COLUMN_SUBJECT);
	}
	public void setApplicantId(int id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public void setApplicantId(Integer id) {
		setColumn(COLUMN_APPLICANT, id);
	}

	public int getApplicantId() {
		return getIntColumnValue(COLUMN_APPLICANT);
	}

	public Applicant getApplicant() {
		return (Applicant) getColumnValue(COLUMN_APPLICANT);
	}

	public void setSubmitted(Timestamp submitted) {
		setColumn(COLUMN_SUBMITTED, submitted);
	}

	public Timestamp getSubmitted() {
		return (Timestamp) getColumnValue(COLUMN_SUBMITTED);
	}

	public void setStatus(String status) throws IllegalStateException {
		if ((status.equalsIgnoreCase(STATUS_SUBMITTED))
				|| (status.equalsIgnoreCase(STATUS_APPROVED))
				|| (status.equalsIgnoreCase(STATUS_SIGNED))
				|| (status.equalsIgnoreCase(STATUS_REJECTED))
				|| (status.equalsIgnoreCase(STATUS_GARBAGE))) {
			setColumn(COLUMN_STATUS, status);
			setStatusChanged(IWTimestamp.getTimestampRightNow());
		} else
			throw new IllegalStateException("Undefined state : " + status);
	}

	public void setStatusSubmitted() {
		setStatus(STATUS_SUBMITTED);
	}

	public void setStatusApproved() {
		setStatus(STATUS_APPROVED);
	}

	public void setStatusRejected() {
		setStatus(STATUS_REJECTED);
	}

	public void setStatusSigned() {
		setStatus(STATUS_SIGNED);
	}

	public String getStatus() {
		return getStringColumnValue(COLUMN_STATUS);
	}

	public void setStatusChanged(Timestamp statusChanged) {
		setColumn(COLUMN_STATUS_CHANGED, statusChanged);
	}

	public Timestamp getStatusChanged() {
		return (Timestamp) getColumnValue(COLUMN_STATUS_CHANGED);
	}

	public Collection ejbFindByApplicantID(Integer ID) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_APPLICANT, ID.intValue()));
	}

	public Collection ejbFindByApplicantAndStatus(Integer ID, String status)
			throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_APPLICANT, ID.intValue())
				.appendAndEqualsQuoted(COLUMN_STATUS, status)
				.appendOrderByDescending(COLUMN_STATUS_CHANGED));
	}

	public Collection ejbFindBySubjectAndStatus(Integer subjectID, String status)
			throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_SUBJECT,
						subjectID.intValue()).appendAndEqualsQuoted(
						COLUMN_STATUS, status));
	}

	public Collection ejbFindByStatus(String status) throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEqualsQuoted(COLUMN_STATUS, status)
				.appendOrderByDescending(COLUMN_STATUS_CHANGED));
	}

	public Collection ejbFindBySubject(Integer subjectID)
			throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEquals(COLUMN_SUBJECT,
						subjectID.intValue()));
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}
}