/*
 * $Id: ApplicantBMPBean.java,v 1.5.2.3 2007/04/12 12:18:20 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import java.sql.SQLException;
import java.util.Collection;
import java.util.StringTokenizer;

import javax.ejb.FinderException;

import com.idega.data.TreeableEntityBMPBean;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicantBMPBean extends TreeableEntityBMPBean implements
		Applicant {
	public static final String ENTITY_NAME = "app_applicant";

	public static final String COLUMN_FIRST_NAME = "first_name";

	public static final String COLUMN_MIDDLE_NAME = "middle_name";

	public static final String COLUMN_LAST_NAME = "last_name";

	public static final String COLUMN_FULL_NAME = "full_name";

	public static final String COLUMN_SSN = "ssn";

	public static final String COLUMN_LEGAL_RESIDENCE = "legal_residence";

	public static final String COLUMN_RESIDENCE = "residence";

	public static final String COLUMN_PHONE = "residence_phone";

	public static final String COLUMN_PO = "po";

	public static final String COLUMN_MOBILE_PHONE = "mobile_phone";

	public static final String COLUMN_SEND_SMS = "send_sms";

	public static final String COLUMN_STATUS = "status";

	public ApplicantBMPBean() {
		super();
	}

	public ApplicantBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_FIRST_NAME, "First name", String.class, 255);
		addAttribute(COLUMN_MIDDLE_NAME, "Middle name", String.class, 255);
		addAttribute(COLUMN_LAST_NAME, "Last name", String.class, 255);
		addAttribute(COLUMN_FULL_NAME, "Full name", String.class, 255);
		addAttribute(COLUMN_SSN, "Social security number", String.class, 20);
		addAttribute(COLUMN_LEGAL_RESIDENCE, "Legal residence", String.class, 255);
		addAttribute(COLUMN_RESIDENCE, "Residence", String.class, 255);
		addAttribute(COLUMN_PHONE, "Telephone", String.class, 40);
		addAttribute(COLUMN_PO, "Post office", String.class, 255);
		addAttribute(COLUMN_MOBILE_PHONE, "Mobile phone", String.class, 40);
		addAttribute(COLUMN_SEND_SMS, "Send SMS", String.class, 1);
		addAttribute(COLUMN_STATUS, "Status", String.class, 1);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	public static String getEntityTableName() {
		return ENTITY_NAME;
	}

	public static String getFullnameOrderValue() {
		StringBuffer ret = new StringBuffer(COLUMN_FIRST_NAME);
		ret.append(",");
		ret.append(COLUMN_MIDDLE_NAME);
		ret.append(",");
		ret.append(COLUMN_LAST_NAME);
		return (ret.toString());
	}

	public void setFirstName(String name) {
		setColumn(COLUMN_FIRST_NAME, name);
	}

	public String getFirstName() {
		return (getStringColumnValue(COLUMN_FIRST_NAME));
	}

	public void setMiddleName(String name) {
		setColumn(COLUMN_MIDDLE_NAME, name);
	}

	public String getMiddleName() {
		return (getStringColumnValue(COLUMN_MIDDLE_NAME));
	}

	public void setLastName(String name) {
		setColumn(COLUMN_LAST_NAME, name);
	}

	public String getLastName() {
		return (getStringColumnValue(COLUMN_LAST_NAME));
	}

	private void setFullName() {
		setColumn(COLUMN_FULL_NAME, getFullName());
	}

	public void setSSN(String ssn) {
		setColumn(COLUMN_SSN, ssn);
	}

	public String getSSN() {
		return (getStringColumnValue(COLUMN_SSN));
	}

	public void setLegalResidence(String legal) {
		setColumn(COLUMN_LEGAL_RESIDENCE, legal);
	}

	public String getLegalResidence() {
		return (getStringColumnValue(COLUMN_LEGAL_RESIDENCE));
	}

	public void setResidence(String residence) {
		setColumn(COLUMN_RESIDENCE, residence);
	}

	public String getResidence() {
		return (getStringColumnValue(COLUMN_RESIDENCE));
	}

	public void setResidencePhone(String phone) {
		setColumn(COLUMN_PHONE, phone);
	}

	public String getResidencePhone() {
		return (getStringColumnValue(COLUMN_PHONE));
	}

	public void setPO(String po) {
		setColumn(COLUMN_PO, po);
	}

	public String getPO() {
		return (getStringColumnValue(COLUMN_PO));
	}

	public void setMobilePhone(String mobilePhone) {
		setColumn(COLUMN_MOBILE_PHONE, mobilePhone);
	}

	public String getMobilePhone() {
		return (getStringColumnValue(COLUMN_MOBILE_PHONE));
	}

	public boolean getSendSMS() {
		String send = getStringColumnValue(COLUMN_SEND_SMS);
		if ((send == null) || (send.equalsIgnoreCase("n"))) {
			return (false);
		} else if (send.equalsIgnoreCase("y")) {
			return (true);
		} else {
			return (false);
		}
	}

	public void setSendSMS(boolean send) {
		if (send) {
			setColumn(COLUMN_SEND_SMS, "Y");
		} else {
			setColumn(COLUMN_SEND_SMS, "N");
		}
	}

	public void setStatus(String status) {
		setColumn(COLUMN_STATUS, status);
	}

	public String getStatus() {
		return getStringColumnValue(COLUMN_STATUS);
	}

	public String getName() {
		return getFullName();
	}

	public String getFullName() {
		StringBuffer fullName = new StringBuffer("");
		if (getFirstName() != null) {
			fullName.append(getFirstName());
			fullName.append(" ");
		}

		if (getMiddleName() != null) {
			fullName.append(getMiddleName());
			fullName.append(" ");
		}

		if (getLastName() != null) {
			fullName.append(getLastName());
		}

		return fullName.toString();
	}

	public void setFullName(String fullName) {
		StringTokenizer tok = new StringTokenizer(fullName);
		String temp = "";
		if (tok.hasMoreTokens()) {
			temp = tok.nextToken();
			setFirstName(temp);
			temp = "";
			if (tok.hasMoreTokens()) {
				temp = tok.nextToken();
				setMiddleName(temp);
			}
			if (tok.hasMoreTokens()) {
				temp = tok.nextToken();
				setLastName(temp);
			} else {
				setMiddleName("");
				setLastName(temp);
			}
		}
	}

	public void insert() throws SQLException {
		setFullName();
		super.insert();
	}

	public void update() throws SQLException {
		setFullName();
		super.update();
	}

	public java.util.Collection ejbFindBySSN(String SSN)
			throws javax.ejb.FinderException {

		return super.idoFindPKsByQuery(super.idoQueryGetSelect()
				.appendWhereEqualsQuoted(COLUMN_SSN, SSN));

	}

	public Collection ejbFindByApplicationStatusOrderedBy(String status,
			String order) throws FinderException {
		String applicantTable = ENTITY_NAME;
		String applicationTable = ApplicationBMPBean.ENTITY_NAME;
		StringBuffer sql = new StringBuffer("select distinct ");
		sql.append(applicantTable);
		sql.append(".* from ");
		sql.append(applicantTable);
		sql.append(",");
		sql.append(applicationTable);
		sql.append(" where ");
		sql.append(applicantTable);
		sql.append(".");
		sql.append(getIDColumnName());
		sql.append(" = ");
		sql.append(applicationTable);
		sql.append(".");
		sql.append(ApplicationBMPBean.getApplicantIdColumnName());
		sql.append(" and ");
		if (status != null) {
			sql.append(applicationTable);
			sql.append(".");
			sql.append(ApplicationBMPBean.getStatusColumnName());
			sql.append(" = '");
			sql.append(status);
			sql.append("' ");
		}
		if (order != null && order.length() > 0) {
			sql.append(" order by ");
			sql.append(order);
		}
		return super.idoFindPKsBySQL(sql.toString());
	}

	public Collection ejbFindBySQL(String sql) throws FinderException {
		return super.idoFindPKsBySQL(sql);
	}
}