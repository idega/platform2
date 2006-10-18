/*
 * $Id: ApplicationSubjectBMPBean.java,v 1.4.2.1 2006/10/18 13:53:58 palli Exp $
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

import com.idega.util.IWTimestamp;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationSubjectBMPBean extends com.idega.data.GenericEntity implements com.idega.block.application.data.ApplicationSubject {
  private static final String ENTITY_NAME = "app_subject";
  private static final String COLUMN_DESCRIPTION = "description";
  private static final String COLUMN_CREATED = "created";
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
    addAttribute(COLUMN_DESCRIPTION,"Description",true,true,"java.lang.String");
    addAttribute(COLUMN_CREATED,"Created",true,true,"java.sql.Date");
    addAttribute(COLUMN_EXPIRES,"Expires",true,true,"java.sql.Date");
    addAttribute(COLUMN_STATUS,"Status?",true,true,"java.lang.String");
    addAttribute(COLUMN_ATTRIBUTE,"Status?",true,true,"java.lang.String");
    setMaxLength(COLUMN_DESCRIPTION,255);
    setMaxLength(COLUMN_STATUS,1);
  }

  public String getEntityName() {
    return(ENTITY_NAME);
  }

  public String getName() {
    return(getDescription());
  }

  public String getDescriptionColumnName() {
    return(COLUMN_DESCRIPTION);
  }

  public String getCreatedColumnName() {
    return(COLUMN_CREATED);
  }

  public String getExpiresColumnName() {
    return(COLUMN_EXPIRES);
  }

  public String getStatusColumnName() {
    return(COLUMN_STATUS);
  }

  public void setDescription(String description) {
    setColumn(COLUMN_DESCRIPTION,description);
  }

  public String getDescription() {
    return((String)getColumnValue(COLUMN_DESCRIPTION));
  }

  public void setCreated(Date date) {
    setColumn(COLUMN_CREATED,date);
  }

  public Date getCreated() {
    return((Date)getColumnValue(COLUMN_CREATED));
  }

  public void setExpires(Date date) {
    setColumn(COLUMN_EXPIRES,date);
  }

  public Date getExpires() {
    return((Date)getColumnValue(COLUMN_EXPIRES));
  }

  public void setStatus(String status) {
    setColumn(COLUMN_STATUS,status);
  }

  public String getStatus() {
    return((String)getColumnValue(COLUMN_STATUS));
  }
  
  public void setExtraAttribute(String attribute) {
    setColumn(COLUMN_ATTRIBUTE,attribute);
  }

  public String getExtraAttribute() {
    return((String)getColumnValue(COLUMN_ATTRIBUTE));
  }
  
  public Collection ejbFindAll() throws FinderException{
  	return super.idoFindPKsByQuery(super.idoQueryGetSelect());
  }
  
  public Collection ejbFindNonExpired() throws FinderException{
	return super.idoFindPKsByQuery(super.idoQueryGetSelect().appendWhere(getExpiresColumnName()).appendGreaterThanSign().append(IWTimestamp.RightNow().getDate()));
  }
}
