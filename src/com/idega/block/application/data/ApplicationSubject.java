/*
 * $Id: ApplicationSubject.java,v 1.2 2001/06/22 11:33:08 palli Exp $
 *
 * Copyright (C) 2001 Idega hf. All Rights Reserved.
 *
 * This software is the proprietary information of Idega hf.
 * Use is subject to license terms.
 *
 */
package com.idega.block.application.data;

import com.idega.data.GenericEntity;
import java.sql.SQLException;
import java.sql.Date;

/**
 *
 * @author <a href="mailto:palli@idega.is">Pall Helgason</a>
 * @version 1.0
 */
public class ApplicationSubject extends GenericEntity {
  public static final String name_ = "app_subject";
  public static final String description_ = "description";
  public static final String created_ = "created";
  public static final String expires_ = "expires";
  public static final String status_ = "status";

  public ApplicationSubject() {
    super();
  }

  public ApplicationSubject(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Description",true,true,"java.lang.String");
    addAttribute(created_,"Created",true,true,"java.sql.Date");
    addAttribute(expires_,"Expires",true,true,"java.sql.Date");
    addAttribute(status_,"Status",true,true,"java.lang.String");
    setMaxLength(description_,255);
    setMaxLength(status_,1);
  }

  public String getEntityName() {
    return(name_);
  }

  public void setDescription(String description) {
    setColumn(description_,description);
  }

  public String getDescription() {
    return((String)getColumnValue(description_));
  }

  public void setCreated(Date date) {
    setColumn(created_,date);
  }

  public Date getCreated() {
    return((Date)getColumnValue(created_));
  }

  public void setExpires(Date date) {
    setColumn(expires_,date);
  }

  public Date getExpires() {
    return((Date)getColumnValue(expires_));
  }

  public void setStatus(String status) {
    setColumn(status_,status);
  }

  public String getStatus() {
    return((String)getColumnValue(status_));
  }
}