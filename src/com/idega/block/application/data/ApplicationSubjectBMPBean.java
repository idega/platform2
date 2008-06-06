/*
 * $Id: ApplicationSubjectBMPBean.java,v 1.4.2.2 2008/06/06 11:27:48 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.FinderException;

import com.idega.data.IDOQuery;
import com.idega.util.IWTimestamp;

/**
 * 
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationSubjectBMPBean extends com.idega.data.GenericEntity
		implements com.idega.block.application.data.ApplicationSubject {
	private static final String ENTITY_NAME = "app_subject";
	private static final String COLUMN_DESCRIPTION = "description";
	private static final String COLUMN_CREATED = "created";
	private static final String COLUMN_STARTS = "starts";
	private static final String COLUMN_EXPIRES = "expires";
	private static final String COLUMN_STATUS = "status";
	private static final String COLUMN_ATTRIBUTE = "INFO_ATTRIBUE";

	public ApplicationSubjectBMPBean() {
		super();
	}

	public ApplicationSubjectBMPBean(int id) throws SQLException {
		super(id);
	}

	public void initializeAttributes() {
		addAttribute(getIDColumnName());
		addAttribute(COLUMN_DESCRIPTION, "Description", String.class, 255);
		addAttribute(COLUMN_CREATED, "Created", Date.class);
		addAttribute(COLUMN_EXPIRES, "Expires", Date.class);
		addAttribute(COLUMN_STATUS, "Status", String.class, 1);
		addAttribute(COLUMN_ATTRIBUTE, "Attribute", String.class);
		addAttribute(COLUMN_STARTS, "Starts", Date.class);
	}

	public String getEntityName() {
		return ENTITY_NAME;
	}

	//getters
	public String getName() {
		return getDescription();
	}

	public String getDescription() {
		return getStringColumnValue(COLUMN_DESCRIPTION);
	}
	
	public Date getCreated() {
		return getDateColumnValue(COLUMN_CREATED);
	}

	public Date getExpires() {
		return getDateColumnValue(COLUMN_EXPIRES);
	}

	public String getStatus() {
		return getStringColumnValue(COLUMN_STATUS);
	}

	public String getExtraAttribute() {
		return getStringColumnValue(COLUMN_ATTRIBUTE);
	}

	public Date getStarts() {
		return getDateColumnValue(COLUMN_STARTS);
	}
	
    //setters
	public void setDescription(String description) {
		setColumn(COLUMN_DESCRIPTION, description);
	}

	public void setCreated(Date date) {
		setColumn(COLUMN_CREATED, date);
	}

	public void setExpires(Date date) {
		setColumn(COLUMN_EXPIRES, date);
	}

	public void setStatus(String status) {
		setColumn(COLUMN_STATUS, status);
	}

	public void setExtraAttribute(String attribute) {
		setColumn(COLUMN_ATTRIBUTE, attribute);
	}

	public void setStarts(Date starts) {
		setColumn(COLUMN_STARTS, starts);
	}
	
	//ejb
	public Collection ejbFindAll() throws FinderException {
		return super.idoFindPKsByQuery(super.idoQueryGetSelect());
	}

	public Collection ejbFindNonExpired() throws FinderException {
		IWTimestamp now = IWTimestamp.RightNow();
		IDOQuery query = super.idoQueryGetSelect();
		query.appendWhere(COLUMN_EXPIRES);
		query.appendGreaterThanSign();
		query.append(now.getDate());
		query.appendAnd();
		query.appendLeftParenthesis();
		query.append(COLUMN_STARTS);
		query.appendIsNull();
		query.appendOr();
		query.append(COLUMN_STARTS);
		query.appendLessThanOrEqualsSign();
		query.append(now.getDate());
		query.appendRightParenthesis();
		
		System.out.println("query = " + query.toString());
		
		return super.idoFindPKsByQuery(query);
	}
}