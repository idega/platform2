/*
 * $Id: ApplicationCategory.java,v 1.1 2001/06/15 01:31:03 palli Exp $
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
public class ApplicationCategory extends GenericEntity {
  private static String name_ = "app_allocation";
  private static String description_ = "description";
  private static String created_ = "created";

  public ApplicationCategory() {
    super();
  }

  public ApplicationCategory(int id) throws SQLException {
    super(id);
  }

  public void initializeAttributes() {
    addAttribute(getIDColumnName());
    addAttribute(description_,"Um hvað er sótt",true,true,"java.lang.String");
    addAttribute(created_,"Hvenær búið til",true,true,"java.sql.Date");
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
}